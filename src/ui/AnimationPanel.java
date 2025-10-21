package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panneau d'animation en pixel art affichant les scènes du jeu
 */
public class AnimationPanel extends JPanel {
    private static final int LARGEUR = 300;
    private static final int HAUTEUR = 600;
    private static final int PIXEL_SIZE = 10; // Taille d'un "pixel" en pixel art
    
    private SceneType currentScene;
    private int animationFrame = 0;
    private Timer animationTimer;
    private int nombreChoix = 2; // Nombre de choix/portes à afficher

    public enum SceneType {
        MENU,
        EXPLORATION,
        COMBAT,
        ENNEMI_BLESSE,
        VICTOIRE,
        BOSS,
        SOIN,
        ITEM,
        AMELIORATION,
        GAME_OVER
    }
    
    public AnimationPanel() {
        setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        setBackground(new Color(20, 20, 30));
        currentScene = SceneType.MENU;
        
        // Timer pour l'animation (30 FPS)
        animationTimer = new Timer(33, e -> {
            animationFrame++;
            repaint();
        });
        animationTimer.start();
    }
    
    public void setScene(SceneType scene) {
        this.currentScene = scene;
        this.animationFrame = 0;
        repaint();
    }

    public void setScene(SceneType scene, int nombreChoix) {
        this.currentScene = scene;
        this.nombreChoix = nombreChoix;
        this.animationFrame = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Dessiner la scène actuelle
        switch (currentScene) {
            case MENU:
                drawMenuScene(g2d);
                break;
            case EXPLORATION:
                drawExplorationScene(g2d);
                break;
            case COMBAT:
                drawCombatScene(g2d);
                break;
            case ENNEMI_BLESSE:
                drawEnnemisBlesseScene(g2d);
                break;
            case VICTOIRE:
                drawVictoireScene(g2d);
                break;
            case BOSS:
                drawBossScene(g2d);
                break;
            case SOIN:
                drawSoinScene(g2d);
                break;
            case ITEM:
                drawItemScene(g2d);
                break;
            case AMELIORATION:
                drawAmeliorationScene(g2d);
                break;
            case GAME_OVER:
                drawGameOverScene(g2d);
                break;
        }
    }
    
