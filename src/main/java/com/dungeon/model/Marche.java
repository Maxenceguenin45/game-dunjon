package com.dungeon.model;

import com.dungeon.model.item.Rarete;
import java.util.Random;

/**
 * Classe représentant le marché du village avec des prix fluctuants
 * Le marché évolue en fonction de l'offre et de la demande
 */
public class Marche {
    private final Random random;
    private double multiplicateurGlobal; // Multiplicateur global des prix
    private TendanceMarche tendance;
    private int joursDepuisChangement;
    private String messageMarche;
    
    // Demande par type de rareté (0.5 = faible demande, 1.5 = forte demande)
    private double demandeCommun;
    private double demandePeuCommun;
    private double demandeRare;
    private double demandeEpique;
    private double demandeLegendaire;

    public enum TendanceMarche {
        CRISE("📉 CRISE", 0.6, 0.8, "Les temps sont durs, les marchands achètent peu."),
        DEPRESSION("📉 Dépression", 0.75, 0.9, "Le marché est morose."),
        STABLE("➡️ Stable", 0.9, 1.1, "Le marché est stable."),
        CROISSANCE("📈 Croissance", 1.1, 1.3, "Le marché est dynamique !"),
        BOOM("📈 BOOM", 1.4, 1.8, "C'est la fête ! Les marchands achètent à prix d'or !");

        private final String nom;
        private final double multiplicateurMin;
        private final double multiplicateurMax;
        private final String description;

        TendanceMarche(String nom, double multiplicateurMin, double multiplicateurMax, String description) {
            this.nom = nom;
            this.multiplicateurMin = multiplicateurMin;
            this.multiplicateurMax = multiplicateurMax;
            this.description = description;
        }

        public String getNom() {
            return nom;
        }

        public double getMultiplicateurMin() {
            return multiplicateurMin;
        }

        public double getMultiplicateurMax() {
            return multiplicateurMax;
        }

        public String getDescription() {
            return description;
        }
    }

    public Marche() {
        this.random = new Random();
        this.tendance = TendanceMarche.STABLE;
        this.multiplicateurGlobal = 1.0;
        this.joursDepuisChangement = 0;
        
        // Initialiser la demande de manière aléatoire
        this.demandeCommun = 0.8 + random.nextDouble() * 0.4; // 0.8-1.2
        this.demandePeuCommun = 0.8 + random.nextDouble() * 0.4;
        this.demandeRare = 0.8 + random.nextDouble() * 0.4;
        this.demandeEpique = 0.8 + random.nextDouble() * 0.4;
        this.demandeLegendaire = 0.8 + random.nextDouble() * 0.4;
        
        genererMessageMarche();
    }

    /**
     * Fait évoluer le marché (à appeler à chaque visite du village)
     */
    public void evoluer() {
        joursDepuisChangement++;

        // Le marché change de tendance tous les 3-5 jours
        if (joursDepuisChangement >= 3 + random.nextInt(3)) {
            changerTendance();
            joursDepuisChangement = 0;
        }

        // Ajuster le multiplicateur global selon la tendance
        double min = tendance.getMultiplicateurMin();
        double max = tendance.getMultiplicateurMax();
        multiplicateurGlobal = min + random.nextDouble() * (max - min);

        // Faire fluctuer la demande pour chaque rareté
        fluctuerDemande();
        
        // Occasionnellement, créer un événement spécial
        if (random.nextDouble() < 0.15) { // 15% de chance
            creerEvenementSpecial();
        }
        
        genererMessageMarche();
    }

    /**
     * Change la tendance générale du marché
     */
    private void changerTendance() {
        TendanceMarche[] tendances = TendanceMarche.values();
        
        // Favoriser les tendances proches de l'actuelle
        int indexActuel = tendance.ordinal();
        int changement = random.nextInt(3) - 1; // -1, 0, ou 1
        int nouvelIndex = Math.max(0, Math.min(tendances.length - 1, indexActuel + changement));
        
        tendance = tendances[nouvelIndex];
    }

    /**
     * Fait fluctuer la demande pour chaque type de rareté
     */
    private void fluctuerDemande() {
        demandeCommun = ajusterDemande(demandeCommun);
        demandePeuCommun = ajusterDemande(demandePeuCommun);
        demandeRare = ajusterDemande(demandeRare);
        demandeEpique = ajusterDemande(demandeEpique);
        demandeLegendaire = ajusterDemande(demandeLegendaire);
    }

