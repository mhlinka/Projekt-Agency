package cz.muni.fi.pv168.web;

import cz.muni.fi.pv168.backend.entities.*;
import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import cz.muni.fi.pv168.web.vm.MissionViewModel;
import cz.muni.fi.pv168.web.vm.SpyViewModel;
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

@WebServlet(urlPatterns = {SpyServlet.URL_MAPPING})
public class SpyServlet extends HttpServlet
{
	public static final String LIST_JSP = "/spylist.jsp";
	public static final String UPDATE_JSP = "/spyupdate.jsp";
	public static final String URL_MAPPING = "/spies/*";
	public static final String SPY_TO_MISSION = "/spytomission.jsp";

	public static final ResourceBundle errors = ResourceBundle.getBundle("StringsWebErrorDialogs", Locale.getDefault());
	private static final Logger log = LoggerFactory.getLogger(MissionServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		request.setCharacterEncoding("UTF-8");
		Utility.fillRequestWithBundle("Strings", request);

		showSpyList(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");
		Utility.fillRequestWithBundle("Strings", request);
		String action = request.getPathInfo();
		System.out.println("action = " + action);
		switch (action)
		{
			case "/addToMission":
				doAddToMission(request, response);
				return;
			case "/removeFromMission":
				doRemoveFromMission(request, response);
				return;
			case "/add":
				doAdd(request, response);
				return;
			case "/delete":
				doDeletePost(request, response);
				return;
			case "/update":
				doUpdate(request, response);
				return;
			case "/search":
				doSearch(request, response);
				return;
			default:
				log.error("Unknown action " + action);
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
		}
	}

	private void showSpyList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		showSpyList(request, response, getSpyManager().listSpies());
	}

