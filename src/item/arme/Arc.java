package item.arme;

import item.Item;

/**
 * Arc - Arme à distance avec des attaques élémentaires
 */
public class Arc extends Arme {

    public Arc(String nom, int degats, TypeAttaque typeAttaque, Rarete rarete) {
        super(nom, "Un arc " + typeAttaque.getDescription().toLowerCase(), degats, typeAttaque, rarete);
    }

    /**
     * Arc de débutant
     */
    public static Arc simple() {
        return new Arc("Arc Simple", 15, TypeAttaque.PHYSIQUE, Rarete.COMMUN);
    }

    /**
     * Arc long
     */
    public static Arc arcLong() {
        return new Arc("Arc Long", 30, TypeAttaque.PHYSIQUE, Rarete.RARE);
    }

    /**
     * Arc magique
     */
    public static Arc magique() {
        return new Arc("Arc Enchanté", 40, TypeAttaque.MAGIQUE, Rarete.EPIQUE);
    }

    /**
     * Arc de feu
     */
    public static Arc feu() {
        return new Arc("Arc du Phénix", 50, TypeAttaque.FEU, Rarete.EPIQUE);
    }

    /**
     * Arc de glace
     */
    public static Arc glace() {
        return new Arc("Arc du Givre", 50, TypeAttaque.GLACE, Rarete.EPIQUE);
    }

    /**
     * Arc de foudre
     */
    public static Arc foudre() {
        return new Arc("Arc de la Tempête", 55, TypeAttaque.FOUDRE, Rarete.LEGENDAIRE);
    }
}

