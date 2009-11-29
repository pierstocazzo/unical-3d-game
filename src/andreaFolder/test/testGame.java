package andreaFolder.test;

import andreaFolder.core.LogicWorld;
import andreaFolder.enemyAI.MovementList.MovementType;
import andreaFolder.graphics.GraphicalWorld;

import com.jme.math.Vector3f;

public class testGame implements Runnable{

	ThreadController tc;
	LogicWorld logicGame;
	
	public testGame(ThreadController tc) {
		this.tc = tc;
		logicGame = new LogicWorld();
		logicGame.createPlayer( 100, new Vector3f( 3, 10, 3) );
    	logicGame.createEnemy( new Vector3f( 140, 10, 140 ), MovementType.SMALL_PERIMETER );
    	logicGame.createEnemies( 3, new Vector3f( 50, 10, 50 ) );
	}
	
	public testGame(ThreadController tc, LogicWorld logicGame){
		this.tc = tc;
		this.logicGame = logicGame;
	}

	public void run() {
    	
        GraphicalWorld game = new GraphicalWorld( logicGame, tc );
//        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }
}
