package model.salle;

import personnage.Joueur;
import item.arme.Arme;
import java.util.ArrayList;
import java.util.List;

public class SalleAmelioration implements Salle {

    @Override
    public void entrer(Joueur joueur) {
        // Cette méthode sera appelée avec l'arme choisie
        joueur.setAttaque(joueur.getAttaque() + 5);
    }

    @Override
    public String getDescription() {
        return "Salle d'entraînement - Améliorez vos compétences";
    }

    /**
     * Entraîne le joueur avec une arme spécifique
     *
     * @param joueur le joueur
     * @param arme   l'arme avec laquelle s'entraîner
     * @return message de progression
     */
    public String entrainerAvecArme(Joueur joueur, Arme arme) {
        // Bonus d'attaque de base pour le joueur
        int bonusAttaque = 2 + (joueur.getNiveau() / 3); // +2, +2, +3, +3, +4...
        joueur.setAttaque(joueur.getAttaque() + bonusAttaque);

        // Expérience de maîtrise pour l'arme
        int xpMaitrise = 15 + (joueur.getNiveau() * 2);
        String messageArme = arme.gagnerExperienceMaitrise(xpMaitrise);

        // Bonus d'attaque supplémentaire grâce au niveau de maîtrise
        int bonusMaitrise = (int) (arme.calculerDegatsTotal() - arme.calculerDegats());

        StringBuilder message = new StringBuilder();
        message.append(String.format("⚔️ Entraînement avec %s !\n", arme.getNom()));
        message.append(String.format("• Attaque de base +%d\n", bonusAttaque));

        if (bonusMaitrise > 0) {
            message.append(String.format("• Bonus de maîtrise: +%d dégâts\n", bonusMaitrise));
        }

        message.append("• ").append(messageArme);

        return message.toString();
    }

    /**
     * Récupère l'arme équipée si disponible
     */
    public List<Arme> getArmesDisponibles(Joueur joueur) {
        List<Arme> armes = new ArrayList<>();

        // Pour le moment, seule l'arme équipée peut être entraînée
        // Dans le futur, on pourrait ajouter un système pour gérer toutes les armes
        if (joueur.getArmeEquipee() != null) {
            armes.add(joueur.getArmeEquipee());
        }

        return armes;
    }
}
