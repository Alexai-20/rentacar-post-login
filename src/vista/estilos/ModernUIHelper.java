package vista.estilos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

/**
 * Utilidades centralizadas para mantener un estilo uniforme en todas las
 * ventanas de la aplicación.
 */
public final class ModernUIHelper {

    private ModernUIHelper() {
    }

    public static final Color BACKGROUND = new Color(243, 246, 249);
    public static final Color SURFACE = Color.WHITE;
    public static final Color PRIMARY = new Color(52, 152, 219);
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);
    public static final Color TEXT = new Color(33, 47, 61);
    public static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(DEFAULT_FONT.deriveFont(Font.BOLD));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 18, 10, 18));
        button.setOpaque(true);
        button.setBorder(new LineBorder(PRIMARY_DARK, 1, true));
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(236, 240, 241));
        button.setForeground(TEXT);
        button.setFont(DEFAULT_FONT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 18, 10, 18));
        button.setOpaque(true);
        button.setBorder(new LineBorder(new Color(210, 214, 218), 1, true));
        return button;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 234, 239));
        table.setFont(DEFAULT_FONT);
        table.setForeground(TEXT);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setIntercellSpacing(new java.awt.Dimension(0, 8));
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(236, 241, 245));
        header.setForeground(TEXT.darker());
        header.setFont(DEFAULT_FONT.deriveFont(Font.BOLD));
        header.setReorderingAllowed(false);
        header.setBorder(new LineBorder(new Color(220, 224, 230), 1, true));
    }

    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(DEFAULT_FONT.deriveFont(Font.BOLD, 18f));
        label.setForeground(TEXT);
        return label;
    }

    public static void applyCardStyle(JComponent component) {
        component.setOpaque(true);
        component.setBackground(SURFACE);
        component.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(231, 234, 238), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
    }

    public static void registerBackground(Component component) {
        component.setBackground(BACKGROUND);
    }
}
