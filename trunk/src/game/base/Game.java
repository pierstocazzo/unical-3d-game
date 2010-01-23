package game.base;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

import game.base.PhysicsGame;
import game.menu.LoadingFrame;

/**
 * Class Game
 * 
 * Useful class that allow to divide initialization
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public abstract class Game extends PhysicsGame {
	
	/** Used for debug mode */
	public InputHandler freeCamInput;
	
	/** Frame that displays loading info */
	public LoadingFrame loadingFrame;
	
    @Override
    protected void setupGame() {
    	loadingFrame.setLoadingText("Init game");
    	loadingFrame.setProgress(5);
        setupInit();
        loadingFrame.setLoadingText("Loading environment");
        loadingFrame.setProgress(20);
        setupEnvironment();
        loadingFrame.setLoadingText("Loading players");
        loadingFrame.setProgress(60);
        setupPlayer();
        loadingFrame.setLoadingText("Loading enemies");
        loadingFrame.setProgress(80);
        setupEnemies();
        loadingFrame.setLoadingText("Setting up input");
        loadingFrame.setProgress(100);
        setupCamera();
        setupInput();
        
        freeCamInput = new FirstPersonHandler( cam, 200, 1 );
        freeCamInput.setEnabled( false );
        loadingFrame.setVisible(false);
    }
    
    /** initialize system */
    public abstract void setupInit();
    
    /** initialize environment */
    public abstract void setupEnvironment();
    
    /** initialize player */
    public abstract void setupPlayer();
    
    /** initialize enemies */
    public abstract void setupEnemies();
    
    /** initialize camera */
    public abstract void setupCamera();
    
    /** initialize input handler */
    public abstract void setupInput();
}
