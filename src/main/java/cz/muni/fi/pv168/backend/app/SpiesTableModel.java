package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.Spy;
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
public class SpiesTableModel extends AbstractTableModel {

    private List<Spy> spies = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(SpiesTableModel.class);
	private static ResourceBundle bundle = ResourceBundle.getBundle("AppGeneral", Locale.getDefault());
    @Override
    public int getRowCount() {
        return spies.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Spy spy = spies.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return spy.getSpyId();
            case 1:
                return spy.getFirstName();
            case 2:
                return spy.getLastName();
            case 3:
                return spy.getDateOfBirth();
            case 4:
                return spy.getCodename();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    public void addSpy(Spy spy) {
        log.debug("addSpyTableModel({})");
        spies.add(spy);
        int lastRow = spies.size() - 1;
        fireTableRowsInserted(lastRow, lastRow);
    }

    public void removeSpy(int row)
    {
        log.debug("removeSpyTableModel({})");
        spies.remove(row);
        fireTableRowsDeleted(row,row);
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return bundle.getString("Id");
            case 1:
                return bundle.getString("FirstName");
            case 2:
                return bundle.getString("LastName");
            case 3:
                return bundle.getString("DateOfBirth");
            case 4:
                return bundle.getString("Codename");
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
                return String.class;
            case 3:
                //return java.util.Date.class;
                return java.sql.Date.class;
            case 4:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Spy spy = spies.get(rowIndex);
        switch (columnIndex) {
            case 0:
                spy.setSpyId((Long) aValue);
                break;
            case 1:
                spy.setFirstName((String) aValue);
                break;
            case 2:
                spy.setLastName((String) aValue);
                break;
            case 3:
                spy.setDateOfBirth((java.sql.Date) aValue);
                break;
            case 4:
                spy.setCodename((String) aValue);
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
            case 4:
                return false;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
