import java.io.File;
import java.util.Scanner;

// Ajout des imports nécessaires
// ...

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final String SAVE_FILE = "save.txt";
        Joueur joueur = null;
        File save = new File(SAVE_FILE);
        if (save.exists()) {
            System.out.print("Une sauvegarde a été trouvée. Voulez-vous la reprendre ? (o/n) : ");
            String rep = scanner.nextLine();
            if (rep.trim().equalsIgnoreCase("o")) {
                joueur = Joueur.loadFromFile(SAVE_FILE);
                if (joueur != null) {
                    System.out.println("Bienvenue de retour, " + joueur.getPseudo() + " !");
                    System.out.println("Stats : " + joueur.getPv() + "/" + joueur.getPvMax() + " PV, " + joueur.getAttaque() + " ATK");
                }
            }
        }
        if (joueur == null) {
            System.out.print("Entrez votre pseudo : ");
            String pseudo = scanner.nextLine();
            joueur = new Joueur(pseudo, 100, 20);
            System.out.println("Bienvenue dans le jeu d'aventure, " + joueur.getPseudo() + " !");
            System.out.println("Votre personnage a " + joueur.getPv() + " points de vie et " + joueur.getAttaque() + " points d'attaque.");
        }
        // Déroulement du jeu : traversée de 5 salles
        java.util.Random random = new java.util.Random();
        int niveau = 1;
        for (int i = 1; i <= 5; i++) {
            System.out.println("\nSalle " + i + ":");
            int type = random.nextInt(4); // 0: Ennemi, 1: Boss, 2: Soin, 3: Amélioration
            switch (type) {
                case 0: // Ennemi
                    int pvEnnemi = 30 + niveau * 10 + random.nextInt(11);
                    int atkEnnemi = 5 + niveau * 2 + random.nextInt(3);
                    System.out.println("Un ennemi apparaît ! PV : " + pvEnnemi + ", ATK : " + atkEnnemi);
                    pvEnnemi -= joueur.getAttaque();
                    if (pvEnnemi <= 0) {
                        System.out.println("Vous avez vaincu l'ennemi !");
                        joueur.setEnnemisTues(joueur.getEnnemisTues() + 1);
                    } else {
                        joueur.setPv(joueur.getPv() - atkEnnemi);
                        if (joueur.getPv() > 0) {
                            System.out.println("L'ennemi vous a attaqué. Il lui reste " + pvEnnemi + " PV.");
                        }
                    }
                    System.out.println("Après le combat : " + joueur.getPv() + "/" + joueur.getPvMax() + " PV");
                    break;
                case 1: // Boss
                    int pvBoss = 80 + niveau * 20 + random.nextInt(21);
                    int atkBoss = 15 + niveau * 4 + random.nextInt(6);
                    System.out.println("Le boss surgit ! PV : " + pvBoss + ", ATK : " + atkBoss);
                    pvBoss -= joueur.getAttaque();
                    if (pvBoss <= 0) {
                        System.out.println("Vous avez vaincu le boss !");
                        joueur.setBossVaincus(joueur.getBossVaincus() + 1);
                    } else {
                        joueur.setPv(joueur.getPv() - atkBoss);
                        if (joueur.getPv() > 0) {
                            System.out.println("Le boss vous a attaqué. Il lui reste " + pvBoss + " PV.");
                        }
                    }
                    System.out.println("Après le combat : " + joueur.getPv() + "/" + joueur.getPvMax() + " PV");
                    break;
                case 2: // Soin
                    int soin = (int) (joueur.getPvMax() * 0.15);
                    joueur.setPv(joueur.getPv() + soin);
                    System.out.println("Vous trouvez une fontaine de soin. Vous récupérez 15% de vos PV max.");
                    System.out.println("Après le soin : " + joueur.getPv() + "/" + joueur.getPvMax() + " PV");
                    break;
                case 3: // Amélioration
                default:
                    joueur.setAttaque(joueur.getAttaque() + 5);
                    System.out.println("Vous trouvez une salle d'entraînement. Votre attaque augmente de 5 !");
                    System.out.println("Nouvelle attaque : " + joueur.getAttaque());
                    break;
            }
            if (joueur.getPv() <= 0) {
                System.out.println("Vous êtes mort... Fin de la partie.");
                break;
            }
            niveau++;
        }
        // À la fin de la session, sauvegarde automatique
        joueur.saveToFile(SAVE_FILE);
        System.out.println("Partie sauvegardée. À bientôt, " + joueur.getPseudo() + " !");
        // Affichage des compteurs depuis l'objet joueur
        System.out.println("Ennemis vaincus : " + joueur.getEnnemisTues());
        System.out.println("Boss vaincus : " + joueur.getBossVaincus());
        int maxSalles = 10;
        int sallesParcourues = 0;
        while (joueur.getPv() > 0 && sallesParcourues < maxSalles) {
            sallesParcourues++;
            System.out.println("\nSalle " + sallesParcourues + ":");
            int nbChemins = 1 + random.nextInt(3); // 1, 2 ou 3 chemins
            Salle[] chemins = new Salle[nbChemins];
            String[] descriptions = new String[nbChemins];
            for (int c = 0; c < nbChemins; c++) {
                int type = random.nextInt(4);
                switch (type) {
                    case 0:
                        int pvEnnemi = 30 + sallesParcourues * 10 + random.nextInt(11);
                        int atkEnnemi = 5 + sallesParcourues * 2 + random.nextInt(3);
                        chemins[c] = new SalleEnnemi(pvEnnemi, atkEnnemi);
                        descriptions[c] = "Ennemi (PV: " + pvEnnemi + ", ATK: " + atkEnnemi + ")";
                        break;
                    case 1:
                        int pvBoss = 80 + sallesParcourues * 20 + random.nextInt(21);
                        int atkBoss = 15 + sallesParcourues * 4 + random.nextInt(6);
                        chemins[c] = new SalleBoss(pvBoss, atkBoss);
                        descriptions[c] = "Boss (PV: " + pvBoss + ", ATK: " + atkBoss + ")";
                        break;
                    case 2:
                        chemins[c] = new SalleSoin();
                        descriptions[c] = "Fontaine de soin (+15% PV max)";
                        break;
                    case 3:
                    default:
                        chemins[c] = new SalleAmelioration();
                        descriptions[c] = "Salle d'entraînement (+5 ATK)";
                        break;
                }
            }
            System.out.println("Chemins disponibles :");
            for (int c = 0; c < nbChemins; c++) {
                System.out.println((c+1) + ". " + descriptions[c]);
            }
            int choix = -1;
            while (choix < 1 || choix > nbChemins) {
                System.out.print("Choisissez un chemin (1-" + nbChemins + ") : ");
                String input = scanner.nextLine();
                try {
                    choix = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    choix = -1;
                }
                if (choix < 1 || choix > nbChemins) {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }
            Salle salleChoisie = chemins[choix-1];
            System.out.println("Vous avancez sur le chemin " + choix + "...");
            // Application de la salle choisie
            if (salleChoisie instanceof SalleEnnemi) {
                SalleEnnemi s = (SalleEnnemi) salleChoisie;
                int pvAvant = s.getPv();
                s.entrer(joueur);
                if (s.getPv() <= 0 && pvAvant > 0) {
                    System.out.println("Vous avez vaincu l'ennemi !");
                    joueur.setEnnemisTues(joueur.getEnnemisTues() + 1);
                } else if (joueur.getPv() > 0) {
                    System.out.println("L'ennemi vous a attaqué. Il lui reste " + s.getPv() + " PV.");
                }
            } else if (salleChoisie instanceof SalleBoss) {
                SalleBoss s = (SalleBoss) salleChoisie;
                int pvAvant = s.getPv();
                s.entrer(joueur);
                if (s.getPv() <= 0 && pvAvant > 0) {
                    System.out.println("Vous avez vaincu le boss !");
                    joueur.setBossVaincus(joueur.getBossVaincus() + 1);
                } else if (joueur.getPv() > 0) {
                    System.out.println("Le boss vous a attaqué. Il lui reste " + s.getPv() + " PV.");
                }
            } else if (salleChoisie instanceof SalleSoin) {
                salleChoisie.entrer(joueur);
                System.out.println("Vous trouvez une fontaine de soin. Vous récupérez 15% de vos PV max.");
            } else if (salleChoisie instanceof SalleAmelioration) {
                salleChoisie.entrer(joueur);
                System.out.println("Vous trouvez une salle d'entraînement. Votre attaque augmente de 5 !");
            }
            System.out.println("Après la salle : " + joueur.getPv() + "/" + joueur.getPvMax() + " PV, ATK : " + joueur.getAttaque());
            if (joueur.getPv() <= 0) {
                System.out.println("Vous êtes mort... Fin de la partie.");
                break;
            }
        }
        // À la fin de la session, sauvegarde automatique
        joueur.saveToFile(SAVE_FILE);
        System.out.println("Partie sauvegardée. À bientôt, " + joueur.getPseudo() + " !");
        System.out.println("Ennemis vaincus : " + joueur.getEnnemisTues());
        System.out.println("Boss vaincus : " + joueur.getBossVaincus());
    }
}