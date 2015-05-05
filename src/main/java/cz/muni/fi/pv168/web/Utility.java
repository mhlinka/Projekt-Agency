package cz.muni.fi.pv168.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by FH on 3.5.2015.
 */
public class Utility
{
	public static void fillRequestWithBundle(String bundleName, HttpServletRequest request)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.getDefault());
		Enumeration<String> x = bundle.getKeys();
		while(x.hasMoreElements())
		{
			String s = x.nextElement();
			System.out.println(s.toString() + " - "+bundle.getString(s));
			request.setAttribute(s.toString(),bundle.getString(s));
		}
	}
}
