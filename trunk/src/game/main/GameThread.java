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
    	logicGame.createPlayer( 100, -700, -900 );
    	
    	PointPath custom = new PointPath();
    	custom.add( new Point( -868, -958 ) );
		custom.add( new Point( -1023, -969 ) );
		custom.add( new Point( -859, -824 ) );
		LinkedList<Movement> list = custom.generateMovementsList();
    	
    	logicGame.createEnemy( -900, -900, State.DEFAULT, list );
    	logicGame.createEnemy( -1165, -1175, State.DEFAULT, MovementType.REST );
//    	logicGame.createEnemiesGroup( 15, -800, -800 );
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
