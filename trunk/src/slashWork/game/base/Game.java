package slashWork.game.base;

public abstract class Game extends PhysicsGame {
	
    @Override
    protected void setupGame() {
        setupInit();
        setupEnvironment();
        setupPlayer();
        setupEnemies();
        setupCamera();
        setupInput();
    }
    
    public abstract void setupInit();
    
    public abstract void setupEnvironment();
    
    public abstract void setupPlayer();
    
    public abstract void setupEnemies();
    
    public abstract void setupCamera();
    
    public abstract void setupInput();
}
