package model.salle;

import personnage.Joueur;

public class SalleAmelioration implements Salle {
    @Override
    public void entrer(Joueur joueur) {
        joueur.setAttaque(joueur.getAttaque() + 5);
    }

    @Override
    public String getDescription() {
        return "Vous trouvez une salle d'entraînement. Votre attaque augmente de 5 !";
    }
}


