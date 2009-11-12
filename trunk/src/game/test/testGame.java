package game.test;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import game.core.LogicWorld;
import game.enemyAI.MovementList;
import game.enemyAI.MovementList.MovementType;
import game.graphics.GraphicalWorld;

public class testGame {

	public static void main( String args[] ) {
    	
    	LogicWorld logicGame = new LogicWorld();
    	
    	logicGame.createPlayer( "player1", 100, new Vector3f(10,10,10));
    	logicGame.createEnemies( 1 , new Vector3f(140,30,140), new MovementList( MovementType.LARGE_PERIMETER ) );
    	
        GraphicalWorld game = new GraphicalWorld( logicGame );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }
}
