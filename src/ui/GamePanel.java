package ui;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import animation.CombatAnimation;
import util.GameConstants;

public class GamePanel extends JPanel implements IGamePanel {
    private final CombatAnimation combatAnimation;
    private boolean enableCombatAnimation = false;
    private int playerPosition = 0;
    private String[] infoMessages = new String[5];
    private String[] choices;
    private String statsMessage = "";
    private boolean gameOver = false;
    private String gameOverMessage = "";

    // Données pour la barre de niveau
    private int niveau = 1;
    private int experience = 0;
    private int experienceRequise = 100;
    private int pv = 100;
    private int pvMax = 100;
    private int attaque = 10;

    // Système de notifications popup
    private final List<Notification> notifications = new ArrayList<>();

    // Classe interne pour gérer les notifications
    private static class Notification {
        String message;
        long startTime;
        int duration; // en millisecondes
        NotificationType type;

        Notification(String message, int duration, NotificationType type) {
            this.message = message;
            this.duration = duration;
            this.type = type;
            this.startTime = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - startTime > duration;
        }

        float getAlpha() {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed > duration - 500) {
                // Fade out dans les 500 dernières ms
                return (duration - elapsed) / 500f;
            }
            return 1.0f;
        }
    }

    public enum NotificationType {
        LEVEL_UP(new Color(255, 215, 0), "⭐"),
        BOSS_DEFEATED(new Color(255, 50, 50), "👑"),
        RARE_ITEM(new Color(138, 43, 226), "💎"),
        LEGENDARY_ITEM(new Color(255, 140, 0), "✨"),
        ACHIEVEMENT(new Color(50, 205, 50), "🏆"),
        INFO(new Color(100, 149, 237), "ℹ");

        final Color color;
        final String icon;

        NotificationType(Color color, String icon) {
            this.color = color;
            this.icon = icon;
        }
    }

    public GamePanel() {
        setBackground(new Color(20, 20, 30));
        setPreferredSize(new Dimension(GameConstants.LARGEUR_FENETRE, GameConstants.HAUTEUR_FENETRE));
        combatAnimation = new CombatAnimation();
        infoMessages[0] = "=== Bienvenue dans le Donjon ===";
        infoMessages[1] = "Utilisez les flèches HAUT/BAS pour naviguer";
        infoMessages[2] = "Appuyez sur ENTRÉE pour valider";
    }

    /**
     * Affiche une notification popup temporaire
     */
    public void showNotification(String message, NotificationType type, int durationMs) {
        notifications.add(new Notification(message, durationMs, type));
        repaint();
    }

    /**
     * Affiche une notification avec durée par défaut (3 secondes)
     */
    public void showNotification(String message, NotificationType type) {
        showNotification(message, type, 3000);
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

    /**
     * Met à jour les statistiques du joueur pour l'affichage
     */
    public void setStatsDetailles(int niveau, int exp, int expRequise, int pv, int pvMax, int attaque) {
        this.niveau = niveau;
        this.experience = exp;
        this.experienceRequise = expRequise;
        this.pv = pv;
        this.pvMax = pvMax;
        this.attaque = attaque;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (gameOver) {
            drawGameOver(g2d);
            if (choices != null && choices.length > 0) {
                g2d.setFont(new Font("Serif", Font.BOLD, 20));
                int y = getHeight() / 2 + 100;
                for (int i = 0; i < choices.length; i++) {
                    if (choices[i] != null) {
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
            return;
        }

        // Dessiner le panneau de stats en haut avec style parchemin
        drawStatsPanel(g2d);

        // Afficher le panneau d'historique des messages avec style
        drawMessagePanel(g2d);

        // Afficher les choix disponibles avec style amélioré
        if (choices != null && choices.length > 0) {
            g2d.setFont(new Font("Serif", Font.PLAIN, 16));
            int y = 300;
            for (int i = 0; i < choices.length; i++) {
                if (choices[i] != null) {
                    if (i == playerPosition) {
                        // Fond de sélection
                        g2d.setColor(new Color(255, 215, 0, 50));
                        g2d.fillRoundRect(5, y - 20, getWidth() - 10, 28, 10, 10);

                        // Bordure dorée
                        g2d.setColor(new Color(218, 165, 32));
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawRoundRect(5, y - 20, getWidth() - 10, 28, 10, 10);

                        // Texte
                        g2d.setColor(new Color(255, 215, 0));
                        g2d.drawString("▶ " + choices[i], 15, y);
                    } else {
                        g2d.setColor(new Color(200, 200, 200));
                        g2d.drawString("  " + choices[i], 15, y);
                    }
                    y += 35;
                }
            }
        }

        // Afficher les instructions en bas
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Serif", Font.ITALIC, 12));
        String saveHint = "S: Sauvegarder | I: Inventaire | ↑↓: Naviguer | Enter: Valider";
        int hintWidth = g2d.getFontMetrics().stringWidth(saveHint);
        g2d.drawString(saveHint, (getWidth() - hintWidth) / 2, getHeight() - 10);

        if (enableCombatAnimation && null != combatAnimation && combatAnimation.isActive()) {
            combatAnimation.draw(g2d);
            repaint();
        }

        // Dessiner les notifications popup
        drawNotifications(g2d);
    }

    /**
     * Dessine un panneau élégant pour l'historique des messages
     */
    private void drawMessagePanel(Graphics2D g2d) {
        int panelY = 150;
        int panelHeight = 130;
        int margin = 10;
        int padding = 10;

        // Fond du panneau avec dégradé subtil
        GradientPaint bgGradient = new GradientPaint(
            0, panelY, new Color(30, 30, 45, 200),
            0, panelY + panelHeight, new Color(20, 20, 35, 200)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRoundRect(margin, panelY, getWidth() - 2 * margin, panelHeight, 10, 10);

        // Bordure
        g2d.setColor(new Color(80, 80, 120, 150));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(margin, panelY, getWidth() - 2 * margin, panelHeight, 10, 10);

        // Afficher les messages avec découpage automatique
        if (infoMessages != null) {
            g2d.setColor(new Color(220, 220, 220));
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
            FontMetrics fm = g2d.getFontMetrics();

            int y = panelY + padding + fm.getHeight();
            int maxWidth = getWidth() - 2 * margin - 2 * padding;
            int lineHeight = 18;
            int maxLines = 6; // Nombre maximum de lignes affichables
            int lineCount = 0;

            // Parcourir les messages du plus récent au plus ancien
            for (int i = infoMessages.length - 1; i >= 0 && lineCount < maxLines; i--) {
                if (infoMessages[i] != null && !infoMessages[i].isEmpty()) {
                    // Découper le message en lignes si nécessaire
                    String[] wrappedLines = wrapText(infoMessages[i], fm, maxWidth);

                    for (int j = 0; j < wrappedLines.length && lineCount < maxLines; j++) {
                        // Gradient de couleur pour les messages plus anciens
                        float alpha = 1.0f - (lineCount * 0.15f);
                        alpha = Math.max(0.4f, alpha); // Minimum 40% d'opacité

                        g2d.setColor(new Color(220, 220, 220, (int)(255 * alpha)));
                        g2d.drawString(wrappedLines[j], margin + padding, y);

                        y += lineHeight;
                        lineCount++;
                    }
                }
            }
        }
    }

    /**
     * Découpe un texte long en plusieurs lignes pour respecter une largeur maximale
     */
    private String[] wrapText(String text, FontMetrics fm, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return new String[]{""};
        }

        // Si le texte tient sur une ligne, le retourner tel quel
        if (fm.stringWidth(text) <= maxWidth) {
            return new String[]{text};
        }

        // Découper le texte en mots
        String[] words = text.split(" ");
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;

            if (fm.stringWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                // Si même un seul mot est trop long, le couper
                if (currentLine.length() == 0) {
                    // Couper le mot lui-même
                    int maxChars = maxWidth / fm.charWidth('m'); // Estimation
                    for (int i = 0; i < word.length(); i += maxChars) {
                        int end = Math.min(i + maxChars, word.length());
                        lines.add(word.substring(i, end));
                    }
                } else {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
            }
        }

        // Ajouter la dernière ligne
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }

    /**
     * Dessine les notifications popup avec animation
     */
    private void drawNotifications(Graphics2D g2d) {
        // Nettoyer les notifications expirées
        notifications.removeIf(Notification::isExpired);

        if (notifications.isEmpty()) {
            return;
        }

        int y = 160; // Position de départ (sous le panneau de stats)
        int margin = 20;

        for (Notification notif : notifications) {
            float alpha = notif.getAlpha();

            // Mesurer le texte
            g2d.setFont(new Font("Serif", Font.BOLD, 18));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(notif.message);
            int textHeight = fm.getHeight();

            // Dimensions du popup
            int padding = 15;
            int popupWidth = textWidth + padding * 2 + 30; // +30 pour l'icône
            int popupHeight = textHeight + padding;
            int x = (getWidth() - popupWidth) / 2;

            // Ombre portée
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.3f));
            g2d.setColor(new Color(0, 0, 0));
            g2d.fillRoundRect(x + 4, y + 4, popupWidth, popupHeight, 15, 15);

            // Fond de la barre avec couleur spécifique au type
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.95f));
            Color bgColor = new Color(
                notif.type.color.getRed(),
                notif.type.color.getGreen(),
                notif.type.color.getBlue(),
                30
            );
            g2d.setColor(bgColor);
            g2d.fillRoundRect(x, y, popupWidth, popupHeight, 15, 15);

            // Bordure brillante
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(notif.type.color);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(x, y, popupWidth, popupHeight, 15, 15);

            // Icône
            g2d.setFont(new Font("Serif", Font.BOLD, 24));
            g2d.drawString(notif.type.icon, x + padding, y + popupHeight - padding + 2);

            // Texte
            g2d.setFont(new Font("Serif", Font.BOLD, 18));
            g2d.setColor(Color.WHITE);
            g2d.drawString(notif.message, x + padding + 30, y + popupHeight - padding);

            // Réinitialiser la transparence
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            y += popupHeight + 10; // Espacement entre les notifications
        }

        // Continuer l'animation si des notifications sont actives
        if (!notifications.isEmpty()) {
            repaint();
        }
    }

    /**
     * Dessine le panneau de statistiques avec un style élégant
     */
    private void drawStatsPanel(Graphics2D g2d) {
        int panelHeight = 140;
        int margin = 10;

        // Fond plus opaque pour meilleure visibilité
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(40, 40, 60, 240),
            0, panelHeight, new Color(25, 25, 40, 240)
        );
        g2d.setPaint(bgGradient);
        g2d.fillRoundRect(margin, margin, getWidth() - 2 * margin, panelHeight, 15, 15);

        // Bordure plus visible
        g2d.setColor(new Color(120, 120, 180));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRoundRect(margin, margin, getWidth() - 2 * margin, panelHeight, 15, 15);

        // Bordure interne
        g2d.setColor(new Color(80, 80, 120, 120));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(margin + 5, margin + 5, getWidth() - 2 * margin - 10, panelHeight - 10, 12, 12);

        int x = margin + 20;
        int y = margin + 25;

        // Titre "Niveau" à gauche et ATK à droite sur la même ligne
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        g2d.drawString("Niveau " + niveau, x, y);

        // ATK à droite avec icône épée
        g2d.setColor(new Color(255, 150, 50));
        g2d.setFont(new Font("Serif", Font.BOLD, 18));
        String atkText = "⚔ ATK: " + attaque;
        int atkWidth = g2d.getFontMetrics().stringWidth(atkText);
        g2d.drawString(atkText, getWidth() - margin - 20 - atkWidth, y);

        // Stats du joueur avec taille augmentée
        y += 20;
        g2d.setFont(new Font("Monospaced", Font.BOLD, 15));

        // BARRE DE VIE VISUELLE GRANDE
        int barWidth = getWidth() - 2 * margin - 40;
        int barHeight = 28;
        int barX = x;

        // Label PV au-dessus de la barre
        g2d.setColor(new Color(220, 220, 220));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString("Points de Vie", barX, y);

        // Position de la barre
        int pvBarY = y + 3;

        // Ombre de la barre
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(barX + 2, pvBarY + 2, barWidth, barHeight, 12, 12);

        // Fond de la barre (PV max)
        g2d.setColor(new Color(60, 20, 20));
        g2d.fillRoundRect(barX, pvBarY, barWidth, barHeight, 12, 12);

        // Bordure de la barre
        g2d.setColor(new Color(150, 50, 50));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(barX, pvBarY, barWidth, barHeight, 12, 12);

        // Barre de PV avec couleur selon le pourcentage
        if (pvMax > 0) {
            double pvPercent = (double) pv / pvMax;
            int pvWidth = (int) (barWidth * pvPercent);

            if (pvWidth > 0) {
                // Couleur en fonction des PV
                Color pvColor1, pvColor2;
                if (pvPercent > 0.6) {
                    // Vert si > 60%
                    pvColor1 = new Color(50, 205, 50);
                    pvColor2 = new Color(34, 139, 34);
                } else if (pvPercent > 0.3) {
                    // Orange si entre 30% et 60%
                    pvColor1 = new Color(255, 165, 0);
                    pvColor2 = new Color(255, 140, 0);
                } else {
                    // Rouge si < 30%
                    pvColor1 = new Color(255, 50, 50);
                    pvColor2 = new Color(220, 20, 20);
                }

                GradientPaint pvGradient = new GradientPaint(
                    barX, pvBarY, pvColor1,
                    barX + pvWidth, pvBarY, pvColor2
                );
                g2d.setPaint(pvGradient);
                g2d.fillRoundRect(barX, pvBarY, pvWidth, barHeight, 12, 12);

                // Effet de brillance
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillRoundRect(barX, pvBarY, pvWidth, barHeight / 2, 12, 12);
            }
        }

        // Texte PV au centre de la barre (bien visible)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Serif", Font.BOLD, 16));
        String pvText = pv + " / " + pvMax;
        int pvTextWidth = g2d.getFontMetrics().stringWidth(pvText);

        // Ombre du texte pour le rendre encore plus visible
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.drawString(pvText, barX + (barWidth - pvTextWidth) / 2 + 1, pvBarY + barHeight - 5 + 1);

        g2d.setColor(Color.WHITE);
        g2d.drawString(pvText, barX + (barWidth - pvTextWidth) / 2, pvBarY + barHeight - 5);

        // Barre d'expérience - plus d'espace entre PV et XP
        y += 45;
        g2d.setFont(new Font("Serif", Font.PLAIN, 12));
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString(String.format("XP: %d / %d", experience, experienceRequise), x, y);

        // Barre de progression XP
        y += 8;
        int xpBarHeight = 16;

        // Ombre de la barre
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(barX + 2, y + 2, barWidth, xpBarHeight, 10, 10);

        // Fond de la barre
        g2d.setColor(new Color(40, 40, 60));
        g2d.fillRoundRect(barX, y, barWidth, xpBarHeight, 10, 10);

        // Bordure de la barre
        g2d.setColor(new Color(100, 100, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(barX, y, barWidth, xpBarHeight, 10, 10);

        // Progression avec dégradé doré
        if (experienceRequise > 0) {
            double progression = (double) experience / experienceRequise;
            int progressWidth = (int) (barWidth * progression);

            if (progressWidth > 0) {
                GradientPaint xpGradient = new GradientPaint(
                    barX, y, new Color(255, 215, 0),
                    barX + progressWidth, y, new Color(255, 165, 0)
                );
                g2d.setPaint(xpGradient);
                g2d.fillRoundRect(barX, y, progressWidth, xpBarHeight, 10, 10);

                // Effet de brillance
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(barX, y, progressWidth, xpBarHeight / 2, 10, 10);
            }
        }

        // Pourcentage au centre de la barre
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Serif", Font.BOLD, 11));
        int percentage = experienceRequise > 0 ? (int) ((experience * 100.0) / experienceRequise) : 0;
        String percentText = percentage + "%";
        int percentWidth = g2d.getFontMetrics().stringWidth(percentText);
        g2d.drawString(percentText, barX + (barWidth - percentWidth) / 2, y + xpBarHeight - 3);
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Serif", Font.BOLD, 30));
        int y = getHeight() / 3;
        String gameOverText = "GAME OVER";
        int textWidth = g2d.getFontMetrics().stringWidth(gameOverText);
        g2d.drawString(gameOverText, (getWidth() - textWidth) / 2, y);

        if (gameOverMessage != null && !gameOverMessage.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Serif", Font.PLAIN, 18));
            for (String line : gameOverMessage.split("\n")) {
                y += 30;
                int lineWidth = g2d.getFontMetrics().stringWidth(line);
                g2d.drawString(line, (getWidth() - lineWidth) / 2, y);
            }
        }
    }
}
