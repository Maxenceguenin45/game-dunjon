package service;

import model.salle.*;
import model.Item;
import java.util.Random;

public class SalleService {
    private static final Random random = new Random();
    private static final int PV_BASE_ENNEMI = 30;
    private static final int ATK_BASE_ENNEMI = 5;
    private static final int PV_BASE_BOSS = 80;
    private static final int ATK_BASE_BOSS = 15;
    private static final int NB_TYPES_SALLE = 5;
    private final ItemService itemService;

    public SalleService() {
        this.itemService = new ItemService();
    }

    public int genererNombreChemins() {
        return 1 + random.nextInt(3);
    }

    public Salle[] genererChemins(int nbChemins, int niveau, String[] descriptions) {
        if (0 > nbChemins || null == descriptions) {
            return new Salle[0];
        }

        Salle[] chemins = new Salle[nbChemins];
        for (int i = 0; i < nbChemins; i++) {
            chemins[i] = genererSalle(niveau);
            descriptions[i] = genererDescription(chemins[i], niveau);
        }
        return chemins;
    }

    private Salle genererSalle(int niveau) {
        int type = random.nextInt(NB_TYPES_SALLE);
        switch (type) {
            case 0:
            case 4: // Augmenter la fréquence des ennemis
                int pvEnnemi = PV_BASE_ENNEMI + niveau * 10 + random.nextInt(11);
                int atkEnnemi = ATK_BASE_ENNEMI + niveau * 2 + random.nextInt(3);
                return new SalleEnnemi(pvEnnemi, atkEnnemi);
            case 1:
                int pvBoss = PV_BASE_BOSS + niveau * 20 + random.nextInt(21);
                int atkBoss = ATK_BASE_BOSS + niveau * 4 + random.nextInt(6);
                return new SalleBoss(pvBoss, atkBoss);
            case 2:
                return new SalleSoin();
            case 3:
            default:
                return new SalleAmelioration();
        }
    }

    private String genererDescription(Salle salle, int niveau) {
        if (salle instanceof SalleEnnemi) {
            SalleEnnemi salleEnnemi = (SalleEnnemi) salle;
            return String.format("Ennemi (PV: %d, ATK: %d)", 
                salleEnnemi.getPv(), salleEnnemi.getAttaque());
        } else if (salle instanceof SalleBoss) {
            SalleBoss salleBoss = (SalleBoss) salle;
            return String.format("Boss (PV: %d, ATK: %d)",
                salleBoss.getPv(), salleBoss.getAttaque());
        } else if (salle instanceof SalleSoin) {
            return "Fontaine de soin (+15% PV max)";
        } else {
            return "Salle d'entraînement (+5 ATK)";
        }
    }

    public String traiterCombat(Salle salle, int pvAvant) {
        if (0 >= pvAvant) {
            return null;
        }

        if (salle instanceof SalleEnnemi) {
            SalleEnnemi salleEnnemi = (SalleEnnemi) salle;
            if (0 >= salleEnnemi.getPv() && 0 < pvAvant) {
                return "Vous avez vaincu l'ennemi !";
            } else {
                return String.format("L'ennemi vous a attaqué. Il lui reste %d PV.", 
                    salleEnnemi.getPv());
            }
        } else if (salle instanceof SalleBoss) {
            SalleBoss salleBoss = (SalleBoss) salle;
            if (0 >= salleBoss.getPv() && 0 < pvAvant) {
                return "Vous avez vaincu le boss !";
            } else {
                return String.format("Le boss vous a attaqué. Il lui reste %d PV.", 
                    salleBoss.getPv());
            }
        }
        return null;
    }

    public String traiterSalleItem(Salle salle) {
        return null;
    }
}
