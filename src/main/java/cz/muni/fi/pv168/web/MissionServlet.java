package cz.muni.fi.pv168.web;

import cz.muni.fi.pv168.backend.entities.*;
import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by FH on 21.4.2015.
 */
@WebServlet(urlPatterns = {MissionServlet.URL_MAPPING})
public class MissionServlet extends HttpServlet
{
	public static final String LIST_JSP = "/missionlist.jsp";
	public static final String UPDATE_JSP = "/missionupdate.jsp";
	public static final String URL_MAPPING = "/missions/*";
	private static final List<String> VALID_ENUMS = new ArrayList<>(Arrays.asList("ASSASSINATION", "ABDUCTION", "SURVEILLANCE", "SABOTAGE", "UNSPECIFIED"));
	private static final Logger log = LoggerFactory.getLogger(MissionServlet.class);
	private static final String SPIES_ON_MISSION_JSP = "/spiesonmission.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		System.out.println(request.getCharacterEncoding());
		showMissionList(request, response);
	}

	private boolean areNullOrEmpty(List<String> params)
	{
		return params.stream().allMatch(p -> p == null || p.isEmpty());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
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
		if (areNullOrEmpty(new ArrayList<>(Arrays.asList(startDateStr, endDateStr, missionTypeStr))))
		{
			request.setAttribute("error", "Some of the values were not filled in.");
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

				MissionType missionType = MissionType.valueOf(missionTypeStr.toUpperCase());
				if (missionType == null)
				{
					request.setAttribute("error", "This error should not happen because mission type is a combobox with the right options.");
					showMissionList(request, response);
					return;
				}

				Mission mission = new Mission(null, startDate, endDate, missionType);
				getMissionManager().createMission(mission);
				response.sendRedirect(request.getContextPath() + URL_MAPPING);
				return;
			}
			catch (ParseException ex)
			{
				request.setAttribute("error", "One of the dates is incorrectly formatted.");
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
			request.setAttribute("error", "The mission ID is invalid.");
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
				missionTypeStr = request.getParameter("missionType").toUpperCase();
				if (!VALID_ENUMS.contains(missionTypeStr))
				{
					request.setAttribute("error", "Invalid value for mission type.");
					request.setAttribute("doUpdate", null);
					request.getRequestDispatcher(UPDATE_JSP).forward(request, response);
					return;
				}
				Long id = Long.valueOf(request.getParameter("id"));
				try
				{
					try
					{
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date startDate = df.parse(startDateStr);
						Date endDate = df.parse(endDateStr);

						try
						{
							String enumValue = missionTypeStr.toUpperCase();
						}
						catch (IllegalArgumentException ex)
						{
							request.setAttribute("error", "Invalid value for mission type.");
							showMissionList(request, response);
							return;
						}
						MissionType missionType = MissionType.valueOf(missionTypeStr);
						if (missionType == null)
						{
							request.setAttribute("error", "This error should not happen.");
							showMissionList(request, response);
							return;
						}

						Mission mission = new Mission(id, startDate, endDate, missionType);
						getMissionManager().updateMission(mission);
						response.sendRedirect(request.getContextPath() + URL_MAPPING);
						return;
					}
					catch (ParseException ex)
					{
						request.setAttribute("error", "One of the dates is incorrectly formatted.");
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

	private MissionManager getMissionManager()
	{
		return (MissionManager) getServletContext().getAttribute("missionManager");
	}

	private void showMissionList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			List<Mission> missions = getMissionManager().getAllMissions();
			request.setAttribute("missions", getMissionManager().getAllMissions());
			request.getRequestDispatcher(LIST_JSP).forward(request, response);
		}
		catch (IllegalEntityException ex)
		{
			log.error("Unable to display missions", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

}
