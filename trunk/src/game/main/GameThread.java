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
    	logicGame.createPlayer( 100, 651, 132 );
    	
    	

		//two sentinel
    	PointPath pointPathSentinel3 = new PointPath();
    	pointPathSentinel3.add( new Point( 892, 528 ) );
		pointPathSentinel3.add( new Point( 869, 571 ) );
		LinkedList<Movement> pathSentinel3 = pointPathSentinel3.generateMovementsList();
    	logicGame.createEnemy( 892, 528, State.DEFAULT, pathSentinel3 );
    	
    	PointPath pointPathSentinel4 = new PointPath();
    	pointPathSentinel4.add( new Point( 892, 575 ) );
    	pointPathSentinel4.add( new Point( 909, 541 ) );
		LinkedList<Movement> pathSentinel4 = pointPathSentinel4.generateMovementsList();
    	logicGame.createEnemy( 892, 575, State.DEFAULT, pathSentinel4 );
    	
    	// four enemy in rest state
    	logicGame.createEnemy( 832, 720, State.DEFAULT, MovementType.REST );
    	logicGame.createEnemy( 827, 692, State.DEFAULT, MovementType.REST );
    	
    	//two guard
    	
    	logicGame.createEnemy( 867, 485, State.DEFAULT, MovementType.REST );
    	logicGame.createEnemy( 822, 634, State.DEFAULT, MovementType.REST );
    	
    	PointPath pointPathSentinel5 = new PointPath();
    	pointPathSentinel5.add( new Point( 1025, 551 ) );
    	pointPathSentinel5.add( new Point( 1117, 578 ) );
    	pointPathSentinel5.add( new Point( 1207, 614 ) );
    	pointPathSentinel5.add( new Point( 1207, 671 ) );
    	pointPathSentinel5.add( new Point( 1143, 719 ) );
    	pointPathSentinel5.add( new Point( 1109, 695 ) );
    	pointPathSentinel5.add( new Point( 1031, 674 ) );
    	pointPathSentinel5.add( new Point( 1023, 620 ) );
		LinkedList<Movement> pathSentinel5 = pointPathSentinel5.generateMovementsList();
    	logicGame.createEnemy( 1025, 551, State.DEFAULT, pathSentinel5 );
    	
    	logicGame.createEnemy( 939, 643, State.DEFAULT, MovementType.SMALL_PERIMETER );
    	logicGame.createEnemy( 1005, 740, State.DEFAULT, MovementType.SMALL_PERIMETER );
    	logicGame.createEnemy( 1169, 775, State.DEFAULT, MovementType.EIGHT_PATH_LARGE );
    	
    	logicGame.createEnemy( 1093, 759, State.DEFAULT, MovementType.EIGHT_PATH_SMALL );
    	logicGame.createEnemy( 1058, 720, State.DEFAULT, MovementType.CIRCLE_SENTINEL_SMALL );
    	logicGame.createEnemy( 1208, 821, State.DEFAULT, MovementType.DIAGONAL_SENTINEL_SECONDARY );
    	
    	logicGame.createEnemy( 975, 562, State.DEFAULT, MovementType.CIRCLE_SENTINEL_LARGE );
    	
    	logicGame.createEnemy( 1301, 772, State.DEFAULT, MovementType.REST );
    	logicGame.createEnemy( 1107, 641, State.DEFAULT, MovementType.REST );
    	logicGame.createEnemy( 1133, 864, State.DEFAULT, MovementType.REST );
    	
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