	private void showSpyList(HttpServletRequest request, HttpServletResponse response, List<Spy> spies) throws ServletException, IOException
	{
		try
		{
			List<Spy> unassigned = getAgencyManager().listUnassignedSpies();
			List<SpyViewModel> assignedSpies = new ArrayList<>();
			List<SpyViewModel> unassignedSpies = new ArrayList<>();

			for (Spy spy : spies)
			{
				if (unassigned.contains(spy))
				{
					unassignedSpies.add(SpyViewModel.fromSpy(spy));
				}
				else
				{
					assignedSpies.add(SpyViewModel.fromSpy(spy));
				}
			}
			request.setAttribute("assignedSpies", assignedSpies);
			request.setAttribute("unassignedSpies", unassignedSpies);
			request.getRequestDispatcher(LIST_JSP).forward(request, response);
		}
		catch (IllegalEntityException ex)
		{
			log.error("Unable to display spies", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	private SpyManager getSpyManager()
	{
		return (SpyManager) getServletContext().getAttribute("spyManager");
	}

	private AgencyManager getAgencyManager()
	{
		return (AgencyManager) getServletContext().getAttribute("agencyManager");
	}

	private void doSearch(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String searchingFor = Utility.removeHackyCarky(request.getParameter("s"));
		System.out.println("action = " + searchingFor);
		List<Spy> spiesToDisplay = new ArrayList<>();

		if (!searchingFor.equals(""))
		{
			for (Spy spy : getSpyManager().listSpies())
			{
				String searchingIn = spy.getSpyId() + " " + spy.getFirstName() + " " + spy.getLastName() + " " + spy.getCodename() + " " + spy.getDateOfBirth();
				if (Utility.removeHackyCarky(searchingIn.toLowerCase()).contains(searchingFor.toLowerCase()))
				{
					spiesToDisplay.add(spy);
				}
			}
		}
		if (!spiesToDisplay.isEmpty())
		{
			showSpyList(request, response, spiesToDisplay);
		}
		else
		{
			request.setAttribute("error", errors.getString("e_NoResults"));
			showSpyList(request, response);
		}
	}

	private void doDeletePost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			Long id = Long.valueOf(request.getParameter("id"));
			Spy spy = getSpyManager().findSpyById(id);
			getSpyManager().removeSpy(spy);
			log.debug("removed spy {}", id);
			response.sendRedirect(request.getContextPath() + URL_MAPPING);
			return;
		}
		catch (IllegalEntityException ex)
		{
			log.error("Cannot remove spy", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
			return;
		}
	}

	private void doAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String dateOfBirthStr = request.getParameter("dateOfBirth");
		String codename = request.getParameter("codename");
		if (areNullOrEmpty(new ArrayList<>(Arrays.asList(firstName, lastName, dateOfBirthStr, codename))))
		{
			request.setAttribute("error", errors.getString("e_ValueNotFilledIn"));
			showSpyList(request, response);
			return;
		}

		try
		{
			try
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dateOfBirth = df.parse(dateOfBirthStr);

				Spy spy = new Spy(null, firstName, lastName, dateOfBirth, codename);
				getSpyManager().addSpy(spy);
				response.sendRedirect(request.getContextPath() + URL_MAPPING);
				return;
			}
			catch (ParseException ex)
			{
				request.setAttribute("error", errors.getString("e_DateOfBirthIncorrect"));
				showSpyList(request, response);
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

	private void doRemoveFromMission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Long spyId = Long.parseLong(request.getParameter("spyId"));
			Spy spy = getSpyManager().findSpyById(spyId);
			Mission mission = getAgencyManager().findMissionWithSpy(spy);
			try
			{
				getAgencyManager().removeSpyFromMission(spy, mission);
				showSpyList(request, response);
			}
			catch (IllegalEntityException ex)
			{
				request.setAttribute("error", errors.getString("e_CancelMissionFailed"));
			}
		}
		catch (NumberFormatException ex)
		{
			//this can happen if the url is manually entered with invalid values, not by clicking the link.
			request.setAttribute("error", errors.getString("e_RemoveSpyFromMissionFailed"));
		}
	}

	private void doAddToMission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("addingSpyToMission") != null)
		{
			try
			{
				Long spyId = Long.valueOf(request.getParameter("spyId"));
				Long missionId = Long.parseLong(request.getParameter("missionId"));

				List<MissionViewModel> missions = new ArrayList<>();
				for (Mission mission : getMissionManager().getAllMissions())
				{
					missions.add(MissionViewModel.fromMission(mission));
				}
				request.setAttribute("missions", missions);

				Spy spy = getSpyManager().findSpyById(spyId);
				Mission mission = getMissionManager().getMissionById(missionId);
				try
				{
					getAgencyManager().addSpyToMission(spy, mission);
				}
				catch (IllegalEntityException ex)
				{
					request.setAttribute("error", errors.getString("e_NoSuchMission"));
					request.getRequestDispatcher(SPY_TO_MISSION).forward(request, response);
					return;
				}
				request.setAttribute("success", errors.getString("s_NothingWrong"));
				showSpyList(request, response);
			}
			catch (NumberFormatException ex)
			{
				request.setAttribute("error", errors.getString("e_SomethingWrong"));
				request.getRequestDispatcher(SPY_TO_MISSION).forward(request, response);
				return;
			}
		}
		else
		{
			Long id = Long.valueOf(request.getParameter("spyId"));
			try
			{
				List<MissionViewModel> missions = new ArrayList<>();
				for (Mission mission : getMissionManager().getAllMissions())
				{
					missions.add(MissionViewModel.fromMission(mission));
				}
				request.setAttribute("missions", missions);
			}
			catch (IllegalEntityException ex)
			{
				log.error("Unable to display missions", ex);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
			}
			request.setAttribute("spyId", id);
			request.getRequestDispatcher(SPY_TO_MISSION).forward(request, response);
		}

	}

	private boolean areNullOrEmpty(List<String> params)
	{
		return params.stream().anyMatch(p -> p == null || p.isEmpty());
	}

	private void doUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{

			String firstName;
			String lastName;
			String dateOfBirthStr;
			String codename;

			if (request.getParameter("doUpdate") != null)
			{
				firstName = request.getParameter("firstName");
				lastName = request.getParameter("lastName");
				dateOfBirthStr = request.getParameter("dateOfBirth");
				codename = request.getParameter("codename");
				Long id = Long.valueOf(request.getParameter("id"));
				try
				{
					try
					{
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date dateOfBirth = df.parse(dateOfBirthStr);

						Spy spy = new Spy(id, firstName, lastName, dateOfBirth, codename);
						getSpyManager().updateSpy(spy);
						response.sendRedirect(request.getContextPath() + URL_MAPPING);
						return;
					}
					catch (ParseException ex)
					{
						request.setAttribute("error", errors.getString("e_DateOfBirthIncorrect"));
						showSpyList(request, response);
						return;
					}
				}
				catch (IllegalEntityException ex)
				{
					log.error("Cannot add spy", ex);
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
					return;
				}
			}
			else
			{
				Long id = Long.valueOf(request.getParameter("id"));
				Spy spy = getSpyManager().findSpyById(id);
				request.setAttribute("spyId", spy.getSpyId());
				request.setAttribute("firstName", spy.getFirstName());
				request.setAttribute("lastName", spy.getLastName());
				request.setAttribute("dateOfBirth", spy.getDateOfBirth());
				request.setAttribute("codename", spy.getCodename());

				request.getRequestDispatcher(UPDATE_JSP).forward(request, response);
			}
			return;
		}
		catch (IllegalEntityException ex)
		{
			log.error("Can't update spy", ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	private MissionManager getMissionManager()
	{
		return (MissionManager) getServletContext().getAttribute("missionManager");
	}
}
