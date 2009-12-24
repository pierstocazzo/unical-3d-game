package game.main;

import game.core.LogicWorld;
import game.enemyAI.MovementList.MovementType;
import game.graphics.GraphicalWorld;
import game.main.menu.LoadingFrame;

import com.jme.math.Vector3f;

public class GameThread implements Runnable {

//	public ThreadController threadController;
	public LogicWorld logicGame;
	public GraphicalWorld game;
	public LoadingFrame loadingFrame;
	
	public GameThread(LoadingFrame loadingFrame ) {
		this.loadingFrame = loadingFrame;
//		this.threadController = new ThreadController(loadingFrame,this);
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
	
	public GameThread( LogicWorld logicGame, LoadingFrame loadingFrame ){
//		this.threadController = new ThreadController(loadingFrame,this);
		this.loadingFrame = loadingFrame;
		this.logicGame = logicGame;
	}

	public void run() {
//        game = new GraphicalWorld( logicGame, threadController );
		game = new GraphicalWorld( logicGame, loadingFrame );
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
