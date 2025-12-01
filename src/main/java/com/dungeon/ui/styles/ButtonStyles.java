package com.dungeon.ui.styles;

/**
 * Styles prédéfinis pour les boutons
 */
public final class ButtonStyles {

    private ButtonStyles() {} // Empêche l'instanciation

    /**
     * Style par défaut pour les boutons de choix
     */
    public static String getChoiceButtonDefaultStyle() {
        return new StyleBuilder()
                .backgroundGradient("to bottom", ColorPalette.BUTTON_MEDIUM, ColorPalette.BUTTON_DARK)
                .textFill(ColorPalette.TEXT_WHITE)
                .fontFamily("Monospace")
                .fontSize(UIConstants.NORMAL_FONT_SIZE)
                .fontWeight("bold")
                .padding("10px 15px")
                .border(ColorPalette.BORDER_LIGHT, 2)
                .borderRadius(5)
                .custom("-fx-cursor", "hand")
                .build();
    }

    /**
     * Style pour les boutons de choix au survol
     */
    public static String getChoiceButtonHoverStyle() {
        return new StyleBuilder()
                .backgroundGradient("to bottom", ColorPalette.BUTTON_LIGHT, ColorPalette.BUTTON_MEDIUM)
                .textFill(ColorPalette.ACCENT_YELLOW)
                .fontFamily("Monospace")
                .fontSize(UIConstants.NORMAL_FONT_SIZE)
                .fontWeight("bold")
                .padding("10px 15px")
                .border(ColorPalette.ACCENT_YELLOW, 3)
                .borderRadius(5)
                .custom("-fx-cursor", "hand")
                .build();
    }

    /**
     * Style pour les boutons de choix pressés
     */
    public static String getChoiceButtonPressedStyle() {
        return new StyleBuilder()
                .backgroundGradient("to bottom", ColorPalette.BUTTON_PRESSED_LIGHT, ColorPalette.BUTTON_PRESSED_DARK)
                .textFill(ColorPalette.ACCENT_GREEN)
                .fontFamily("Monospace")
                .fontSize(UIConstants.NORMAL_FONT_SIZE)
                .fontWeight("bold")
                .padding("10px 15px")
                .border(ColorPalette.ACCENT_GREEN, 3)
                .borderRadius(5)
                .build();
    }

    /**
     * Style par défaut pour les boutons d'action
     */
    public static String getActionButtonDefaultStyle() {
        return new StyleBuilder()
                .backgroundGradient("to bottom", ColorPalette.BUTTON_LIGHT, ColorPalette.BACKGROUND_LIGHT)
                .textFill(ColorPalette.TEXT_WHITE)
                .fontSize(UIConstants.SMALL_FONT_SIZE)
                .fontWeight("bold")
                .padding("8px 15px")
                .border(ColorPalette.BORDER_LIGHTER, 2)
                .borderRadius(5)
                .build();
    }

    /**
     * Style pour les boutons d'action au survol
     */
    public static String getActionButtonHoverStyle() {
        return new StyleBuilder()
                .backgroundGradient("to bottom", ColorPalette.BUTTON_HOVER, ColorPalette.BUTTON_MEDIUM)
                .textFill(ColorPalette.ACCENT_YELLOW)
                .fontSize(UIConstants.SMALL_FONT_SIZE)
                .fontWeight("bold")
                .padding("8px 15px")
                .border(ColorPalette.ACCENT_YELLOW, 2)
                .borderRadius(5)
                .build();
    }

    /**
     * Style pour les boutons médiévaux (popups)
     */
    public static String getMedievalButtonStyle() {
        return new StyleBuilder()
                .backgroundGradient("to bottom", ColorPalette.WOOD_LIGHT, ColorPalette.WOOD_MEDIUM)
                .textFill("black")
                .fontSize(UIConstants.NORMAL_FONT_SIZE)
                .fontWeight("bold")
                .padding("10px 25px")
                .border("#8b6f47", 2)
                .borderRadius(5)
                .build();
    }
}

