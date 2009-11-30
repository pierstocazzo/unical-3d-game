package game.main;

import game.core.LogicWorld;
import game.enemyAI.MovementList.MovementType;
import game.graphics.GraphicalWorld;

import com.jme.math.Vector3f;

public class GameThread implements Runnable {

	ThreadController tc;
	public LogicWorld logicGame;
	
	public GameThread(ThreadController tc) {
		this.tc = tc;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, new Vector3f( 160, 10, 160 ) );
    	logicGame.createEnemy( new Vector3f( 100, 10, 100 ), MovementType.REST );
    	logicGame.createEnemiesGroup( 3, new Vector3f( 50, 10, 50 ) );
    	logicGame.createEnemiesGroup( 4, new Vector3f( 220, 10, 220 ) );
//    	logicGame.createEnemiesGroup( 5, new Vector3f( 250, 10, 250 ) );
//    	
//    	logicGame.createEnemiesGroup( 20, new Vector3f( 1000, 10, 1000 ) );
    	logicGame.createEnergyPackages( 10, 640, 640 );
	}
	
	public GameThread( ThreadController tc, LogicWorld logicGame ){
		this.tc = tc;
		this.logicGame = logicGame;
	}

	public void run() {
    	
        GraphicalWorld game = new GraphicalWorld( logicGame, tc, 4096, 4096 );
//        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }
}
