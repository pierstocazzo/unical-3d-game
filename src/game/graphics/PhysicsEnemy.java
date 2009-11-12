package game.graphics;

import game.enemyAI.Movement;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class PhysicsEnemy extends PhysicsCharacter {
	
	float distance;
	Movement currentMovement;
	
	/** Helper movement vector */
	private Vector3f currentPosition;
	private Vector3f initialPosition;
	
	public PhysicsEnemy( String id, GraphicalWorld world, Vector3f direction, float speed, float mass, Node model ) {
		super( id, world, direction, speed, mass, model );
		currentMovement = world.getCore().getEnemyNextMovement( id );
		
		currentPosition = new Vector3f();

		initialPosition = new Vector3f();
		initialPosition.set( world.getCore().getCharacterInitialPosition(id) );
		initialPosition.setY(0);
	}
	
        @Override
	public void update( float time ) {
		super.update(time);
		super.move( currentMovement.getDirection().toVector() );
		
		currentPosition.set( world.getCore().getCharacterPosition(id) );
		currentPosition.setY(0);
		
		distance = currentPosition.distance( initialPosition );
		
		if( distance >= currentMovement.getLength() ) {
			super.clearDynamics();
			initialPosition.set( currentPosition );
			currentMovement = world.getCore().getEnemyNextMovement( id );
		}
	}
}
