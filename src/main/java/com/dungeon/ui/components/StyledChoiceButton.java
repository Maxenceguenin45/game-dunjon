package com.dungeon.ui.components;
import com.dungeon.ui.styles.ButtonStyles;
import javafx.scene.control.Button;

/**
 * Bouton stylisé pour les choix du joueur
 */
public class StyledChoiceButton extends Button {
    public StyledChoiceButton(String text) {
        super(text);
        setupStyle();
    }
    private void setupStyle() {
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMinHeight(40);
        // Appliquer le style par défaut (inclut déjà la font)
        this.setStyle(ButtonStyles.getChoiceButtonDefaultStyle());
        // Style au survol
        this.setOnMouseEntered(e -> 
            this.setStyle(ButtonStyles.getChoiceButtonHoverStyle())
        );
        // Retour au style par défaut
        this.setOnMouseExited(e -> 
            this.setStyle(ButtonStyles.getChoiceButtonDefaultStyle())
        );
        // Style au clic
        this.setOnMousePressed(e -> 
            this.setStyle(ButtonStyles.getChoiceButtonPressedStyle())
        );
    }
}
