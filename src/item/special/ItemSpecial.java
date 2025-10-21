package item.special;

import item.AbstractItem;
import item.Item;
import personnage.Personnage;
import personnage.Joueur;

/**
 * Item spécial qui combine plusieurs effets
 */
public class ItemSpecial extends AbstractItem {
    private final int bonusPv;
    private final int bonusAttaque;
    private final int bonusPvMax;
    private final String effetSpecial;

    public ItemSpecial(String nom, String description, int bonusPv, int bonusAttaque, 
                      int bonusPvMax, String effetSpecial, Rarete rarete) {
        super(nom, description, true, rarete);
        this.bonusPv = bonusPv;
        this.bonusAttaque = bonusAttaque;
        this.bonusPvMax = bonusPvMax;
        this.effetSpecial = effetSpecial;
    }

    @Override
    public String utiliser(Personnage cible) {
        StringBuilder message = new StringBuilder("Vous utilisez " + nom + " !\n");

        if (bonusPvMax > 0 && cible instanceof Joueur) {
            Joueur joueur = (Joueur) cible;
            joueur.setPvMax(joueur.getPvMax() + bonusPvMax);
            message.append("• PV max +").append(bonusPvMax).append("\n");
        }

        if (bonusPv > 0) {
            cible.soigner(bonusPv);
            message.append("• PV +").append(bonusPv).append("\n");
        }

        if (bonusAttaque > 0) {
            cible.setAttaque(cible.getAttaque() + bonusAttaque);
            message.append("• Attaque +").append(bonusAttaque).append("\n");
        }

        if (effetSpecial != null && !effetSpecial.isEmpty()) {
            message.append("• ").append(effetSpecial);
        }

        return message.toString();
    }

    /**
     * Élixir divin - Restaure tout et améliore les stats
     */
    public static ItemSpecial elixirDivin() {
        return new ItemSpecial(
            "Élixir Divin",
            "Restaure complètement et améliore toutes les statistiques",
            9999, 30, 50,
            "Vous vous sentez invincible !",
            Rarete.LEGENDAIRE
        );
    }

    /**
     * Pierre de puissance
     */
    public static ItemSpecial pierrePuissance() {
        return new ItemSpecial(
            "Pierre de Puissance",
            "Augmente drastiquement l'attaque et les PV",
            0, 50, 100,
            "Une aura de pouvoir vous entoure !",
            Rarete.LEGENDAIRE
        );
    }

    /**
     * Fruit sacré
     */
    public static ItemSpecial fruitSacre() {
        return new ItemSpecial(
            "Fruit Sacré",
            "Restaure les PV et augmente les stats",
            100, 15, 30,
            "Vous ressentez une énergie divine !",
            Rarete.EPIQUE
        );
    }

    /**
     * Cristal de régénération
     */
    public static ItemSpecial cristalRegeneration() {
        return new ItemSpecial(
            "Cristal de Régénération",
            "Soigne et améliore la vitalité",
            150, 0, 50,
            "Vos blessures se referment instantanément !",
            Rarete.EPIQUE
        );
    }
}

