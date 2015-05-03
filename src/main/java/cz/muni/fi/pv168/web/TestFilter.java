package cz.muni.fi.pv168.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by FH on 17.4.2015.
 */
@WebFilter(urlPatterns = {"/*"})
public class TestFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		System.out.println("Filter initialized successfully.");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
	{
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		Enumeration<String> attributeNames = req.getAttributeNames();
		while (attributeNames.hasMoreElements())
		{
			System.out.println(req.getAttribute(attributeNames.nextElement()));
		}
		chain.doFilter(req, res);
	}

	@Override
	public void destroy()
	{

	}
}
