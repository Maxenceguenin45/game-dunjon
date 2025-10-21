package model;

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
     * Utilise l'item sur le joueur.
     *
     * @param joueur Le joueur qui utilise l'item
     * @return Message décrivant l'effet de l'item
     */
    public String utiliser(Joueur joueur) {
        switch (type) {
            case POTION_SOIN:
                int pvActuels = joueur.getPv();
                int nouveauxPv = Math.min(pvActuels + valeur, joueur.getPvMax());
                joueur.setPv(nouveauxPv);
                int pvGagnes = nouveauxPv - pvActuels;
                return "Vous utilisez " + nom + " et récupérez " + pvGagnes + " PV !";

            case POTION_FORCE:
                joueur.setAttaque(joueur.getAttaque() + valeur);
                return "Vous utilisez " + nom + " ! Votre attaque augmente de " + valeur + " !";

            case ARMURE:
                joueur.setPvMax(joueur.getPvMax() + valeur);
                joueur.setPv(joueur.getPv() + valeur);
                return "Vous équipez " + nom + " ! Vos PV max augmentent de " + valeur + " !";

            default:
                return "Vous utilisez " + nom + ".";
        }
    }

    @Override
    public String toString() {
        return nom + " (" + type.getDescription() + ")";
    }

    /**
     * Énumération des types d'items disponibles.
     */
    public enum TypeItem {
        POTION_SOIN("Restaure des PV"),
        POTION_FORCE("Augmente l'attaque"),
        ARMURE("Augmente les PV max");

        private final String description;

        TypeItem(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}