    private void drawMenuScene(Graphics2D g2d) {
        // Dessiner un donjon avec une porte
        drawDungeon(g2d, 50, 150);
        
        // Titre animé
        int offset = (int)(Math.sin(animationFrame * 0.1) * 5);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 24));
        drawPixelText(g2d, "DONJON", 70, 50 + offset);
    }
    
    private void drawExplorationScene(Graphics2D g2d) {
        // Couloir du donjon
        drawCorridor(g2d);
        
        // Afficher les portes selon le nombre de choix (2 ou 3)
        if (nombreChoix == 2) {
            // 2 portes avec torches
            drawTorch(g2d, 50, 180, animationFrame);
            drawDoor(g2d, 35, 220, 1);

            drawTorch(g2d, 220, 180, animationFrame + 15);
            drawDoor(g2d, 205, 220, 2);
        } else if (nombreChoix == 3) {
            // 3 portes avec torches
            drawTorch(g2d, 30, 180, animationFrame);
            drawDoor(g2d, 15, 220, 1);

            drawTorch(g2d, 135, 180, animationFrame + 10);
            drawDoor(g2d, 120, 220, 2);

            drawTorch(g2d, 240, 180, animationFrame + 20);
            drawDoor(g2d, 225, 220, 3);
        }

        // Joueur qui marche au centre (animation simple)
        int walkCycle = (animationFrame / 10) % 2;
        drawPlayer(g2d, 140, 400, walkCycle);

        // Texte indicatif
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
        String texte = nombreChoix + (nombreChoix > 1 ? " chemins" : " chemin");
        int textWidth = g2d.getFontMetrics().stringWidth(texte);
        g2d.drawString(texte, (getWidth() - textWidth) / 2, 30);
    }
    
    private void drawCombatScene(Graphics2D g2d) {
        // Fond de combat
        drawCombatBackground(g2d);
        
        // Joueur à gauche
        drawPlayer(g2d, 60, 350, 0);
        
        // Ennemi à droite
        drawEnemy(g2d, 200, 340);
        
        // Effet d'épée si animation
        if (animationFrame % 30 < 15) {
            drawSwordSlash(g2d, 130, 350);
        }
    }
    
    private void drawEnnemisBlesseScene(Graphics2D g2d) {
        // Fond de combat
        drawCombatBackground(g2d);
        
        // Joueur à gauche
        drawPlayer(g2d, 60, 350, 0);
        
        // Ennemi blessé (clignotant)
        if (animationFrame % 10 < 5) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(new Color(139, 0, 0));
        }
        drawEnemy(g2d, 200, 340);
    }
    
    private void drawVictoireScene(Graphics2D g2d) {
        // Fond de victoire
        drawStars(g2d);
        
        // Joueur victorieux
        drawPlayer(g2d, 120, 350, 0);
        
        // Bras levés
        g2d.setColor(new Color(255, 220, 180));
        fillPixel(g2d, 10, 33, 1, 1);
        fillPixel(g2d, 19, 33, 1, 1);
        
        // Effet de victoire
        g2d.setColor(Color.YELLOW);
        int sparkle = animationFrame % 20;
        if (sparkle < 5) {
            fillPixel(g2d, 10 + sparkle, 30, 1, 1);
            fillPixel(g2d, 20 - sparkle, 30, 1, 1);
        }
    }
    
    private void drawBossScene(Graphics2D g2d) {
        // Fond dramatique
        g2d.setColor(new Color(50, 0, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Joueur à gauche
        drawPlayer(g2d, 40, 400, 0);
        
        // Boss (plus grand)
        drawBoss(g2d, 180, 300);
        
        // Effet de menace
        if (animationFrame % 20 < 10) {
            g2d.setColor(new Color(255, 0, 0, 50));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void drawSoinScene(Graphics2D g2d) {
        // Fond apaisant
        g2d.setColor(new Color(20, 40, 60));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Joueur qui se soigne
        drawPlayer(g2d, 120, 350, 0);
        
        // Particules de soin vertes
        g2d.setColor(Color.GREEN);
        for (int i = 0; i < 8; i++) {
            double angle = (animationFrame * 0.1 + i * Math.PI / 4);
            int x = 140 + (int)(30 * Math.cos(angle));
            int y = 340 + (int)(30 * Math.sin(angle));
            fillPixel(g2d, x / PIXEL_SIZE, y / PIXEL_SIZE, 1, 1);
        }
        
        // Croix de soin
        g2d.setColor(Color.RED);
        fillPixel(g2d, 14, 32, 3, 1);
        fillPixel(g2d, 15, 31, 1, 3);
    }
    
    private void drawItemScene(Graphics2D g2d) {
        // Fond
        drawCorridor(g2d);
        
        // Coffre au centre
        drawChest(g2d, 120, 300, animationFrame % 40 > 20);
        
        // Joueur devant
        drawPlayer(g2d, 120, 380, 0);
        
        // Items qui flottent
        if (animationFrame % 40 > 20) {
            int floatY = (int)(Math.sin(animationFrame * 0.2) * 10);
            drawItem(g2d, 140, 250 + floatY);
        }
    }
    
    private void drawAmeliorationScene(Graphics2D g2d) {
        // Fond mystique
        g2d.setColor(new Color(30, 0, 60));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Joueur
        drawPlayer(g2d, 120, 350, 0);
        
        // Aura d'amélioration
        g2d.setColor(new Color(150, 0, 255, 100));
        int radius = 30 + (int)(Math.sin(animationFrame * 0.1) * 10);
        g2d.fillOval(140 - radius, 350 - radius, radius * 2, radius * 2);
        
        // Étoiles magiques
        g2d.setColor(Color.CYAN);
        for (int i = 0; i < 6; i++) {
            double angle = animationFrame * 0.05 + i * Math.PI / 3;
            int x = 140 + (int)(40 * Math.cos(angle));
            int y = 350 + (int)(40 * Math.sin(angle));
            fillPixel(g2d, x / PIXEL_SIZE, y / PIXEL_SIZE, 1, 1);
        }
    }
    
    private void drawGameOverScene(Graphics2D g2d) {
        // Fond sombre
        g2d.setColor(new Color(10, 10, 10));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Joueur mort
        g2d.setColor(new Color(100, 100, 100));
        fillPixel(g2d, 12, 38, 6, 2); // Corps couché
        fillPixel(g2d, 11, 39, 2, 2); // Tête
        
        // Effet de fade
        int alpha = Math.min(255, animationFrame * 5);
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
    
    // Méthodes utilitaires de dessin
    
    private void fillPixel(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, width * PIXEL_SIZE, height * PIXEL_SIZE);
    }
    
    private void drawPlayer(Graphics2D g2d, int x, int y, int walkCycle) {
        // Conversion en coordonnées pixel art
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Tête
        g2d.setColor(new Color(255, 220, 180));
        fillPixel(g2d, px, py, 3, 3);
        
        // Corps (armure)
        g2d.setColor(new Color(100, 100, 150));
        fillPixel(g2d, px, py + 3, 3, 4);
        
        // Jambes
        g2d.setColor(new Color(50, 50, 100));
        if (walkCycle == 0) {
            fillPixel(g2d, px, py + 7, 1, 2);
            fillPixel(g2d, px + 2, py + 7, 1, 2);
        } else {
            fillPixel(g2d, px, py + 7, 1, 2);
            fillPixel(g2d, px + 2, py + 8, 1, 1);
        }
        
        // Épée
        g2d.setColor(Color.LIGHT_GRAY);
        fillPixel(g2d, px + 3, py + 4, 1, 3);
    }
    
    private void drawEnemy(Graphics2D g2d, int x, int y) {
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Corps ennemi (rouge)
        g2d.setColor(new Color(150, 50, 50));
        fillPixel(g2d, px, py, 3, 3);
        fillPixel(g2d, px, py + 3, 3, 4);
        
        // Yeux
        g2d.setColor(Color.YELLOW);
        fillPixel(g2d, px, py + 1, 1, 1);
        fillPixel(g2d, px + 2, py + 1, 1, 1);
        
        // Jambes
        g2d.setColor(new Color(100, 30, 30));
        fillPixel(g2d, px, py + 7, 1, 2);
        fillPixel(g2d, px + 2, py + 7, 1, 2);
    }
    
    private void drawBoss(Graphics2D g2d, int x, int y) {
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Corps boss (plus grand et plus sombre)
        g2d.setColor(new Color(80, 0, 0));
        fillPixel(g2d, px - 1, py, 5, 5);
        fillPixel(g2d, px - 1, py + 5, 5, 6);
        
        // Cornes
        g2d.setColor(Color.BLACK);
        fillPixel(g2d, px - 2, py - 1, 1, 2);
        fillPixel(g2d, px + 4, py - 1, 1, 2);
        
        // Yeux rouges
        g2d.setColor(Color.RED);
        fillPixel(g2d, px, py + 2, 1, 1);
        fillPixel(g2d, px + 3, py + 2, 1, 1);
    }
    
    private void drawDungeon(Graphics2D g2d, int x, int y) {
        // Murs
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(x, y, 200, 250);
        
        // Porte
        g2d.setColor(new Color(100, 50, 0));
        g2d.fillRect(x + 75, y + 150, 50, 100);
        
        // Poignée
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x + 110, y + 195, 10, 10);
    }
    
    private void drawCorridor(Graphics2D g2d) {
        // Sol
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(0, 400, getWidth(), 200);
        
        // Murs
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(0, 0, getWidth(), 400);
        
        // Perspective (lignes)
        g2d.setColor(new Color(50, 50, 50));
        g2d.drawLine(0, 400, 150, 200);
        g2d.drawLine(getWidth(), 400, 150, 200);
    }
    
    private void drawCombatBackground(Graphics2D g2d) {
        g2d.setColor(new Color(40, 40, 50));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Sol de combat
        g2d.setColor(new Color(60, 50, 40));
        g2d.fillRect(0, 400, getWidth(), 200);
    }
    
    private void drawTorch(Graphics2D g2d, int x, int y, int frame) {
        // Support
        g2d.setColor(new Color(80, 80, 80));
        g2d.fillRect(x, y, 10, 40);
        
        // Flamme animée
        int flameHeight = 20 + (int)(Math.sin(frame * 0.2) * 5);
        g2d.setColor(Color.ORANGE);
        g2d.fillOval(x - 5, y - flameHeight, 20, flameHeight);
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x, y - flameHeight + 5, 10, flameHeight - 10);
    }
    
    private void drawSwordSlash(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(3));
        int offset = (animationFrame % 15) * 2;
        g2d.drawArc(x - offset, y - 20, 40, 40, 45, 90);
    }
    
    private void drawStars(Graphics2D g2d) {
        g2d.setColor(new Color(20, 20, 40));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(Color.YELLOW);
        for (int i = 0; i < 20; i++) {
            int x = (i * 37) % getWidth();
            int y = (i * 53) % getHeight();
            if ((animationFrame + i * 10) % 30 < 15) {
                g2d.fillOval(x, y, 3, 3);
            }
        }
    }
    
    private void drawChest(Graphics2D g2d, int x, int y, boolean open) {
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Corps du coffre
        g2d.setColor(new Color(139, 69, 19));
        fillPixel(g2d, px, py + 2, 4, 3);
        
        // Couvercle
        if (open) {
            fillPixel(g2d, px, py - 1, 4, 2);
        } else {
            fillPixel(g2d, px, py, 4, 2);
        }
        
        // Serrure
        g2d.setColor(Color.YELLOW);
        fillPixel(g2d, px + 1, py + 3, 2, 1);
    }
    
    private void drawItem(Graphics2D g2d, int x, int y) {
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Potion ou objet brillant
        g2d.setColor(new Color(0, 200, 255));
        fillPixel(g2d, px, py, 2, 3);
        
        g2d.setColor(Color.CYAN);
        fillPixel(g2d, px, py + 1, 1, 1);
    }
    
    private void drawPixelText(Graphics2D g2d, String text, int x, int y) {
        g2d.drawString(text, x, y);
    }

    private void drawDoor(Graphics2D g2d, int x, int y, int numero) {
        // Porte en bois
        g2d.setColor(new Color(100, 50, 0));
        g2d.fillRect(x, y, 60, 100);

        // Bordure de la porte
        g2d.setColor(new Color(70, 35, 0));
        g2d.drawRect(x, y, 60, 100);
        g2d.drawRect(x + 1, y + 1, 58, 98);

        // Planches horizontales
        g2d.setColor(new Color(80, 40, 0));
        for (int i = 0; i < 4; i++) {
            g2d.fillRect(x + 5, y + 20 + i * 20, 50, 3);
        }

        // Poignée
        g2d.setColor(new Color(180, 150, 50));
        g2d.fillOval(x + 45, y + 50, 8, 12);

        // Numéro de la porte
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 24));
        String numStr = String.valueOf(numero);
        int numWidth = g2d.getFontMetrics().stringWidth(numStr);
        g2d.drawString(numStr, x + (60 - numWidth) / 2, y + 60);

        // Effet de lueur autour du numéro (animation)
        if (animationFrame % 60 < 30) {
            g2d.setColor(new Color(255, 255, 0, 50));
            g2d.fillOval(x + 15, y + 35, 30, 30);
        }
    }
}
