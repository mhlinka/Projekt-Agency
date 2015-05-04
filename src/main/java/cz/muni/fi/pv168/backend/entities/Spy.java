package cz.muni.fi.pv168.backend.entities;

import java.util.Date;

/**
 * Created by FH on 27.2.2015.
 */
public class Spy
{
	private Long spyId;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String codename;

	public Spy()
	{

	}

	public Spy(Long SpyId, String firstName, String lastName, Date dateOfBirth, String codeName)
	{
		this.spyId = SpyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.codename = codeName;
	}

	@Override
	public int hashCode()
	{
		int result = spyId.hashCode();
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
		result = 31 * result + (codename != null ? codename.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Spy spy = (Spy) o;

		if (codename != null ? !codename.equals(spy.codename) : spy.codename != null) return false;
		if (dateOfBirth != null ? !dateOfBirth.equals(spy.dateOfBirth) : spy.dateOfBirth != null) return false;
		if (firstName != null ? !firstName.equals(spy.firstName) : spy.firstName != null) return false;
		if (spyId != null ? !spyId.equals(spy.spyId) : spy.spyId != null) return false;
		if (lastName != null ? !lastName.equals(spy.lastName) : spy.lastName != null) return false;

		return true;
	}

	@Override
	public String toString()
	{
		return "Spy{" +
				"spyId=" + spyId +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", dateOfBirth=" + dateOfBirth +
				", codename='" + codename + '\'' +
				'}';
	}

	public Long getSpyId()
	{
		return spyId;
	}

	public void setSpyId(Long spyId)
	{
		this.spyId = spyId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getCodename()
	{
		return codename;
	}

	public void setCodename(String codename)
	{
		this.codename = codename;
	}
}
