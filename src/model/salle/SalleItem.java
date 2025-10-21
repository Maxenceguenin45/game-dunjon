package model.salle;

import model.Item;
import model.Joueur;

/**
 * Salle contenant un item à récupérer.
 */
public class SalleItem implements Salle {
    private final Item item;
    private boolean itemRecupere;

    public SalleItem(Item item) {
        this.item = item;
        this.itemRecupere = false;
    }

    @Override
    public void entrer(Joueur joueur) {
        if (!itemRecupere) {
            if (joueur.getInventaire().ajouterItem(item)) {
                itemRecupere = true;
            }
        }
    }

    public Item getItem() {
        return item;
    }

    public boolean isItemRecupere() {
        return itemRecupere;
    }

    public String getDescription() {
        return "Vous trouvez : " + item.toString();
    }
}

