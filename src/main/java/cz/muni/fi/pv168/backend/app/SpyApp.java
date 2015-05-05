package cz.muni.fi.pv168.backend.app;

import cz.muni.fi.pv168.backend.entities.SpyManager;
import cz.muni.fi.pv168.utils.DBUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Michal on 4/24/2015.
 */
public class SpyApp {

    private JPanel topPanel;
    private JTabbedPane tabbedPane1;
    private static Logger log = LoggerFactory.getLogger(SpyApp.class);
	private static ResourceBundle bundle = ResourceBundle.getBundle("AppGeneral_sk_SK",Locale.getDefault());
    public SpyApp() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:agencydb-test;create=true");

        DBUtils.executeSqlScript(ds, SpyManager.class.getResourceAsStream("/tables.sql"));
		Locale.setDefault(new Locale("sk","SK"));
		tabbedPane1.add(bundle.getString("SpyManagement"), new SpiesMan().getTopPanel());
        tabbedPane1.add(bundle.getString("MissionManagement"), new MissionsMan().getTopPanel());
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
        JMenu fileMenu = new JMenu(bundle.getString("File"));
        final JMenu helpMenu = new JMenu(bundle.getString("Help"));
        menubar.add(fileMenu);
        menubar.add(Box.createHorizontalGlue());
        menubar.add(helpMenu);
        //menu File
        JMenuItem exitMenuItem = new JMenuItem(bundle.getString("Exit"));
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        //menu Help
        JMenuItem aboutMenuItem = new JMenuItem(bundle.getString("About"));
        helpMenu.add(aboutMenuItem);
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(helpMenu,bundle.getString("AboutText"),bundle.getString("About"),JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return menubar;
    }

}
