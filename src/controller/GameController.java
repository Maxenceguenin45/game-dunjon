package controller;

import model.salle.Salle;
import model.salle.CombattantSalle;
import personnage.Joueur;
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
    private final ItemService itemService;

    public GameController() {
        this.joueurService = new JoueurService();
        this.salleService = new SalleService();
        this.uiService = new UIService();
        this.gameWindow = uiService.creerFenetre();
        this.combatService = new CombatService(gameWindow.getGamePanel());
        this.itemService = new ItemService();

        // Activer un rendu plus précis pour l'animation (2 = HD, 3 = très HD)
        uiService.reglerQualiteAnimation(gameWindow, 2);

        // Démarrer avec la scène de menu
        uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.MENU);
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
            try {
                Joueur joueurSauvegarde = joueurService.chargerSauvegarde();

                // Vérifier si le joueur sauvegardé est mort
                if (joueurSauvegarde.getPv() <= 0) {
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "La sauvegarde contient un personnage mort.");
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "Création d'une nouvelle partie...");
                    Joueur nouveauJoueur = joueurService.creerJoueur("Aventurier");
                    uiService.afficherStats(gameWindow.getGamePanel(),
                        joueurService.genererStatsJoueur(nouveauJoueur));
                    return nouveauJoueur;
                }

                // Proposer de reprendre la sauvegarde si le joueur est vivant
                uiService.afficherMessage(gameWindow.getGamePanel(),
                    "Une sauvegarde a été trouvée (" + joueurSauvegarde.getPseudo() + ")");
                uiService.afficherMessage(gameWindow.getGamePanel(),
                    "PV: " + joueurSauvegarde.getPv() + "/" + joueurSauvegarde.getPvMax());
                String[] options = {"Reprendre la sauvegarde", "Nouvelle partie"};
                int choix = gameWindow.waitForChoice(options);

                if (choix == 0) {
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "Sauvegarde chargée : " + joueurSauvegarde.getPseudo());
                    uiService.afficherStats(gameWindow.getGamePanel(),
                        joueurService.genererStatsJoueur(joueurSauvegarde));
                    return joueurSauvegarde;
                }
            } catch (Exception e) {
                uiService.afficherMessage(gameWindow.getGamePanel(),
                    "Erreur lors du chargement : " + e.getMessage());
            }
        }

        // Créer un nouveau joueur avec un pseudo par défaut ou demandé
        uiService.afficherMessage(gameWindow.getGamePanel(), "Nouvelle partie créée");
        Joueur joueur = joueurService.creerJoueur("Aventurier");
        uiService.afficherStats(gameWindow.getGamePanel(),
            joueurService.genererStatsJoueur(joueur));
        return joueur;
    }
    
    private boolean deroulerPartie(Joueur joueur) {
        int sallesParcourues = 0;
        
        while (0 < joueur.getPv() && MAX_SALLES > sallesParcourues) {
            sallesParcourues++;
            
            int resultat = gererSalle(joueur, sallesParcourues);

            // Si le joueur a quitté avec la touche S (résultat -1), sortir sans game over
            if (resultat == -1) {
                System.exit(0); // Fermer complètement le jeu
                return false; // Quitter le jeu sans afficher game over
            }

            // Si le joueur est mort ou a abandonné
            if (resultat == 0) {
                return gererFinPartie(joueur, sallesParcourues, false);
            }
            
            // Sauvegarder automatiquement après chaque salle
            try {
                joueurService.sauvegarderJoueur(joueur);
            } catch (Exception e) {
                System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            }

            combatService.attendreProchaineAction();
        }
        
        return gererFinPartie(joueur, sallesParcourues, MAX_SALLES <= sallesParcourues);
    }
    
    private int gererSalle(Joueur joueur, int sallesParcourues) {
        int nbChemins = salleService.genererNombreChemins();
        String[] descriptions = new String[nbChemins];
        Salle[] chemins = salleService.genererChemins(nbChemins, sallesParcourues, descriptions);

        // Changer la scène en exploration avec le nombre de chemins
        uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.EXPLORATION, nbChemins);

        boolean choixValide = false;
        while (!choixValide && joueur.getPv() > 0) {
            int choix = gameWindow.waitForChoice(descriptions);

            // Vérifier si le joueur a demandé à sauvegarder et quitter
            if (gameWindow.isSaveAndQuitRequested()) {
                gameWindow.resetSaveAndQuitRequest();

                // Sauvegarder d'abord
                try {
                    joueurService.sauvegarderJoueur(joueur);
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "Partie sauvegardée avec succès !");
                } catch (Exception e) {
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "Erreur lors de la sauvegarde : " + e.getMessage());
                }

                // Demander confirmation pour quitter
                String[] optionsSauvegarde = {"Continuer à jouer", "Quitter le jeu"};
                int choixSauvegarde = gameWindow.waitForChoice(optionsSauvegarde);

                if (choixSauvegarde == 1) {
                    // L'utilisateur veut vraiment quitter
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "À bientôt, " + joueur.getPseudo() + " !");
                    combatService.attendreProchaineAction();
                    return -1; // Code spécial pour quitter
                }
                // Sinon, on continue le jeu normalement
                continue;
            }

            if (0 > choix) {
                return 0; // Abandon
            }

            Salle salleChoisie = chemins[choix];
            uiService.afficherMessage(gameWindow.getGamePanel(),
                uiService.genererMessageSalle(sallesParcourues, descriptions[choix]));

            int resultatCombat = gererCombat(joueur, salleChoisie, nbChemins);

            // Si le joueur a quitté avec S pendant le combat
            if (resultatCombat == -1) {
                return -1;
            }

            choixValide = (resultatCombat == 1);

            // Si le joueur a battu en retraite, on revient au choix
            if (!choixValide && joueur.getPv() > 0) {
                uiService.afficherMessage(gameWindow.getGamePanel(),
                    "Vous revenez à l'embranchement précédent. Choisissez un autre chemin.");
                uiService.afficherStats(gameWindow.getGamePanel(),
                    joueurService.genererStatsJoueur(joueur));
            }
        }

        return (joueur.getPv() > 0) ? 1 : 0;
    }
    
    private int gererCombat(Joueur joueur, Salle salle, int nbChemins) {
        // Changer la scène selon le type de salle
        if (salle instanceof model.salle.SalleBoss) {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.BOSS);
        } else if (salle instanceof model.salle.SalleEnnemi) {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.COMBAT);
        } else if (salle instanceof model.salle.SalleSoin) {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.SOIN);
        } else if (salle instanceof model.salle.SalleItem) {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.ITEM);
        } else if (salle instanceof model.salle.SalleAmelioration) {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.AMELIORATION);
        }

        boolean salleFinie = false;
        while (!salleFinie && 0 < joueur.getPv()) {
            // Vérifier si le joueur a demandé à sauvegarder et quitter
            if (gameWindow.isSaveAndQuitRequested()) {
                gameWindow.resetSaveAndQuitRequest();

                // Sauvegarder d'abord
                try {
                    joueurService.sauvegarderJoueur(joueur);
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "Partie sauvegardée avec succès !");
                } catch (Exception e) {
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "Erreur lors de la sauvegarde : " + e.getMessage());
                }

                // Demander confirmation pour quitter
                String[] optionsSauvegarde = {"Continuer à jouer", "Quitter le jeu"};
                int choixSauvegarde = gameWindow.waitForChoice(optionsSauvegarde);

                if (choixSauvegarde == 1) {
                    // L'utilisateur veut vraiment quitter
                    uiService.afficherMessage(gameWindow.getGamePanel(),
                        "À bientôt, " + joueur.getPseudo() + " !");
                    combatService.attendreProchaineAction();
                    return -1; // Code spécial pour quitter
                }
                // Sinon, on continue le combat normalement
            }

            int pvAvant = (salle instanceof CombattantSalle) ? ((CombattantSalle)salle).getPv() : 0;
            salle.entrer(joueur);
            // Déclencher l'animation de coup d'épée côté AnimationPanel (la scène)
            if (gameWindow != null && gameWindow.getAnimationPanel() != null) {
                // Coordonnées autour du milieu entre le joueur (60,350) et l'ennemi (200,340)
                gameWindow.getAnimationPanel().startSceneCombatAnimation(130, 345);
            }

            String messageCombat = salleService.traiterCombat(salle, pvAvant);
            if (null != messageCombat) {
                uiService.afficherMessage(gameWindow.getGamePanel(), messageCombat);
                
                if (salle instanceof CombattantSalle && 0 >= ((CombattantSalle)salle).getPv()) {
                    salleFinie = true;

                    // Changer la scène en victoire
                    uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.VICTOIRE);

                    // Donner des items après avoir vaincu l'ennemi ou le boss
                    String messageItem;
                    if (salle instanceof model.salle.SalleBoss) {
                        model.salle.SalleBoss boss = (model.salle.SalleBoss) salle;
                        int difficulte = boss.getAttaque() + 100;
                        messageItem = itemService.donnerItemsBoss(joueur, difficulte);
                    } else if (salle instanceof model.salle.SalleEnnemi) {
                        model.salle.SalleEnnemi ennemi = (model.salle.SalleEnnemi) salle;
                        int difficulte = ennemi.getAttaque() * 2;
                        messageItem = itemService.donnerItemEnnemi(joueur, difficulte);
                    } else {
                        messageItem = null;
                    }

                    if (messageItem != null) {
                        uiService.afficherMessage(gameWindow.getGamePanel(), messageItem);
                    }
                } else {
                    // Si l'ennemi est blessé mais toujours vivant
                    if (salle instanceof CombattantSalle) {
                        uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.ENNEMI_BLESSE);
                    }

                    boolean actionValide = false;
                    while (!actionValide && joueur.getPv() > 0) {
                        // Vérifier si le joueur a demandé à sauvegarder et quitter
                        if (gameWindow.isSaveAndQuitRequested()) {
                            gameWindow.resetSaveAndQuitRequest();

                            // Sauvegarder d'abord
                            try {
                                joueurService.sauvegarderJoueur(joueur);
                                uiService.afficherMessage(gameWindow.getGamePanel(),
                                    "Partie sauvegardée avec succ��s !");
                            } catch (Exception e) {
                                uiService.afficherMessage(gameWindow.getGamePanel(),
                                    "Erreur lors de la sauvegarde : " + e.getMessage());
                            }

                            // Demander confirmation pour quitter
                            String[] optionsSauvegarde = {"Continuer à jouer", "Quitter le jeu"};
                            int choixSauvegarde = gameWindow.waitForChoice(optionsSauvegarde);

                            if (choixSauvegarde == 1) {
                                // L'utilisateur veut vraiment quitter
                                uiService.afficherMessage(gameWindow.getGamePanel(),
                                    "À bientôt, " + joueur.getPseudo() + " !");
                                combatService.attendreProchaineAction();
                                return -1; // Code spécial pour quitter
                            }
                            // Sinon, on continue le combat normalement
                            continue;
                        }

                        boolean aDesItems = !joueur.getInventaire().estVide();
                        String[] options = combatService.getOptionsCombat(nbChemins > 1, aDesItems);
                        int decision = gameWindow.waitForChoice(options);

                        // Vérifier si le joueur veut utiliser un item
                        if (combatService.veutUtiliserItem(decision, aDesItems)) {
                            gererInventaireCombat(joueur);
                            // Ne pas terminer la boucle, le joueur continue le combat
                        } else if (combatService.doitFuir(decision, aDesItems)) {
                            // Appliquer les dégâts de retraite
                            String messageRetraite = combatService.appliquerDegatsRetraite(joueur);
                            uiService.afficherMessage(gameWindow.getGamePanel(), messageRetraite);
                            uiService.afficherStats(gameWindow.getGamePanel(),
                                joueurService.genererStatsJoueur(joueur));

                            // Vérifier si le joueur est toujours en vie
                            if (joueur.getPv() <= 0) {
                                uiService.afficherMessage(gameWindow.getGamePanel(),
                                    "Vous êtes mort en fuyant...");
                            }

                            return 0; // Retourner au choix précédent
                        } else {
                            // Choix "Combattre"
                            actionValide = true;
                        }
                    }
                }
            } else {
                salleFinie = true;
            }
            
            uiService.afficherStats(gameWindow.getGamePanel(), 
                joueurService.genererStatsJoueur(joueur));
        }
        
        return 1; // Salle terminée avec succès
    }
    
    private boolean gererFinPartie(Joueur joueur, int sallesParcourues, boolean victoire) {
        // Changer la scène selon le résultat
        if (victoire) {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.VICTOIRE);
        } else {
            uiService.changerSceneAnimation(gameWindow, ui.AnimationPanel.SceneType.GAME_OVER);
        }

        // Supprimer la sauvegarde quand la partie se termine
        joueurService.supprimerSauvegarde();

        String stats = joueurService.genererScoreFinal(joueur, sallesParcourues);
        String messageGameOver = uiService.genererMessageGameOver(victoire, stats);
        gameWindow.getGamePanel().setGameOver(messageGameOver);
        
        String[] optionsFinPartie = {"Rejouer", "Quitter"};
        int choix = gameWindow.waitForChoice(optionsFinPartie);

        if (choix == 0) {
            // Réinitialiser l'affichage pour une nouvelle partie
            gameWindow.getGamePanel().resetGame();
            return true; // Rejouer
        }

        // L'utilisateur a choisi de quitter
        System.exit(0);
        return false; // Quitter
    }

    private void gererInventaire(Joueur joueur) {
        boolean continuer = true;
        while (continuer && joueur.getPv() > 0) {
            String[] optionsInventaire = itemService.getDescriptionsInventaire(joueur);
            uiService.afficherMessage(gameWindow.getGamePanel(),
                "=== Inventaire (" + joueur.getInventaire().getNombreItems() + "/"
                + joueur.getInventaire().getCapaciteMax() + ") ===");

            int choix = gameWindow.waitForChoice(optionsInventaire);

            if (choix < 0) {
                continuer = false;
            } else if (choix >= joueur.getInventaire().getNombreItems()) {
                // Option "Retour" sélectionnée
                continuer = false;
            } else {
                // Utiliser l'item
                String messageUtilisation = itemService.utiliserItem(joueur, choix);
                uiService.afficherMessage(gameWindow.getGamePanel(), messageUtilisation);
                uiService.afficherStats(gameWindow.getGamePanel(),
                    joueurService.genererStatsJoueur(joueur));
            }
        }
    }

    private void gererInventaireCombat(Joueur joueur) {
        boolean continuer = true;
        while (continuer && joueur.getPv() > 0) {
            String[] optionsInventaire = itemService.getDescriptionsInventaire(joueur);
            int choix = gameWindow.waitForChoice(optionsInventaire);

            if (choix < 0 || choix >= joueur.getInventaire().getNombreItems()) {
                continuer = false; // Retour ou invalide
            } else {
                String messageUtilisation = itemService.utiliserItem(joueur, choix);
                uiService.afficherMessage(gameWindow.getGamePanel(), messageUtilisation);
                uiService.afficherStats(gameWindow.getGamePanel(),
                    joueurService.genererStatsJoueur(joueur));
            }
        }
    }
}
