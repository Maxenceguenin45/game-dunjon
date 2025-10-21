public class SalleBoss implements Salle {
    private int pv;
    private int attaque;

    public SalleBoss(int pv, int attaque) {
        this.pv = pv;
        this.attaque = attaque;
    }

    @Override
    public void entrer(Joueur joueur) {
        // Combat simple : le joueur attaque en premier
        pv -= joueur.getAttaque();
        if (pv > 0) {
            joueur.setPv(joueur.getPv() - attaque);
        }
    }

    @Override
    public String getDescription() {
        return "Le boss surgit ! PV : " + pv + ", ATK : " + attaque;
    }

    public int getPv() {
        return pv;
    }
}
