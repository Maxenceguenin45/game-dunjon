package item.arme;

import item.AbstractItem;
import item.Item;
import personnage.Personnage;

/**
 * Classe de base pour les armes
 */
public abstract class Arme extends AbstractItem {
    protected final int degats;
    protected final TypeAttaque typeAttaque;

    public Arme(String nom, String description, int degats, TypeAttaque typeAttaque, Rarete rarete) {
        super(nom, description, false, rarete);
        this.degats = degats;
        this.typeAttaque = typeAttaque;
    }

    public int getDegats() {
        return degats;
    }

    public TypeAttaque getTypeAttaque() {
        return typeAttaque;
    }

    /**
     * Énumération des types d'attaque
     */
    public enum TypeAttaque {
        PHYSIQUE("Attaque physique", 1.0),
        MAGIQUE("Attaque magique", 1.2),
        FEU("Attaque de feu", 1.3),
        GLACE("Attaque de glace", 1.3),
        FOUDRE("Attaque de foudre", 1.4),
        POISON("Attaque empoisonnée", 1.5),
        SACREE("Attaque sacrée", 1.6),
        TENEBRES("Attaque des ténèbres", 1.7);

        private final String description;
        private final double multiplicateur;

        TypeAttaque(String description, double multiplicateur) {
            this.description = description;
            this.multiplicateur = multiplicateur;
        }

        public String getDescription() {
            return description;
        }

        public double getMultiplicateur() {
            return multiplicateur;
        }
    }

    /**
     * Calcule les dégâts de l'arme avec le multiplicateur d'attaque
     */
    public int calculerDegats() {
        return (int) (degats * typeAttaque.getMultiplicateur());
    }

    @Override
    public String utiliser(Personnage cible) {
        int bonus = calculerDegats() - degats;
        cible.setAttaque(cible.getAttaque() + degats);
        return String.format("Vous équipez %s ! Attaque +%d (Type: %s, Bonus: +%d)", 
                           nom, degats, typeAttaque.getDescription(), bonus);
    }
}

