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
	
	/** PhysicsEnemy Constructor<br>
	 * Create a new graphical enemy and start his movements
	 * 
	 * @param id - (String) the enemy's identifier
	 * @param world - (GraphicalWorld) the graphical world in whitch the enemy is created
	 * @param direction - (Vector3f) the initial direction
	 * @param speed - (int) the enemy's movement's speed
	 * @param mass - (int) the enemy's mass
	 * @param model - (Node) the model to attach to the enemy
	 */
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
		/** move the character in the direction specified in the current movement */
		super.move( currentMovement.getDirection().toVector() );
		
		currentPosition.set( world.getCore().getCharacterPosition(id) );
		currentPosition.setY(0);
		
		/** calculate distance between the current movement start position and
		 *  the current character position
		 */
		distance = currentPosition.distance( initialPosition );
		
		/** If this distance is major than the lenght specified in the movement 
		 *  start the next movement
		 */
		if( distance >= currentMovement.getLength() ) {
			super.clearDynamics();
			initialPosition.set( currentPosition );
			currentMovement = world.getCore().getEnemyNextMovement( id );
		}
	}
}
