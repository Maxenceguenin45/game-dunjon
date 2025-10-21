package item;

import personnage.Personnage;

/**
 * Classe de base pour les items du jeu
 */
public abstract class AbstractItem implements Item {
    protected final String nom;
    protected final String description;
    protected final boolean consommable;
    protected final Rarete rarete;

    protected AbstractItem(String nom, String description, boolean consommable, Rarete rarete) {
        this.nom = nom;
        this.description = description;
        this.consommable = consommable;
        this.rarete = rarete;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean estConsommable() {
        return consommable;
    }

    @Override
    public Rarete getRarete() {
        return rarete;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", rarete.getNom(), nom, description);
    }
}

