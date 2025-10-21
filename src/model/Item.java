package model;

import personnage.Joueur;

/**
 * Classe représentant un item dans le jeu.
 */
public class Item {
    private final String nom;
    private final TypeItem type;
    private final int valeur;

    public Item(String nom, TypeItem type, int valeur) {
        this.nom = nom;
        this.type = type;
        this.valeur = valeur;
    }

    public String getNom() {
        return nom;
    }

    public TypeItem getType() {
        return type;
    }

    public int getValeur() {
        return valeur;
    }

    /**
     * Utilise l'item sur le joueur
     * @param joueur le joueur qui utilise l'item
     * @return message décrivant l'effet
     */
    public String utiliser(Joueur joueur) {
        switch (type) {
            case POTION_SOIN:
                int soin = valeur;
                joueur.setPv(joueur.getPv() + soin);
                return String.format("Vous utilisez %s et récupérez %d PV !", nom, soin);

            case POTION_FORCE:
                int bonusForce = valeur;
                joueur.setAttaque(joueur.getAttaque() + bonusForce);
                return String.format("Vous utilisez %s ! Attaque +%d", nom, bonusForce);

            case ARMURE:
                int bonusPv = valeur;
                joueur.setPvMax(joueur.getPvMax() + bonusPv);
                joueur.setPv(joueur.getPv() + bonusPv);
                return String.format("Vous équipez %s ! PV Max +%d", nom, bonusPv);

            case ARME:
                int bonusAttaque = valeur;
                joueur.setAttaque(joueur.getAttaque() + bonusAttaque);
                return String.format("Vous équipez %s ! Attaque +%d", nom, bonusAttaque);

            default:
                return "Vous utilisez " + nom;
        }
    }

    /**
     * Énumération des types d'items
     */
    public enum TypeItem {
        POTION_SOIN("Potion de soin"),
        POTION_FORCE("Potion de force"),
        ARMURE("Armure"),
        ARME("Arme");

        private final String description;

        TypeItem(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public String toString() {
        return nom + " (" + type.getDescription() + ", valeur: " + valeur + ")";
    }
}
