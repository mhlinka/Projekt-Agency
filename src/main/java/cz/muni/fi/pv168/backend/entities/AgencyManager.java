package cz.muni.fi.pv168.backend.entities;

import cz.muni.fi.pv168.backend.ex.IllegalEntityException;

import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public interface AgencyManager
{
	void addSpyToMission(Spy spy, Mission mission)throws IllegalEntityException;
	void removeSpyFromMission(Spy spy, Mission mission) throws IllegalEntityException;
	Mission findMissionWithSpy(Spy spy) throws IllegalEntityException;
	List<Spy> findSpiesOnMission(Mission mission) throws  IllegalEntityException;
    List<Spy> listUnassignedSpies();
}
