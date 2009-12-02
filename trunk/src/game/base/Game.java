package game.base;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

import game.base.PhysicsGame;

public abstract class Game extends PhysicsGame {
	
	public InputHandler input;
	
    @Override
    protected void setupGame() {
        setupInit();
        setupEnvironment();
        setupPlayer();
        setupEnemies();
        setupCamera();
        setupInput();
        
        input = new FirstPersonHandler( cam, 50, 1 );
        input.setEnabled( false );
    }
    
    public abstract void setupInit();
    
    public abstract void setupEnvironment();
    
    public abstract void setupPlayer();
    
    public abstract void setupEnemies();
    
    public abstract void setupCamera();
    
    public abstract void setupInput();
}
