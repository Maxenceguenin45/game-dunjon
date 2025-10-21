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
    private final ArmePanel armePanel;

    // Fenêtres séparées pour l'inventaire et l'arme
    private JFrame inventaireWindow;
    private JFrame armeWindow;

    private volatile boolean choiceValidated;
    private volatile int currentChoice;
    private volatile int maxChoices;
    private volatile boolean saveAndQuitRequested;
    private boolean inventaireVisible = false;
    private int inventaireSelectedIndex = 0;
    private boolean inventaireBloque = false;

    public GameWindow() {
        setTitle("Dungeon Game");
        this.gamePanel = new GamePanel();
        this.animationPanel = new AnimationPanel();
        this.inventairePanel = new InventairePanel();
        this.armePanel = new ArmePanel();

        // Layout principal
        setLayout(new BorderLayout());

        // Panel principal avec gamePanel et animationPanel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(animationPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Créer les fenêtres séparées pour l'inventaire et l'arme
        creerFenetresInventaire();

        setupKeyListener();
        setVisible(true);
    }

    private void creerFenetresInventaire() {
        // Fenêtre pour l'inventaire
        inventaireWindow = new JFrame("Inventaire");
        inventaireWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        inventaireWindow.setUndecorated(false); // Avec bordures
        inventaireWindow.setResizable(false);
        inventaireWindow.add(inventairePanel);
        inventaireWindow.pack();
        inventaireWindow.setSize(420, 520);

        // Fenêtre pour l'arme
        armeWindow = new JFrame("Arme Équipée");
        armeWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        armeWindow.setUndecorated(false);
        armeWindow.setResizable(false);
        armeWindow.add(armePanel);
        armeWindow.pack();
        armeWindow.setSize(300, 420);

        // Positionner les fenêtres à droite de la fenêtre principale
        positionnerFenetresInventaire();

        // Cacher initialement
        inventaireWindow.setVisible(false);
        armeWindow.setVisible(false);

        // Rendre les panneaux visibles
        inventairePanel.setVisible(true);
        armePanel.setVisible(true);
    }

    private void positionnerFenetresInventaire() {
        // Obtenir la position de la fenêtre principale
        Point mainLocation = getLocation();
        int mainWidth = getWidth();

        // Positionner l'inventaire à droite de la fenêtre principale
        inventaireWindow.setLocation(
            mainLocation.x + mainWidth + 10,
            mainLocation.y + 50
        );

        // Positionner le panneau d'arme en dessous de l'inventaire
        armeWindow.setLocation(
            mainLocation.x + mainWidth + 10,
            mainLocation.y + 50 + inventaireWindow.getHeight() + 10
        );
    }

    private void setupKeyListener() {
        KeyAdapter keyListener = new KeyAdapter() {
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
        };

        addKeyListener(keyListener);
        // Ajouter aussi le listener aux fenêtres d'inventaire
        inventaireWindow.addKeyListener(keyListener);
        armeWindow.addKeyListener(keyListener);

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
                armePanel.repaint(); // Rafraîchir le panneau d'arme aussi
            }
        }
    }

    private void toggleInventaire() {
        inventaireVisible = !inventaireVisible;

        if (inventaireVisible) {
            // Repositionner les fenêtres au cas où la fenêtre principale aurait bougé
            positionnerFenetresInventaire();

            inventaireSelectedIndex = 0;
            inventairePanel.setSelectedIndex(inventaireSelectedIndex);

            // Afficher les fenêtres
            inventaireWindow.setVisible(true);
            armeWindow.setVisible(true);

            // Donner le focus à la fenêtre d'inventaire
            inventaireWindow.requestFocus();
        } else {
            // Cacher les fenêtres
            inventaireWindow.setVisible(false);
            armeWindow.setVisible(false);

            // Remettre le focus sur la fenêtre principale
            this.requestFocus();
        }
    }

    public void setJoueur(Joueur joueur) {
        inventairePanel.setJoueur(joueur);
        armePanel.setJoueur(joueur);
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
            inventaireWindow.setVisible(false);
            armeWindow.setVisible(false);
            this.requestFocus();
        }
    }
}
