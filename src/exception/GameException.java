package exception;

public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}

class SauvegardeException extends GameException {
    public SauvegardeException(String message) {
        super(message);
    }
}

class CombatException extends GameException {
    public CombatException(String message) {
        super(message);
    }
}
