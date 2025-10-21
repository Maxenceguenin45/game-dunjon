package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import personnage.Joueur;
import item.arme.Arme;

/**
 * Panneau affichant l'arme équipée du joueur avec ses informations de maîtrise
 */
public class ArmePanel extends JPanel {
    private Joueur joueur;
    private boolean visible = false;

    public ArmePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(280, 400));
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        repaint();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        super.setVisible(visible);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!visible || joueur == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Dessiner le parchemin
        dessinerParchemin(g2d);

        // Dessiner le titre
        g2d.setColor(new Color(80, 40, 20));
        g2d.setFont(new Font("Serif", Font.BOLD, 24));
        String titre = "⚔ Arme ⚔";
        int titreWidth = g2d.getFontMetrics().stringWidth(titre);
        g2d.drawString(titre, (getWidth() - titreWidth) / 2, 50);

        // Dessiner une ligne décorative
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(40, 60, getWidth() - 40, 60);

        // Dessiner l'arme équipée
        dessinerArmeEquipee(g2d);
    }

    private void dessinerParchemin(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int margin = 15;

        // Ombre du parchemin
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(margin + 4, margin + 4, width - 2 * margin, height - 2 * margin, 15, 15);

        // Fond du parchemin (couleur parchemin)
        GradientPaint gradient = new GradientPaint(
            0, margin, new Color(255, 245, 220),
            0, height - margin, new Color(240, 220, 190)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(margin, margin, width - 2 * margin, height - 2 * margin, 15, 15);

        // Bordure du parchemin
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRoundRect(margin, margin, width - 2 * margin, height - 2 * margin, 15, 15);

        // Bordure interne
        g2d.setColor(new Color(180, 140, 80));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(margin + 8, margin + 8, width - 2 * margin - 16, height - 2 * margin - 16, 12, 12);

        // Texture
        g2d.setColor(new Color(200, 180, 150, 20));
        for (int y = margin + 25; y < height - margin; y += 12) {
            g2d.drawLine(margin + 15, y, width - margin - 15, y);
        }

        // Coins décoratifs
        dessinerCoinDecor(g2d, margin + 15, margin + 15);
        dessinerCoinDecor(g2d, width - margin - 25, margin + 15);
        dessinerCoinDecor(g2d, margin + 15, height - margin - 25);
        dessinerCoinDecor(g2d, width - margin - 25, height - margin - 25);
    }

    private void dessinerCoinDecor(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(139, 90, 43, 100));
        g2d.setStroke(new BasicStroke(1.5f));
        
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + 8, y);
        path.moveTo(x, y);
        path.lineTo(x, y + 8);
        path.moveTo(x + 2, y + 2);
        path.lineTo(x + 6, y + 2);
        path.moveTo(x + 2, y + 2);
        path.lineTo(x + 2, y + 6);
        
        g2d.draw(path);
    }

    private void dessinerArmeEquipee(Graphics2D g2d) {
        Arme arme = joueur.getArmeEquipee();
        
        if (arme == null) {
            // Aucune arme équipée
            g2d.setColor(new Color(120, 70, 30, 150));
            g2d.setFont(new Font("Serif", Font.ITALIC, 16));
            String message = "Aucune arme";
            String message2 = "équipée";
            int w1 = g2d.getFontMetrics().stringWidth(message);
            int w2 = g2d.getFontMetrics().stringWidth(message2);
            g2d.drawString(message, (getWidth() - w1) / 2, 120);
            g2d.drawString(message2, (getWidth() - w2) / 2, 145);
            
            // Dessiner une icône d'arme vide
            g2d.setColor(new Color(150, 150, 150, 80));
            g2d.setStroke(new BasicStroke(3));
            int centerX = getWidth() / 2;
            int centerY = 200;
            g2d.drawLine(centerX - 20, centerY + 30, centerX - 5, centerY - 20);
            g2d.drawLine(centerX - 5, centerY - 20, centerX + 5, centerY - 30);
            g2d.drawLine(centerX - 10, centerY - 15, centerX + 10, centerY - 15);
            
            return;
        }

        int y = 90;
        int margin = 30;

        // Cadre pour l'arme avec effet de surbrillance
        g2d.setColor(new Color(218, 165, 32, 50));
        g2d.fillRoundRect(margin - 5, y - 5, getWidth() - 2 * margin + 10, 280, 12, 12);
        
        g2d.setColor(new Color(218, 165, 32));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(margin - 5, y - 5, getWidth() - 2 * margin + 10, 280, 12, 12);

        // Icône de l'arme (grande)
        dessinerIconeArme(g2d, getWidth() / 2 - 30, y + 10, arme);

        y += 90;

        // Nom de l'arme
        g2d.setColor(new Color(80, 40, 20));
        g2d.setFont(new Font("Serif", Font.BOLD, 18));
        String nomArme = arme.getNom();
        int nomWidth = g2d.getFontMetrics().stringWidth(nomArme);
        g2d.drawString(nomArme, (getWidth() - nomWidth) / 2, y);

        y += 25;

        // Type d'attaque
        g2d.setColor(new Color(100, 60, 30));
        g2d.setFont(new Font("Serif", Font.PLAIN, 14));
        String typeArme = "Type: " + arme.getTypeAttaque().name();
        int typeWidth = g2d.getFontMetrics().stringWidth(typeArme);
        g2d.drawString(typeArme, (getWidth() - typeWidth) / 2, y);

        y += 20;

        // Dégâts de base
        String degatsBase = "Dégâts: " + arme.getDegats();
        int degatsWidth = g2d.getFontMetrics().stringWidth(degatsBase);
        g2d.drawString(degatsBase, (getWidth() - degatsWidth) / 2, y);

        y += 30;

        // Séparateur
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(margin + 10, y, getWidth() - margin - 10, y);

        y += 25;

        // Niveau de maîtrise
        g2d.setColor(new Color(80, 40, 20));
        g2d.setFont(new Font("Serif", Font.BOLD, 16));
        String niveauText = "✨ Maîtrise Niv. " + arme.getNiveauMaitrise();
        int niveauWidth = g2d.getFontMetrics().stringWidth(niveauText);
        g2d.drawString(niveauText, (getWidth() - niveauWidth) / 2, y);

        y += 25;

        // Bonus de maîtrise
        g2d.setColor(new Color(218, 165, 32));
        g2d.setFont(new Font("Serif", Font.BOLD, 18));
        String bonusText = "+" + String.format("%.0f", arme.getBonusMaitrise()) + "% ATK";
        int bonusWidth = g2d.getFontMetrics().stringWidth(bonusText);
        g2d.drawString(bonusText, (getWidth() - bonusWidth) / 2, y);

        y += 25;

        // Barre de progression XP
        g2d.setColor(new Color(100, 60, 30));
        g2d.setFont(new Font("Serif", Font.PLAIN, 12));
        String xpText = String.format("XP: %d / %d", arme.getExperienceMaitrise(), arme.getExperienceRequise());
        int xpWidth = g2d.getFontMetrics().stringWidth(xpText);
        g2d.drawString(xpText, (getWidth() - xpWidth) / 2, y);

        y += 8;

        // Barre visuelle
        int barWidth = 180;
        int barHeight = 12;
        int barX = (getWidth() - barWidth) / 2;

        // Fond de la barre
        g2d.setColor(new Color(139, 90, 43, 100));
        g2d.fillRoundRect(barX, y, barWidth, barHeight, 6, 6);

        // Progression
        double progression = (double) arme.getExperienceMaitrise() / arme.getExperienceRequise();
        int progressWidth = (int) (barWidth * progression);

        GradientPaint gradient = new GradientPaint(
            barX, y, new Color(255, 215, 0),
            barX + progressWidth, y, new Color(218, 165, 32)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(barX, y, progressWidth, barHeight, 6, 6);

        // Bordure de la barre
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(barX, y, barWidth, barHeight, 6, 6);

        y += 25;

        // Dégâts totaux
        g2d.setColor(new Color(100, 60, 30));
        g2d.setFont(new Font("Serif", Font.BOLD, 14));
        String degatsTotal = "Dégâts totaux: " + arme.calculerDegatsTotal();
        int totalWidth = g2d.getFontMetrics().stringWidth(degatsTotal);
        g2d.drawString(degatsTotal, (getWidth() - totalWidth) / 2, y);
    }

    private void dessinerIconeArme(Graphics2D g2d, int x, int y, Arme arme) {
        int size = 60;
        
        // Fond de l'icône avec effet de brillance
        GradientPaint gradient = new GradientPaint(
            x, y, new Color(255, 215, 0, 80),
            x + size, y + size, new Color(218, 165, 32, 40)
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x, y, size, size);

        // Bordure dorée
        g2d.setColor(new Color(218, 165, 32));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x, y, size, size);

        // Dessiner l'épée stylisée
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(4));
        
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        
        // Lame
        g2d.setColor(new Color(192, 192, 192));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(centerX, centerY - 18, centerX, centerY + 10);
        
        // Pointe
        int[] xPoints = {centerX, centerX - 4, centerX + 4};
        int[] yPoints = {centerY - 22, centerY - 18, centerY - 18};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Garde
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(centerX - 12, centerY + 10, centerX + 12, centerY + 10);
        
        // Poignée
        g2d.setColor(new Color(101, 67, 33));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(centerX, centerY + 10, centerX, centerY + 20);
        
        // Pommeau
        g2d.setColor(new Color(218, 165, 32));
        g2d.fillOval(centerX - 4, centerY + 18, 8, 8);
    }
}

