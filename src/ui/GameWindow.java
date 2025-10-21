package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import personnage.Joueur;

public class GameWindow extends JFrame {
    private final GamePanel gamePanel;
    private final AnimationPanel animationPanel;
    private final InventairePanel inventairePanel;
    private volatile boolean choiceValidated;
    private volatile int currentChoice;
    private volatile int maxChoices;
    private volatile boolean saveAndQuitRequested;
    private boolean inventaireVisible = false;
    private int inventaireSelectedIndex = 0;
    private boolean inventaireBloque = false; // Bloquer l'inventaire pendant le combat

    public GameWindow() {
        setTitle("Dungeon Game");
        this.gamePanel = new GamePanel();
        this.animationPanel = new AnimationPanel();
        this.inventairePanel = new InventairePanel();

        // Layout avec le texte à gauche, l'animation au centre-droit et l'inventaire par-dessus
        setLayout(new BorderLayout());

        // Panel principal avec gamePanel et animationPanel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(animationPanel, BorderLayout.EAST);

        // Ajouter le panel principal
        add(mainPanel, BorderLayout.CENTER);

        // Ajouter l'inventaire en overlay (LayeredPane)
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        mainPanel.setBounds(0, 0, 800, 600);
        inventairePanel.setBounds(200, 50, 400, 500);
        inventairePanel.setVisible(false);

        layeredPane.add(mainPanel, Integer.valueOf(0));
        layeredPane.add(inventairePanel, Integer.valueOf(1));

        setContentPane(layeredPane);

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
                // Gestion de l'inventaire avec la touche I (sauf si bloqué pendant le combat)
                if (e.getKeyCode() == KeyEvent.VK_I && !inventaireBloque) {
                    toggleInventaire();
                    return;
                }

                // Si l'inventaire est visible, gérer la navigation dans l'inventaire
                if (inventaireVisible) {
                    handleInventaireInput(e);
                    return;
                }

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

    private void handleInventaireInput(KeyEvent e) {
        Joueur joueur = inventairePanel.getJoueur();
        if (joueur == null || joueur.getInventaire().estVide()) {
            return;
        }

        int nbItems = joueur.getInventaire().getNombreItems();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (inventaireSelectedIndex > 0) {
                    inventaireSelectedIndex--;
                    inventairePanel.setSelectedIndex(inventaireSelectedIndex);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (inventaireSelectedIndex < nbItems - 1) {
                    inventaireSelectedIndex++;
                    inventairePanel.setSelectedIndex(inventaireSelectedIndex);
                }
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_E:
                // Utiliser/Équiper l'item sélectionné
                utiliserItemSelectionne();
                break;
        }
    }

    private void utiliserItemSelectionne() {
        Joueur joueur = inventairePanel.getJoueur();
        if (joueur == null || joueur.getInventaire().estVide()) {
            return;
        }

        if (inventaireSelectedIndex >= 0 && inventaireSelectedIndex < joueur.getInventaire().getNombreItems()) {
            model.Item item = joueur.getInventaire().getItem(inventaireSelectedIndex);
            if (item != null) {
                // Utiliser l'item
                String message = item.utiliser(joueur);

                // Retirer l'item de l'inventaire après utilisation
                joueur.getInventaire().retirerItem(inventaireSelectedIndex);

                // Afficher le message dans le GamePanel
                gamePanel.setInfoMessage(message);
                gamePanel.setStats(String.format("PV: %d/%d | ATK: %d",
                    joueur.getPv(), joueur.getPvMax(), joueur.getAttaque()));

                // Ajuster l'index si nécessaire
                if (inventaireSelectedIndex >= joueur.getInventaire().getNombreItems() && inventaireSelectedIndex > 0) {
                    inventaireSelectedIndex--;
                }

                inventairePanel.setSelectedIndex(inventaireSelectedIndex);
                inventairePanel.repaint();
            }
        }
    }

    private void toggleInventaire() {
        inventaireVisible = !inventaireVisible;
        inventairePanel.setVisible(inventaireVisible);

        if (inventaireVisible) {
            // Réinitialiser la sélection au premier item
            inventaireSelectedIndex = 0;
            inventairePanel.setSelectedIndex(inventaireSelectedIndex);
        }

        repaint();
    }

    public void setJoueur(Joueur joueur) {
        inventairePanel.setJoueur(joueur);
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

    /**
     * Bloque ou débloque l'accès à l'inventaire (utilisé pendant le combat)
     */
    public void setBloquerInventaire(boolean bloquer) {
        this.inventaireBloque = bloquer;
        if (bloquer && inventaireVisible) {
            // Fermer l'inventaire s'il est ouvert
            inventaireVisible = false;
            inventairePanel.setVisible(false);
            repaint();
        }
    }
}
