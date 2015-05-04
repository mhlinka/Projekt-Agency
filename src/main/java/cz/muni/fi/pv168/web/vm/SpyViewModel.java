package cz.muni.fi.pv168.web.vm;

import cz.muni.fi.pv168.backend.entities.Spy;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by FH on 4.5.2015.
 */
public class SpyViewModel
{
	private String spyId;
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private String codename;


	public static SpyViewModel fromSpy(Spy spy)
	{
		TimeZone tz = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		format.setTimeZone(tz);

		SpyViewModel model = new SpyViewModel();
		model.setSpyId(spy.getSpyId().toString());
		model.setFirstName(spy.getFirstName());
		model.setLastName(spy.getLastName());
		model.setCodename(spy.getCodename());
		model.setDateOfBirth(format.format(spy.getDateOfBirth()));

		return model;
	}

	public String getCodename()
	{
		return codename;
	}

	public void setCodename(String codename)
	{
		this.codename = codename;
	}

	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getSpyId()
	{
		return spyId;
	}

	public void setSpyId(String spyId)
	{
		this.spyId = spyId;
	}
}
