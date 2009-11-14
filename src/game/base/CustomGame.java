package game.base;

import com.jme.input.InputHandler;

/** 
 *
 */
public abstract class CustomGame extends PhysicsGame {

    protected InputHandler input;

    @Override
    protected void simpleInitGame() {
        // Chiamo i principali metodi per creare il videogame
        setupInit();
        setupEnvironment();
        setupPlayer();
        setupEnemies();
        setupCamera();
        setupChaseCamera();
        setupInput();
    }
    
    protected abstract void setupInit();
    protected abstract void setupEnvironment();
    protected abstract void setupEnemies();
    protected abstract void setupPlayer();
    protected abstract void setupCamera();
    protected abstract void setupChaseCamera();
    protected abstract void setupInput();

}
