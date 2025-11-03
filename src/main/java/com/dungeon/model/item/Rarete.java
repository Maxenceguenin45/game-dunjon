package com.dungeon.model.item;

/**
 * Énumération des niveaux de rareté des items
 */
public enum Rarete {
    COMMUN("Commun", 1.0f),
    PEU_COMMUN("Peu commun", 1.5f),
    RARE("Rare", 2.0f),
    EPIQUE("Épique", 3.0f),
    LEGENDAIRE("Légendaire", 5.0f);

    private final String nom;
    private final float multiplicateur;

    Rarete(String nom, float multiplicateur) {
        this.nom = nom;
        this.multiplicateur = multiplicateur;
    }

    public String getNom() {
        return nom;
    }

    public float getMultiplicateur() {
        return multiplicateur;
    }

    @Override
    public String toString() {
        return nom;
    }
}
