package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;

/**
 * Interface représentant un état du jeu dans le pattern State
 */
public interface GameState {
    /**
     * Entre dans cet état
     * @param context le contexte du jeu
     */
    void enter(GameContext context);
    
    /**
     * Gère une action dans cet état
     * @param context le contexte du jeu
     * @param choix le choix du joueur
     */
    void handleAction(GameContext context, int choix);
    
    /**
     * Sort de cet état
     * @param context le contexte du jeu
     */
    void exit(GameContext context);
    
    /**
     * Retourne le nom de l'état pour le debug
     */
    String getStateName();
}

