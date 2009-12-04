package game.main;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import game.core.LogicWorld;
import game.enemyAI.MovementList.MovementType;
import game.graphics.GraphicalWorld;

public class TestACazzi {
	public static void main( String[] args ) {
		LogicWorld logicGame = new LogicWorld();
    	logicGame.createPlayer( 100, new Vector3f( 700, 60, 700 ) );
    	logicGame.createEnemy( new Vector3f( 800, 50, 800 ), MovementType.REST );
    	logicGame.createEnemiesGroup( 10, new Vector3f( 800, 50, 900 ) );
//    	logicGame.createEnemiesGroup( 4, new Vector3f( 220, 50, 220 ) );
        GraphicalWorld game = new GraphicalWorld( logicGame, new ThreadController() );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.isThread = false;
        game.start();
	}
}
