package slashWork.game.test;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import slashWork.game.core.LogicWorld;
import slashWork.game.enemyAI.MovementList.MovementType;
import slashWork.game.graphics.GraphicalWorld;

public class testGame {

	public static void main( String args[] ) {
    	
    	LogicWorld logicGame = new LogicWorld();
    	
    	logicGame.createPlayer( 100, new Vector3f( 160, 10, 160 ) );
    	logicGame.createEnemy( new Vector3f( 100, 10, 100 ), MovementType.REST );
    	logicGame.createEnemiesGroup( 3, new Vector3f( 50, 10, 50 ) );
    	logicGame.createEnemiesGroup( 7, new Vector3f( 220, 10, 220 ) );
//    	logicGame.createEnemiesGroup( 5, new Vector3f( 80, 10, 80 ) );
//    	
//    	logicGame.createEnemiesGroup( 20, new Vector3f( 1000, 10, 1000 ) );
    	logicGame.createEnergyPackages( 10, 640, 640 );
    	
        GraphicalWorld game = new GraphicalWorld( logicGame, 4096, 4096 );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.start();
    }
}
