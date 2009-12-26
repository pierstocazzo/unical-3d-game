package game.main;

import game.core.LogicWorld;
import game.enemyAI.MovementList.MovementType;
import game.graphics.GraphicalWorld;
import game.menu.LoadingFrame;

public class GameThread implements Runnable {

	public LogicWorld logicGame;
	public GraphicalWorld game;
	public LoadingFrame loadingFrame;
	
	public GameThread( LoadingFrame loadingFrame ) {
		this.loadingFrame = loadingFrame;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, -1000, -1000 );
    	logicGame.createEnemy( -900, -900, MovementType.REST );
//    	logicGame.createEnemiesGroup( 3, 50, 50 );
//    	logicGame.createEnemiesGroup( 4, 220, 220 );
//    	logicGame.createEnemiesGroup( 5, 250, 250 );
//    	logicGame.createEnemiesGroup( 20, 800, 800 );
    	
//    	logicGame.createEnergyPackages( 20, 129*20, 129*20 );
	}
	
	public GameThread( LogicWorld logicGame, LoadingFrame loadingFrame ){
		this.loadingFrame = loadingFrame;
		this.logicGame = logicGame;
	}

	public void run() {
		game = new GraphicalWorld( logicGame, loadingFrame );
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
