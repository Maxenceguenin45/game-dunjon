package controller;

import model.Joueur;
import model.salle.Salle;
import model.salle.CombattantSalle;
import service.*;
import ui.GameWindow;

/**
 * Contrôleur principal du jeu gérant la logique de déroulement d'une partie.
 */
public class GameController {
    private static final int MAX_SALLES = 10;
    
    private final JoueurService joueurService;
    private final SalleService salleService;
    private final CombatService combatService;
    private final UIService uiService;
    private final GameWindow gameWindow;
    
    public GameController() {
        this.joueurService = new JoueurService();
        this.salleService = new SalleService();
        this.uiService = new UIService();
        this.gameWindow = uiService.creerFenetre();
        this.combatService = new CombatService(gameWindow.getGamePanel());
    }
    
    public void demarrerJeu() {
        boolean running = true;
        while (running) {
            running = executerPartie();
        }
    }
    
    private boolean executerPartie() {
        try {
            Joueur joueur = initialiserJoueur();
            if (null == joueur) {
                return false;
            }

            return deroulerPartie(joueur);
        } catch (Exception e) {
            System.err.println("Une erreur est survenue : " + e.getMessage());
            return false;
        }
    }
    
    private Joueur initialiserJoueur() {
        if (joueurService.sauvegardeExiste()) {
            uiService.afficherMessage(gameWindow.getGamePanel(), "Une sauvegarde a été trouvée.");
            String[] options = {"Reprendre la sauvegarde", "Nouvelle partie"};
            int choix = gameWindow.waitForChoice(options);

            if (choix == 0) {
                try {
                    Joueur joueur = joueurService.chargerSauvegarde();
                    uiService.afficherMessage(gameWindow.getGamePanel(), "Sauvegarde chargée : " + joueur.getPseudo());
                    uiService.afficherStats(gameWindow.getGamePanel(), joueurService.genererStatsJoueur(joueur));
                    return joueur;
                } catch (Exception e) {
                    uiService.afficherMessage(gameWindow.getGamePanel(), "Erreur lors du chargement : " + e.getMessage());
                }
            }
        }

        // Créer un nouveau joueur avec un pseudo par défaut ou demandé
        uiService.afficherMessage(gameWindow.getGamePanel(), "Nouvelle partie créée");
        Joueur joueur = joueurService.creerJoueur("Aventurier");
        uiService.afficherStats(gameWindow.getGamePanel(), joueurService.genererStatsJoueur(joueur));
        return joueur;
    }
    
    private boolean deroulerPartie(Joueur joueur) {
        int sallesParcourues = 0;
        
        while (0 < joueur.getPv() && MAX_SALLES > sallesParcourues) {
            sallesParcourues++;
            
            if (!gererSalle(joueur, sallesParcourues)) {
                return gererFinPartie(joueur, sallesParcourues, false);
            }
            
            combatService.attendreProchaineAction();
        }
        
        return gererFinPartie(joueur, sallesParcourues, MAX_SALLES <= sallesParcourues);
    }
    
    private boolean gererSalle(Joueur joueur, int sallesParcourues) {
        int nbChemins = salleService.genererNombreChemins();
        String[] descriptions = new String[nbChemins];
        Salle[] chemins = salleService.genererChemins(nbChemins, sallesParcourues, descriptions);
        
        int choix = gameWindow.waitForChoice(descriptions);
        if (0 > choix) {
            return false;
        }
        
        Salle salleChoisie = chemins[choix];
        uiService.afficherMessage(gameWindow.getGamePanel(), 
            uiService.genererMessageSalle(sallesParcourues, descriptions[choix]));
        
        return gererCombat(joueur, salleChoisie, nbChemins);
    }
    
    private boolean gererCombat(Joueur joueur, Salle salle, int nbChemins) {
        boolean salleFinie = false;
        while (!salleFinie && 0 < joueur.getPv()) {
            int pvAvant = (salle instanceof CombattantSalle) ? ((CombattantSalle)salle).getPv() : 0;
            salle.entrer(joueur);
            
            String messageCombat = salleService.traiterCombat(salle, pvAvant);
            if (null != messageCombat) {
                uiService.afficherMessage(gameWindow.getGamePanel(), messageCombat);
                
                if (salle instanceof CombattantSalle && 0 >= ((CombattantSalle)salle).getPv()) {
                    salleFinie = true;
                } else {
                    String[] options = combatService.getOptionsCombat(nbChemins > 1);
                    int decision = gameWindow.waitForChoice(options);
                    if (combatService.doitFuir(decision)) {
                        return false;
                    }
                }
            } else {
                salleFinie = true;
            }
            
            uiService.afficherStats(gameWindow.getGamePanel(), 
                joueurService.genererStatsJoueur(joueur));
        }
        
        return true;
    }
    
    private boolean gererFinPartie(Joueur joueur, int sallesParcourues, boolean victoire) {
        String stats = joueurService.genererScoreFinal(joueur, sallesParcourues);
        String messageGameOver = uiService.genererMessageGameOver(victoire, stats);
        gameWindow.getGamePanel().setGameOver(messageGameOver);
        
        int choix = gameWindow.waitForChoice(new String[]{"Appuyez sur ENTRÉE"});
        return -1 != choix;
    }
}
