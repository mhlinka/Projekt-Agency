package cz.muni.fi.pv168.web;

import cz.muni.fi.pv168.backend.entities.Spy;
import cz.muni.fi.pv168.backend.entities.SpyManager;
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
 * Created by Michal on 4/23/2015.
 */

@WebServlet(urlPatterns =  {SpyServlet.URL_MAPPING})
public class SpyServlet extends HttpServlet
{
    public static final String LIST_JSP = "/spylist.jsp";
    public static final String UPDATE_JSP = "/spyupdate.jsp";
    public static final String URL_MAPPING = "/spies/*";

    private static final Logger log = LoggerFactory.getLogger(MissionServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        showSpyList(request, response);
    }

    private boolean areNullOrEmpty(List<String> params)
    {
        return params.stream().allMatch(p -> p == null || p.isEmpty());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        switch (action) {
            case "/add":
                String firstName = request.getParameter("firstName");
                String lastName = request.getParameter("lastName");
                String dateOfBirthStr = request.getParameter("dateOfBirth");
                String codename = request.getParameter("codename");
                if (areNullOrEmpty(new ArrayList<>(Arrays.asList(firstName, lastName, dateOfBirthStr, codename))))
                {
                    request.setAttribute("error", "Some of the values were not filled in.");
                    showSpyList(request, response);
                    return;
                }

                try {
                    try
                    {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateOfBirth = df.parse(dateOfBirthStr);

                        Spy spy = new Spy(null,firstName,lastName,dateOfBirth,codename);
                        getSpyManager().addSpy(spy);
                        response.sendRedirect(request.getContextPath() + URL_MAPPING);
                        return;
                    }
                    catch (ParseException ex)
                    {
                        request.setAttribute("error", "Date of birth is incorrectly formatted.");
                        showSpyList(request, response);
                        return;
                    }
                } catch (IllegalEntityException ex) {
                    log.error("Cannot add mission", ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/delete":
                try {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Spy spy = getSpyManager().findSpyById(id);
                    getSpyManager().removeSpy(spy);
                    log.debug("removed spy {}",id);
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (IllegalEntityException ex) {
                    log.error("Cannot remove spy", ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/update":
                try {
                    doUpdate(request, response);
                    return;
                }
                catch(IllegalEntityException ex)
                {
                    log.error("Can't update spy",ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                }
                return;

            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    private void doUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String firstName;
        String lastName;
        String dateOfBirthStr;
        String codename;

        System.out.println(1);
        if(request.getParameter("doUpdate") != null)
        {
            System.out.println(2);
            firstName = request.getParameter("firstName");
            lastName = request.getParameter("lastName");
            dateOfBirthStr = request.getParameter("dateOfBirth");
            codename = request.getParameter("codename");
            Long id = Long.valueOf(request.getParameter("id"));
            try {
                try
                {
                    System.out.println(3);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateOfBirth = df.parse(dateOfBirthStr);

                    Spy spy = new Spy(id,firstName,lastName,dateOfBirth,codename);
                    getSpyManager().updateSpy(spy);
                    response.sendRedirect(request.getContextPath() + URL_MAPPING);
                    return;
                }
                catch (ParseException ex)
                {
                    request.setAttribute("error", "Date of birth is incorrectly formatted.");
                    showSpyList(request, response);
                    return;
                }
            } catch (IllegalEntityException ex) {
                log.error("Cannot add spy", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                return;
            }
        }
        else //display values for update
        {
            Long id = Long.valueOf(request.getParameter("id"));
            Spy spy = getSpyManager().findSpyById(id);
            System.out.println("HERE!");
            request.setAttribute("spyId", spy.getSpyId());
            request.setAttribute("firstName", spy.getFirstName());
            request.setAttribute("lastName",spy.getLastName());
            request.setAttribute("dateOfBirth",spy.getDateOfBirth());
            request.setAttribute("codename",spy.getCodename());

            request.getRequestDispatcher(UPDATE_JSP).forward(request, response);
        }
        return;
    }

    private SpyManager getSpyManager() {
        return (SpyManager) getServletContext().getAttribute("spyManager");
    }

    private void showSpyList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("spies", getSpyManager().listSpies());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (IllegalEntityException ex) {
            log.error("Unable to display spies", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
