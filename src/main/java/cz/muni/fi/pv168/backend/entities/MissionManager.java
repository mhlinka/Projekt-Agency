package cz.muni.fi.pv168.backend.entities;
import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public interface MissionManager
{
	void createMission(Mission mission);
	Mission getMissionById(Long id);
	List<Mission> getAllMissions();

	List<Mission> getMissionsOfType(MissionType type);
	void updateMission(Mission mission);
	void deleteMission(Mission mission);

}
