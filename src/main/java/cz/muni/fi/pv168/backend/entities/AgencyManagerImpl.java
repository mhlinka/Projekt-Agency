package cz.muni.fi.pv168.backend.entities;

import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import cz.muni.fi.pv168.backend.ex.ServiceFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public class AgencyManagerImpl implements AgencyManager
{
    // no rowmapper apparently

    private final static Logger log = LoggerFactory.getLogger(AgencyManagerImpl.class);

    private final JdbcTemplate jdbc;
    private final TransactionTemplate transaction;

    public AgencyManagerImpl(DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
        transaction = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void addSpyToMission(Spy spy, Mission mission) throws ServiceFailureException, IllegalEntityException
    {
        validateSpyAndMission(spy, mission);

        transaction.execute(transactionStatus -> {
            int n = jdbc.update("UPDATE SPIES SET spies.missionId = ? WHERE spyId = ? AND spies.missionId IS NULL", mission.getMissionId(), spy.getSpyId());
            if (n == 0)
                throw new IllegalEntityException("Spy " + spy + " not found or already assigned to another mission");
            return null;
        });
    }

    @Override
    public void removeSpyFromMission(Spy spy, Mission mission) throws ServiceFailureException, IllegalEntityException
    {
        validateSpyAndMission(spy, mission);
        int n = jdbc.update("UPDATE SPIES SET spies.missionId = NULL WHERE spyId = ? AND spies.missionId = ?", spy.getSpyId(), mission.getMissionId());
        if(n!=1) throw new IllegalEntityException("spy id="+spy.getSpyId()+" is not assigned to mission with id ="+mission.getMissionId());
    }

    @Override
    public Mission findMissionWithSpy(Spy spy) throws ServiceFailureException,IllegalEntityException
    {
        log.debug("findMissionWithSpy({})", spy);
        validateSpy(spy);

        List<Mission> mission = jdbc.query(
                "SELECT missions.missionId, startDate, endDate, type " +
                        "FROM missions JOIN spies ON missions.missionId = spies.missionId " +
                        "WHERE spies.spyId = ?", MissionManagerImpl.missionMapper, spy.getSpyId());
        return mission.isEmpty() ? null : mission.get(0);
    }

    @Override
    public List<Spy> findSpiesOnMission(Mission mission) throws ServiceFailureException,IllegalEntityException
    {
        log.debug("findSpiesOnMission({})", mission);
        validateMission(mission);

        return jdbc.query(
                "SELECT spyId, firstname, lastname, codename, dateofbirth " +
                        "FROM spies JOIN missions ON missions.missionId = spies.missionId " +
                        "WHERE missions.missionId = ?", SpyManagerImpl.spyMapper, mission.getMissionId());
    }

    @Override
    public List<Spy> listUnassignedSpies() throws ServiceFailureException {
        log.debug("listUnassignedSpies()");
        return jdbc.query("SELECT spyId, firstname, lastname, codename, dateofbirth FROM SPIES WHERE missionId IS NULL", SpyManagerImpl.spyMapper);
    }

    private void validateSpyAndMission(Spy spy, Mission mission) throws IllegalEntityException
    {
        validateSpy(spy);
        validateMission(mission);
    }

    private void validateSpy(Spy spy)
    {
        if (spy == null) throw new IllegalEntityException("The spy is null.");
        if (spy.getSpyId() == null) throw new IllegalEntityException("The grave's id is null.");
    }

    private void validateMission(Mission mission)
    {
        if (mission == null) throw new IllegalEntityException("The mission is null.");
        if (mission.getMissionId() == null) throw new IllegalEntityException("The mission's id is null.");
    }
}
