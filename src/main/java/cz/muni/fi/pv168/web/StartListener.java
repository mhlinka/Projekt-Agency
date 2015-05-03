package cz.muni.fi.pv168.web;

import cz.muni.fi.pv168.backend.SpringConfig;
import cz.muni.fi.pv168.backend.entities.AgencyManager;
import cz.muni.fi.pv168.backend.entities.MissionManager;
import cz.muni.fi.pv168.backend.entities.SpyManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by FH on 17.4.2015.
 */

@WebListener
public class StartListener implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		System.out.println("Context init");
		ServletContext servletContext = event.getServletContext();
		ApplicationContext springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
		servletContext.setAttribute("spyManager", springContext.getBean("spyManager", SpyManager.class));
		servletContext.setAttribute("missionManager", springContext.getBean("missionManager", MissionManager.class));
		servletContext.setAttribute("agencyManager",springContext.getBean("agencyManager", AgencyManager.class));
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		System.out.println("Context destroyed.");
	}
}
