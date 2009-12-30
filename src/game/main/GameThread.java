package game.main;

import java.util.logging.Level;

import game.base.PhysicsGame;
import game.common.MovementList.MovementType;
import game.core.LogicWorld;
import game.graphics.GraphicalWorld;
import game.menu.LoadingFrame;

public class GameThread implements Runnable {

	public LogicWorld logicGame;
	public GraphicalWorld game;
	public LoadingFrame loadingFrame;
	
	public GameThread( LoadingFrame loadingFrame ) {
		this.loadingFrame = loadingFrame;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 2000, -900, -900 );
    	logicGame.createEnemy( -900, -900, MovementType.REST );
    	logicGame.createEnemiesGroup( 5, -700, -700 );
    	logicGame.createEnemiesGroup( 5, -400, -400 );
    	logicGame.createEnemiesGroup( 5, -200, -200 );
//    	logicGame.createEnemiesGroup( 10, 400, 400 );
//    	logicGame.createEnemiesGroup( 10, 300, 300 );
//    	logicGame.createEnemiesGroup( 100, 0, 0 );
    	
//    	logicGame.createEnergyPackages( 20, 129*20, 129*20 );
    	
    	logicGame.initScoreManager();
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
