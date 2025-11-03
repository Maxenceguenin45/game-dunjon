package com.dungeon.ui;

import com.dungeon.controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface utilisateur principale du jeu
 */
public class GameUI extends Application {
    private static final Logger logger = Logger.getLogger(GameUI.class.getName());
    private GameController gameController;
    private final GamePanel gamePanel;
    private final AnimationPanel animationPanel;

    public GameUI() {
        this.gamePanel = new GamePanel();
        this.animationPanel = gamePanel.getAnimationPanel();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Passer la référence du stage au GamePanel
            gamePanel.setParentStage(primaryStage);

            // Configurer la fenêtre
            Scene scene = new Scene(gamePanel, 1200, 700);
            primaryStage.setTitle("Dungeon Quest - Pixel Art Adventure");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Initialiser le contrôleur après l'affichage de la fenêtre
            gameController = new GameController(this);
            new Thread(() -> gameController.demarrerJeu()).start();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de l'initialisation de l'interface", e);
            System.exit(1);
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public AnimationPanel getAnimationPanel() {
        return animationPanel;
    }

    /**
     * Affiche un message dans le panneau de messages
     */
    public void displayMessage(String message) {
        gamePanel.displayMessage(message);
    }

    /**
     * Affiche l'écran de game over
     */
    public void setGameOver(String message) {
        gamePanel.setGameOver(message);
    }
}
