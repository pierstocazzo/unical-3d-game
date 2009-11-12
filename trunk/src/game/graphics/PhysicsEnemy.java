package game.graphics;

import game.enemyAI.Movement;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class PhysicsEnemy extends PhysicsCharacter {
	
	float distance;
	Movement currentMovement;
	
	/** vettori debug */
	private Vector3f currentPosition;
	private Vector3f initPosition;
	
	public PhysicsEnemy( String id, GraphicalWorld world, Vector3f direction, float speed, float mass, Node model ) {
		super( id, world, direction, speed, mass, model );
		currentMovement = world.getCore().getEnemyNextMovement( id );
		
		currentPosition = new Vector3f();
		initPosition = new Vector3f();
		initPosition.set( world.getCore().getEnemyInitialPosition(id) );
		initPosition.setY(0);
	}
	
	public void update( float time ) {
		super.update(time);
		super.move( currentMovement.getDirection().toVector() );
		
		currentPosition.set( world.getCore().getCharacterPosition(id) );
		currentPosition.setY(0);
		
		distance = currentPosition.distance( initPosition );	
		
		if( distance >= currentMovement.getLength() ) {
			super.clearDynamics();
			
//			world.getCore().setEnemyInitialPosition( id, feet.getWorldTranslation() );
			initPosition.set( currentPosition );
									
			System.out.println("*********\nRAGGIUNTA DISTANZA: " + distance + " di " + currentMovement.getLength());
			System.out.println( "Current Position: " + currentPosition.toString() + 
					"\nInitPosition: " + initPosition.toString() );
			
			currentMovement = world.getCore().getEnemyNextMovement( id );
			
			System.out.println( "nuovo movimento: " + currentMovement.getDirection().toVector().toString() );
		}
	}
}
