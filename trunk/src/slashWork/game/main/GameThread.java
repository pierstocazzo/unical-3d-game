package slashWork.game.main;

import slashWork.game.core.LogicWorld;
import slashWork.game.enemyAI.MovementList.MovementType;
import slashWork.game.graphics.GraphicalWorld;
import slashWork.game.main.menu.LoadingFrame;

import com.jme.math.Vector3f;

public class GameThread implements Runnable {

	public ThreadController threadController;
	public LogicWorld logicGame;
	
	public GameThread(LoadingFrame loadingFrame ) {
		this.threadController = new ThreadController(loadingFrame,this);
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, new Vector3f( 500, 10, 500 ) );
    	logicGame.createEnemy( new Vector3f( 400, 10, 400 ), MovementType.REST );
//    	logicGame.createEnemiesGroup( 3, new Vector3f( 50, 10, 50 ) );
//    	logicGame.createEnemiesGroup( 4, new Vector3f( 220, 10, 220 ) );
//    	logicGame.createEnemiesGroup( 5, new Vector3f( 250, 10, 250 ) );
//    	logicGame.createEnemiesGroup( 20, new Vector3f( 1000, 10, 1000 ) );
    	logicGame.createEnergyPackages( 5, 1000, 1000 );
	}
	
	public GameThread( LogicWorld logicGame, LoadingFrame loadingFrame ){
		this.threadController = new ThreadController(loadingFrame,this);
		this.logicGame = logicGame;
	}

	public void run() {
        GraphicalWorld game = new GraphicalWorld( logicGame, threadController );
        game.isThread = true;
        game.start();
    }
}
