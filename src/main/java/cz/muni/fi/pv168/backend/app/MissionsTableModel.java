package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.Mission;
import cz.muni.fi.pv168.backend.entities.MissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Michal on 5/2/2015.
 */
public class MissionsTableModel extends AbstractTableModel {

    private List<Mission> missions = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(MissionsTableModel.class);
	private static ResourceBundle bundle = ResourceBundle.getBundle("AppGeneral", Locale.getDefault());
	ResourceBundle enumValues = ResourceBundle.getBundle("MissionTypes",Locale.getDefault());
    @Override
    public int getRowCount() {
        return missions.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return mission.getMissionId();
            case 1:
                return mission.getStartDate();
            case 2:
                return mission.getEndDate();
            case 3:
                return enumValues.getString(mission.getType().toString());
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void createMission(Mission mission) {
        log.debug("createMissionTableModel({})");
        missions.add(mission);
        int lastRow = missions.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void deleteMission(int row)
    {
        log.debug("deleteMissionTableModel({})");
        missions.remove(row);
        fireTableRowsDeleted(row,row);
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return bundle.getString("Id");
            case 1:
                return bundle.getString("StartDate");
            case 2:
                return bundle.getString("EndDate");
            case 3:
                return bundle.getString("MissionType");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
            case 2:
                return java.sql.Date.class;
            case 3:
                return MissionType.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        switch (columnIndex) {
            case 0:
                mission.setMissionId((Long) aValue);
                break;
            case 1:
                mission.setStartDate((java.sql.Date) aValue);
                break;
            case 2:
                mission.setEndDate((java.sql.Date) aValue);
                break;
            case 3:
                mission.setType(MissionType.valueOf(Shared.getEnumStringFromTranslatedValue(aValue.toString())));
                break;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
