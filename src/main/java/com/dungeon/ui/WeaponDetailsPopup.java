package com.dungeon.ui;

import com.dungeon.model.item.arme.Arme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Fenêtre popup pour afficher les détails d'une arme
 */
public class WeaponDetailsPopup {
    
    public static Stage show(Arme arme, Stage parentStage) {
        Stage popup = new Stage();
        popup.initModality(Modality.NONE); // Non-modale pour pouvoir interagir avec la fenêtre principale
        popup.initOwner(parentStage);
        popup.initStyle(StageStyle.DECORATED);
        popup.setTitle("Détails de l'arme");
        popup.setResizable(false);

        // Positionner la popup à côté de la fenêtre principale
        if (parentStage != null) {
            popup.setX(parentStage.getX() + parentStage.getWidth() + 10);
            popup.setY(parentStage.getY());
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f4e8d0, #e8dcc0);" +
            "-fx-border-color: #8b7355;" +
            "-fx-border-width: 4;" +
            "-fx-border-style: solid;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;"
        );

        // Titre avec nom de l'arme
        Label titre = new Label(arme.getNom());
        titre.setFont(Font.font("Serif", 26));
        titre.setTextFill(Color.rgb(50, 30, 20));
        titre.setAlignment(Pos.CENTER);
        titre.setStyle("-fx-font-weight: bold;");
        
        // Rareté
        Label rarete = new Label("[" + arme.getRarete().getNom() + "]");
        rarete.setFont(Font.font("Serif", 16));
        rarete.setTextFill(getRareteColor(arme.getRarete()));
        rarete.setAlignment(Pos.CENTER);
        rarete.setStyle("-fx-font-style: italic;");

        // Séparateur
        javafx.scene.shape.Line separator1 = new javafx.scene.shape.Line(0, 0, 400, 0);
        separator1.setStroke(Color.GRAY);
        
        // Statistiques principales
        GridPane stats = new GridPane();
        stats.setHgap(20);
        stats.setVgap(10);
        stats.setPadding(new Insets(15));
        stats.setStyle(
            "-fx-background-color: rgba(255, 250, 240, 0.7);" +
            "-fx-border-color: #a67c52;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        addStatRow(stats, 0, "Dégâts", String.valueOf(arme.getDegats()));
        addStatRow(stats, 1, "Dégâts totaux", String.valueOf(arme.calculerDegatsTotal()));
        addStatRow(stats, 2, "Niveau maîtrise", String.valueOf(arme.getNiveauMaitrise()));
        addStatRow(stats, 3, "Bonus maîtrise", String.format("%.1f%%", arme.getBonusMaitrise()));
        addStatRow(stats, 4, "Durabilité", String.format("%d / %d (%.0f%%)",
            arme.getDurabilite(), arme.getDurabiliteMax(), arme.getPourcentageDurabilite()));

        // Séparateur
        javafx.scene.shape.Line separator2 = new javafx.scene.shape.Line(0, 0, 400, 0);
        separator2.setStroke(Color.GRAY);
        
        // Barre de progression durabilité
        VBox duraBox = new VBox(5);
        duraBox.setPadding(new Insets(15));
        duraBox.setStyle(
            "-fx-background-color: rgba(255, 250, 240, 0.7);" +
            "-fx-border-color: #a67c52;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        Label duraLabel = new Label(String.format("Durabilité: %d / %d",
            arme.getDurabilite(), arme.getDurabiliteMax()));
        duraLabel.setFont(Font.font("Serif", 14));

        // Couleur selon l'état de l'arme
        if (arme.estCassee()) {
            duraLabel.setTextFill(Color.RED);
        } else if (arme.getPourcentageDurabilite() < 30.0) {
            duraLabel.setTextFill(Color.ORANGE);
        } else {
            duraLabel.setTextFill(Color.rgb(34, 139, 34));
        }

        ProgressBar duraBar = new ProgressBar();
        duraBar.setPrefWidth(380);
        duraBar.setPrefHeight(25);
        duraBar.setProgress(arme.getDurabilite() / (double) arme.getDurabiliteMax());

        // Couleur selon l'état
        String barColor = arme.estCassee() ? "#ff4444, #cc0000" :
                         (arme.getPourcentageDurabilite() < 30.0 ? "#ff9933, #ff6600" : "#7fb069, #5a8c4f");
        duraBar.setStyle(
            "-fx-accent: linear-gradient(to right, " + barColor + ");" +
            "-fx-control-inner-background: rgba(139, 115, 85, 0.3);"
        );

        duraBox.getChildren().addAll(duraLabel, duraBar);

        // Séparateur
        javafx.scene.shape.Line separator3 = new javafx.scene.shape.Line(0, 0, 400, 0);
        separator3.setStroke(Color.GRAY);

        // Barre de progression XP
        VBox xpBox = new VBox(5);
        xpBox.setPadding(new Insets(15));
        xpBox.setStyle(
            "-fx-background-color: rgba(255, 250, 240, 0.7);" +
            "-fx-border-color: #a67c52;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        Label xpLabel = new Label(String.format("Expérience: %d / %d",
            arme.getExperienceMaitrise(), arme.getExperienceRequise()));
        xpLabel.setFont(Font.font("Serif", 14));
        xpLabel.setTextFill(Color.rgb(30, 80, 180));

