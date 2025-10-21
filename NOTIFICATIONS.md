# Système de Notifications Popup

## Description
Un système de notifications visuelles temporaires a été ajouté pour mettre en valeur les événements importants du jeu.

## Types de notifications

### 🟡 LEVEL_UP (Montée de niveau)
- **Couleur** : Or (255, 215, 0)
- **Icône** : ⭐
- **Durée** : 4 secondes
- **Déclencheur** : Quand le joueur gagne un niveau

### 🔴 BOSS_DEFEATED (Boss vaincu)
- **Couleur** : Rouge (255, 50, 50)
- **Icône** : 👑
- **Durée** : 4 secondes
- **Déclencheur** : Quand le joueur bat un boss

### 🟣 RARE_ITEM (Item rare)
- **Couleur** : Violet (138, 43, 226)
- **Icône** : 💎
- **Durée** : 3 secondes
- **Déclencheur** : Obtention d'un item rare (tous les 2 niveaux)

### 🟠 LEGENDARY_ITEM (Item légendaire)
- **Couleur** : Orange (255, 140, 0)
- **Icône** : ✨
- **Durée** : 3 secondes
- **Déclencheur** : Obtention d'un item épique (tous les 3 niveaux)

### 🟢 ACHIEVEMENT (Succès)
- **Couleur** : Vert (50, 205, 50)
- **Icône** : 🏆
- **Durée** : 4 secondes
- **Déclencheur** : Bonus exceptionnels (tous les 5 niveaux)

### 🔵 INFO (Information)
- **Couleur** : Bleu clair (100, 149, 237)
- **Icône** : ℹ
- **Durée** : 3 secondes
- **Déclencheur** : Événements informatifs généraux

## Caractéristiques visuelles

- **Position** : Centrée horizontalement, sous le panneau de stats
- **Animation** : Fade out dans les 500 dernières millisecondes
- **Style** : 
  - Fond semi-transparent avec la couleur du type de notification
  - Bordure brillante de 3px
  - Ombre portée pour la profondeur
  - Icône emoji à gauche
  - Texte en blanc, police Serif Bold 18px

## Utilisation dans le code

```java
// Afficher une notification
if (gamePanel instanceof GamePanel) {
    ((GamePanel) gamePanel).showNotification(
        "NIVEAU 5 ATTEINT !",
        GamePanel.NotificationType.LEVEL_UP,
        4000  // durée en millisecondes (optionnel, défaut: 3000)
    );
}
```

## Événements actuellement notifiés

1. **Montée de niveau** - Notification dorée qui s'affiche pendant 4 secondes
2. **Victoire contre un boss** - Notification rouge avec couronne
3. **Obtention d'items rares** (niv. 2, 4, 6...) - Notification violette
4. **Obtention d'items épiques** (niv. 3, 6, 9...) - Notification orange
5. **Bonus exceptionnels** (niv. 5, 10, 15...) - Notification verte

## Notes techniques

- Les notifications s'empilent verticalement si plusieurs sont actives
- Elles disparaissent automatiquement après leur durée
- L'animation de fade out se déclenche 500ms avant la fin
- Le système utilise `repaint()` pour maintenir l'animation fluide

