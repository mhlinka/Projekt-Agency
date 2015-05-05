package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.*;
import cz.muni.fi.pv168.utils.DBUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by Michal on 4/24/2015.
 */
public class SpyApp {

    private JPanel topPanel;
    private JTabbedPane tabbedPane1;
    private static Logger log = LoggerFactory.getLogger(SpyApp.class);

    public SpyApp() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");

        DBUtils.executeSqlScript(ds, SpyManager.class.getResourceAsStream("/tables.sql"));

        tabbedPane1.add("Spy management", new SpiesMan().getTopPanel());
        tabbedPane1.add("Mission management", new MissionsMan().getTopPanel());
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.debug("SpyApp start({})");
                JFrame frame = new JFrame("SpyApp");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                try {
                    frame.setContentPane(new SpyApp().topPanel);
                } catch (SQLException e){
                    e.printStackTrace();
                }

                frame.setPreferredSize(new Dimension(800, 600));
                frame.setJMenuBar(createMenu());

                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private static JMenuBar createMenu() {
        log.debug("createMenu({})");
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        final JMenu helpMenu = new JMenu("Help");
        menubar.add(fileMenu);
        menubar.add(Box.createHorizontalGlue());
        menubar.add(helpMenu);
        //menu File
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        //menu Help
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(helpMenu,"Skvělá aplikace (c) Já","About",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return menubar;
    }

}
