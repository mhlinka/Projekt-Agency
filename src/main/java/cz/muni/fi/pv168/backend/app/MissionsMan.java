package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.Mission;
import cz.muni.fi.pv168.backend.entities.MissionManager;
import cz.muni.fi.pv168.backend.entities.MissionManagerImpl;
import cz.muni.fi.pv168.backend.entities.MissionType;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Michal on 5/3/2015.
 */
public class MissionsMan {
    private JPanel topPanel;
    private JTable table1;
    private JTextField textField1;
    private JButton deleteMissionButton;
    private JButton updateMissionButton;
    private JButton getMissionByIDButton;
    private JButton createMissionButton;
    private JButton showAllMissionsButton;
    private JButton getMissionByTypeButton;
    private JComboBox missionTypeSelect;

    private MissionManager manager;
    private static Logger log = LoggerFactory.getLogger(MissionsMan.class);
	private static ResourceBundle bundle = ResourceBundle.getBundle("MissionsMan", Locale.getDefault());
    public MissionsMan() {
        table1.setModel(new MissionsTableModel());
        table1.setDefaultRenderer(Color.class, new ColorCellRenderer());
        JComboBox typeComboBox = new JComboBox();
        for (MissionType type : MissionType.values()) {
            typeComboBox.addItem(type);
        }
        MissionsTableModel missionsModel = (MissionsTableModel) table1.getModel();
		Shared.AddMissionTypes(missionTypeSelect);
        table1.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(typeComboBox));

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        manager = new MissionManagerImpl(ds);
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SABOTAGE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SURVEILLANCE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SABOTAGE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SABOTAGE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SURVEILLANCE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SURVEILLANCE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SURVEILLANCE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ASSASSINATION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ASSASSINATION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SURVEILLANCE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.SURVEILLANCE));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ASSASSINATION));
        manager.createMission(new Mission(null, new Date("01/01/1234"), new Date("01/01/1234"), MissionType.ABDUCTION));

        for(Mission mission : manager.getAllMissions())
        {
            missionsModel.createMission(mission);
        }
        createMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("createMissionButton({})");
                manager = new MissionManagerImpl(ds);
                Mission mission = new Mission();
                JFrame iFrame = new JFrame();
                iFrame.setTitle(bundle.getString("AddMission"));
                iFrame.add(new AddMission(mission,missionsModel,iFrame).getTopPanel());
                iFrame.setContentPane(new AddMission(mission,missionsModel,iFrame).getTopPanel());
                iFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                iFrame.setPreferredSize(new Dimension(600, 200));

                iFrame.pack();
                iFrame.setVisible(true);
                textField1.setText("");
            }
        });
        deleteMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("deleteMissionButton({})");
                JFrame frame = new JFrame();
                Object[] options = {bundle.getString("Yes"),
                        bundle.getString("No")};
                int n = JOptionPane.showOptionDialog(frame,
                        bundle.getString("ReallyDelete"),
                        bundle.getString("DeleteMission"),
                        JOptionPane.YES_OPTION,
                        JOptionPane.NO_OPTION,
                        null,
                        options,
                        options[1]);
                if(n == JOptionPane.NO_OPTION)
                {
                    return;
                }
                int[] rows = table1.getSelectedRows();
                int counter = 0;
                for(int row : rows)
                {
                    manager.deleteMission(manager.getMissionById((Long) table1.getValueAt(row-counter,0)));
                    missionsModel.deleteMission(row - counter);
                    counter++;
                }
                missionsModel.fireTableDataChanged();
                textField1.setText("");
            }
        });
        updateMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("updateMissionButton({})");
                JFrame frame = new JFrame();
                int[] rows = table1.getSelectedRows();
                if(rows.length != 1)
                {
                    JOptionPane.showMessageDialog(frame,bundle.getString("e_SelectOne"));
                    return;
                }
                int row = table1.getSelectedRow();
                Mission mission = manager.getMissionById((Long) table1.getValueAt(row,0));
                JFrame iFrame = new JFrame();
                iFrame.setTitle(bundle.getString("UpdateMission"));
                iFrame.add(new UpdateMission(mission,missionsModel,iFrame,table1).getTopPanel());
                iFrame.setContentPane(new UpdateMission(mission,missionsModel,iFrame,table1).getTopPanel());
                iFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                iFrame.setPreferredSize(new Dimension(600, 200));

                iFrame.pack();
                iFrame.setVisible(true);
                textField1.setText("");
            }
        });
        getMissionByIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("getMissionByIDButton({})");
                try {
                    if(textField1.getText().equals(""))
                    {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame,bundle.getString("IdMustBeFilled"));
                        return;
                    }
                    Long id = Long.parseLong(textField1.getText());
                    Mission mission = manager.getMissionById(id);
                    if(mission == null)
                    {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame,bundle.getString("NoSuchMission"));
                        return;
                    } else {
                        int rowCount = table1.getRowCount();
                        for(int i = 0; i < rowCount; i++)
                        {
                            missionsModel.deleteMission(0);
                        }
                        for (Mission m : manager.getAllMissions()) {
                            if(m.getMissionId() == id) {
                                missionsModel.createMission(mission);
                            }
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,bundle.getString("e_ArgumentNumber"));
                    ex.printStackTrace();
                }

                missionsModel.fireTableDataChanged();
                textField1.setText("");
            }
        });
        getMissionByTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("getMissionByTypeButton({})");
                int rowCount = table1.getRowCount();
                for(int i = 0; i < rowCount; i++)
                {
                    missionsModel.deleteMission(0);
                }
				MissionType type = MissionType.valueOf(Shared.getEnumStringFromTranslatedValue(missionTypeSelect.getSelectedItem().toString()));
                for (Mission mission : manager.getMissionsOfType(type)) {
                    missionsModel.createMission(mission);
                }
            }
        });
        showAllMissionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("showAllMissionsButton({})");
                int rowCount = table1.getRowCount();
                for(int i = 0; i < rowCount; i++)
                {
                    missionsModel.deleteMission(0);
                }
                for (Mission mission : manager.getAllMissions()) {
                    missionsModel.createMission(mission);
                }
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("spies");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(new MissionsMan().topPanel);
                frame.setPreferredSize(new Dimension(800, 600));

                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public JPanel getTopPanel() {
        return topPanel;
    }
}
