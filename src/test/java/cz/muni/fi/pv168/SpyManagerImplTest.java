package cz.muni.fi.pv168;

import cz.muni.fi.pv168.backend.Spy;
import cz.muni.fi.pv168.backend.SpyManager;
import cz.muni.fi.pv168.backend.SpyManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DBUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class SpyManagerImplTest
{
	private static Comparator<Spy> idComparator = (o1, o2) -> o1.getSpyId().compareTo(o2.getSpyId());
	private SpyManager manager;
	private DataSource ds;

	@Before
	public void beforeTest() throws SQLException
	{
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl("jdbc:derby:memory:gravemgr-test;create=true");
		this.ds = ds;
		this.manager = new SpyManagerImpl(ds);

		DBUtils.executeSqlScript(ds, SpyManager.class.getResourceAsStream("/tables.sql"));
	}

	@After
	public void dropTables() throws SQLException
	{
		DBUtils.executeSqlScript(ds, SpyManager.class.getResourceAsStream("/droptables.sql"));
	}
	@Test
	public void testAddSpy()
	{
		Spy spy = new Spy();
		spy.setCodename("Stary Jano");

		manager.addSpy(spy);
		assertNotNull("ID was not assigned", spy.getSpyId());

		Spy expectedSpy = manager.findSpyById(spy.getSpyId());
		assertEquals("findSpyById did not find the spy correctly", spy, expectedSpy);

		spy = new Spy();
		try
		{
			manager.findSpyById(-1L); //negative id
			fail("Negative ID was allowed.");
		}
		catch (IllegalArgumentException e)
		{
			//ok
		}

		try
		{
			manager.addSpy(null);
			fail("Null spy was allowed to be added.");
		}
		catch (IllegalArgumentException e)
		{
			//ok
		}
	}

	@Test
	public void testUniqueID()
	{
		Spy spy = new Spy();
		Spy spy2 = new Spy();
		manager.addSpy(spy);
		manager.addSpy(spy2);
		assertTrue("IDs are not unique", !spy.getSpyId().equals(spy2.getSpyId()));
	}

	@Test
	public void testListAll()
	{
		assertTrue(manager.listSpies().isEmpty());

		Spy spy1 = new Spy();
		Spy spy2 = new Spy();

		manager.addSpy(spy1);
		manager.addSpy(spy2);

		List<Spy> expected = Arrays.asList(spy1, spy2);
		List<Spy> actual = manager.listSpies();

		Collections.sort(actual, idComparator);
		Collections.sort(expected, idComparator);

		assertEquals(expected, actual);
	}

	@Test
	public void testRemoveSpy() throws Exception
	{
		Spy spy1 = new Spy();
		Spy spy2 = new Spy();
		manager.addSpy(spy1);
		manager.addSpy(spy2);
		manager.removeSpy(spy1);
		List<Spy> expected = Arrays.asList(spy2);
		List<Spy> actual = manager.listSpies();
		assertEquals("removeSpy was unsuccessful", expected, actual);
	}

	@Test
	public void testRemoveSpyById() throws Exception
	{
		Spy spy1 = new Spy();
		Spy spy2 = new Spy();
		manager.addSpy(spy1);
		manager.addSpy(spy2);
		manager.removeSpy(spy1.getSpyId());
		List<Spy> expected = Arrays.asList(spy2);
		List<Spy> actual = manager.listSpies();
		assertEquals("removeSpyById was unsuccessful", expected, actual);
	}

	@Test
	public void testUpdateSpy() throws Exception
	{
		//codename
		Spy spy = new Spy();
		spy.setCodename("Dano");
		manager.addSpy(spy);
		Long id = spy.getSpyId();

		spy = manager.findSpyById(id);
		spy.setCodename("Peder");
		manager.updateSpy(spy);

		assertEquals("Peder", spy.getCodename());

		//birthdate
		spy = new Spy();
		spy.setDateOfBirth(new Date(9000));
		manager.addSpy(spy);
		id = spy.getSpyId();

		spy = manager.findSpyById(id);
		Date dateOfBirth = new Date(10000);
		spy.setDateOfBirth(dateOfBirth);
		manager.updateSpy(spy);

		assertEquals(dateOfBirth, spy.getDateOfBirth());

		//first name
		spy = new Spy();
		spy.setFirstName("Robo");
		manager.addSpy(spy);
		id = spy.getSpyId();

		spy = manager.findSpyById(id);
		spy.setFirstName("Brigita");
		manager.updateSpy(spy);

		assertEquals("Brigita", spy.getFirstName());

		//last name
		spy = new Spy();
		spy.setLastName("Novakova");
		manager.addSpy(spy);
		id = spy.getSpyId();

		spy = manager.findSpyById(id);
		spy.setLastName("Kvalitna");
		manager.updateSpy(spy);

		assertEquals("Kvalitna", spy.getLastName());
	}
}