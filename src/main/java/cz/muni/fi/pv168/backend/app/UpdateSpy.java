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
 * Created by Michal on 5/4/2015.
 */
public class UpdateSpy {
    private JTextField textField1;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JButton updateButton;

    public JPanel getTopPanel() {
        return topPanel;
    }

    private JPanel topPanel;

    private SpyManager manager;

    public UpdateSpy(Spy spy, SpiesTableModel spiesModel, JFrame iFrame,JTable table) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        manager = new SpyManagerImpl(ds);

        updateButton.addActionListener(new ActionListener() {
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
                        "Do you really want to update this spy?",
                        "Spy update",
                        JOptionPane.YES_OPTION,
                        JOptionPane.NO_OPTION,
                        null,
                        options,
                        options[1]);
                if(n == JOptionPane.NO_OPTION)
                {
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
                    int row = table.getSelectedRow();
                    spiesModel.setValueAt(table.getValueAt(row,0), row, 0);
                    spiesModel.setValueAt(textField1.getText(),row,1);
                    spiesModel.setValueAt(textField3.getText(),row,2);
                    spiesModel.setValueAt(sqlDate,row,3);
                    spiesModel.setValueAt(textField4.getText(),row,4);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                spy.setFirstName(textField1.getText());
                spy.setLastName(textField3.getText());
                spy.setCodename(textField4.getText());

                manager.updateSpy(spy);
                spiesModel.fireTableDataChanged();
                iFrame.dispose();
            }
        });
    }
}
