package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.*;
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
 * Created by Michal on 5/2/2015.
 */
public class SpiesMan {

    private JPanel topPanel;
    private JTable table1;
    private JButton addSpyButton;
    private JButton removeSpyButton;
    private JButton updateSpyButton;
    private JButton findSpyByIDButton;
    private JTextField textField1;
    private JButton showAllSpiesButton;
    private JButton showUnassignedSpiesButton;
    private JButton showSpiesOnMissionButton;
    private JButton addSpyToMissionButton;
    private JButton removeSpyFromMissionButton;

    private SpiesTableModel spiesModel;

    private SpyManager manager;
    private AgencyManager agencyManager;
    private static Logger log = LoggerFactory.getLogger(SpyApp.class);
	private static ResourceBundle bundle = ResourceBundle.getBundle("SpyMan", Locale.getDefault());
    public SpiesMan() {
        table1.setModel(new SpiesTableModel());
        table1.setDefaultRenderer(Color.class, new ColorCellRenderer());
        this.spiesModel = (SpiesTableModel) table1.getModel();
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");
        agencyManager = new AgencyManagerImpl(ds);
        manager = new SpyManagerImpl(ds);
        manager.addSpy(new Spy(null,"a","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"a","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"a","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"a","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"a","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"e","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"d","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"c","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"b","f",new Date("01/01/1234"),"xyz"));
        manager.addSpy(new Spy(null,"a","f",new Date("01/01/1234"),"xyz"));
        for(Spy spy : manager.listSpies())
        {
            spiesModel.addSpy(spy);
        }

        addSpyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("addSpyButton({})");
                Spy spy = new Spy();
                JFrame iFrame = new JFrame();
                iFrame.setTitle("Add spy");
                iFrame.add(new AddSpy(spy,spiesModel,iFrame).getTopPanel());
                iFrame.setContentPane(new AddSpy(spy,spiesModel,iFrame).getTopPanel());
                iFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                iFrame.setPreferredSize(new Dimension(600, 200));

                iFrame.pack();
                iFrame.setVisible(true);
                textField1.setText("");
            }
        });
        addSpyToMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("addSpyToMissionButton({})");
                if (table1.getSelectedRowCount() != 1) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, bundle.getString("e_MustSelectOne"));
                    return;
                }
                int row = table1.getSelectedRow();
                Spy spy = manager.findSpyById((Long) table1.getValueAt(row, 0));
                if(!agencyManager.listUnassignedSpies().contains(spy))
                {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, bundle.getString("e_AlreadyAssigned"));
                    return;
                }
                JFrame iFrame = new JFrame();
                iFrame.setTitle(bundle.getString("AddToMission"));
                iFrame.add(new MissionTable(bundle.getString("Add"), spy, spiesModel, iFrame, table1).getTopPanel());
                iFrame.setContentPane(new MissionTable(bundle.getString("Add"), spy, spiesModel, iFrame, table1).getTopPanel());
                iFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                iFrame.setPreferredSize(new Dimension(600, 400));

                iFrame.pack();
                iFrame.setVisible(true);
                textField1.setText("");
            }
        });
        removeSpyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("removeSpyButton({})");
                if (table1.getSelectedRowCount() == 0) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, bundle.getString("e_MustSelectOne"));
                    return;
                }
                JFrame frame = new JFrame();
                Object[] options = {bundle.getString("Yes"),
                        bundle.getString("No")};
                int n = JOptionPane.showOptionDialog(frame,
                        bundle.getString("ReallyRemove"),
                        bundle.getString("SpyRemoval"),
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
                    manager.removeSpy((Long) table1.getValueAt(row-counter,0));
                    spiesModel.removeSpy(row-counter);
                    counter++;
                }
                spiesModel.fireTableDataChanged();
                textField1.setText("");
            }
        });
        removeSpyFromMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("removeSpyFromMissionButton({})");
                if (table1.getSelectedRowCount() != 1) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, bundle.getString("e_MustSelectOne"));
                    return;
                }
                int row = table1.getSelectedRow();
                Spy spy2 = (manager.findSpyById((Long) table1.getValueAt(row,0)));
                Mission mission = agencyManager.findMissionWithSpy(spy2);
                if (mission == null) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, bundle.getString("e_NotAssigned"));
                    return;
                }
                JFrame frame = new JFrame();
                Object[] options = {bundle.getString("Yes"),
                        bundle.getString("No")};
                int n = JOptionPane.showOptionDialog(frame,
                        bundle.getString("Continue"),
                        bundle.getString("RemoveFromMission"),
                        JOptionPane.YES_OPTION,
                        JOptionPane.NO_OPTION,
                        null,
                        options,
                        options[1]);
                if(n == JOptionPane.NO_OPTION)
                {
                    return;
                }

                agencyManager.removeSpyFromMission(spy2,mission);
                spiesModel.fireTableDataChanged();
                textField1.setText("");
            }
        });
        updateSpyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("updateSpyButton({})");
                JFrame frame = new JFrame();
                int count = table1.getSelectedRowCount();
                if(count != 1)
                {
                    JOptionPane.showMessageDialog(frame,bundle.getString("e_MustSelectOne"));
                    return;
                }
                int row = table1.getSelectedRow();
                Spy spy = manager.findSpyById((Long) table1.getValueAt(row, 0));
                JFrame iFrame = new JFrame();
                iFrame.setTitle(bundle.getString("UpdateSpy"));
                iFrame.add(new UpdateSpy(spy,spiesModel,iFrame, table1).getTopPanel());
                iFrame.setContentPane(new UpdateSpy(spy,spiesModel,iFrame,table1).getTopPanel());
                iFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                iFrame.setPreferredSize(new Dimension(600, 200));

                iFrame.pack();
                iFrame.setVisible(true);
                textField1.setText("");
            }
        });
        findSpyByIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("findSpyByIDButton({})");
                try {
                    if(textField1.getText().equals(""))
                    {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame,bundle.getString("e_SpyIdNotFilled"));
                        return;
                    }
                    Long id = Long.parseLong(textField1.getText());
                    Spy spy = manager.findSpyById(id);
                    if (spy == null) {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame, bundle.getString("e_NoSuchSpy"));
                        return;
                    } else {
                        clearAll();
                        for (Spy s : manager.listSpies()) {
                            if(s.getSpyId() == id) {
                                spiesModel.addSpy(s);
                            }
                        }
                    }
                } catch (IllegalArgumentException ex) {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame,bundle.getString("e_ArgumentNonNumber"));
                ex.printStackTrace();
            }

                spiesModel.fireTableDataChanged();
                textField1.setText("");
            }
        });
        showAllSpiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("showAllSpiesButton({})");
                showAllSpies();
            }
        });
        showUnassignedSpiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("showUnassignedSpiesButton({})");
                showUnassigned();
            }
        });
        showSpiesOnMissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("showSpiesOnMissionButton({})");
                JFrame iFrame = new JFrame();
                iFrame.setTitle(bundle.getString("ShowSpiesOnMission"));
                iFrame.add(new MissionTable("show",null,spiesModel,iFrame, table1).getTopPanel());
                iFrame.setContentPane(new MissionTable("show",null,spiesModel,iFrame,table1).getTopPanel());
                iFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                iFrame.setPreferredSize(new Dimension(600, 400));

                iFrame.pack();
                iFrame.setVisible(true);
                textField1.setText("");
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame(bundle.getString("Spies"));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setContentPane(new SpiesMan().topPanel);
                frame.setPreferredSize(new Dimension(800, 600));

                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public void showUnassigned()
    {
        clearAll();
        for(Spy spy : agencyManager.listUnassignedSpies())
        {
            spiesModel.addSpy(spy);
        }
    }

    public void showAllSpies()
    {
        clearAll();
        for (Spy spy : manager.listSpies()) {
            spiesModel.addSpy(spy);
        }
    }

    /*public void showOnMission(Mission mission)
    {
        clearAll();
        for(Spy spy : agencyManager.findSpiesOnMission(mission))
        {
            spiesModel.addSpy(spy);
        }
    }*/

    public void clearAll()
    {
        int rowCount = table1.getRowCount();
        for(int i = 0; i < rowCount; i++)
        {
            spiesModel.removeSpy(0);
        }
    }

    public JTable getTable1() {
        return table1;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }
}
