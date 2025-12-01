package com.dungeon.model.item;

import com.dungeon.model.item.arme.Arc;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.item.arme.Epee;
import com.dungeon.model.item.armure.Armure;
import com.dungeon.model.item.consomable.PotionForce;
import com.dungeon.model.item.consomable.PotionSoin;
import com.dungeon.model.item.special.ItemSpecial;

/**
 * Classe utilitaire pour la sérialisation et désérialisation des items
 */
public class ItemSerializer {
    
    private ItemSerializer() {
        // Classe utilitaire, pas d'instanciation
    }
    
    /**
     * Désérialise un item à partir d'une chaîne
     * @param data la chaîne de données
     * @return l'item désérialisé
     */
    public static Item deserialiser(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Données d'item vides");
        }
        
        // On utilise le préfixe du type pour savoir quelle classe instancier
        if (data.startsWith("POTION_SOIN:")) {
            return PotionSoin.deserialiser(data.substring(12));
        } else if (data.startsWith("POTION_FORCE:")) {
            return PotionForce.deserialiser(data.substring(13));
        } else if (data.startsWith("ARMURE:")) {
            return Armure.deserialiser(data.substring(7));
        } else if (data.startsWith("ITEM_SPECIAL:")) {
            return ItemSpecial.deserialiser(data.substring(13));
        } else if (data.startsWith("EPEE:")) {
            return Epee.deserialiser(data.substring(5));
        } else if (data.startsWith("ARC:")) {
            return Arc.deserialiser(data.substring(4));
        } else {
            // Comportement par défaut ou erreur
            throw new IllegalArgumentException("Type d'item inconnu pour la désérialisation: " + data);
        }
    }
}

