package cz.muni.fi.pv168.backend.entities;

import cz.muni.fi.pv168.backend.ex.IllegalEntityException;
import cz.muni.fi.pv168.backend.ex.ServiceFailureException;

import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public interface AgencyManager
{
	void addSpyToMission(Spy spy, Mission mission) throws ServiceFailureException, IllegalEntityException;
	void removeSpyFromMission(Spy spy, Mission mission) throws ServiceFailureException, IllegalEntityException;
	Mission findMissionWithSpy(Spy spy) throws ServiceFailureException, IllegalEntityException;
	List<Spy> findSpiesOnMission(Mission mission) throws ServiceFailureException, IllegalEntityException;
    List<Spy> listUnassignedSpies() throws  ServiceFailureException;
}
