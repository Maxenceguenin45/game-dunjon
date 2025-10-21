package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant l'inventaire du joueur.
 */
public class Inventaire {
    private final List<Item> items;
    private static final int CAPACITE_MAX = 10;

    public Inventaire() {
        this.items = new ArrayList<>();
    }

    /**
     * Ajoute un item à l'inventaire.
     *
     * @param item L'item à ajouter
     * @return true si l'item a été ajouté, false si l'inventaire est plein
     */
    public boolean ajouterItem(Item item) {
        if (items.size() >= CAPACITE_MAX) {
            return false;
        }
        items.add(item);
        return true;
    }

    /**
     * Retire un item de l'inventaire.
     *
     * @param index L'index de l'item à retirer
     * @return L'item retiré, ou null si l'index est invalide
     */
    public Item retirerItem(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.remove(index);
    }

    /**
     * Récupère un item sans le retirer.
     *
     * @param index L'index de l'item
     * @return L'item, ou null si l'index est invalide
     */
    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    /**
     * Retourne tous les items de l'inventaire.
     *
     * @return Liste des items
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Retourne le nombre d'items dans l'inventaire.
     *
     * @return Nombre d'items
     */
    public int getNombreItems() {
        return items.size();
    }

    /**
     * Vérifie si l'inventaire est vide.
     *
     * @return true si vide, false sinon
     */
    public boolean estVide() {
        return items.isEmpty();
    }

    /**
     * Vérifie si l'inventaire est plein.
     *
     * @return true si plein, false sinon
     */
    public boolean estPlein() {
        return items.size() >= CAPACITE_MAX;
    }

    /**
     * Retourne la capacité maximale de l'inventaire.
     *
     * @return Capacité maximale
     */
    public int getCapaciteMax() {
        return CAPACITE_MAX;
    }
}

