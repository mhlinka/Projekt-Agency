package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.Spy;
import cz.muni.fi.pv168.backend.entities.SpyManager;
import cz.muni.fi.pv168.backend.entities.SpyManagerImpl;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Michal on 5/5/2015.
 */
public class AddSpy {
    private JPanel topPanel;
    private JTextField textField1;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JButton addButton;

    public JPanel getTopPanel() {
        return topPanel;
    }

    private SpyManager manager;

    public AddSpy(Spy spy, SpiesTableModel spiesModel, JFrame iFrame) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        manager = new SpyManagerImpl(ds);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField1.getText().equals("") || textField3.getText().equals("") || textField4.getText().equals("")) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "The fields 'First name', 'Last name', 'Codename' must be filled.");
                    return;
                }
                JFrame frame2 = new JFrame();
                Object[] options = {"Yes",
                        "No"};
                int n = JOptionPane.showOptionDialog(frame2,
                        "Do you really want to add this spy?",
                        "Add spy",
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
                String s = year + "-" + month + "-" + day;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    java.util.Date date = sdf.parse(s);
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    System.out.println(sqlDate);
                    System.out.println(date);
                    spy.setDateOfBirth(sqlDate);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                spy.setFirstName(textField1.getText());
                spy.setLastName(textField3.getText());
                spy.setCodename(textField4.getText());
                manager.addSpy(spy);
                spiesModel.addSpy(spy);
                spiesModel.fireTableDataChanged();
                iFrame.dispose();
            }
        });
    }
}
