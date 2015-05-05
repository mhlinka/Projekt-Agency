package cz.muni.fi.pv168.backend.app;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Michal on 5/3/2015.
 */
public class ColorCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        setBackground((Color) value);
        setText("");
        return this;
    }

}
