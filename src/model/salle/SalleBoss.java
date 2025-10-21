package model.salle;

import personnage.Joueur;

public class SalleBoss implements Salle, CombattantSalle {
    private int pv;
    private final int attaque;

    public SalleBoss(int pv, int attaque) {
        this.pv = pv;
        this.attaque = attaque;
    }

    @Override
    public void entrer(Joueur joueur) {
        // Combat avec le boss : le joueur attaque en premier avec son attaque totale (incluant l'arme)
        pv -= joueur.getAttaqueTotale();
        if (pv > 0) {
            // Le boss fait plus de dégâts qu'un ennemi normal
            joueur.setPv(joueur.getPv() - attaque);
        }
    }

    @Override
    public String getDescription() {
        return "";
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

