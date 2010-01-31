package game.base;

import game.common.GameConfiguration;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

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
	
    @Override
    protected void setupGame() {
    	getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_init" ) );
    	getMenu().setProgress(5);
        setupInit();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_environment" ) );
        getMenu().setProgress(20);
        setupEnvironment();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_players" ) );
        getMenu().setProgress(60);
        setupPlayer();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_enemies" ) );
        getMenu().setProgress(80);
        setupEnemies();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_input" ) );
        setupCamera();
        setupInput();
        getMenu().setProgress(100);
        freeCamInput = new FirstPersonHandler( cam, 200, 1 );
        freeCamInput.setEnabled( false );
        try{
        	Thread.sleep(2000);
        } catch (Exception e) {
			// TODO: handle exception
		}
        
        getMenu().hideMenu();
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
