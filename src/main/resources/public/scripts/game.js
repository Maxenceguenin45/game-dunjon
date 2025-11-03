let gameState = {
    health: 100,
    maxHealth: 100,
    strength: 10,
    gold: 0
};

function updateStats(stats) {
    gameState = { ...gameState, ...stats };

    document.querySelector('.health-bar > div').style.width =
        `${(gameState.health / gameState.maxHealth) * 100}%`;
    document.querySelector('.health-value').textContent =
        `${gameState.health}/${gameState.maxHealth}`;
    document.querySelector('.strength-value').textContent =
        `Force: ${gameState.strength}`;
    document.querySelector('.gold-value').textContent =
        `Or: ${gameState.gold}`;
}

function updateChoices(choices) {
    const choicesPanel = document.querySelector('.choices-panel');
    choicesPanel.innerHTML = '';

    choices.forEach((choice, index) => {
        const button = document.createElement('button');
        button.className = 'choice-button';
        button.textContent = choice;
        button.onclick = () => window.javaGameBridge.makeChoice(index);
        choicesPanel.appendChild(button);
    });
}

function addMessage(message) {
    const messagePanel = document.querySelector('.message-panel');
    const messageElement = document.createElement('div');
    messageElement.className = 'message';
    messageElement.textContent = message;
    messagePanel.appendChild(messageElement);
    messagePanel.scrollTop = messagePanel.scrollHeight;
}

function updateAnimation(data) {
    const canvas = document.querySelector('#gameCanvas');
    const ctx = canvas.getContext('2d');

    // Effacer le canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Dessiner la nouvelle scène
    switch (data.scene) {
        case 'COMBAT':
            drawCombatScene(ctx, data);
            break;
        case 'EXPLORATION':
            drawExplorationScene(ctx, data);
            break;
        case 'GAME_OVER':
            drawGameOverScene(ctx, data);
            break;
    }
}

// Fonctions de dessin pixel art
function drawCombatScene(ctx, data) {
    // Implémenter le dessin de la scène de combat
    ctx.fillStyle = '#fff';
    ctx.font = '16px "Press Start 2P"';
    ctx.textAlign = 'center';
    ctx.fillText('Combat en cours...', canvas.width/2, canvas.height/2);
}

function drawExplorationScene(ctx, data) {
    // Implémenter le dessin de la scène d'exploration
    ctx.fillStyle = '#fff';
    ctx.font = '16px "Press Start 2P"';
    ctx.textAlign = 'center';
    ctx.fillText('Exploration...', canvas.width/2, canvas.height/2);
}

function drawGameOverScene(ctx, data) {
    ctx.fillStyle = '#fff';
    ctx.font = '24px "Press Start 2P"';
    ctx.textAlign = 'center';
    ctx.fillText('GAME OVER', canvas.width/2, canvas.height/2);
    if (data.message) {
        ctx.font = '16px "Press Start 2P"';
        ctx.fillText(data.message, canvas.width/2, canvas.height/2 + 40);
    }
}
