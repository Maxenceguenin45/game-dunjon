package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameWindow extends JFrame {
    private final GamePanel gamePanel;
    private final AnimationPanel animationPanel;
    private volatile boolean choiceValidated;
    private volatile int currentChoice;
    private volatile int maxChoices;
    private volatile boolean saveAndQuitRequested;

    public GameWindow() {
        setTitle("Dungeon Game");
        this.gamePanel = new GamePanel();
        this.animationPanel = new AnimationPanel();

        // Layout avec le texte à gauche et l'animation à droite
        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(animationPanel, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setupKeyListener();
        setVisible(true);
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (choiceValidated) {
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (currentChoice > 0) {
                            currentChoice--;
                            gamePanel.setPlayerPosition(currentChoice);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (currentChoice < maxChoices - 1) {
                            currentChoice++;
                            gamePanel.setPlayerPosition(currentChoice);
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                        validateChoice(currentChoice);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        validateChoice(-1);
                        break;
                    case KeyEvent.VK_S:
                        // Sauvegarder et quitter avec la touche S
                        saveAndQuitRequested = true;
                        validateChoice(-2);
                        break;
                }
            }
        });
        setFocusable(true);
        requestFocus();
    }

    public IGamePanel getGamePanel() {
        return gamePanel;
    }

    public AnimationPanel getAnimationPanel() {
        return animationPanel;
    }

    public int waitForChoice(String[] descriptions) {
        currentChoice = 0;
        maxChoices = descriptions.length;
        choiceValidated = false;

        if (gamePanel != null) {
            gamePanel.updateChoices(descriptions);
            gamePanel.setPlayerPosition(0);
        }

        synchronized (this) {
            while (!choiceValidated) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return -1;
                }
            }
        }

        return currentChoice;
    }

    public void validateChoice(int choice) {
        synchronized (this) {
            currentChoice = choice;
            choiceValidated = true;
            notify();
        }
    }

    public boolean isSaveAndQuitRequested() {
        return saveAndQuitRequested;
    }

    public void resetSaveAndQuitRequest() {
        saveAndQuitRequested = false;
    }
}
