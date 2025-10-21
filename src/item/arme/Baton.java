package item.arme;

import item.Item;

/**
 * Bâton magique - Arme magique puissante
 */
public class Baton extends Arme {

    public Baton(String nom, int degats, TypeAttaque typeAttaque, Rarete rarete) {
        super(nom, "Un bâton magique " + typeAttaque.getDescription().toLowerCase(), degats, typeAttaque, rarete);
    }

    /**
     * Bâton de débutant
     */
    public static Baton apprenti() {
        return new Baton("Bâton d'Apprenti", 12, TypeAttaque.MAGIQUE, Rarete.COMMUN);
    }

    /**
     * Bâton de mage
     */
    public static Baton mage() {
        return new Baton("Bâton de Mage", 28, TypeAttaque.MAGIQUE, Rarete.RARE);
    }

    /**
     * Bâton de feu
     */
    public static Baton feu() {
        return new Baton("Bâton des Flammes", 55, TypeAttaque.FEU, Rarete.EPIQUE);
    }

    /**
     * Bâton de glace
     */
    public static Baton glace() {
        return new Baton("Bâton du Blizzard", 55, TypeAttaque.GLACE, Rarete.EPIQUE);
    }

    /**
     * Bâton de foudre
     */
    public static Baton foudre() {
        return new Baton("Bâton de l'Orage", 60, TypeAttaque.FOUDRE, Rarete.LEGENDAIRE);
    }

    /**
     * Bâton sacré
     */
    public static Baton sacre() {
        return new Baton("Sceptre Divin", 65, TypeAttaque.SACREE, Rarete.LEGENDAIRE);
    }
}

