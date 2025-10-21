package item;

import personnage.Personnage;

/**
 * Interface représentant un item utilisable dans le jeu
 */
public interface Item {
    /**
     * Obtient le nom de l'item
     * @return le nom de l'item
     */
    String getNom();
    
    /**
     * Obtient la description de l'item
     * @return la description de l'item
     */
    String getDescription();
    
    /**
     * Utilise l'item sur un personnage
     * @param cible le personnage qui utilise l'item
     * @return un message décrivant l'effet de l'item
     */
    String utiliser(Personnage cible);
    
    /**
     * Vérifie si l'item est consommable (disparaît après utilisation)
     * @return true si l'item est consommable
     */
    boolean estConsommable();
    
    /**
     * Obtient la rareté de l'item
     * @return la rareté de l'item
     */
    default Rarete getRarete() {
        return Rarete.COMMUN;
    }
    
    /**
     * Énumération des raretés d'items
     */
    enum Rarete {
        COMMUN("Commun", "§7"),
        RARE("Rare", "§9"),
        EPIQUE("Épique", "§5"),
        LEGENDAIRE("Légendaire", "§6");
        
        private final String nom;
        private final String couleur;
        
        Rarete(String nom, String couleur) {
            this.nom = nom;
            this.couleur = couleur;
        }
        
        public String getNom() {
            return nom;
        }
        
        public String getCouleur() {
            return couleur;
        }
    }
}

