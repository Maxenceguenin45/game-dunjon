package item.consommable;

import item.AbstractItem;
import item.Item;
import personnage.Personnage;
import personnage.Joueur;

/**
 * Potion qui augmente l'attaque du personnage
 */
public class PotionForce extends AbstractItem {
    private final int bonusAttaque;
    private final boolean permanent;

    public PotionForce(String nom, int bonusAttaque, boolean permanent, Rarete rarete) {
        super(nom, 
              "Augmente l'attaque de " + bonusAttaque + (permanent ? " (permanent)" : " (temporaire)"), 
              true, 
              rarete);
        this.bonusAttaque = bonusAttaque;
        this.permanent = permanent;
    }

    @Override
    public String utiliser(Personnage cible) {
        cible.setAttaque(cible.getAttaque() + bonusAttaque);
        String effet = permanent ? "de façon permanente" : "temporairement";
        return String.format("Vous utilisez %s ! Votre attaque augmente de %d %s !", 
                           nom, bonusAttaque, effet);
    }

    /**
     * Crée une petite potion de force
     */
    public static PotionForce petite() {
        return new PotionForce("Petite Potion de Force", 5, true, Rarete.COMMUN);
    }

    /**
     * Crée une potion de force moyenne
     */
    public static PotionForce moyenne() {
        return new PotionForce("Potion de Force", 10, true, Rarete.RARE);
    }

    /**
     * Crée une grande potion de force
     */
    public static PotionForce grande() {
        return new PotionForce("Grande Potion de Force", 20, true, Rarete.EPIQUE);
    }

    /**
     * Crée une potion de rage (bonus temporaire important)
     */
    public static PotionForce rage() {
        return new PotionForce("Potion de Rage", 50, false, Rarete.LEGENDAIRE);
    }
}

