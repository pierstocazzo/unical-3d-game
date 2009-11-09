package game.test;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import game.graphics.GraphicalWorld;
import game.logic.LogicWorld;

public class testGame {

	public static void main( String args[] ) {
    	
    	LogicWorld logicGame = new LogicWorld();
    	
    	logicGame.createPlayer( "player", 100, 100, new Vector3f(10,10,10));
    	logicGame.createEnemies( 10 , new Vector3f(40,10,40) );
    	
        GraphicalWorld game = new GraphicalWorld( logicGame );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }

}
