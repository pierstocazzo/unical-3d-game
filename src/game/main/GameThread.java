package game.main;

import game.core.LogicWorld;
import game.graphics.GraphicalWorld;
import game.menu.MainMenu;

public class GameThread extends Thread {

	LogicWorld logicGame;
	GraphicalWorld game;
	MainMenu menu;
	boolean isLoaded;
	
	
	public GameThread( MainMenu menu ) {
		this.menu = menu;
		logicGame = new LogicWorld();
//    	logicGame.createPlayer( 100, 476, 561 );  	
    	logicGame.createEnergyPackages( 100 );
    	isLoaded = false;
	}
	
	public GameThread( LogicWorld logicGame, MainMenu menu ){
		this.menu = menu;
		this.logicGame = logicGame;
		isLoaded = true;
	}

	public void run() {
		game = new GraphicalWorld( logicGame, menu, isLoaded );
		menu.setGame( game );
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
