package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import animation.CombatAnimation;

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
    // Facteur de suréchantillonnage (1 = normal, 2 = rendu 2x puis réduction)
    private int hdScale = 2;
    private final CombatAnimation sceneCombatAnimation = new CombatAnimation();

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

    /**
     * Permet d'ajuster le facteur de précision visuelle.
     * 1 = désactivé (rendu normal), 2 ou 3 = rendu plus fin.
     */
    public void setHdScale(int scale) {
        if (scale < 1) scale = 1;
        if (scale > 3) scale = 3;
        this.hdScale = scale;
        repaint();
    }

    /**
     * Démarre l'animation de combat dans la scène (coordonnées en pixels du panneau AnimationPanel)
     */
    public void startSceneCombatAnimation(int x, int y) {
        sceneCombatAnimation.start(x, y);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Hints de rendu haute qualité pour des formes plus lisses et un texte net
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (hdScale <= 1) {
            drawCurrentScene(g2d);
            return;
        }

        // Suréchantillonnage : rendu dans un buffer HD puis réduction
        int w = Math.max(1, getWidth() * hdScale);
        int h = Math.max(1, getHeight() * hdScale);
        BufferedImage hdImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gHD = hdImg.createGraphics();
        try {
            // Appliquer les mêmes hints + échelle
            gHD.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gHD.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            gHD.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            gHD.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            gHD.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            gHD.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            gHD.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Mise à l'échelle du contexte pour conserver les coordonnées d'origine
            gHD.scale(hdScale, hdScale);

            drawCurrentScene(gHD);
        } finally {
            gHD.dispose();
        }

        // Dessiner l'image HD réduite dans le panneau
        g2d.drawImage(hdImg, 0, 0, getWidth(), getHeight(), null);
    }

    // Factorisation du dessin de la scène courante
    private void drawCurrentScene(Graphics2D g2d) {
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

        // Halo doux derrière le personnage pour le rendre plus visible
        g2d.setColor(new Color(255, 255, 200, 60));
        g2d.fillOval(115, 360, 100, 100); // autour de la tête/torse du joueur

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
        
        // Animation de coup (surimpression si active)
        drawSceneCombatAnimation(g2d);
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

        // Animation de coup
        drawSceneCombatAnimation(g2d);
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

        // Animation de coup
        drawSceneCombatAnimation(g2d);
    }

    private void drawSceneCombatAnimation(Graphics2D g2d) {
        if (sceneCombatAnimation.isActive()) {
            sceneCombatAnimation.draw(g2d);
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

        // Ombre au sol pour un rendu plus "réel"
        g2d.setColor(new Color(0, 0, 0, 60));
        fillPixel(g2d, px - 1, py + 9, 5, 1);

        // Tête
        g2d.setColor(new Color(255, 220, 180));
        fillPixel(g2d, px, py, 3, 3);

        // Casque plus visible (2 rangées + reflet central)
        g2d.setColor(new Color(90, 90, 140));
        fillPixel(g2d, px, py - 2, 3, 1);
        fillPixel(g2d, px, py - 1, 3, 1);
        g2d.setColor(new Color(180, 180, 230)); // reflet
        fillPixel(g2d, px + 1, py - 2, 1, 1);

        // Yeux
        g2d.setColor(new Color(40, 40, 60));
        fillPixel(g2d, px, py + 1, 1, 1);
        fillPixel(g2d, px + 2, py + 1, 1, 1);

        // Corps (armure) avec léger ombrage latéral
        g2d.setColor(new Color(100, 100, 150));
        fillPixel(g2d, px, py + 3, 3, 4);
        g2d.setColor(new Color(80, 80, 120)); // ombre côté gauche
        fillPixel(g2d, px, py + 3, 1, 4);
        g2d.setColor(new Color(120, 120, 170)); // éclaircie au centre
        fillPixel(g2d, px + 1, py + 4, 1, 2);
        // Ceinture
        g2d.setColor(new Color(120, 90, 40));
        fillPixel(g2d, px, py + 6, 3, 1);

        // Jambes
        g2d.setColor(new Color(50, 50, 100));
        if (walkCycle == 0) {
            fillPixel(g2d, px, py + 7, 1, 2);
            fillPixel(g2d, px + 2, py + 7, 1, 2);
        } else {
            fillPixel(g2d, px, py + 7, 1, 2);
            fillPixel(g2d, px + 2, py + 8, 1, 1);
        }
        // Bottes plus sombres
        g2d.setColor(new Color(30, 30, 70));
        fillPixel(g2d, px, py + 9, 1, 1);
        fillPixel(g2d, px + 2, py + 9, 1, 1);

        // Épée
        g2d.setColor(new Color(210, 210, 210));
        fillPixel(g2d, px + 3, py + 4, 1, 3);
        // Garde de l'épée
        g2d.setColor(new Color(170, 140, 60));
        fillPixel(g2d, px + 3, py + 6, 1, 1);
    }

    
    private void drawEnemy(Graphics2D g2d, int x, int y) {
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Ombre
        g2d.setColor(new Color(0, 0, 0, 60));
        fillPixel(g2d, px - 1, py + 9, 5, 1);

        // Corps ennemi (rouge) avec ombrage
        g2d.setColor(new Color(150, 50, 50));
        fillPixel(g2d, px, py, 3, 3);
        fillPixel(g2d, px, py + 3, 3, 4);
        g2d.setColor(new Color(110, 30, 30));
        fillPixel(g2d, px, py + 3, 1, 4); // ombre côté gauche

        // Yeux
        g2d.setColor(Color.YELLOW);
        fillPixel(g2d, px, py + 1, 1, 1);
        fillPixel(g2d, px + 2, py + 1, 1, 1);
        // Bouche sombre
        g2d.setColor(new Color(60, 0, 0));
        fillPixel(g2d, px + 1, py + 2, 1, 1);

        // Jambes
        g2d.setColor(new Color(100, 30, 30));
        fillPixel(g2d, px, py + 7, 1, 2);
        fillPixel(g2d, px + 2, py + 7, 1, 2);
        // Griffes/pieds
        g2d.setColor(new Color(70, 20, 20));
        fillPixel(g2d, px, py + 9, 1, 1);
        fillPixel(g2d, px + 2, py + 9, 1, 1);
    }
    
    private void drawBoss(Graphics2D g2d, int x, int y) {
        int px = x / PIXEL_SIZE;
        int py = y / PIXEL_SIZE;
        
        // Ombre
        g2d.setColor(new Color(0, 0, 0, 70));
        fillPixel(g2d, px - 2, py + 11, 9, 1);

        // Corps boss (plus grand et plus sombre)
        g2d.setColor(new Color(80, 0, 0));
        fillPixel(g2d, px - 1, py, 5, 5);
        fillPixel(g2d, px - 1, py + 5, 5, 6);
        // Ombrage latéral
        g2d.setColor(new Color(60, 0, 0));
        fillPixel(g2d, px - 1, py + 1, 1, 9);

        // Cornes
        g2d.setColor(Color.BLACK);
        fillPixel(g2d, px - 2, py - 1, 1, 2);
        fillPixel(g2d, px + 4, py - 1, 1, 2);
        
        // Yeux rouges
        g2d.setColor(Color.RED);
        fillPixel(g2d, px, py + 2, 1, 1);
        fillPixel(g2d, px + 3, py + 2, 1, 1);
        // Cuirasse/pectorale claire
        g2d.setColor(new Color(120, 20, 20));
        fillPixel(g2d, px, py + 5, 3, 2);
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

        // Léger dégradé vertical sur les murs pour de la profondeur
        Paint oldPaint = g2d.getPaint();
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 20), 0, 400, new Color(0, 0, 0, 90));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), 400);
        g2d.setPaint(oldPaint);

        // Motif de briques (joint horizontal + joints verticaux décalés)
        g2d.setColor(new Color(70, 70, 70));
        for (int y = 40; y < 400; y += 40) {
            g2d.drawLine(0, y, getWidth(), y);
            int offset = ((y / 40) % 2) * 30; // décalage 0/30
            for (int x = offset; x < getWidth(); x += 60) {
                g2d.drawLine(x, y - 40, x, y); // petits joints verticaux
            }
        }

        // Perspective (lignes) – multiplier les lignes vers le point de fuite
        g2d.setColor(new Color(50, 50, 50));
        int vx = 150, vy = 200; // point de fuite
        g2d.drawLine(0, 400, vx, vy);
        g2d.drawLine(getWidth(), 400, vx, vy);
        for (int i = 1; i <= 6; i++) {
            int x = (int) ((i / 7.0) * getWidth());
            g2d.drawLine(x, getHeight(), vx, vy);
        }
        // Bandes horizontales sur le sol
        for (int y = 440; y < getHeight(); y += 40) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }
    
    private void drawCombatBackground(Graphics2D g2d) {
        g2d.setColor(new Color(40, 40, 50));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Sol de combat
        g2d.setColor(new Color(60, 50, 40));
        g2d.fillRect(0, 400, getWidth(), 200);
    }
    
    private void drawTorch(Graphics2D g2d, int x, int y, int frame) {
        // Lueur de torche (avant le support/les flammes)
        g2d.setColor(new Color(255, 220, 120, 60));
        g2d.fillOval(x - 30, y - 70, 80, 100);

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
        // Porte en bois (fond)
        g2d.setColor(new Color(100, 50, 0));
        g2d.fillRect(x, y, 60, 100);

        // Bordure arrondie pour un look plus "joli"
        g2d.setColor(new Color(70, 35, 0));
        g2d.drawRoundRect(x, y, 60, 100, 12, 12);
        g2d.drawRect(x + 1, y + 1, 58, 98);

        // Planches horizontales
        g2d.setColor(new Color(80, 40, 0));
        for (int i = 0; i < 4; i++) {
            g2d.fillRect(x + 5, y + 20 + i * 20, 50, 3);
        }

        // Charnières m��talliques
        g2d.setColor(new Color(60, 40, 10));
        g2d.fillRect(x + 5, y + 25, 12, 4);
        g2d.fillRect(x + 5, y + 65, 12, 4);

        // Clous décoratifs
        g2d.setColor(new Color(160, 130, 60));
        for (int cx = x + 12; cx <= x + 48; cx += 12) {
            g2d.fillOval(cx, y + 15, 3, 3);
            g2d.fillOval(cx, y + 85, 3, 3);
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

        // Ombre interne à gauche et en bas
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRect(x + 1, y + 1, 4, 98);
        g2d.fillRect(x + 1, y + 96, 58, 3);
    }
}
