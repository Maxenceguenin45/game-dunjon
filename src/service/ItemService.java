package service;

import item.Item;
import item.ItemFactory;
import personnage.Joueur;

import java.util.Random;

/**
 * Service gérant les items et l'inventaire.
 */
public class ItemService {
    private final Random random;
    private final ItemFactory itemFactory;

    public ItemService() {
        this.random = new Random();
        this.itemFactory = new ItemFactory(random);
    }

    /**
     * Génère un item aléatoire.
     *
     * @return Un item aléatoire
     */
    public Item genererItemAleatoire() {
        return itemFactory.genererItemAleatoire();
    }

    /**
     * Génère un item aléatoire selon la rareté
     *
     * @param rarete la rareté minimale de l'item
     * @return Un item aléatoire
     */
    public Item genererItemAleatoire(Item.Rarete rarete) {
        return itemFactory.genererItemAleatoire(rarete);
    }

    /**
     * Donne un item au joueur après avoir vaincu un ennemi.
     *
     * @param joueur Le joueur
     * @param difficulte La difficulté de l'ennemi (PV + ATK)
     * @return Message indiquant l'item reçu
     */
    public String donnerItemEnnemi(Joueur joueur, int difficulte) {
        // Pour les ennemis normaux, FORCER la rareté COMMUNE
        Item.Rarete rarete = Item.Rarete.COMMUN;

        // Les ennemis normaux donnent généralement 1 item
        int nbItems = 1;

        // Ennemi difficile : 20% de chance d'avoir un 2ᵉ item
        if (difficulte > 80 && random.nextInt(100) < 20) {
            nbItems = 2;
        }

        StringBuilder message = new StringBuilder();
        int itemsRecus = 0;

        for (int i = 0; i < nbItems; i++) {
            // Utiliser la nouvelle méthode avec estBoss = false
            Item item = itemFactory.genererItemAleatoire(rarete, false);
            if (joueur.getInventaire().ajouterItem(adaptItemToModel(item))) {
                if (itemsRecus == 0) {
                    message.append("💀 L'ennemi a laissé tomber : ").append(item.getNom());
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
        // Boss : Déterminer la rareté selon la difficulté (RARE, ÉPIQUE, LÉGENDAIRE)
        Item.Rarete rarete = itemFactory.determinerRarete(difficulte, true);

        // Boss donne 2 à 4 items garantis
        int nbItemsBase = 2 + random.nextInt(3); // 2 à 4 items

        // Boss très difficile : +1 item bonus
        if (difficulte > 250) {
            nbItemsBase++;
        }

        StringBuilder message = new StringBuilder("🏆 Le boss a laissé tomber :\n");
        int itemsRecus = 0;

        for (int i = 0; i < nbItemsBase; i++) {
            // Utiliser la nouvelle méthode avec estBoss = true
            Item item = itemFactory.genererItemAleatoire(rarete, true);
            if (joueur.getInventaire().ajouterItem(adaptItemToModel(item))) {
                message.append("• ").append(item.getNom()).append(" [").append(item.getRarete().getNom()).append("]\n");
                itemsRecus++;
            } else {
                message.append("• ").append(item.getNom()).append(" (inventaire plein)\n");
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
        model.Item oldItem = joueur.getInventaire().retirerItem(indexItem);
        if (oldItem == null) {
            return "Item invalide.";
        }
        return oldItem.utiliser(joueur);
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
            model.Item item = joueur.getInventaire().getItem(i);
            descriptions[i] = "Utiliser : " + item.toString();
        }
        descriptions[nbItems] = "Retour";
        
        return descriptions;
    }

    /**
     * Adapte un item du nouveau package item vers l'ancien model.Item
     * (méthode de compatibilité temporaire)
     */
    private model.Item adaptItemToModel(Item newItem) {
        // Convertir le nouvel item en ancien format pour la compatibilité avec l'inventaire
        model.Item.TypeItem type = model.Item.TypeItem.POTION_SOIN; // valeur par défaut
        int valeur = 0;

        // Déterminer le type en fonction du nom/description
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
        return 50; // moyenne par défaut
    }

    private int extraireValeurForce(String nom) {
        if (nom.contains("petite")) return 5;
        if (nom.contains("grande")) return 20;
        if (nom.contains("rage")) return 50;
        return 10; // moyenne par défaut
    }

    private int extraireValeurArmure(String nom) {
        if (nom.contains("cuir")) return 20;
        if (nom.contains("fer")) return 40;
        if (nom.contains("acier")) return 60;
        if (nom.contains("enchantée")) return 80;
        if (nom.contains("dragon") || nom.contains("légendaire")) return 150;
        return 40; // valeur par défaut
    }
}
