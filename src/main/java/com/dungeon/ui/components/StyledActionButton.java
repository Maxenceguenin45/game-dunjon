package com.dungeon.ui.components;
import com.dungeon.ui.styles.ButtonStyles;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
/**
 * Bouton stylisé pour les actions (Inventaire, Armes, etc.)
 */
public class StyledActionButton extends Button {
    public StyledActionButton(String text) {
        super(text);
        setupStyle();
    }
    private void setupStyle() {
        this.setFont(Font.font("Monospace", 12));
        // Appliquer le style par défaut
        this.setStyle(ButtonStyles.getActionButtonDefaultStyle());
        // Style au survol
        this.setOnMouseEntered(e -> 
            this.setStyle(ButtonStyles.getActionButtonHoverStyle())
        );
        // Retour au style par défaut
        this.setOnMouseExited(e -> 
            this.setStyle(ButtonStyles.getActionButtonDefaultStyle())
        );
    }
}
