package cz.muni.fi.pv168;

import cz.muni.fi.pv168.backend.entities.Mission;
import cz.muni.fi.pv168.backend.entities.MissionManager;
import cz.muni.fi.pv168.backend.entities.MissionManagerImpl;
import cz.muni.fi.pv168.backend.entities.MissionType;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import cz.muni.fi.pv168.utils.DBUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MissionManagerImplTest
{
	private MissionManager manager;
	private Comparator<Mission> missionIdComparator = (o1, o2) -> o1.getMissionId().compareTo(o2.getMissionId());
	private DataSource ds;

	@Before
	public void beforeTest() throws SQLException
	{
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
		this.ds = ds;
		this.manager = new MissionManagerImpl(ds);
		DBUtils.executeSqlScript(ds, MissionManagerImpl.class.getResourceAsStream("/tables.sql"));
	}

	@After
	public void dropTables() throws SQLException
	{
		DBUtils.executeSqlScript(ds, MissionManagerImpl.class.getResourceAsStream("/droptables.sql"));
	}

	@Test
	public void testCreateMission() throws Exception
	{
		try
		{
			manager.createMission(null);
			fail("A null mission was allowed to be created");
		}
		catch (IllegalArgumentException ex)
		{
			//ok
		}

		Mission mission = new Mission();
		Date start = new Date(100000);
		Date end = new Date(70000);
		mission.setStartDate(start);
		mission.setEndDate(end);
		try
		{
			manager.createMission(mission);
			fail("Mission which ended before it started was created.");
		}
		catch (IllegalStateException e)
		{

		}
	}

	@Test
	public void testGetMissionById() throws Exception
	{
		Mission mission = new Mission();
		manager.createMission(mission);
		Long id = mission.getMissionId();

		assertEquals(mission, manager.getMissionById(id));

		try
		{
			manager.getMissionById(-1L);
			fail("GetMissionByID was called with invalid parameter");
		}
		catch (IllegalArgumentException e)
		{

		}

		try
		{
			manager.getMissionById(null);
			fail("GetMissionByID was called with null ID");
		}
		catch (IllegalArgumentException e)
		{

		}
	}

	@Test
	public void testGetAllMissions() throws Exception
	{
		Mission m1 = new Mission();
		Mission m2 = new Mission();

		manager.createMission(m1);
		manager.createMission(m2);

		List<Mission> expected = Arrays.asList(m1, m2);
		List<Mission> actual = manager.getAllMissions();

		Collections.sort(expected, missionIdComparator);
		Collections.sort(actual, missionIdComparator);

		assertEquals("Successfully returned all the missions", expected, actual);
	}

	@Test
	public void testGetMissionsOfType() throws Exception
	{
		Mission m1 = new Mission();
		Mission m2 = new Mission();
		m1.setType(MissionType.ABDUCTION);
		m2.setType(MissionType.SABOTAGE);
		manager.createMission(m1);
		manager.createMission(m2);

		List<Mission> expected = Arrays.asList(m1);
		List<Mission> actual = manager.getMissionsOfType(MissionType.ABDUCTION);

		assertEquals("Did not return the correct mission(s).", expected, actual);
	}

	@Test
	public void testUpdateMission() throws Exception
	{
		//type
		Mission mission = new Mission();
		mission.setType(MissionType.ABDUCTION);

		manager.createMission(mission);
		Long missionId = mission.getMissionId();

		mission = manager.getMissionById(missionId);
		mission.setType(MissionType.ASSASSINATION);
		manager.updateMission(mission);

		assertEquals(MissionType.ASSASSINATION, mission.getType());

		//start
		mission = new Mission();
		mission.setStartDate(new Date(4000));

		manager.createMission(mission);
		missionId = mission.getMissionId();

		mission = manager.getMissionById(missionId);
		mission.setStartDate(new Date(8000));
		manager.updateMission(mission);

		assertEquals(new Date(8000), mission.getStartDate());


		//end
		mission = new Mission();
		mission.setEndDate(new Date(4000));

		manager.createMission(mission);
		missionId = mission.getMissionId();

		mission = manager.getMissionById(missionId);
		mission.setEndDate(new Date(8000));
		manager.updateMission(mission);

		assertEquals(new Date(8000), mission.getEndDate());
	}

	@Test
	public void testDeleteMission() throws Exception
	{
		Mission m1 = new Mission();
		Mission m2 = new Mission();

		manager.createMission(m1);
		manager.createMission(m2);

		manager.deleteMission(m1);

		List<Mission> expected = Arrays.asList(m2);
		List<Mission> actual = manager.getAllMissions();

		assertEquals("Deletion was unsuccessful", expected, actual);

		try
		{
			manager.deleteMission(null);
			fail("Null was allowed to be passed to deleteMission()");
		}
		catch (IllegalArgumentException e)
		{

		}
	}
}