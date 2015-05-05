package cz.muni.fi.pv168.backend.app;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by FH on 5.5.2015.
 */
public class Shared
{
	public static void AddMissionTypes(JComboBox cb)
	{
		ResourceBundle enumValues = ResourceBundle.getBundle("MissionTypes", Locale.getDefault());
		for(String value:enumValues.keySet())
		{
			cb.addItem(enumValues.getString(value));
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
}
