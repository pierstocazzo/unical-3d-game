package slashWork.game.main;

import slashWork.game.core.LogicWorld;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.jme.math.Vector3f;

import slashWork.game.enemyAI.MovementList.MovementType;
import slashWork.game.graphics.GraphicalWorld;

public class TestACazzi {
	public static void main( String[] args ) {
		LogicWorld logicGame = new LogicWorld();
		
    	logicGame.createPlayer( 100, new Vector3f( 500, 10, 500 ) );

		logicGame.createEnemy( new Vector3f( 400, 10, 400 ), MovementType.HORIZONTAL_SENTINEL );
    	//logicGame.createEnemy( new Vector3f( 400, 10, 400 ), MovementType.REST );
    	//logicGame.createEnemiesGroup( 10, new Vector3f( 200, 10, 200 ) );
    	//logicGame.createEnemiesGroup( 4, new Vector3f( 300, 10, 300 ) );
    	
    	
        GraphicalWorld game = new GraphicalWorld( logicGame, null );
        game.setConfigShowMode( ConfigShowMode.AlwaysShow );
        game.isThread = false;
        game.start();
	} 
}
