package game.graphics;

import game.enemyAI.Direction;
import game.enemyAI.Movement;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
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
		vectorToLookAt.addLocal( currentMovement.getDirection().toVector().x, 0, currentMovement.getDirection().toVector().z );
		this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
	}
	
    @Override
	public void update( float time ) {
		super.update(time);
		
		/** move the character in the direction specified in the current movement */
		super.move( currentMovement.getRotationalAxis() );
		        
		/** update the utility direction currentPosition */
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
	
//			DON'T WORK WELL
//			/** 
//			 *  Enemy look at action. 
//			 */
//	        vectorToLookAt.set( this.getModel().getWorldTranslation() );
//	        vectorToLookAt.addLocal( currentMovement.getDirection().getDirectionVector().x, 0, currentMovement.getDirection().getDirectionVector().z );
//	        this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
			
//			LET'S USE THE QUATERNION... :-)
			float angle = currentMovement.getDirection().toVector().angleBetween( vectorToLookAt );
			if( currentMovement.getDirection() == Direction.RIGHT ) {
				angle = FastMath.PI*3/2;
			}
			model.setLocalRotation( new Quaternion().fromAngleAxis( angle, Vector3f.UNIT_Y ) );
		}
		
		/** 
		 *  Set the correct animation and control variables status
		 */
		if( currentMovement.getDirection() != Direction.REST ) 
			setMovingForward( true );
		else 
			setRest( true );
	}
}
