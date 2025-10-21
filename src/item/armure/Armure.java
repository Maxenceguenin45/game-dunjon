package item.armure;

import item.AbstractItem;
import item.Item;
import personnage.Personnage;
import personnage.Joueur;

/**
 * Armure qui augmente les points de vie maximum
 */
public class Armure extends AbstractItem {
    private final int bonusPvMax;
    private final int bonusDefense;

    public Armure(String nom, int bonusPvMax, int bonusDefense, Rarete rarete) {
        super(nom, 
              String.format("PV max +%d, Défense +%d", bonusPvMax, bonusDefense), 
              false, 
              rarete);
        this.bonusPvMax = bonusPvMax;
        this.bonusDefense = bonusDefense;
    }

    public int getBonusPvMax() {
        return bonusPvMax;
    }

    public int getBonusDefense() {
        return bonusDefense;
    }

    @Override
    public String utiliser(Personnage cible) {
        if (cible instanceof Joueur) {
            Joueur joueur = (Joueur) cible;
            joueur.setPvMax(joueur.getPvMax() + bonusPvMax);
            joueur.setPv(joueur.getPv() + bonusPvMax);
        }
        return String.format("Vous équipez %s ! PV max +%d, Défense +%d !", 
                           nom, bonusPvMax, bonusDefense);
    }

    /**
     * Armure légère de débutant
     */
    public static Armure cuir() {
        return new Armure("Armure en Cuir", 20, 5, Rarete.COMMUN);
    }

    /**
     * Armure en fer
     */
    public static Armure fer() {
        return new Armure("Armure en Fer", 40, 10, Rarete.RARE);
    }

    /**
     * Armure en acier
     */
    public static Armure acier() {
        return new Armure("Armure en Acier", 60, 15, Rarete.RARE);
    }

    /**
     * Armure enchantée
     */
    public static Armure enchantee() {
        return new Armure("Armure Enchantée", 80, 25, Rarete.EPIQUE);
    }

    /**
     * Armure légendaire
     */
    public static Armure legendaire() {
        return new Armure("Armure du Dragon", 150, 50, Rarete.LEGENDAIRE);
    }
}

