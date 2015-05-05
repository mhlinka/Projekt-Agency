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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Michal on 5/5/2015.
 */
public class UpdateMission {
    private JPanel topPanel;
    private JButton updateButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox missionTypeSelect;

    public JPanel getTopPanel() {
        return topPanel;
    }

    private MissionManager manager;
	private static ResourceBundle bundle = ResourceBundle.getBundle("AppAddMission", Locale.getDefault());
	private static ResourceBundle enumValues = ResourceBundle.getBundle("MissionTypes", Locale.getDefault());
	public UpdateMission(Mission mission, MissionsTableModel model, JFrame iFrame, JTable table) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        manager = new MissionManagerImpl(ds);

		Shared.AddMissionTypes(missionTypeSelect);
		missionTypeSelect.setSelectedItem(enumValues.getString(mission.getType().toString()));
        updateButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFrame frame2 = new JFrame();
				Object[] options = {bundle.getString("Yes"),
						bundle.getString("No")};
				int n = JOptionPane.showOptionDialog(frame2,
						bundle.getString("UpdateMissionQuestion"),
						bundle.getString("UpdateMission"),
						JOptionPane.YES_OPTION,
						JOptionPane.NO_OPTION,
						null,
						options,
						options[1]);
				if (n == JOptionPane.NO_OPTION)
				{
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

				try
				{
					java.util.Date date = sdf.parse(start);
					java.sql.Date sqlDateStart = new java.sql.Date(date.getTime());
					date = sdf.parse(end);
					java.sql.Date sqlDateEnd = new java.sql.Date(date.getTime());

					if (sqlDateStart.after(sqlDateEnd))
					{
						JFrame frame = new JFrame();
						JOptionPane.showMessageDialog(frame, bundle.getString("e_StartAfterEnd"));
						return;
					}
					mission.setStartDate(sqlDateStart);
					mission.setEndDate(sqlDateEnd);
					mission.setType(MissionType.valueOf(Shared.getEnumStringFromTranslatedValue(missionTypeSelect.getSelectedItem().toString())));
					int row = table.getSelectedRow();
					model.setValueAt(table.getValueAt(row, 0), row, 0);
					model.setValueAt(sqlDateStart, row, 1);
					model.setValueAt(sqlDateEnd, row, 2);
					/*String missionTypeStr = missionTypeSelect.getSelectedItem().toString();
					String enumStringFromTranslatedValue = Shared.getEnumStringFromTranslatedValue(missionTypeStr);**/
					model.setValueAt(missionTypeSelect.getSelectedItem().toString(), row, 3);
				}
				catch (ParseException e1)
				{
					e1.printStackTrace();
				}

				manager.updateMission(mission);
				model.fireTableDataChanged();
				iFrame.dispose();
			}
		});
    }
}
