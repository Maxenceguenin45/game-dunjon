package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import personnage.Joueur;
import model.Item;

/**
 * Panneau affichant l'inventaire du joueur sur un parchemin
 */
public class InventairePanel extends JPanel {
    private Joueur joueur;
    private int selectedIndex = -1;
    private boolean visible = false;

    public InventairePanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 500));
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
        repaint();
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
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
        g2d.setFont(new Font("Serif", Font.BOLD, 28));
        String titre = "~ Inventaire ~";
        int titreWidth = g2d.getFontMetrics().stringWidth(titre);
        g2d.drawString(titre, (getWidth() - titreWidth) / 2, 60);

        // Dessiner une ligne décorative sous le titre
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(50, 75, getWidth() - 50, 75);

        // Dessiner les stats du joueur
        dessinerStats(g2d);

        // Dessiner les items
        dessinerItems(g2d);

        // Dessiner les instructions
        dessinerInstructions(g2d);
    }

    private void dessinerParchemin(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int margin = 20;

        // Ombre du parchemin
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(margin + 5, margin + 5, width - 2 * margin, height - 2 * margin, 20, 20);

        // Fond du parchemin (couleur papier ancien)
        GradientPaint gradient = new GradientPaint(
            0, margin, new Color(245, 235, 210),
            0, height - margin, new Color(230, 215, 185)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(margin, margin, width - 2 * margin, height - 2 * margin, 20, 20);

        // Bordure du parchemin
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(margin, margin, width - 2 * margin, height - 2 * margin, 20, 20);

        // Bordure interne décorative
        g2d.setColor(new Color(180, 140, 80));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(margin + 10, margin + 10, width - 2 * margin - 20, height - 2 * margin - 20, 15, 15);

        // Effet de texture (lignes horizontales légères)
        g2d.setColor(new Color(200, 180, 150, 30));
        for (int y = margin + 30; y < height - margin; y += 15) {
            g2d.drawLine(margin + 20, y, width - margin - 20, y);
        }

        // Coins décoratifs (petits ornements)
        dessinerCoinDecor(g2d, margin + 20, margin + 20);
        dessinerCoinDecor(g2d, width - margin - 30, margin + 20);
        dessinerCoinDecor(g2d, margin + 20, height - margin - 30);
        dessinerCoinDecor(g2d, width - margin - 30, height - margin - 30);
    }

    private void dessinerCoinDecor(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(139, 90, 43, 100));
        g2d.setStroke(new BasicStroke(2));
        
        // Petit motif en coin
        Path2D path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + 10, y);
        path.moveTo(x, y);
        path.lineTo(x, y + 10);
        path.moveTo(x + 3, y + 3);
        path.lineTo(x + 7, y + 3);
        path.moveTo(x + 3, y + 3);
        path.lineTo(x + 3, y + 7);
        
        g2d.draw(path);
    }

    private void dessinerStats(Graphics2D g2d) {
        g2d.setColor(new Color(100, 50, 20));
        g2d.setFont(new Font("Serif", Font.PLAIN, 16));

        int y = 105;
        String stats = String.format("Niv.%d | PV: %d/%d | ATK: %d",
            joueur.getNiveau(),
            joueur.getPv(), joueur.getPvMax(), joueur.getAttaque());
        int statsWidth = g2d.getFontMetrics().stringWidth(stats);
        g2d.drawString(stats, (getWidth() - statsWidth) / 2, y);

        // Barre d'expérience
        y += 20;
        g2d.setFont(new Font("Serif", Font.PLAIN, 13));
        String xpText = String.format("XP: %d / %d", joueur.getExperience(), joueur.getExperienceRequise());
        int xpTextWidth = g2d.getFontMetrics().stringWidth(xpText);
        g2d.drawString(xpText, (getWidth() - xpTextWidth) / 2, y);

        // Barre de progression visuelle
        y += 5;
        int barWidth = 200;
        int barHeight = 10;
        int barX = (getWidth() - barWidth) / 2;

        // Fond de la barre
        g2d.setColor(new Color(139, 90, 43, 100));
        g2d.fillRoundRect(barX, y, barWidth, barHeight, 5, 5);

        // Progression
        double progression = (double) joueur.getExperience() / joueur.getExperienceRequise();
        int progressWidth = (int) (barWidth * progression);

        GradientPaint gradient = new GradientPaint(
            barX, y, new Color(255, 215, 0),
            barX + progressWidth, y, new Color(218, 165, 32)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(barX, y, progressWidth, barHeight, 5, 5);

        // Bordure de la barre
        g2d.setColor(new Color(139, 90, 43));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(barX, y, barWidth, barHeight, 5, 5);
    }

    private void dessinerItems(Graphics2D g2d) {
        int startY = 140;
        int itemHeight = 60;
        int margin = 40;

        if (joueur.getInventaire().estVide()) {
            g2d.setColor(new Color(120, 70, 30, 150));
            g2d.setFont(new Font("Serif", Font.ITALIC, 18));
            String vide = "~ Inventaire vide ~";
            int videWidth = g2d.getFontMetrics().stringWidth(vide);
            g2d.drawString(vide, (getWidth() - videWidth) / 2, startY + 50);
            return;
        }

        int nbItems = joueur.getInventaire().getNombreItems();
        
        for (int i = 0; i < nbItems; i++) {
            Item item = joueur.getInventaire().getItem(i);
            if (item == null) continue;

            int y = startY + i * itemHeight;
            
            // Fond de l'item (surbrillance si sélectionné)
            if (i == selectedIndex) {
                g2d.setColor(new Color(255, 215, 0, 100));
                g2d.fillRoundRect(margin, y - 5, getWidth() - 2 * margin, itemHeight - 10, 10, 10);
                
                // Bordure dorée
                g2d.setColor(new Color(218, 165, 32));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(margin, y - 5, getWidth() - 2 * margin, itemHeight - 10, 10, 10);
            }

            // Icône de l'item
            dessinerIconeItem(g2d, margin + 10, y, item);

            // Nom de l'item
            g2d.setColor(new Color(80, 40, 20));
            g2d.setFont(new Font("Serif", Font.BOLD, 16));
            g2d.drawString(item.getNom(), margin + 50, y + 15);

            // Description de l'item
            g2d.setColor(new Color(100, 60, 30));
            g2d.setFont(new Font("Serif", Font.PLAIN, 13));
            String description = item.toString();
            if (description.length() > 40) {
                description = description.substring(0, 37) + "...";
            }
            g2d.drawString(description, margin + 50, y + 32);

            // Indicateur de sélection
            if (i == selectedIndex) {
                g2d.setColor(new Color(218, 165, 32));
                g2d.setFont(new Font("Serif", Font.BOLD, 18));
                g2d.drawString("▶", margin - 15, y + 20);
            }
        }
    }

    private void dessinerIconeItem(Graphics2D g2d, int x, int y, Item item) {
        // Déterminer la couleur selon le type d'item
        Color couleurIcone;
        String symbole;
        
        String typeStr = item.getType().toString();
        switch (typeStr) {
            case "POTION_SOIN":
                couleurIcone = new Color(255, 100, 100);
                symbole = "❤";
                break;
            case "POTION_FORCE":
                couleurIcone = new Color(255, 150, 0);
                symbole = "⚔";
                break;
            case "ARMURE":
                couleurIcone = new Color(150, 150, 200);
                symbole = "🛡";
                break;
            default:
                couleurIcone = new Color(200, 200, 100);
                symbole = "✦";
                break;
        }

        // Fond de l'icône
        g2d.setColor(new Color(couleurIcone.getRed(), couleurIcone.getGreen(), couleurIcone.getBlue(), 50));
        g2d.fillOval(x, y, 35, 35);

        // Bordure de l'icône
        g2d.setColor(couleurIcone);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x, y, 35, 35);

        // Symbole (ou lettre si symbole non supporté)
        g2d.setColor(couleurIcone);
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        
        // Fallback si les symboles ne s'affichent pas
        String affichage = symbole;
        if (g2d.getFont().canDisplayUpTo(symbole) != -1) {
            affichage = typeStr.substring(0, 1);
        }
        
        int symbWidth = g2d.getFontMetrics().stringWidth(affichage);
        g2d.drawString(affichage, x + (35 - symbWidth) / 2, y + 24);
    }

    private void dessinerInstructions(Graphics2D g2d) {
        int y = getHeight() - 70;

        g2d.setColor(new Color(100, 60, 30, 150));
        g2d.setFont(new Font("Serif", Font.ITALIC, 13));

        String instruction1 = "↑↓ : Naviguer | ENTER/E : Utiliser/Équiper";
        int instr1Width = g2d.getFontMetrics().stringWidth(instruction1);
        g2d.drawString(instruction1, (getWidth() - instr1Width) / 2, y);

        g2d.setFont(new Font("Serif", Font.ITALIC, 14));
        String instruction2 = "Appuyez sur [I] pour fermer l'inventaire";
        int instr2Width = g2d.getFontMetrics().stringWidth(instruction2);
        g2d.drawString(instruction2, (getWidth() - instr2Width) / 2, y + 20);
    }
}
