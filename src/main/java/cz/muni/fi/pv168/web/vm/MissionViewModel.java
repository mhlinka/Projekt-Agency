package cz.muni.fi.pv168.web.vm;

import cz.muni.fi.pv168.backend.entities.Mission;

import java.text.DateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Created by FH on 4.5.2015.
 */
public class MissionViewModel
{
	public static MissionViewModel fromMission(Mission mission)
	{
		TimeZone tz = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		DateFormat format = DateFormat.getDateInstance(DateFormat.LONG, locale);
		format.setTimeZone(tz);
		ResourceBundle enums = ResourceBundle.getBundle("MissionTypes",locale);

		MissionViewModel model = new MissionViewModel();
		model.setMissionId(mission.getMissionId().toString());
		model.setStartDate(format.format(mission.getStartDate()));
		model.setEndDate(format.format(mission.getEndDate()));
		model.setType(enums.getString(mission.getType().toString()));

		return model;
	}

	public String getMissionId()
	{
		return missionId;
	}

	public void setMissionId(String id)
	{
		this.missionId = id;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String missionType)
	{
		this.type = missionType;
	}

	private String missionId;
	private String startDate;
	private String endDate;
	private String type;
}
