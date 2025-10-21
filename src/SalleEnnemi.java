public class SalleEnnemi implements Salle {
    private int pv;
    private final int attaque;

    public SalleEnnemi(int pv, int attaque) {
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
        return "Un ennemi apparaît ! PV : " + pv + ", ATK : " + attaque;
    }

    public int getPv() {
        return pv;
    }
}