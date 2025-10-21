package item.arme;

import item.Item;

/**
 * Épée - Arme de mêlée polyvalente
 */
public class Epee extends Arme {

    public Epee(String nom, int degats, TypeAttaque typeAttaque, Rarete rarete) {
        super(nom, "Une épée " + typeAttaque.getDescription().toLowerCase(), degats, typeAttaque, rarete);
    }

    /**
     * Épée de débutant
     */
    public static Epee debutant() {
        return new Epee("Épée Rouillée", 10, TypeAttaque.PHYSIQUE, Rarete.COMMUN);
    }

    /**
     * Épée en fer
     */
    public static Epee fer() {
        return new Epee("Épée en Fer", 20, TypeAttaque.PHYSIQUE, Rarete.COMMUN);
    }

    /**
     * Épée en acier
     */
    public static Epee acier() {
        return new Epee("Épée en Acier", 35, TypeAttaque.PHYSIQUE, Rarete.RARE);
    }

    /**
     * Épée de feu
     */
    public static Epee feu() {
        return new Epee("Lame Enflammée", 45, TypeAttaque.FEU, Rarete.EPIQUE);
    }

    /**
     * Épée de glace
     */
    public static Epee glace() {
        return new Epee("Lame Glaciale", 45, TypeAttaque.GLACE, Rarete.EPIQUE);
    }

    /**
     * Épée de foudre
     */
    public static Epee foudre() {
        return new Epee("Lame Foudroyante", 50, TypeAttaque.FOUDRE, Rarete.EPIQUE);
    }

    /**
     * Épée sacrée
     */
    public static Epee sacree() {
        return new Epee("Excalibur", 70, TypeAttaque.SACREE, Rarete.LEGENDAIRE);
    }

    /**
     * Épée des ténèbres
     */
    public static Epee tenebres() {
        return new Epee("Ombre Éternelle", 80, TypeAttaque.TENEBRES, Rarete.LEGENDAIRE);
    }
}

