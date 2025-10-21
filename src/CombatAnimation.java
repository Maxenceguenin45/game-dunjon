import java.awt.*;

public class CombatAnimation {
    private long startTime;
    private boolean isActive;
    private static final int DURATION = 500; // durée de l'animation en millisecondes
    private int x, y;

    public void start(int x, int y) {
        this.x = x;
        this.y = y;
        startTime = System.currentTimeMillis();
        isActive = true;
    }

    public boolean isActive() {
        return isActive && (System.currentTimeMillis() - startTime < DURATION);
    }

    public void draw(Graphics2D g2d) {
        if (!isActive()) {
            isActive = false;
            return;
        }

        long elapsed = System.currentTimeMillis() - startTime;
        float progress = (float) elapsed / DURATION;

        // Animation d'épée qui frappe
        g2d.setColor(Color.WHITE);
        int size = 30;
        double angle = progress * Math.PI; // Rotation de l'épée
        
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        
        // Calculer les points de l'épée
        xPoints[0] = x;
        yPoints[0] = y;
        xPoints[1] = (int) (x + size * Math.cos(angle));
        yPoints[1] = (int) (y + size * Math.sin(angle));
        xPoints[2] = (int) (x + (size/2) * Math.cos(angle + Math.PI/6));
        yPoints[2] = (int) (y + (size/2) * Math.sin(angle + Math.PI/6));

        g2d.setStroke(new BasicStroke(3));
        g2d.drawPolyline(xPoints, yPoints, 3);
    }
}
