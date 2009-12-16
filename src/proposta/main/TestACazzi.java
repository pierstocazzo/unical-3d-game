package proposta.main;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import proposta.core.LogicWorld;
import proposta.enemyAI.MovementList.MovementType;
import proposta.graphics.GraphicalWorld;
import utils.Loader;

public class TestACazzi {
	public static void main( String[] args ) {
		LogicWorld logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, new Vector3f( -1000, 60, -1000 ) );
    	logicGame.createEnemy( new Vector3f( -900, 20, -900 ), MovementType.REST );
    	logicGame.createEnemiesGroup( 3, -800, -800 );
//    	logicGame.createEnemiesGroup( 10, new Vector3f( 700, 50, 700 ) );
//    	logicGame.createEnemiesGroup( 10, new Vector3f( 500, 50, 500 ) );
//    	logicGame.createEnemiesGroup( 10, new Vector3f( 400, 50, 400 ) );
        GraphicalWorld game = new GraphicalWorld( logicGame, new ThreadController() );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow, Loader.load("game/data/images/splashGame.jpg") );
        game.isThread = false;
        game.start();
	}
}
