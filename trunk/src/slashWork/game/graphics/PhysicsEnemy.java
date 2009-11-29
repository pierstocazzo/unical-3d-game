package slashWork.game.graphics;

import java.awt.Color;

import jmetest.TutorialGuide.ExplosionFactory;
import slashWork.game.core.State;
import slashWork.game.enemyAI.Direction;
import slashWork.game.enemyAI.Movement;
import utils.TextLabel2D;

import com.jme.math.Vector3f;
import com.jme.scene.BillboardNode;
import com.jme.scene.Node;
import com.jmex.effects.particles.ParticleMesh;

public class PhysicsEnemy extends PhysicsCharacter {
	
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
		
		TextLabel2D label = new TextLabel2D( id );
		label.setBackground(Color.GREEN);
		label.setFontResolution( 100 );
		label.setForeground(Color.GREEN);
		BillboardNode bNode = label.getBillboard(0.5f);
		bNode.getLocalTranslation().y += 10;
		bNode.setLocalScale( 5 );
		
		model.attachChild( bNode );
	}
    
	public void update( float time ) {
		super.update(time);
		if( world.getCore().isAlive( id ) == true ) {
			world.getCore().updateEnemyState(id);
			if( world.getCore().getEnemyState(id) == State.ATTACK ) {
				//TODO set the correct animation
				if( world.timer.getTimeInSeconds() - previousTime > 0.1f /*world.getCore().getCharacterWeapon(id).getLoadTime() == 0*/ ) {
					previousTime = world.timer.getTimeInSeconds();
					shoot( world.getCore().getEnemyShootDirection(id) );
				}
			}
		}
	}
	
    @Override
    public void moveCharacter() {
    	float distance;
    	
    	/** The enemy will move only if he is in default state. When he attack or is in alert he rest
    	 */
		if( currentMovement.getDirection() != Direction.REST 
			&& world.getCore().getEnemyState(id) != State.ATTACK
			&& world.getCore().getEnemyState(id) != State.ALERT ) {
			
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
			if( getOnGround() )
				clearDynamics();
		}
    }
    
    @Override
	void lookAtAction() {
    	Vector3f lookAtDirection = new Vector3f( world.getCore().getEnemyShootDirection(id) );
    	if( lookAtDirection.equals( Vector3f.ZERO ) ) {
	        vectorToLookAt.set( this.getModel().getWorldTranslation() );
	        moveDirection.set( currentMovement.getDirection().toVector() );
	        vectorToLookAt.addLocal( moveDirection.negate().x, 0, moveDirection.negate().z );
	        this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
    	} else {
	        vectorToLookAt.set( this.getModel().getWorldTranslation() );
	        vectorToLookAt.addLocal( lookAtDirection.negate().x, 0, lookAtDirection.negate().z );
	        this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
    	}
    	
    	lookAtDirection = null;
	}

	@Override
	public void shoot( Vector3f direction ) {
		world.bulletsCounter = world.bulletsCounter + 1;
		Vector3f bulletPosition = world.getCore().getCharacterPosition(id).add( direction.mult( 5 ) );
		bulletPosition.addLocal( 0, 2, 0 );
		PhysicsBullet bullet = new PhysicsBullet( "bullet" + world.bulletsCounter, world, direction, 
				world.getCore().getCharacterWeapon(id), bulletPosition );
		world.bullets.put( bullet.id, bullet );
	}
	
	@Override
	public void die() {
		ParticleMesh explosion = ExplosionFactory.getExplosion();
		explosion.setOriginOffset( feet.getWorldTranslation().clone() );
		explosion.forceRespawn();
		world.getRootNode().attachChild(explosion);
		
		PhysicsAmmoPackage ammo = new PhysicsAmmoPackage( id + "ammoPack", world, world.getCore().getAmmoPack( id + "ammoPack" ).getPosition() );
		world.ammoPackages.put( ammo.id, ammo );
		super.die();
	}
}
