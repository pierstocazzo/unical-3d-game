package game.test;

//import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import game.core.LogicWorld;
import game.enemyAI.MovementList.MovementType;
import game.graphics.GraphicalWorld;

public class testGame {

	public static void main( String args[] ) {
    	
    	LogicWorld logicGame = new LogicWorld();
    	
    	logicGame.createPlayer( 100, new Vector3f( 160, 10, 160 ) );
    	logicGame.createEnemy( new Vector3f( 140, 10, 140 ), MovementType.SMALL_PERIMETER );
    	logicGame.createEnemies( 3, new Vector3f( 50, 10, 50 ) );
    	
        GraphicalWorld game = new GraphicalWorld( logicGame );
//        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }
}
