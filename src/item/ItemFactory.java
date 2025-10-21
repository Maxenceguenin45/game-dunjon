package item;

import item.arme.*;
import item.armure.Armure;
import item.consommable.*;
import item.special.ItemSpecial;

import java.util.Random;

/**
 * Fabrique d'items pour générer des items aléatoires ou spécifiques
 */
public class ItemFactory {
    private final Random random;

    public ItemFactory() {
        this.random = new Random();
    }

    public ItemFactory(Random random) {
        this.random = random;
    }

    /**
     * Génère un item aléatoire selon la rareté
     * @param rarete la rareté minimale de l'item
     * @return un item aléatoire
     */
    public Item genererItemAleatoire(Item.Rarete rarete) {
        int type = random.nextInt(5); // 5 types d'items
        
        switch (type) {
            case 0: // Potion de soin
                return genererPotionSoin(rarete);
            case 1: // Potion de force
                return genererPotionForce(rarete);
            case 2: // Arme
                return genererArme(rarete);
            case 3: // Armure
                return genererArmure(rarete);
            case 4: // Item spécial
                return genererItemSpecial(rarete);
            default:
                return PotionSoin.petite();
        }
    }

    /**
     * Génère un item aléatoire de rareté commune
     */
    public Item genererItemAleatoire() {
        return genererItemAleatoire(Item.Rarete.COMMUN);
    }

    /**
     * Génère une potion de soin selon la rareté
     */
    public Item genererPotionSoin(Item.Rarete rarete) {
        return switch (rarete) {
            case COMMUN -> PotionSoin.petite();
            case RARE -> PotionSoin.moyenne();
            case EPIQUE -> PotionSoin.grande();
            case LEGENDAIRE -> PotionSoin.totale();
            default -> PotionSoin.petite();
        };
    }

    /**
     * Génère une potion de force selon la rareté
     */
    public Item genererPotionForce(Item.Rarete rarete) {
        switch (rarete) {
            case COMMUN:
                return PotionForce.petite();
            case RARE:
                return PotionForce.moyenne();
            case EPIQUE:
                return PotionForce.grande();
            case LEGENDAIRE:
                return PotionForce.rage();
            default:
                return PotionForce.petite();
        }
    }

    /**
     * Génère une arme aléatoire selon la rareté
     */
    public Item genererArme(Item.Rarete rarete) {
        int typeArme = random.nextInt(3); // Épée, Arc, Bâton
        
        switch (typeArme) {
            case 0: // Épée
                return genererEpee(rarete);
            case 1: // Arc
                return genererArc(rarete);
            case 2: // Bâton
                return genererBaton(rarete);
            default:
                return Epee.debutant();
        }
    }

    /**
     * Génère une épée selon la rareté
     */
    public Item genererEpee(Item.Rarete rarete) {
        switch (rarete) {
            case COMMUN:
                return random.nextBoolean() ? Epee.debutant() : Epee.fer();
            case RARE:
                return Epee.acier();
            case EPIQUE:
                int type = random.nextInt(3);
                if (type == 0) return Epee.feu();
                if (type == 1) return Epee.glace();
                return Epee.foudre();
            case LEGENDAIRE:
                return random.nextBoolean() ? Epee.sacree() : Epee.tenebres();
            default:
                return Epee.debutant();
        }
    }

    /**
     * Génère un arc selon la rareté
     */
    public Item genererArc(Item.Rarete rarete) {
        switch (rarete) {
            case COMMUN:
                return Arc.simple();
            case RARE:
                return Arc.arcLong();
            case EPIQUE:
                int type = random.nextInt(4);
                if (type == 0) return Arc.magique();
                if (type == 1) return Arc.feu();
                if (type == 2) return Arc.glace();
                return Arc.foudre();
            case LEGENDAIRE:
                return Arc.foudre();
            default:
                return Arc.simple();
        }
    }

    /**
     * Génère un bâton selon la rareté
     */
    public Item genererBaton(Item.Rarete rarete) {
        switch (rarete) {
            case COMMUN:
                return Baton.apprenti();
            case RARE:
                return Baton.mage();
            case EPIQUE:
                int type = random.nextInt(3);
                if (type == 0) return Baton.feu();
                if (type == 1) return Baton.glace();
                return Baton.foudre();
            case LEGENDAIRE:
                return random.nextBoolean() ? Baton.foudre() : Baton.sacre();
            default:
                return Baton.apprenti();
        }
    }

    /**
     * Génère une armure selon la rareté
     */
    public Item genererArmure(Item.Rarete rarete) {
        switch (rarete) {
            case COMMUN:
                return Armure.cuir();
            case RARE:
                return random.nextBoolean() ? Armure.fer() : Armure.acier();
            case EPIQUE:
                return Armure.enchantee();
            case LEGENDAIRE:
                return Armure.legendaire();
            default:
                return Armure.cuir();
        }
    }

    /**
     * Génère un item spécial selon la rareté
     */
    public Item genererItemSpecial(Item.Rarete rarete) {
        if (rarete == Item.Rarete.LEGENDAIRE) {
            return random.nextBoolean() ? ItemSpecial.elixirDivin() : ItemSpecial.pierrePuissance();
        } else if (rarete == Item.Rarete.EPIQUE) {
            return random.nextBoolean() ? ItemSpecial.fruitSacre() : ItemSpecial.cristalRegeneration();
        } else {
            // Pour les raretés inférieures, retourner une potion
            return genererPotionSoin(rarete);
        }
    }

    /**
     * Détermine la rareté d'un item selon la difficulté de l'ennemi
     * @param difficulte la difficulté de l'ennemi (PV + ATK)
     * @param estBoss true si c'est un boss
     * @return la rareté de l'item
     */
    public Item.Rarete determinerRarete(int difficulte, boolean estBoss) {
        int chance = random.nextInt(100);
        
        if (estBoss) {
            // Boss : meilleures chances d'items rares
            if (chance < 10) return Item.Rarete.LEGENDAIRE;
            if (chance < 40) return Item.Rarete.EPIQUE;
            if (chance < 80) return Item.Rarete.RARE;
            return Item.Rarete.COMMUN;
        } else {
            // Ennemi normal : chances basées sur la difficulté
            if (difficulte > 150 && chance < 5) return Item.Rarete.LEGENDAIRE;
            if (difficulte > 100 && chance < 15) return Item.Rarete.EPIQUE;
            if (difficulte > 50 && chance < 35) return Item.Rarete.RARE;
            return Item.Rarete.COMMUN;
        }
    }
}

