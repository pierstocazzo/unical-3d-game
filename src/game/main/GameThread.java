package game.main;

import game.base.PhysicsGame;
import game.common.MovementList.MovementType;
import game.core.LogicWorld;
import game.graphics.GraphicalWorld;
import game.menu.LoadingFrame;

import java.util.logging.Level;

public class GameThread implements Runnable {

	public LogicWorld logicGame;
	public GraphicalWorld game;
	public LoadingFrame loadingFrame;
	
	public GameThread( LoadingFrame loadingFrame ) {
		this.loadingFrame = loadingFrame;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, -1000, -1000 );
    	logicGame.createEnemy( -1150, -1150, MovementType.CIRCLE_SENTINEL_LARGE);
//    	logicGame.createEnemy( -1155, -1165, MovementType.REST );
//    	logicGame.createEnemiesGroup( 2, 1142, -452 );
//    	logicGame.createEnemiesGroup( 7, 1216, 749 );
//    	logicGame.createEnemiesGroup( 7, 142, 1207 );
//    	logicGame.createEnemiesGroup( 10, -1078, 1771 );
//    	logicGame.createEnemiesGroup( 10, -1645, 306 );
//    	logicGame.createEnemiesGroup( 100, 0, 0 );
//    	logicGame.createEnergyPackages( 200, 129*20, 129*20 );
    	
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
