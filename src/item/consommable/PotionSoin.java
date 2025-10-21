package item.consommable;

import item.AbstractItem;
import item.Item;
import personnage.Personnage;

/**
 * Potion de soin qui restaure les points de vie
 */
public class PotionSoin extends AbstractItem {
    private final int pvRestaures;

    public PotionSoin(String nom, int pvRestaures, Rarete rarete) {
        super(nom, "Restaure " + pvRestaures + " PV", true, rarete);
        this.pvRestaures = pvRestaures;
    }

    @Override
    public String utiliser(Personnage cible) {
        int pvAvant = cible.getPv();
        cible.soigner(pvRestaures);
        int pvGagnes = cible.getPv() - pvAvant;
        return String.format("Vous utilisez %s et récupérez %d PV !", nom, pvGagnes);
    }

    /**
     * Crée une petite potion de soin
     */
    public static PotionSoin petite() {
        return new PotionSoin("Petite Potion de Soin", 30, Rarete.COMMUN);
    }

    /**
     * Crée une potion de soin moyenne
     */
    public static PotionSoin moyenne() {
        return new PotionSoin("Potion de Soin", 50, Rarete.RARE);
    }

    /**
     * Crée une grande potion de soin
     */
    public static PotionSoin grande() {
        return new PotionSoin("Grande Potion de Soin", 80, Rarete.EPIQUE);
    }

    /**
     * Crée une potion de soin légendaire
     */
    public static PotionSoin totale() {
        return new PotionSoin("Potion de Soin Totale", 9999, Rarete.LEGENDAIRE);
    }
}

