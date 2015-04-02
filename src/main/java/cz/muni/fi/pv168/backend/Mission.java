package cz.muni.fi.pv168.backend;

import java.util.Date;

public class Mission
{
	private Long missionId;
	private Date startDate;
	private Date endDate;
	private MissionType type;

	public Mission()
	{

	}

	public Mission(Long missionId, Date startDate, Date endDate, MissionType type)
	{
		this.missionId = missionId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		int result = Long.valueOf(missionId).hashCode();
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Mission mission = (Mission) o;

		if (!missionId.equals(mission.missionId)) return false;
		if (endDate != null ? !endDate.equals(mission.endDate) : mission.endDate != null) return false;
		if (startDate != null ? !startDate.equals(mission.startDate) : mission.startDate != null) return false;
		if (type != mission.type) return false;

		return true;
	}

	@Override
	public String toString()
	{
		return "Mission{" +
				"missionId=" + missionId +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", type=" + type +
				'}';
	}

	public Long getMissionId()
	{
		return missionId;
	}

	public void setMissionId(Long missionId)
	{
		this.missionId = missionId;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public MissionType getType()
	{
		return type;
	}

	public void setType(MissionType type)
	{
		this.type = type;
	}
}
