package game.base;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

import game.base.PhysicsGame;

public abstract class Game extends PhysicsGame {
	
	public InputHandler freeCamInput;
	
    @Override
    protected void setupGame() {
    	threadController.loadingFrame.setProgress(25);
        setupInit();
        threadController.loadingFrame.setProgress(50);
        setupEnvironment();
        threadController.loadingFrame.setProgress(75);
        setupPlayer();
        threadController.loadingFrame.setProgress(100);
        setupEnemies();
        setupCamera();
        setupInput();
        
        freeCamInput = new FirstPersonHandler( cam, 50, 1 );
        freeCamInput.setEnabled( false );
        threadController.loadingFrame.setVisible(false);
    }
    
    public abstract void setupInit();
    
    public abstract void setupEnvironment();
    
    public abstract void setupPlayer();
    
    public abstract void setupEnemies();
    
    public abstract void setupCamera();
    
    public abstract void setupInput();
}