    /**
     * Ajuste une valeur de demande avec une légère variation aléatoire
     */
    private double ajusterDemande(double demandeActuelle) {
        double variation = (random.nextDouble() - 0.5) * 0.3; // -0.15 à +0.15
        double nouvelleDemande = demandeActuelle + variation;
        return Math.max(0.5, Math.min(1.5, nouvelleDemande)); // Entre 0.5 et 1.5
    }

    /**
     * Crée un événement spécial qui affecte fortement un type de rareté
     */
    private void creerEvenementSpecial() {
        int typeEvent = random.nextInt(5);
        
        switch (typeEvent) {
            case 0 -> {
                demandeCommun = 1.3 + random.nextDouble() * 0.2;
                messageMarche += "\n⭐ Les objets COMMUNS sont très recherchés !";
            }
            case 1 -> {
                demandePeuCommun = 1.3 + random.nextDouble() * 0.2;
                messageMarche += "\n⭐ Les objets PEU COMMUNS se vendent bien !";
            }
            case 2 -> {
                demandeRare = 1.3 + random.nextDouble() * 0.2;
                messageMarche += "\n⭐ Les objets RARES sont en forte demande !";
            }
            case 3 -> {
                demandeEpique = 1.3 + random.nextDouble() * 0.2;
                messageMarche += "\n⭐ Les collectionneurs achètent des objets ÉPIQUES !";
            }
            case 4 -> {
                demandeLegendaire = 1.4 + random.nextDouble() * 0.3;
                messageMarche += "\n⭐⭐ OFFRE EXCEPTIONNELLE pour les objets LÉGENDAIRES !";
            }
        }
    }

    /**
     * Génère un message décrivant l'état actuel du marché
     */
    private void genererMessageMarche() {
        messageMarche = "╔══════════════════════════════════════╗\n";
        messageMarche += "║    📊 ÉTAT DU MARCHÉ 📊            ║\n";
        messageMarche += "╚══════════════════════════════════════╝\n";
        messageMarche += "\n";
        messageMarche += "Tendance : " + tendance.getNom() + "\n";
        messageMarche += tendance.getDescription() + "\n";
        messageMarche += "\n";
        messageMarche += "💹 Demande actuelle :\n";
        messageMarche += String.format("  • Commun      : %s\n", getIndicateurDemande(demandeCommun));
        messageMarche += String.format("  • Peu Commun  : %s\n", getIndicateurDemande(demandePeuCommun));
        messageMarche += String.format("  • Rare        : %s\n", getIndicateurDemande(demandeRare));
        messageMarche += String.format("  • Épique      : %s\n", getIndicateurDemande(demandeEpique));
        messageMarche += String.format("  • Légendaire  : %s\n", getIndicateurDemande(demandeLegendaire));
    }

    /**
     * Retourne un indicateur visuel de la demande
     */
    private String getIndicateurDemande(double demande) {
        if (demande < 0.7) return "🔴 Faible";
        if (demande < 0.9) return "🟡 Basse";
        if (demande < 1.1) return "🟢 Normal";
        if (demande < 1.3) return "🟢 Haute";
        return "🟢🟢 Très Haute";
    }

    /**
     * Calcule le prix de vente d'un objet selon le marché actuel
     */
    public int calculerPrixVente(int prixBase, Rarete rarete) {
        double demande = switch (rarete) {
            case COMMUN -> demandeCommun;
            case PEU_COMMUN -> demandePeuCommun;
            case RARE -> demandeRare;
            case EPIQUE -> demandeEpique;
            case LEGENDAIRE -> demandeLegendaire;
        };

        // Prix final = prixBase × multiplicateur global × demande spécifique
        double prixFinal = prixBase * multiplicateurGlobal * demande;
        
        return Math.max(5, (int) Math.round(prixFinal));
    }

    /**
     * Retourne le message décrivant l'état du marché
     */
    public String getMessageMarche() {
        return messageMarche;
    }

    /**
     * Retourne la tendance actuelle du marché
     */
    public TendanceMarche getTendance() {
        return tendance;
    }

    /**
     * Retourne le multiplicateur global actuel
     */
    public double getMultiplicateurGlobal() {
        return multiplicateurGlobal;
    }

    /**
     * Obtient la demande pour une rareté spécifique
     */
    public double getDemande(Rarete rarete) {
        return switch (rarete) {
            case COMMUN -> demandeCommun;
            case PEU_COMMUN -> demandePeuCommun;
            case RARE -> demandeRare;
            case EPIQUE -> demandeEpique;
            case LEGENDAIRE -> demandeLegendaire;
        };
    }
}

