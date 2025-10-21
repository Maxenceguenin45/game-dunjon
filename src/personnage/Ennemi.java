package personnage;

/**
 * Représente un ennemi dans le jeu
 */
public class Ennemi implements Personnage {
    private String nom;
    private int pv;
    private int pvMax;
    private int attaque;
    private boolean estBoss;

    /**
     * Crée un ennemi standard
     * @param nom le nom de l'ennemi
     * @param pv les points de vie
     * @param attaque la valeur d'attaque
     */
    public Ennemi(String nom, int pv, int attaque) {
        this(nom, pv, attaque, false);
    }

    /**
     * Crée un ennemi (standard ou boss)
     * @param nom le nom de l'ennemi
     * @param pv les points de vie
     * @param attaque la valeur d'attaque
     * @param estBoss true si c'est un boss
     */
    public Ennemi(String nom, int pv, int attaque, boolean estBoss) {
        this.nom = nom;
        this.pv = pv;
        this.pvMax = pv;
        this.attaque = attaque;
        this.estBoss = estBoss;
    }

    /**
     * Crée un ennemi standard avec des stats par défaut
     * @param niveau le niveau de l'ennemi (influence les stats)
     * @return un nouvel ennemi
     */
    public static Ennemi creerEnnemiStandard(int niveau) {
        int pv = 30 + (niveau * 10);
        int attaque = 8 + (niveau * 2);
        return new Ennemi("Gobelin", pv, attaque, false);
    }

    /**
     * Crée un boss avec des stats élevées
     * @param niveau le niveau du boss (influence les stats)
     * @return un nouvel ennemi boss
     */
    public static Ennemi creerBoss(int niveau) {
        int pv = 80 + (niveau * 20);
        int attaque = 15 + (niveau * 3);
        return new Ennemi("Dragon", pv, attaque, true);
    }

    // Getters
    public String getNom() {
        return nom;
    }

    @Override
    public int getPv() {
        return pv;
    }

    @Override
    public int getPvMax() {
        return pvMax;
    }

    @Override
    public int getAttaque() {
        return attaque;
    }

    public boolean estBoss() {
        return estBoss;
    }

    // Setters
    @Override
    public void setPv(int pv) {
        this.pv = Math.max(0, Math.min(pv, pvMax));
    }

    @Override
    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        String type = estBoss ? "Boss" : "Ennemi";
        return String.format("%s: %s (PV: %d/%d, ATK: %d)", type, nom, pv, pvMax, attaque);
    }
}

