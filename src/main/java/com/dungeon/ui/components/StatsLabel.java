package com.dungeon.ui.components;
import com.dungeon.ui.styles.ColorPalette;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
/**
 * Label stylisé pour les statistiques
 */
public class StatsLabel extends Label {
    public StatsLabel(String text) {
        super(text);
        setupStyle();
    }
    private void setupStyle() {
        this.setFont(Font.font("Monospace", 14));
        this.setTextFill(Color.web(ColorPalette.TEXT_LIGHT_GREEN));
    }
    /**
     * Met à jour le texte avec une couleur personnalisée
     */
    public void updateText(String text, String color) {
        this.setText(text);
        this.setTextFill(Color.web(color));
    }
}
