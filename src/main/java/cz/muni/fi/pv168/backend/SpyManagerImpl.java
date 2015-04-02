package cz.muni.fi.pv168.backend;

import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import cz.muni.fi.pv168.backend.ex.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FH on 27.2.2015.
 */
public class SpyManagerImpl implements SpyManager
{
	public static final RowMapper<Spy> spyMapper = (rs, rowNum) -> {
		java.sql.Date birthdate = rs.getDate("dateofbirth");
		return new Spy(
				rs.getLong("spyid"),
				rs.getString("firstname"),
				rs.getString("lastname"),
				birthdate == null ? null : (Date) birthdate,
				rs.getString("codename"));
	};
	private static Logger log = LoggerFactory.getLogger(SpyManagerImpl.class);
	private final JdbcTemplate jdbc;

	public SpyManagerImpl(DataSource dataSource)
	{
		log.debug("SpyManagerImpl created");
		jdbc = new JdbcTemplate(dataSource);
	}

	@Override
	public void addSpy(Spy spy)
	{
		validateSpy(spy);
		log.debug("addSpy({})", spy);

		if (spy.getSpyId() != null) throw new IllegalEntityException("The spy's id is already set");

		Map<String, Object> params = new HashMap<>();
		params.put("firstname", spy.getFirstName());
		params.put("lastname", spy.getLastName());
		params.put("dateofbirth", spy.getDateOfBirth());
		params.put("codename", spy.getCodename());

		long id = new SimpleJdbcInsert(jdbc).withTableName("SPIES").usingGeneratedKeyColumns("spyid").executeAndReturnKey(params).longValue();
		spy.setSpyId(id);
		log.trace("spy is={}", spy.getSpyId());
	}

	@Override
	public void removeSpy(Spy spy)
	{
		validateSpy(spy);
		log.debug("removeSpy({})", spy);
		removeSpy(spy.getSpyId());
		log.trace("spy is={}", spy.getSpyId());
	}

	@Override
	public void removeSpy(Long id)
	{
		validateId(id);
		log.debug("removeSpy with id({})", id);

		int deletedCount = jdbc.update("DELETE FROM SPIES WHERE spyid=?", id);
		if (deletedCount != 1)
		{
			throw new IllegalEntityException("Spy " + id + " was not deleted.");
		}
	}

	@Override
	public void updateSpy(Spy spy) throws ServiceFailureException
	{
		log.debug("updateSpy({})", spy);
		validateSpy(spy);

		int n = jdbc.update("UPDATE SPIES SET firstname=?, lastname=?, dateofbirth=?, codename=? WHERE spyid=?",
				spy.getFirstName(),
				spy.getLastName(),
				spy.getDateOfBirth() == null ? null : new java.sql.Date(spy.getDateOfBirth().getTime()),
				spy.getCodename(),
				spy.getSpyId());
		if (n != 1) throw new IllegalEntityException("Spy" + spy.getSpyId() + "not updated.");

		log.trace("spy is={}", spy.getSpyId());
	}

	@Override
	public Spy findSpyById(Long id) throws ServiceFailureException
	{
		validateId(id);

		List<Spy> retrievedSpies = jdbc.query(
				"SELECT spyId, firstname, lastname, dateofbirth, codename FROM SPIES WHERE spyId=?"
				, spyMapper, id);

		log.debug("findSpyById({})", id);
		return retrievedSpies.isEmpty() ? null : retrievedSpies.get(0);
	}

	@Override
	public List<Spy> listSpies() throws ServiceFailureException
	{
		log.debug("listSpies({})");
		return jdbc.query("SELECT spyid, firstname, lastname, dateofbirth, codename FROM SPIES", spyMapper);
	}

	private void validateId(Long id)
	{
		if (id == null || id <= 0)
		{
			throw new IllegalArgumentException("The id parameter is invalid.");
		}
	}

	private void validateSpy(Spy spy)
	{
		if (spy == null)
		{
			throw new IllegalArgumentException("The spy was null.");
		}
		//validateId(spy.getSpyId());
	}

}
