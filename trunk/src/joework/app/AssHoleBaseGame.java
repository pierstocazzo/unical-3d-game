package joework.app;

/**
 *
 * @author joseph
 */
public abstract class AssHoleBaseGame extends PhysicsGame {

    @Override
    protected void simpleInitGame() {
        // Chiamo i principali metodi per creare il videogame
        setupInit();
        setupEnvironment();
        setupPlayer();
        setupCharacters();
        setupCamera();
        setupInput();
    }
    
    protected abstract void setupInit();
    protected abstract void setupEnvironment();
    protected abstract void setupCharacters();
    protected abstract void setupPlayer();
    protected abstract void setupCamera();
    protected abstract void setupInput();
}
