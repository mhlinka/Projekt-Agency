package cz.muni.fi.pv168.backend;

import cz.muni.fi.pv168.backend.ex.ServiceFailureException;

import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public interface MissionManager
{
	void createMission(Mission mission) throws ServiceFailureException;
	Mission getMissionById(Long id) throws ServiceFailureException;
	List<Mission> getAllMissions() throws  ServiceFailureException;

	List<Mission> getMissionsOfType(MissionType type) throws ServiceFailureException;
	void updateMission(Mission mission) throws  ServiceFailureException;
	void deleteMission(Mission mission) throws ServiceFailureException;

}
