package com.formdev.flatlaf;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Implementación ligera inspirada en FlatLightLaf para el proyecto sin
 * dependencias externas. Extiende Nimbus y ajusta los colores, radios de
 * esquinas y tipografías para ofrecer una apariencia plana y minimalista.
 */
public class FlatLightLaf extends NimbusLookAndFeel {

    private static final long serialVersionUID = 1L;

    @Override
    public void initialize() {
        super.initialize();

        Color background = new Color(248, 249, 252);
        Color panelBackground = Color.WHITE;
        Color primary = new Color(52, 152, 219);
        Color secondary = new Color(236, 240, 241);
        Color text = new Color(44, 62, 80);

        UIManager.put("control", panelBackground);
        UIManager.put("info", panelBackground);
        UIManager.put("nimbusBase", primary.darker());
        UIManager.put("nimbusBlueGrey", new Color(189, 195, 199));
        UIManager.put("nimbusLightBackground", background);
        UIManager.put("nimbusFocus", primary);
        UIManager.put("text", text);
        UIManager.put("Table.alternateRowColor", new Color(244, 246, 248));
        UIManager.put("Component.arc", 14);
        UIManager.put("Button.arc", 20);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ProgressBar.arc", 12);
        UIManager.put("ScrollBar.thumb", primary.brighter());
        UIManager.put("ScrollBar.track", secondary);

        Font defaultFont = new Font("SansSerif", Font.PLAIN, 14);
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("defaultFont", defaultFont);

        List<String> fontKeys = Arrays.asList(
                "Button.font", "Label.font", "Table.font", "TableHeader.font",
                "TextField.font", "PasswordField.font", "ComboBox.font",
                "List.font", "RadioButton.font", "CheckBox.font",
                "TabbedPane.font", "TitledBorder.font"
        );
        for (String key : fontKeys) {
            defaults.put(key, defaultFont);
        }

        defaults.put("Panel.background", background);
        defaults.put("ScrollPane.background", background);
        defaults.put("Table.background", panelBackground);
        defaults.put("Table.foreground", text);
        defaults.put("TableHeader.background", new Color(236, 241, 245));
        defaults.put("TableHeader.foreground", text.darker());
        defaults.put("Table.gridColor", new Color(220, 224, 230));
        defaults.put("Table.selectionBackground", primary);
        defaults.put("Table.selectionForeground", Color.WHITE);
        defaults.put("Button.background", primary);
        defaults.put("Button.foreground", Color.WHITE);
        defaults.put("Button.focusPainted", Boolean.FALSE);
        defaults.put("Button.border", null);
        defaults.put("TextField.background", panelBackground);
        defaults.put("TextField.border", null);
        defaults.put("PasswordField.background", panelBackground);
        defaults.put("PasswordField.border", null);
        defaults.put("ComboBox.background", panelBackground);
        defaults.put("ComboBox.border", null);
    }

    @Override
    public String getName() {
        return "FlatLightLaf";
    }

    @Override
    public String getID() {
        return "FlatLightLaf";
    }

    @Override
    public String getDescription() {
        return "A lightweight flat look and feel tailored for RentaCarMVC";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }
}
