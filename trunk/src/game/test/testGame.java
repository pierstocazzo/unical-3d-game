package game.test;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import game.core.LogicWorld;
import game.graphics.GraphicalWorld;

public class testGame {

	public static void main( String args[] ) {
    	
    	LogicWorld logicGame = new LogicWorld();
    	
    	logicGame.createPlayer( "player1", 100, 100, new Vector3f(10,10,10));
    	logicGame.createEnemies( 10 , new Vector3f(40,10,40) );
    	
        GraphicalWorld game = new GraphicalWorld( logicGame );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }

}
