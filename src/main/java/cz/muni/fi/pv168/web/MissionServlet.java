package cz.muni.fi.pv168.web;

import cz.muni.fi.pv168.backend.entities.*;
import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import cz.muni.fi.pv168.web.vm.MissionViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by FH on 21.4.2015.
 */
@WebServlet(urlPatterns = {MissionServlet.URL_MAPPING})
public class MissionServlet extends HttpServlet
{
	public static final String LIST_JSP = "/missionlist.jsp";
	public static final String UPDATE_JSP = "/missionupdate.jsp";
	public static final String URL_MAPPING = "/missions/*";

	private static final Logger log = LoggerFactory.getLogger(MissionServlet.class);
	private static final String SPIES_ON_MISSION_JSP = "/spiesonmission.jsp";

	public static  final ResourceBundle errors = ResourceBundle.getBundle("StringsWebErrorDialogs",Locale.getDefault());
	private static final List<String> VALID_ENUMS = new ArrayList<>(Arrays.asList("ASSASSINATION", "ABDUCTION", "SURVEILLANCE", "SABOTAGE", "UNSPECIFIED"));
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		Utility.fillRequestWithBundle("Strings",request);
		Utility.fillRequestWithBundle("MissionTypes", request);
		showMissionList(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Utility.fillRequestWithBundle("Strings",request);
		Utility.fillRequestWithBundle("MissionTypes", request);
		String action = request.getPathInfo();
		switch (action)
		{
			case "/listspies":
				doListSpiesOnMission(request, response);
				return;
			case "/add":
				doAdd(request, response);
				return;
			case "/delete":
				doDeletePost(request, response);
				break;
			case "/update":
				doUpdate(request, response);
				return;

			default:
				log.error("Unknown action " + action);
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
		}
	}

	private void showMissionList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			List<MissionViewModel> missions = new ArrayList<>();
			for (Mission mission : getMissionManager().getAllMissions())
			{
				missions.add(MissionViewModel.fromMission(mission));
			}
			request.setAttribute("missions", missions);
			request.getRequestDispatcher(LIST_JSP).forward(request, response);
		}
		catch (IllegalEntityException ex)
		{
			log.error("Unable to display missions", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	private MissionManager getMissionManager()
	{
		return (MissionManager) getServletContext().getAttribute("missionManager");
	}

	private boolean areNullOrEmpty(List<String> params)
	{
		return params.stream().allMatch(p -> p == null || p.isEmpty());
	}

	private void doDeletePost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			Long id = Long.valueOf(request.getParameter("id"));
			Mission mission = getMissionManager().getMissionById(id);
			getMissionManager().deleteMission(mission);
			log.debug("deleted mission {}", id);
			response.sendRedirect(request.getContextPath() + URL_MAPPING);
			return;
		}
		catch (IllegalEntityException ex)
		{
			log.error("Cannot delete mission", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
			return;
		}
	}

	private void doAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");
		String missionTypeStr = request.getParameter("missionType");
		missionTypeStr = getEnumStringFromTranslatedValue(missionTypeStr);

		if (areNullOrEmpty(new ArrayList<>(Arrays.asList(startDateStr, endDateStr, missionTypeStr))))
		{
			request.setAttribute("error", errors.getString("e_ValueNotFilledIn"));
			showMissionList(request, response);
			return;
		}

		try
		{
			try
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = df.parse(startDateStr);
				Date endDate = df.parse(endDateStr);

				MissionType missionType = MissionType.valueOf(missionTypeStr);
				try
				{
					Mission mission = new Mission(null, startDate, endDate, missionType);
					getMissionManager().createMission(mission);
					response.sendRedirect(request.getContextPath() + URL_MAPPING);
					return;
				}
				catch(IllegalStateException ex) //end date earlier than start
				{
					request.setAttribute("error", errors.getString("e_EndEarlierThanStart"));
					showMissionList(request, response);
					return;
				}
			}
			catch (ParseException ex)
			{
				request.setAttribute("error", errors.getString("e_DateIncorrect"));
				showMissionList(request, response);
				return;
			}
		}
		catch (IllegalEntityException ex)
		{
			log.error("Cannot add mission", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
			return;
		}
	}

	private String getEnumStringFromTranslatedValue(String missionTypeStr)
	{
		Locale locale = Locale.getDefault();
		ResourceBundle enums = ResourceBundle.getBundle("MissionTypes",locale);
		for(String key:enums.keySet())
		{
			if(enums.getString(key).equals(missionTypeStr))
			{
				return key;
			}
		}
		return null;
	}

	private void doListSpiesOnMission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Long missionId = Long.parseLong(request.getParameter("missionId"));
			Mission mission = getMissionManager().getMissionById(missionId);
			List<Spy> spies = getAgencyManager().findSpiesOnMission(mission);
			request.setAttribute("spies",spies);
			request.getRequestDispatcher(SPIES_ON_MISSION_JSP).forward(request,response);
		}
		catch(NumberFormatException ex)
		{
			request.setAttribute("error", errors.getString("e_MissionIdInvalid"));
			showMissionList(request, response);
		}
	}

	private void doUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String startDateStr;
			String endDateStr;
			String missionTypeStr;
			if (request.getParameter("doUpdate") != null)
			{
				startDateStr = request.getParameter("startDate");
				endDateStr = request.getParameter("endDate");
				missionTypeStr = request.getParameter("missionType");
				missionTypeStr = getEnumStringFromTranslatedValue(missionTypeStr);
				Long id = Long.valueOf(request.getParameter("id"));
				try
				{
					try
					{
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date startDate = df.parse(startDateStr);
						Date endDate = df.parse(endDateStr);

						MissionType missionType = MissionType.valueOf(missionTypeStr);
						Mission mission = new Mission(id, startDate, endDate, missionType);
						getMissionManager().updateMission(mission);
						response.sendRedirect(request.getContextPath() + URL_MAPPING);
						return;
					}
					catch (ParseException ex)
					{
						request.setAttribute("error", errors.getString("e_DateIncorrect"));
						showMissionList(request, response);
						return;
					}
				}
				catch (IllegalEntityException ex)
				{
					log.error("Cannot add mission", ex);
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
					return;
				}
			}
			else //display values for update
			{
				Long id = Long.valueOf(request.getParameter("id"));
				Mission mission = getMissionManager().getMissionById(id);
				request.setAttribute("missionId", mission.getMissionId());
				request.setAttribute("startDate", mission.getStartDate());
				request.setAttribute("endDate", mission.getEndDate());
				request.setAttribute("missionType", mission.getType().toString());

				request.getRequestDispatcher(UPDATE_JSP).forward(request, response);
			}
		}
		catch (IllegalEntityException ex)
		{
			log.error("Can't update mission", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	private AgencyManager getAgencyManager()
	{
		return (AgencyManager) getServletContext().getAttribute("agencyManager");
	}

}
