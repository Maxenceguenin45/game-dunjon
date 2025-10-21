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
    }

    private void dessinerEpee(Graphics2D g2d, float progress) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));

        double angle = progress * Math.PI;
        int[] xPoints = new int[]{
            x,
            (int) (x + TAILLE_EPEE * Math.cos(angle)),
            (int) (x + (TAILLE_EPEE/2) * Math.cos(angle + ANGLE_GARDE))
        };
        int[] yPoints = new int[]{
            y,
            (int) (y + TAILLE_EPEE * Math.sin(angle)),
            (int) (y + (TAILLE_EPEE/2) * Math.sin(angle + ANGLE_GARDE))
        };

        g2d.drawPolyline(xPoints, yPoints, 3);
    }
}
