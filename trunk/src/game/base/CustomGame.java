package game.base;

import com.jme.input.InputHandler;

/**
 *
 * @author joseph
 */
public abstract class CustomGame extends PhysicsGame {

    protected InputHandler input;

    @Override
    protected void simpleInitGame() {
        // Chiamo i principali metodi per creare il videogame
        setupInit();
        setupEnvironment();
        setupPlayer();
        setupCharacters();
        setupCamera();
        setupChaseCamera();
        setupInput();
    }
    
    protected abstract void setupInit();
    protected abstract void setupEnvironment();
    protected abstract void setupCharacters();
    protected abstract void setupPlayer();
    protected abstract void setupCamera();
    protected abstract void setupChaseCamera();
    protected abstract void setupInput();

}
