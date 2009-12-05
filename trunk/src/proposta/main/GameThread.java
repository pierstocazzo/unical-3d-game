package proposta.main;

import proposta.core.LogicWorld;
import proposta.enemyAI.MovementList.MovementType;
import proposta.graphics.GraphicalWorld;

import com.jme.math.Vector3f;

public class GameThread implements Runnable {

	ThreadController threadController;
	public LogicWorld logicGame;
	
	public GameThread( ThreadController threadController ) {
		this.threadController = threadController;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, new Vector3f( 700, 60, 700 ) );
    	logicGame.createEnemy( new Vector3f( 800, 50, 800 ), MovementType.REST );
//    	logicGame.createEnemiesGroup( 3, new Vector3f( 50, 50, 50 ) );
//    	logicGame.createEnemiesGroup( 4, new Vector3f( 220, 50, 220 ) );
    	
    	// TODO caricare pi� nemici
//    	logicGame.createEnemiesGroup( 5, new Vector3f( 250, 10, 250 ) );
//    	
//    	logicGame.createEnemiesGroup( 20, new Vector3f( 1000, 10, 1000 ) );
    	logicGame.createEnergyPackages( 20, 129*20, 129*20 );
	}
	
	public GameThread( ThreadController threadController, LogicWorld logicGame ){
		this.threadController = threadController;
		this.logicGame = logicGame;
	}

	public void run() {
        GraphicalWorld game = new GraphicalWorld( logicGame, threadController );
        game.isThread = true;
        game.start();
    }
}
