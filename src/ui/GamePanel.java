package ui;

import javax.swing.JPanel;
import java.awt.*;
import animation.CombatAnimation;
import util.GameConstants;

public class GamePanel extends JPanel implements IGamePanel {
    private final CombatAnimation combatAnimation;
    private boolean enableCombatAnimation = false; // désactivé pour ne pas salir la zone texte
    private int playerPosition = 0;
    private String[] infoMessages = new String[5];
    private String[] choices;
    private String statsMessage = "";
    private boolean gameOver = false;
    private String gameOverMessage = "";

    public GamePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GameConstants.LARGEUR_FENETRE, GameConstants.HAUTEUR_FENETRE));
        combatAnimation = new CombatAnimation();
        // Initialiser avec un message d'accueil
        infoMessages[0] = "=== Bienvenue dans le Donjon ===";
        infoMessages[1] = "Utilisez les flèches HAUT/BAS pour naviguer";
        infoMessages[2] = "Appuyez sur ENTRÉE pour valider";
    }

    @Override
    public void startCombatAnimation(int x, int y) {
        if (null != combatAnimation) {
            combatAnimation.start(x, y);
            repaint();
        }
    }

    // Permet d'activer/désactiver l'animation dans la zone texte si un jour nécessaire
    public void setEnableCombatAnimation(boolean enable) {
        this.enableCombatAnimation = enable;
    }

    @Override
    public void setInfoMessage(String message) {
        if (null != message) {
            // Faire défiler les messages vers le haut
            for (int i = 0; i < infoMessages.length - 1; i++) {
                infoMessages[i] = infoMessages[i + 1];
            }
            infoMessages[infoMessages.length - 1] = message;
            repaint();
        }
    }

    @Override
    public void setStats(String stats) {
        statsMessage = (null != stats) ? stats : "";
        repaint();
    }

    @Override
    public void setGameOver(String message) {
        gameOver = true;
        gameOverMessage = (null != message) ? message : "";
        repaint();
    }

    public void resetGame() {
        gameOver = false;
        gameOverMessage = "";
        infoMessages = new String[5];
        statsMessage = "";
        playerPosition = 0;
        repaint();
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int position) {
        this.playerPosition = position;
        repaint();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void updateChoices(String[] descriptions) {
        // Mise à jour des choix affichés
        this.playerPosition = 0;
        this.choices = descriptions;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Afficher l'écran de game over si nécessaire
        if (gameOver) {
            drawGameOver(g2d);
            // Afficher quand même les choix en mode game over (Rejouer/Quitter)
            if (choices != null && choices.length > 0) {
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                int y = getHeight() / 2 + 100;
                for (int i = 0; i < choices.length; i++) {
                    if (choices[i] != null) {
                        // Mettre en surbrillance le choix sélectionné
                        if (i == playerPosition) {
                            g2d.setColor(Color.YELLOW);
                            g2d.drawString("> " + choices[i], (getWidth() - g2d.getFontMetrics().stringWidth("> " + choices[i])) / 2, y);
                        } else {
                            g2d.setColor(Color.WHITE);
                            g2d.drawString("  " + choices[i], (getWidth() - g2d.getFontMetrics().stringWidth("  " + choices[i])) / 2, y);
                        }
                        y += 40;
                    }
                }
            }
            return; // Ne pas afficher le reste en mode game over
        }

        // Afficher les messages
        if (infoMessages != null) {
            g2d.setColor(Color.WHITE);
            int y = 30;
            for (String message : infoMessages) {
                if (message != null) {
                    g2d.drawString(message, 10, y);
                    y += 20;
                }
            }
        }

        // Afficher les choix disponibles
        if (choices != null && choices.length > 0) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            int y = 200;
            for (int i = 0; i < choices.length; i++) {
                if (choices[i] != null) {
                    // Mettre en surbrillance le choix sélectionné
                    if (i == playerPosition) {
                        g2d.setColor(Color.YELLOW);
                        g2d.drawString("> " + choices[i], 10, y);
                    } else {
                        g2d.setColor(Color.WHITE);
                        g2d.drawString("  " + choices[i], 10, y);
                    }
                    y += 30;
                }
            }
        }

        // Afficher les stats
        if (statsMessage != null && !statsMessage.isEmpty()) {
            g2d.setColor(Color.GREEN);
            g2d.drawString(statsMessage, 10, getHeight() - 30);
        }

        // Afficher "Appuyez sur S pour sauvegarder et quitter" en bas à droite
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        String saveHint = "Appuyez sur S pour sauvegarder et quitter";
        int hintWidth = g2d.getFontMetrics().stringWidth(saveHint);
        g2d.drawString(saveHint, getWidth() - hintWidth - 10, getHeight() - 10);

        // Afficher l'animation de combat si active (désactivée par défaut pour garder le texte propre)
        if (enableCombatAnimation && null != combatAnimation && combatAnimation.isActive()) {
            combatAnimation.draw(g2d);
            repaint();
        }
    }

    private void drawGameOver(Graphics2D g2d) {
        // Fond semi-transparent
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Message de game over
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        int y = getHeight() / 3;
        g2d.drawString("GAME OVER", (getWidth() - g2d.getFontMetrics().stringWidth("GAME OVER")) / 2, y);

        // Message détaillé
        if (gameOverMessage != null && !gameOverMessage.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            for (String line : gameOverMessage.split("\n")) {
                y += 30;
                g2d.drawString(line, (getWidth() - g2d.getFontMetrics().stringWidth(line)) / 2, y);
            }
        }
    }
}
