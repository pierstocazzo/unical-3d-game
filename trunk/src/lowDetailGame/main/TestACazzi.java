package lowDetailGame.main;

import lowDetailGame.common.MovementList.MovementType;
import lowDetailGame.core.LogicWorld;
import lowDetailGame.graphics.GraphicalWorld;

import com.jme.app.AbstractGame.ConfigShowMode;

import utils.Loader;

public class TestACazzi {
	public static void main( String[] args ) {
		LogicWorld logicGame = new LogicWorld();
    	logicGame.createPlayer( 100,-1000,-1000 );
    	logicGame.createEnemy( -900,-900 , MovementType.REST );
    	logicGame.createEnemiesGroup( 3, -800, -800 );
//    	logicGame.createEnemiesGroup( 10, new Vector3f( 700, 50, 700 ) );
//    	logicGame.createEnemiesGroup( 10, new Vector3f( 500, 50, 500 ) );
//    	logicGame.createEnemiesGroup( 10, new Vector3f( 400, 50, 400 ) );
        GraphicalWorld game = new GraphicalWorld( logicGame, null );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow, Loader.load("game/data/images/splashGame.jpg") );
        game.start();
	}
}
