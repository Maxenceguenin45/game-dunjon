package ui;

public interface IGamePanel {
    void setInfoMessage(String message);
    void setStats(String stats);
    void setGameOver(String message);
    void startCombatAnimation(int x, int y);
}

@interface UIComponent {}
