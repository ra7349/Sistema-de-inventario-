package org.Kardex.jF.view;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

final class UiStyle {

    static final Color BACKGROUND = new Color(244, 247, 251);
    static final Color CARD = Color.WHITE;
    static final Color PRIMARY = new Color(37, 99, 235);
    static final Color PRIMARY_DARK = new Color(29, 78, 216);
    static final Color DANGER = new Color(220, 38, 38);
    static final Color TEXT = new Color(30, 41, 59);
    static final Color MUTED = new Color(100, 116, 139);
    static final Color BORDER = new Color(203, 213, 225);

    private UiStyle() {
    }

    static JPanel cardPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(CARD);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(18, 18, 14, 18)
        ));
        return panel;
    }

    static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        return label;
    }

    static void styleField(JTextField field) {
        field.setFont(field.getFont().deriveFont(13f));
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
    }

    static void styleCombo(JComboBox<?> combo) {
        combo.setFont(combo.getFont().deriveFont(13f));
        combo.setForeground(TEXT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(new LineBorder(BORDER, 1, true));
    }

    static void styleTextArea(JTextArea area) {
        area.setFont(area.getFont().deriveFont(13f));
        area.setForeground(TEXT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
    }

    static JButton primaryButton(String text) {
        JButton button = button(text, PRIMARY);
        button.setForeground(Color.WHITE);
        return button;
    }

    static JButton dangerButton(String text) {
        JButton button = button(text, DANGER);
        button.setForeground(Color.WHITE);
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = button(text, Color.WHITE);
        button.setForeground(PRIMARY_DARK);
        button.setBorder(new CompoundBorder(
            new LineBorder(new Color(147, 197, 253), 1, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        return button;
    }

    static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);
        table.setFont(table.getFont().deriveFont(13f));
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 13f));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));
    }

    private static JButton button(String text, Color background) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(background);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundBorder(
            new LineBorder(background.darker(), 1, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        return button;
    }
}
