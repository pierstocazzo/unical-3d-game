package game.main;

import game.base.PhysicsGame;
import game.common.PointPath;
import game.common.Movement;
import game.common.Point;
import game.common.State;
import game.common.MovementList.MovementType;
import game.core.LogicWorld;
import game.graphics.GraphicalWorld;
import game.menu.LoadingFrame;

import java.util.LinkedList;
import java.util.logging.Level;

public class GameThread implements Runnable {

	public LogicWorld logicGame;
	public GraphicalWorld game;
	public LoadingFrame loadingFrame;
	
	public GameThread( LoadingFrame loadingFrame ) {
		this.loadingFrame = loadingFrame;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, 476, 561 );  	
    	
    	logicGame.initScoreManager();
    	
    	logicGame.createEnergyPackages( 100 );
	}
	
	public GameThread( LogicWorld logicGame, LoadingFrame loadingFrame ){
		this.loadingFrame = loadingFrame;
		this.logicGame = logicGame;
	}

	public void run() {
		game = new GraphicalWorld( logicGame, loadingFrame );
		PhysicsGame.logger.setLevel( Level.SEVERE );
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
