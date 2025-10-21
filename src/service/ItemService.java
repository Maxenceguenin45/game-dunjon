package service;

import model.Item;
import model.Joueur;
import model.Item.TypeItem;

import java.util.Random;

/**
 * Service gérant les items et l'inventaire.
 */
public class ItemService {
    private final Random random;

    public ItemService() {
        this.random = new Random();
    }

    /**
     * Génère un item aléatoire.
     *
     * @return Un item aléatoire
     */
    public Item genererItemAleatoire() {
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                return new Item("Potion de soin", TypeItem.POTION_SOIN, 30);
            case 1:
                return new Item("Potion de force", TypeItem.POTION_FORCE, 5);
            case 2:
                return new Item("Armure légère", TypeItem.ARMURE, 20);
            default:
                return new Item("Potion de soin", TypeItem.POTION_SOIN, 20);
        }
    }

    /**
     * Donne un item au joueur après avoir vaincu un ennemi.
     *
     * @param joueur Le joueur
     * @param difficulte La difficulté de l'ennemi (PV + ATK)
     * @return Message indiquant l'item reçu
     */
    public String donnerItemEnnemi(Joueur joueur, int difficulte) {
        // Plus l'ennemi est difficile, plus on a de chances d'avoir plusieurs items
        int nbItems = 1;
        if (difficulte > 100) {
            // Ennemi très difficile : 30% de chance d'avoir un 2ᵉ item
            if (random.nextInt(100) < 30) {
                nbItems = 2;
            }
        }

        StringBuilder message = new StringBuilder();
        int itemsRecus = 0;

        for (int i = 0; i < nbItems; i++) {
            Item item = genererItemAleatoire();
            if (joueur.getInventaire().ajouterItem(item)) {
                if (itemsRecus == 0) {
                    message.append("L'ennemi a laissé tomber : ").append(item.getNom());
                } else {
                    message.append(" et ").append(item.getNom());
                }
                itemsRecus++;
            }
        }

        if (itemsRecus > 0) {
            message.append(" !");
            return message.toString();
        } else {
            return "Inventaire plein ! Impossible de ramasser les items.";
        }
    }

    /**
     * Donne 1 à 3 items au joueur après avoir vaincu un boss.
     *
     * @param joueur Le joueur
     * @param difficulte La difficulté du boss (PV + ATK)
     * @return Message indiquant les items reçus
     */
    public String donnerItemsBoss(Joueur joueur, int difficulte) {
        // Plus le boss est difficile, plus il donne d'items
        int nbItemsBase = 1 + random.nextInt(3); // 1 à 3 items de base

        // Boss très difficile : +1 item bonus
        if (difficulte > 200) {
            nbItemsBase++;
        }

        StringBuilder message = new StringBuilder("Le boss a laissé tomber :\n");
        int itemsRecus = 0;

        for (int i = 0; i < nbItemsBase; i++) {
            Item item = genererItemAleatoire();
            if (joueur.getInventaire().ajouterItem(item)) {
                message.append("- ").append(item.getNom()).append("\n");
                itemsRecus++;
            } else {
                message.append("- ").append(item.getNom()).append(" (inventaire plein)\n");
            }
        }

        if (itemsRecus == 0) {
            return "Le boss a laissé des items mais votre inventaire est plein !";
        }

        return message.toString();
    }

    /**
     * Utilise un item de l'inventaire du joueur.
     *
     * @param joueur Le joueur
     * @param indexItem L'index de l'item dans l'inventaire
     * @return Message décrivant l'utilisation de l'item
     */
    public String utiliserItem(Joueur joueur, int indexItem) {
        Item item = joueur.getInventaire().retirerItem(indexItem);
        if (item == null) {
            return "Item invalide.";
        }
        return item.utiliser(joueur);
    }

    /**
     * Génère une description de l'inventaire du joueur.
     *
     * @param joueur Le joueur
     * @return Description de l'inventaire
     */
    public String[] getDescriptionsInventaire(Joueur joueur) {
        if (joueur.getInventaire().estVide()) {
            return new String[]{"Inventaire vide", "Retour"};
        }

        int nbItems = joueur.getInventaire().getNombreItems();
        String[] descriptions = new String[nbItems + 1];
        
        for (int i = 0; i < nbItems; i++) {
            Item item = joueur.getInventaire().getItem(i);
            descriptions[i] = "Utiliser : " + item.toString();
        }
        descriptions[nbItems] = "Retour";
        
        return descriptions;
    }
}
