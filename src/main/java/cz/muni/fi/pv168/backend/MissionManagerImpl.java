package cz.muni.fi.pv168.backend;

import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FH on 27.2.2015.
 */
public class MissionManagerImpl implements MissionManager
{
	public static final RowMapper<Mission> missionMapper = (rs, rowNum) ->
	{
		MissionType missionType = rs.getString("type") == null ? null : MissionType.valueOf(rs.getString("type"));
		return new Mission(rs.getLong("missionId"), rs.getDate("startdate"), rs.getDate("endDate"), missionType);
	};
	private static Logger log = LoggerFactory.getLogger(MissionManagerImpl.class);
	private final JdbcTemplate jdbc;

	//
	public MissionManagerImpl(DataSource dataSource)
	{
		jdbc = new JdbcTemplate(dataSource);
		log.debug("MissionManagerImpl created");
	}

	@Override
	public void createMission(Mission mission)
	{
		validateMission(mission);
		log.debug("createMission({})", mission);

		Map<String, Object> params = new HashMap<>();
		params.put("startdate", mission.getStartDate());
		params.put("enddate", mission.getEndDate());
		MissionType type = mission.getType();
		params.put("type", type == null ? null : type.name());

		long id = new SimpleJdbcInsert(jdbc).withTableName("MISSIONS").usingGeneratedKeyColumns("missionid").executeAndReturnKey(params).longValue();
		mission.setMissionId(id);

		log.trace("mission is {}", mission.getMissionId());

	}

	@Override
	public Mission getMissionById(Long id)
	{
		validateId(id);

		log.debug("getMissionById({})", id);

		List<Mission> missions = jdbc.query("SELECT missionid, startdate,enddate,type FROM MISSIONS WHERE missionid=?", missionMapper, id);
		return missions.isEmpty() ? null : missions.get(0);
	}

	@Override
	public List<Mission> getAllMissions()
	{
		log.debug("getAllMissions()");
		return jdbc.query("SELECT missionid, startdate, enddate, type FROM MISSIONS", missionMapper);
	}

	@Override
	public List<Mission> getMissionsOfType(MissionType type)
	{
		log.debug("getMissionsOfType({})", type);
		return jdbc.query("SELECT missionid, startdate, enddate, type FROM MISSIONS WHERE TYPE=?", missionMapper, type.name());
	}

	@Override
	public void updateMission(Mission mission)
	{
		log.debug("updateMission({})", mission);
		java.sql.Date start = mission.getStartDate() == null ? null : new java.sql.Date(mission.getStartDate().getTime());
		java.sql.Date end = mission.getEndDate() == null ? null : new java.sql.Date(mission.getEndDate().getTime());
		String type = mission.getType() == null ? null : mission.getType().name();
		int updatedCount = jdbc.update("UPDATE MISSIONS SET startdate=?, enddate=?, type=? WHERE missionID = ?",
				start,
				end,
				type,
				mission.getMissionId());
		if (updatedCount != 1)
		{
			throw new IllegalEntityException("Update for mission " + mission.getMissionId() + " failed.");
		}
		//
		log.trace("mission is {}", mission.getMissionId());
	}

	@Override
	public void deleteMission(Mission mission)
	{
		validateMission(mission);
		log.debug("deleteMission({})", mission);
		long id = mission.getMissionId();

		int deletedCount = jdbc.update("DELETE FROM MISSIONS WHERE missionid=?", mission.getMissionId());

		if (deletedCount != 1)
		{
			throw new IllegalEntityException("Could not delete mission " + mission.getMissionId());
		}

		log.trace("mission id is {}", mission.getMissionId());
	}

	private void validateId(Long id)
	{
		if (id == null || id <= 0)
		{
			throw new IllegalArgumentException("The id parameter is invalid.");
		}
	}

	private void validateMission(Mission mission)
	{
		if (mission == null)
		{
			throw new IllegalArgumentException("The mission was null.");
		}

		if ((mission.getEndDate() != null) && (mission.getStartDate() != null))
		{
			if (mission.getStartDate().after(mission.getEndDate()))
			{
				throw new IllegalStateException("endDate is earlier than startDate");
			}
		}
	}
}
