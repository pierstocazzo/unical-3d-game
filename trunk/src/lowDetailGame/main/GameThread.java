package lowDetailGame.main;

import lowDetailGame.common.MovementList.MovementType;
import lowDetailGame.core.LogicWorld;
import lowDetailGame.graphics.GraphicalWorld;
import lowDetailGame.menu.LoadingFrame;

public class GameThread implements Runnable {

	public LogicWorld logicGame;
	public GraphicalWorld game;
	public LoadingFrame loadingFrame;
	
	public GameThread( LoadingFrame loadingFrame ) {
		this.loadingFrame = loadingFrame;
		logicGame = new LogicWorld();
    	logicGame.createPlayer( 2000, -1050, -1100 );
    	logicGame.createEnemy( -900, -900, MovementType.REST );
//    	logicGame.createEnemiesGroup( 5, -700, -700 );
//    	logicGame.createEnemiesGroup( 5, -400, -400 );
//    	logicGame.createEnemiesGroup( 5, -200, -200 );
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
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
