package slashWork.game.graphics;

import slashWork.game.enemyAI.Direction;
import slashWork.game.enemyAI.Movement;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

public class PhysicsEnemy extends PhysicsCharacter {
	
	float distance;
	Movement currentMovement;
	
	/** Helper movement vectors */
	private Vector3f currentPosition;
	private Vector3f initialPosition;
	
	/** utility Vector used for the look at action */
	Vector3f vectorToLookAt;
	
	/** PhysicsEnemy Constructor<br>
	 * Create a new graphical enemy and start his movements
	 * 
	 * @param id - (String) the enemy's identifier
	 * @param world - (GraphicalWorld) the graphical world in whitch the enemy is created
	 * @param speed - (int) the enemy's movement's speed
	 * @param mass - (int) the enemy's mass
	 * @param model - (Node) the model to attach to the enemy
	 */
	public PhysicsEnemy( String id, GraphicalWorld world, float speed, float mass, Node model ) {
		super( id, world, speed, mass, model );
		
		currentMovement = world.getCore().getEnemyNextMovement( id );
		
		currentPosition = new Vector3f();
		initialPosition = new Vector3f();
		
		initialPosition.set( world.getCore().getCharacterPosition(id) );
		initialPosition.setY(0);
		
		vectorToLookAt = new Vector3f();
		
		/** initial look at action */
		vectorToLookAt.set( this.getModel().getWorldTranslation() );
		moveDirection.set( currentMovement.getDirection().toVector() );
		vectorToLookAt.addLocal( moveDirection.negate().x, 0, moveDirection.negate().z );
		this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
	}
    
    @Override
    public void moveCharacter() {
		if( currentMovement.getDirection() != Direction.REST ) {
			setMovingForward( true );
			/** move the character in the direction specified in the current movement */
			super.move( currentMovement.getDirection().toVector() );
			        
			/** update the utility vector currentPosition */
			currentPosition.set( world.getCore().getCharacterPosition(id) );
			currentPosition.setY(0);
			
			/** calculate distance between the current movement's start position and
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
		} else {
			setRest( true );
		}
    }
    
	void lookAtAction() {
        vectorToLookAt.set( this.getModel().getWorldTranslation() );
        moveDirection.set( currentMovement.getDirection().toVector() );
        vectorToLookAt.addLocal( moveDirection.negate().x, 0, moveDirection.negate().z );
        this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
	}
}
