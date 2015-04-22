package cz.muni.fi.pv168;

import cz.muni.fi.pv168.backend.entities.*;
import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import cz.muni.fi.pv168.utils.DBUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class AgencyManagerImplTest
{
	private AgencyManager manager;
    private SpyManager spyManager;
    private MissionManager missionManager;

	private Comparator<Spy> spyIdComparator = (o1, o2) -> o1.getSpyId().compareTo(o2.getSpyId());
	private DataSource ds;

	@Before
	public void beforeTest() throws SQLException
	{
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
		this.ds = ds;
        this.manager = new AgencyManagerImpl(ds);
        this.spyManager = new SpyManagerImpl(ds);
        this.missionManager = new MissionManagerImpl(ds);
        DBUtils.executeSqlScript(ds, MissionManagerImpl.class.getResourceAsStream("/tables.sql"));
    }

	@After
	public void dropTables() throws SQLException, NullPointerException
	{
		DBUtils.executeSqlScript(ds, MissionManagerImpl.class.getResourceAsStream("/droptables.sql"));
	}
	@Test
	public void testAddSpyToMission()
	{
		Spy spy = new Spy();
		Mission mission = new Mission();

        spyManager.addSpy(spy);
        missionManager.createMission(mission);

		try
		{
			manager.addSpyToMission(null, mission);
			fail("addSpyToMission added null to a mission");
		} catch (IllegalEntityException e)
		{

		}
		try
		{
			manager.addSpyToMission(spy, null);
			fail("addSpyToMission added a spy to null");
		} catch (IllegalEntityException e)
		{

		}

		manager.addSpyToMission(spy, mission);

		try
		{
			manager.addSpyToMission(spy, mission);
			fail("addSpyToMission added a spy to a mission he was already assigned to");
		} catch (IllegalEntityException e)
		{

		}
		assertEquals("does not assign mission to spy", mission, manager.findMissionWithSpy(spy));

		List<Spy> spies = manager.findSpiesOnMission(mission);
		assertEquals("does not add spy to mission", 1, spies.size());

		Spy spy2 = new Spy();
        spyManager.addSpy(spy2);

		manager.addSpyToMission(spy2, mission);
		spies = manager.findSpiesOnMission(mission);
		assertEquals("does not add a second spy to mission", 2, spies.size());
	}

	@Test
	public void testRemoveSpyFromMission()
	{
		Spy spy1 = new Spy();
		Mission mission = new Mission();

        spyManager.addSpy(spy1);
        missionManager.createMission(mission);

		try
		{
			manager.removeSpyFromMission(null, mission);
			fail("removeSpyFromMission removed null from mission");
		} catch (IllegalEntityException e)
		{

		}
		try
		{
			manager.removeSpyFromMission(spy1, null);
			fail("removeSpyFromMission removed spy from null");
		} catch (IllegalEntityException e)
		{

		}
		try
		{
			manager.removeSpyFromMission(spy1, mission);
			fail("removeSpyFromMission removed spy from mission he was not assigned to");
		} catch (IllegalEntityException e)
		{

		}

		Spy spy2 = new Spy();
        spyManager.addSpy(spy2);

		manager.addSpyToMission(spy1, mission);
		manager.addSpyToMission(spy2, mission);

		//uz sa testuje inde
		//assertNotEquals("does not add second spy to mission", Arrays.asList(spy1), manager.findSpiesOnMission(mission));

		manager.removeSpyFromMission(spy2, mission);
		assertEquals("does not remove spy from mission", Arrays.asList(spy1), manager.findSpiesOnMission(mission));

		//inde
		//assertEquals("does not show spy in unassigned spies", Arrays.asList(spy2), manager.listUnassignedSpies());

	}

	@Test
	public void testFindMissionWithSpy()
	{
		Spy spy = new Spy();
		Mission mission = new Mission();

        spyManager.addSpy(spy);
        missionManager.createMission(mission);

		try
		{
			manager.findMissionWithSpy(null);
			fail("you cannot search for mission with null");
		} catch (IllegalEntityException e)
		{

		}

		manager.addSpyToMission(spy, mission);

		Spy spy2 = new Spy();
        spyManager.addSpy(spy2);

		assertNull("does not return null", manager.findMissionWithSpy(spy2));
		assertEquals("does not return the mission", mission, manager.findMissionWithSpy(spy));
	}

	@Test
	public void testFindSpiesOnMission()
	{
		try
		{
			manager.findSpiesOnMission(null);
			fail("you can not search for spies with null");
		} catch (IllegalEntityException e)
		{

		}

		Mission mission = new Mission();
        missionManager.createMission(mission);

		List<Spy> empty = new ArrayList<Spy>();

		assertEquals("does not return empty list", manager.findSpiesOnMission(mission), empty);

		Spy spy = new Spy();
		Spy spy2 = new Spy();
        spyManager.addSpy(spy);
        spyManager.addSpy(spy2);

		manager.addSpyToMission(spy, mission);
		manager.addSpyToMission(spy2, mission);

		List<Spy> expected = Arrays.asList(spy, spy2);
		List<Spy> actual = manager.findSpiesOnMission(mission);
		Collections.sort(expected, spyIdComparator);
		Collections.sort(actual, spyIdComparator);
		assertEquals("does not return the array of spies on mission", expected, actual);
	}

	@Test
	public void testListUnassignedSpies()
	{
		Spy spy = new Spy();
		Mission mission = new Mission();

        spyManager.addSpy(spy);
        missionManager.createMission(mission);

		assertEquals("does not return unassigned spy", Arrays.asList(spy), manager.listUnassignedSpies());

		manager.addSpyToMission(spy, mission);
		List<Spy> empty = new ArrayList<Spy>();
		assertEquals("does not return empty list", empty, manager.listUnassignedSpies());

		Spy spy2 = new Spy();
		Spy spy3 = new Spy();
		Mission mission2 = new Mission();

        spyManager.addSpy(spy2);
        spyManager.addSpy(spy3);
        missionManager.createMission(mission2);

		manager.addSpyToMission(spy2, mission2);
		assertEquals("does not work with multiple missions", Arrays.asList(spy3), manager.listUnassignedSpies());

		manager.addSpyToMission(spy3, mission2);
		assertEquals("does not work with multiple spies and missions", empty, manager.listUnassignedSpies());

		manager.removeSpyFromMission(spy3, mission2);
		assertEquals("does not work with multiple spies and missions", Arrays.asList(spy3), manager.listUnassignedSpies());
	}
}