        ProgressBar xpBar = new ProgressBar();
        xpBar.setPrefWidth(380);
        xpBar.setPrefHeight(25);
        xpBar.setProgress((double) arme.getExperienceMaitrise() / arme.getExperienceRequise());
        xpBar.setStyle(
            "-fx-accent: linear-gradient(to right, #c19a6b, #d4a574);" +
            "-fx-control-inner-background: rgba(139, 115, 85, 0.3);"
        );
        
        xpBox.getChildren().addAll(xpLabel, xpBar);
        
        // Description
        VBox descBox = new VBox(5);
        descBox.setPadding(new Insets(15));
        descBox.setStyle(
            "-fx-background-color: rgba(255, 250, 240, 0.7);" +
            "-fx-border-color: #a67c52;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        Label descLabel = new Label("Description:");
        descLabel.setFont(Font.font("Serif", 14));
        descLabel.setTextFill(Color.rgb(60, 40, 30));
        descLabel.setStyle("-fx-font-weight: bold;");
        
        Label description = new Label(arme.getDescription());
        description.setFont(Font.font("Serif", 12));
        description.setTextFill(Color.rgb(60, 40, 30));
        description.setWrapText(true);
        
        descBox.getChildren().addAll(descLabel, description);
        
        // Bouton fermer
        Button closeBtn = new Button("Fermer");
        closeBtn.setFont(Font.font("Serif", 14));
        closeBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #c19a6b, #a67c52);" +
            "-fx-text-fill: black;" +
            "-fx-padding: 10px 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: #8b6f47;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        closeBtn.setOnAction(e -> popup.close());
        
        HBox buttonBox = new HBox(closeBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        root.getChildren().addAll(
            titre, 
            rarete, 
            separator1,
            stats, 
            separator2,
            duraBox,
            separator3,
            xpBox,
            descBox, 
            buttonBox
        );
        
        Scene scene = new Scene(root, 400, 600);
        popup.setScene(scene);
        popup.show();
        return popup;
    }
    
    private static void addStatRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setFont(Font.font("Serif", 14));
        labelNode.setTextFill(Color.rgb(60, 40, 30));

        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("Serif", 16));
        valueNode.setTextFill(Color.rgb(100, 60, 30));
        valueNode.setStyle("-fx-font-weight: bold;");
        
        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }
    
    private static Color getRareteColor(com.dungeon.model.item.Rarete rarete) {
        return switch (rarete) {
            case COMMUN -> Color.rgb(105, 105, 105);
            case PEU_COMMUN -> Color.rgb(34, 139, 34);
            case RARE -> Color.rgb(30, 144, 255);
            case EPIQUE -> Color.rgb(138, 43, 226);
            case LEGENDAIRE -> Color.rgb(218, 165, 32);
        };
    }
}
