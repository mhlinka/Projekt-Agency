package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.Mission;
import cz.muni.fi.pv168.backend.entities.MissionManager;
import cz.muni.fi.pv168.backend.entities.MissionManagerImpl;
import cz.muni.fi.pv168.backend.entities.MissionType;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Michal on 5/5/2015.
 */
public class AddMission {
    private JPanel topPanel;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JButton addButton;
    private JComboBox comboBox6;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox7;

    public JPanel getTopPanel() {
        return topPanel;
    }

    private MissionManager manager;

    public AddMission(Mission mission, MissionsTableModel model, JFrame iFrame) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        manager = new MissionManagerImpl(ds);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame2 = new JFrame();
                Object[] options = {"Yes",
                        "No"};
                int n = JOptionPane.showOptionDialog(frame2,
                        "Create mission?",
                        "Create mission",
                        JOptionPane.YES_OPTION,
                        JOptionPane.NO_OPTION,
                        null,
                        options,
                        options[1]);
                if (n == JOptionPane.NO_OPTION) {
                    return;
                }
                String day = comboBox2.getSelectedItem().toString();
                String month = comboBox1.getSelectedItem().toString();
                String year = comboBox3.getSelectedItem().toString();
                String start = year + "-" + month + "-" + day;
                day = comboBox5.getSelectedItem().toString();
                month = comboBox4.getSelectedItem().toString();
                year = comboBox6.getSelectedItem().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String end = year + "-" + month + "-" + day;

                try {
                    java.util.Date date = sdf.parse(start);
                    java.sql.Date sqlDateStart = new java.sql.Date(date.getTime());
                    date = sdf.parse(end);
                    java.sql.Date sqlDateEnd = new java.sql.Date(date.getTime());

                    if (sqlDateStart.after(sqlDateEnd))
                    {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame,"Mission can not start after it has already ended.");
                        return;
                    }

                    mission.setStartDate(sqlDateStart);
                    mission.setEndDate(sqlDateEnd);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                mission.setType(MissionType.valueOf(comboBox7.getSelectedItem().toString()));

                manager.createMission(mission);
                model.createMission(mission);
                model.fireTableDataChanged();
                iFrame.dispose();
            }
        });
    }
}
