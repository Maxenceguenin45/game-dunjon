package service;

import item.Item;
import item.ItemFactory;
import personnage.Joueur;
import ui.GamePanel;
import ui.IGamePanel;
import java.util.Random;

/**
 * Service gérant le système d'expérience, de niveau et les récompenses
 */
public class NiveauService {
    private final ItemFactory itemFactory;
    private final Random random;

    public NiveauService() {
        this.itemFactory = new ItemFactory();
        this.random = new Random();
    }

    /**
     * Fait gagner de l'XP au joueur et gère les récompenses de niveau
     * @param joueur le joueur
     * @param xp l'XP à ajouter
     * @param gamePanel le panneau de jeu pour afficher les notifications
     * @return un message décrivant le gain d'XP et les récompenses
     */
    public String gagnerExperience(Joueur joueur, int xp, IGamePanel gamePanel) {
        int niveauAvant = joueur.getNiveau();
        boolean aGagneNiveau = joueur.gagnerExperience(xp);
        
        StringBuilder message = new StringBuilder();
        message.append(String.format("Vous gagnez %d XP !", xp));
        
        if (aGagneNiveau) {
            int niveauApres = joueur.getNiveau();
            message.append(String.format("\n\n*** NIVEAU SUPÉRIEUR ! ***\nVous êtes maintenant niveau %d !\n", niveauApres));
            
            // Afficher notification popup pour la montée de niveau
            if (gamePanel instanceof GamePanel) {
                ((GamePanel) gamePanel).showNotification(
                    "NIVEAU " + niveauApres + " ATTEINT !",
                    GamePanel.NotificationType.LEVEL_UP,
                    4000
                );
            }

            // Afficher les bonus de stats
            message.append("Vous vous sentez plus fort !\n");
            message.append(String.format("• PV Max augmentés\n"));
            message.append(String.format("• Attaque augmentée\n"));
            message.append(String.format("• PV restaurés complètement !\n"));
            
            // Donner des récompenses selon le niveau
            String recompenses = donnerRecompensesNiveau(joueur, niveauApres, gamePanel);
            if (recompenses != null && !recompenses.isEmpty()) {
                message.append("\nRécompenses de niveau :\n");
                message.append(recompenses);
            }
        } else {
            message.append(String.format(" (XP: %d/%d)", joueur.getExperience(), joueur.getExperienceRequise()));
        }
        
        return message.toString();
    }

    /**
     * Version surchargée pour compatibilité
     */
    public String gagnerExperience(Joueur joueur, int xp) {
        return gagnerExperience(joueur, xp, null);
    }

    /**
     * Donne des récompenses selon le niveau atteint
     * @param joueur le joueur
     * @param niveau le niveau atteint
     * @param gamePanel le panneau de jeu pour afficher les notifications
     * @return un message décrivant les récompenses
     */
    private String donnerRecompensesNiveau(Joueur joueur, int niveau, IGamePanel gamePanel) {
        StringBuilder recompenses = new StringBuilder();
        
        // Tous les 2 niveaux : item rare
        if (niveau % 2 == 0) {
            Item item = itemFactory.genererItemAleatoire(Item.Rarete.RARE);
            model.Item itemModel = adaptItemToModel(item);
            if (joueur.getInventaire().ajouterItem(itemModel)) {
                recompenses.append(String.format("• Item rare : %s\n", item.getNom()));
                if (gamePanel instanceof GamePanel) {
                    ((GamePanel) gamePanel).showNotification(
                        "Item Rare: " + item.getNom(),
                        GamePanel.NotificationType.RARE_ITEM
                    );
                }
            }
        }
        
        // Tous les 3 niveaux : item épique
        if (niveau % 3 == 0) {
            Item item = itemFactory.genererItemAleatoire(Item.Rarete.EPIQUE);
            model.Item itemModel = adaptItemToModel(item);
            if (joueur.getInventaire().ajouterItem(itemModel)) {
                recompenses.append(String.format("• Item épique : %s\n", item.getNom()));
                if (gamePanel instanceof GamePanel) {
                    ((GamePanel) gamePanel).showNotification(
                        "Item Épique: " + item.getNom(),
                        GamePanel.NotificationType.LEGENDARY_ITEM
                    );
                }
            }
        }
        
        // Tous les 5 niveaux : gros bonus de stats
        if (niveau % 5 == 0) {
            int bonusPvMax = 50;
            int bonusAttaque = 5;

            joueur.setPvMax(joueur.getPvMax() + bonusPvMax);
            joueur.setAttaque(joueur.getAttaque() + bonusAttaque);
            recompenses.append(String.format("• Bonus exceptionnel : +%d PV max, +%d ATK\n", bonusPvMax, bonusAttaque));

            if (gamePanel instanceof GamePanel) {
                ((GamePanel) gamePanel).showNotification(
                    "Bonus Exceptionnel !",
                    GamePanel.NotificationType.ACHIEVEMENT,
                    4000
                );
            }
        }
        
        return recompenses.toString();
    }

    /**
     * Génère un message d'affichage des stats du joueur incluant le niveau et l'XP
     * @param joueur le joueur
     * @return le message de stats
     */
    public String genererStatsAvecNiveau(Joueur joueur) {
        return String.format("Niv.%d | PV: %d/%d | ATK: %d | XP: %d/%d", 
            joueur.getNiveau(),
            joueur.getPv(), 
            joueur.getPvMax(), 
            joueur.getAttaque(),
            joueur.getExperience(),
            joueur.getExperienceRequise());
    }

    /**
     * Adapte un item du nouveau package item vers l'ancien model.Item
     */
    private model.Item adaptItemToModel(Item newItem) {
        model.Item.TypeItem type = model.Item.TypeItem.POTION_SOIN;
        int valeur = 0;
        
        String nom = newItem.getNom().toLowerCase();
        if (nom.contains("soin") || nom.contains("potion de soin")) {
            type = model.Item.TypeItem.POTION_SOIN;
            valeur = extraireValeurSoin(nom);
        } else if (nom.contains("force") || nom.contains("rage")) {
            type = model.Item.TypeItem.POTION_FORCE;
            valeur = extraireValeurForce(nom);
        } else if (nom.contains("armure")) {
            type = model.Item.TypeItem.ARMURE;
            valeur = extraireValeurArmure(nom);
        }
        
        return new model.Item(newItem.getNom(), type, valeur);
    }

    private int extraireValeurSoin(String nom) {
        if (nom.contains("petite")) return 30;
        if (nom.contains("grande")) return 80;
        if (nom.contains("totale")) return 9999;
        return 50;
    }

    private int extraireValeurForce(String nom) {
        if (nom.contains("petite")) return 5;
        if (nom.contains("grande")) return 20;
        if (nom.contains("rage")) return 50;
        return 10;
    }

    private int extraireValeurArmure(String nom) {
        if (nom.contains("cuir")) return 20;
        if (nom.contains("fer")) return 40;
        if (nom.contains("acier")) return 60;
        if (nom.contains("enchantée")) return 80;
        if (nom.contains("dragon") || nom.contains("légendaire")) return 150;
        return 40;
    }
}

