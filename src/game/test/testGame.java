package game.test;

import com.jme.app.AbstractGame.ConfigShowMode;
import game.graphics.GraficalWorld;
import game.logic.LogicWorld;

public class testGame {

	public static void main( String args[] ) {
    	
    	LogicWorld logicGame = new LogicWorld();
    	
    	logicGame.createPlayer( "player", 100, 100 );
    	logicGame.createEnemies( 10 );
    	
        GraficalWorld game = new GraficalWorld( logicGame );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }

}
