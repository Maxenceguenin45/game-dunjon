package animation;

import java.awt.*;

public class CombatAnimation {
    private static final int DURATION = 500;
    private static final int TAILLE_EPEE = 30;
    private static final double ANGLE_GARDE = Math.PI / 6;

    private long startTime;
    private boolean isActive;
    private int x, y;

    public void start(int x, int y) {
        this.x = x;
        this.y = y;
        startTime = System.currentTimeMillis();
        isActive = true;
    }

    public boolean isActive() {
        return isActive && (DURATION > System.currentTimeMillis() - startTime);
    }

    public void draw(Graphics2D g2d) {
        if (!isActive()) {
            isActive = false;
            return;
        }

        float progress = (float) (System.currentTimeMillis() - startTime) / DURATION;
        dessinerEpee(g2d, progress);
        dessinerTrainée(g2d, progress);
        dessinerEtincelles(g2d, progress);
    }

    private void dessinerEpee(Graphics2D g2d, float progress) {
        // Couleur principale de la lame
        g2d.setColor(new Color(230, 230, 255));
        // Épaisseur dynamique (plus épais au début)
        float stroke = 2.5f - 1.5f * progress;
        g2d.setStroke(new BasicStroke(Math.max(1.5f, stroke)));

        double angle = progress * Math.PI;
        int x1 = x;
        int y1 = y;
        int x2 = (int) (x + TAILLE_EPEE * Math.cos(angle));
        int y2 = (int) (y + TAILLE_EPEE * Math.sin(angle));
        int x3 = (int) (x + (TAILLE_EPEE / 2.0) * Math.cos(angle + ANGLE_GARDE));
        int y3 = (int) (y + (TAILLE_EPEE / 2.0) * Math.sin(angle + ANGLE_GARDE));

        int[] xPoints = new int[]{x1, x2, x3};
        int[] yPoints = new int[]{y1, y2, y3};
        g2d.drawPolyline(xPoints, yPoints, 3);
    }

    private void dessinerTrainée(Graphics2D g2d, float progress) {
        // Une légère traînée en arc qui s’estompe
        int size = Math.round(TAILLE_EPEE * 1.6f);
        int fade = (int) (120 * (1.0f - progress));
        fade = Math.max(0, Math.min(120, fade));
        if (fade == 0) return;

        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade / 255f));
        g2d.setColor(new Color(180, 220, 255));
        g2d.setStroke(new BasicStroke(2f));

        int offset = (int) (progress * 20);
        g2d.drawArc(x - offset - size / 2, y - size / 2, size, size, 20, 110);

        g2d.setComposite(old);
    }

    private void dessinerEtincelles(Graphics2D g2d, float progress) {
        // Étincelles à l’impact (fin de l’animation)
        if (progress < 0.8f) return;
        int sparks = 6;
        double baseAngle = progress * Math.PI;
        int radius = Math.round(TAILLE_EPEE * 0.9f);

        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (progress - 0.8f) / 0.2f));
        g2d.setColor(new Color(255, 240, 120));
        g2d.setStroke(new BasicStroke(1.5f));

        int cx = (int) (x + radius * Math.cos(baseAngle));
        int cy = (int) (y + radius * Math.sin(baseAngle));

        for (int i = 0; i < sparks; i++) {
            double ang = baseAngle + (2 * Math.PI / sparks) * i;
            int len = 6 + (i % 2);
            int sx = (int) (cx + len * Math.cos(ang));
            int sy = (int) (cy + len * Math.sin(ang));
            g2d.drawLine(cx, cy, sx, sy);
        }

        g2d.setComposite(old);
    }
}
