package cz.muni.fi.pv168.web;

import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
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
	public static String getEnumStringFromTranslatedValue(String missionTypeStr)
	{
		Locale locale = Locale.getDefault();
		ResourceBundle enums = ResourceBundle.getBundle("MissionTypes", locale);
		for(String key:enums.keySet())
		{
			if(enums.getString(key).equals(missionTypeStr))
			{
				return key;
			}
		}
		return null;
	}

	public static String removeHackyCarky(String s)
	{
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\w\\s\\.]", "");
	}
}
