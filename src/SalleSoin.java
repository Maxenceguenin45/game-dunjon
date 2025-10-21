public class SalleSoin implements Salle {
    @Override
    public void entrer(Joueur joueur) {
        int soin = (int) (joueur.getPvMax() * 0.15);
        joueur.setPv(Math.min(joueur.getPv() + soin, joueur.getPvMax()));
    }

    @Override
    public String getDescription() {
        return "Vous trouvez une fontaine de soin. Vous récupérez 15% de vos PV max.";
    }
}

