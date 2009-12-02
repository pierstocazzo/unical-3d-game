package slashWork.game.base;

public abstract class Game extends PhysicsGame {
	
    @Override
    protected void setupGame() {
    	setupInput();
    	setupCamera();
        setupInit();
        setupEnvironment();
        setupPlayer();
        setupEnemies();
    }
    
    public abstract void setupInit();
    
    public abstract void setupEnvironment();
    
    public abstract void setupPlayer();
    
    public abstract void setupEnemies();
    
    public abstract void setupCamera();
    
    public abstract void setupInput();
}
