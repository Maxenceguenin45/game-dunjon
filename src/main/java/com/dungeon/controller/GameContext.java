package com.dungeon.controller;

import com.dungeon.controller.state.GameState;
import com.dungeon.model.personnage.Joueur;
import com.dungeon.model.salle.Salle;
import com.dungeon.service.*;
import com.dungeon.ui.GameUI;

/**
 * Contexte du jeu qui maintient l'état actuel et les services
 */
public class GameContext {
    private GameState currentState;
    private final GameUI gameUI;
    private final JoueurService joueurService;
    private final SalleService salleService;
    private final CombatService combatService;
    private final ItemService itemService;
    private final NiveauService niveauService;
    private final UIService uiService;
    
    // Données du jeu
    private Joueur joueur;
    private Salle salleActuelle;
    private Salle[] cheminsPossibles;
    private String[] descriptionsSalles;
    private int sallesParcourues;
    private int nbChemins;
    private int pvEnnemiAvantAction;
    
    public GameContext(GameUI gameUI) {
        this.gameUI = gameUI;
        this.joueurService = new JoueurService();
        this.salleService = new SalleService();
        this.combatService = new CombatService();
        this.itemService = new ItemService();
        this.niveauService = new NiveauService();
        this.uiService = new UIService(gameUI);
        this.sallesParcourues = 0;
    }
    
    /**
     * Change l'état du jeu
     */
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter(this);
        }
    }
    
    /**
     * Délègue une action à l'état actuel
     */
    public void handleAction(int choix) {
        if (currentState != null) {
            currentState.handleAction(this, choix);
        }
    }
    
    // Getters
    public GameState getCurrentState() {
        return currentState;
    }
    
    public GameUI getGameUI() {
        return gameUI;
    }
    
    public JoueurService getJoueurService() {
        return joueurService;
    }
    
    public SalleService getSalleService() {
        return salleService;
    }
    
    public CombatService getCombatService() {
        return combatService;
    }
    
    public ItemService getItemService() {
        return itemService;
    }
    
    public NiveauService getNiveauService() {
        return niveauService;
    }
    
    public UIService getUiService() {
        return uiService;
    }
    
    public Joueur getJoueur() {
        return joueur;
    }
    
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }
    
    public Salle getSalleActuelle() {
        return salleActuelle;
    }
    
    public void setSalleActuelle(Salle salle) {
        this.salleActuelle = salle;
    }
    
    public Salle[] getCheminsPossibles() {
        return cheminsPossibles;
    }
    
    public void setCheminsPossibles(Salle[] chemins) {
        this.cheminsPossibles = chemins;
    }
    
    public String[] getDescriptionsSalles() {
        return descriptionsSalles;
    }
    
    public void setDescriptionsSalles(String[] descriptions) {
        this.descriptionsSalles = descriptions;
    }
    
    public int getSallesParcourues() {
        return sallesParcourues;
    }
    
    public void setSallesParcourues(int salles) {
        this.sallesParcourues = salles;
    }
    
    public void incrementerSallesParcourues() {
        this.sallesParcourues++;
    }
    
    public int getNbChemins() {
        return nbChemins;
    }
    
    public void setNbChemins(int nb) {
        this.nbChemins = nb;
    }
    
    public int getPvEnnemiAvantAction() {
        return pvEnnemiAvantAction;
    }
    
    public void setPvEnnemiAvantAction(int pv) {
        this.pvEnnemiAvantAction = pv;
    }
}

