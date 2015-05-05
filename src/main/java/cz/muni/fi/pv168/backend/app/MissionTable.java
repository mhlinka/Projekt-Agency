package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michal on 5/5/2015.
 */
public class MissionTable {
    private JTable table1;
    private JButton selectButton;
    private JPanel topPanel;

    private MissionManager manager;
    private AgencyManager agencyManager;
    private MissionsTableModel model;
    private static Logger log = LoggerFactory.getLogger(MissionTable.class);

    public JPanel getTopPanel() {
        return topPanel;
    }


    public MissionTable(String s,Spy spy,SpiesTableModel spyModel, JFrame iFrame, JTable table) {

        table1.setModel(new MissionsTableModel());
        table1.setDefaultRenderer(Color.class, new ColorCellRenderer());
        this.model = (MissionsTableModel) table1.getModel();
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        this.manager = new MissionManagerImpl(ds);
        this.agencyManager = new AgencyManagerImpl(ds);
        for(Mission mission : manager.getAllMissions())
        {
            model.createMission(mission);
        }

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.getSelectedRowCount() != 1)
                {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "You must select one mission.");
                    return;
                }
                if(s.equals("add")) {
                    JFrame frame2 = new JFrame();
                    Object[] options = {"Yes",
                            "No"};
                    int n = JOptionPane.showOptionDialog(frame2,
                            "Add spy to this mission?",
                            "Add spy to mission",
                            JOptionPane.YES_OPTION,
                            JOptionPane.NO_OPTION,
                            null,
                            options,
                            options[1]);
                    if (n == JOptionPane.NO_OPTION) {
                        return;
                    }
                    int row = table1.getSelectedRow();
                    agencyManager.addSpyToMission(spy, manager.getMissionById((Long) table1.getValueAt(row, 0)));
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (spy.getSpyId() == spyModel.getValueAt(i, 0)) {
                            spyModel.removeSpy(i);
                        }
                    }
                } else {
                    int rowCount = table.getRowCount();
                    for(int i = 0; i < rowCount; i++)
                    {
                        spyModel.removeSpy(0);
                    }
                    int row = table1.getSelectedRow();
                    for(Spy spy2 : agencyManager.findSpiesOnMission(manager.getMissionById((Long) table1.getValueAt(row, 0)))) {
                        spyModel.addSpy(spy2);
                    }
                }
                spyModel.fireTableDataChanged();
                iFrame.dispose();
            }
        });
    }
}
