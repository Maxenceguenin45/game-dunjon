package model.salle;

import personnage.Joueur;

public class SalleEnnemi implements Salle, CombattantSalle {
    private int pv;
    private final int attaque;

    public SalleEnnemi(int pv, int attaque) {
        this.pv = pv;
        this.attaque = attaque;
    }

    @Override
    public void entrer(Joueur joueur) {
        // Combat simple : le joueur attaque en premier avec son attaque totale (incluant l'arme)
        pv -= joueur.getAttaqueTotale();
        if (pv > 0) {
            joueur.setPv(joueur.getPv() - attaque);
        }
    }

    public String getDescription() {
        return "Un ennemi apparaît ! PV : " + pv + ", ATK : " + attaque;
    }

    @Override
    public int getPv() {
        return pv;
    }

    @Override
    public int getAttaque() {
        return attaque;
    }
}