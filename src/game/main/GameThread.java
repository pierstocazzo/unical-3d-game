package game.main;

import game.core.LogicWorld;
import game.graphics.GraphicalWorld;
import game.menu.MainMenu;

public class GameThread extends Thread {

	LogicWorld logicGame;
	GraphicalWorld game;
	MainMenu menu;
	
	
	public GameThread( MainMenu menu ) {
		this.menu = menu;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, 476, 561 );  	
    	logicGame.createEnergyPackages( 100 );
	}
	
	public GameThread( LogicWorld logicGame, MainMenu menu ){
		this.menu = menu;
		this.logicGame = logicGame;
	}

	public void run() {
		game = new GraphicalWorld( logicGame, menu );
		menu.setGame( game );
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
