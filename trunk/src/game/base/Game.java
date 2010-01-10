package game.base;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

import game.base.PhysicsGame;
import game.menu.LoadingFrame;

public abstract class Game extends PhysicsGame {
	
	public InputHandler freeCamInput;
	public LoadingFrame loadingFrame;
	
    @Override
    protected void setupGame() {
    	loadingFrame.setLoadingText("Inizializzazione Sistema");
    	loadingFrame.setProgress(5);
        setupInit();
        loadingFrame.setLoadingText("Caricamento Ambiente");
        loadingFrame.setProgress(20);
        setupEnvironment();
        loadingFrame.setLoadingText("Impostazioni Giocatore");
        loadingFrame.setProgress(60);
        setupPlayer();
        loadingFrame.setLoadingText("Caricamento Nemici");
        loadingFrame.setProgress(80);
        setupEnemies();
        loadingFrame.setLoadingText("Impostazioni Camera e Input");
        loadingFrame.setProgress(100);
        setupCamera();
        setupInput();
        
        freeCamInput = new FirstPersonHandler( cam, 200, 1 );
        freeCamInput.setEnabled( false );
        loadingFrame.setVisible(false);
    }
    
    public abstract void setupInit();
    
    public abstract void setupEnvironment();
    
    public abstract void setupPlayer();
    
    public abstract void setupEnemies();
    
    public abstract void setupCamera();
    
    public abstract void setupInput();
}
