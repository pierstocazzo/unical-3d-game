package joework.app;

import com.jme.input.InputHandler;

/**
 *
 * @author joseph
 */
public abstract class AssHoleBaseGame extends PhysicsGame {

    protected InputHandler input;

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
