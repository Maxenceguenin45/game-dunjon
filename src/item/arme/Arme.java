package item.arme;

import item.AbstractItem;
import item.Item;
import personnage.Personnage;

/**
 * Classe de base pour les armes
 */
public abstract class Arme extends AbstractItem {
    protected final int degats;
    protected final TypeAttaque typeAttaque;

    // Système de maîtrise de l'arme
    protected int niveauMaitrise;
    protected int experienceMaitrise;
    protected int experienceRequise;
    protected double bonusMaitrise; // Pourcentage de bonus d'attaque

    public Arme(String nom, String description, int degats, TypeAttaque typeAttaque, Rarete rarete) {
        super(nom, description, false, rarete);
        this.degats = degats;
        this.typeAttaque = typeAttaque;
        this.niveauMaitrise = 1;
        this.experienceMaitrise = 0;
        this.experienceRequise = 50;
        this.bonusMaitrise = 0.0; // Commence à 0%
    }

    public int getDegats() {
        return degats;
    }

    public TypeAttaque getTypeAttaque() {
        return typeAttaque;
    }

    public int getNiveauMaitrise() {
        return niveauMaitrise;
    }

    public int getExperienceMaitrise() {
        return experienceMaitrise;
    }

    public int getExperienceRequise() {
        return experienceRequise;
    }

    public double getBonusMaitrise() {
        return bonusMaitrise;
    }

    /**
     * Gagne de l'expérience de maîtrise en s'entraînant
     * @return message décrivant la progression
     */
    public String gagnerExperienceMaitrise(int xp) {
        this.experienceMaitrise += xp;

        if (this.experienceMaitrise >= this.experienceRequise) {
            return monterNiveauMaitrise();
        }

        return String.format("Votre maîtrise de %s progresse ! (%d/%d XP)",
            nom, experienceMaitrise, experienceRequise);
    }

    /**
     * Monte d'un niveau de maîtrise
     */
    private String monterNiveauMaitrise() {
        this.niveauMaitrise++;
        this.experienceMaitrise -= this.experienceRequise;
        this.experienceRequise = (int) (50 * Math.pow(1.3, niveauMaitrise));

        // Augmenter le bonus : +5% par niveau
        double ancienBonus = this.bonusMaitrise;
        this.bonusMaitrise += 5.0;

        return String.format("✨ Maîtrise de %s niveau %d ! Bonus: %.0f%% → %.0f%% (+%.0f%%)",
            nom, niveauMaitrise, ancienBonus, bonusMaitrise, bonusMaitrise - ancienBonus);
    }

    /**
     * Calcule les dégâts totaux de l'arme avec le bonus de maîtrise
     */
    public int calculerDegatsTotal() {
        int degatsBase = (int) (degats * typeAttaque.getMultiplicateur());
        int bonusDegats = (int) (degatsBase * (bonusMaitrise / 100.0));
        return degatsBase + bonusDegats;
    }

    /**
     * Calcule les dégâts de l'arme avec le multiplicateur d'attaque
     */
    public int calculerDegats() {
        return (int) (degats * typeAttaque.getMultiplicateur());
    }

    @Override
    public String utiliser(Personnage cible) {
        int bonus = calculerDegats() - degats;
        cible.setAttaque(cible.getAttaque() + degats);
        return String.format("Vous équipez %s ! Attaque +%d (Type: %s, Bonus: +%d)",
                           nom, degats, typeAttaque.getDescription(), bonus);
    }

    @Override
    public String toString() {
        if (niveauMaitrise > 1) {
            return String.format("%s [Niv.%d] (ATK: %d, Bonus: +%.0f%%, Type: %s)",
                nom, niveauMaitrise, degats, bonusMaitrise, typeAttaque.name());
        }
        return String.format("%s (ATK: %d, Type: %s)",
            nom, degats, typeAttaque.name());
    }

    /**
     * Énumération des types d'attaque
     */
    public enum TypeAttaque {
        PHYSIQUE("Attaque physique", 1.0),
        MAGIQUE("Attaque magique", 1.2),
        FEU("Attaque de feu", 1.3),
        GLACE("Attaque de glace", 1.3),
        FOUDRE("Attaque de foudre", 1.4),
        POISON("Attaque empoisonnée", 1.5),
        SACREE("Attaque sacrée", 1.6),
        TENEBRES("Attaque des ténèbres", 1.7);

        private final String description;
        private final double multiplicateur;

        TypeAttaque(String description, double multiplicateur) {
            this.description = description;
            this.multiplicateur = multiplicateur;
        }

        public String getDescription() {
            return description;
        }

        public double getMultiplicateur() {
            return multiplicateur;
        }
    }
}
