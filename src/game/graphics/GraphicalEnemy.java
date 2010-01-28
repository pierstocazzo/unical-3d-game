package game.graphics;

import game.common.GameTimer;
import game.common.State;
import game.graphics.GameAnimationController.Animation;
import jmetest.TutorialGuide.ExplosionFactory;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.material.Material;

public class GraphicalEnemy extends GraphicalCharacter  {
	
	/** utility Vector used for the look at action */
	Vector3f vectorToLookAt;
	
	/** the physics character material */
	Material characterMaterial;
  
    /** The feet of the character: a Sphere that permit the movements by rotating 
     * (rotation controlled by a RotationalJointAxis) */
	DynamicPhysicsNode feet;

    /** the joint that connect the feet to the body */
    Joint feetToBodyJoint;
    
    /** the joint that permit the rotation of the feet sphere */
    RotationalJointAxis rotationalAxis;

    /** movements speed */
    float speed;
    
    /** physics character mass */
    float mass;

    /** true if the character is on the ground */
	boolean onGround;

	/** the previous movement's direction */
	Vector3f previousMoveDirection;
    	
	
    /** PhysicsEnemy Constructor<br>
	 * Create a new graphical enemy and start his movements
	 * 
	 * @param id - (String) the enemy's identifier
	 * @param world - (GraphicalWorld) the graphical world in whitch the enemy is created
	 * @param speed - (int) the enemy's movement's speed
	 * @param mass - (int) the enemy's mass
	 * @param model - (Node) the model to attach to the enemy
	 */
	public GraphicalEnemy( String id, GraphicalWorld world, float speed, float mass, Node model ) {
	
		this.id = id;
		
		this.world = world;
	    
	    characterNode = new Node("character node");
	    body = world.getPhysicsSpace().createDynamicNode();
	    feet = world.getPhysicsSpace().createDynamicNode();
	    feetToBodyJoint = world.getPhysicsSpace().createJoint();
	    rotationalAxis = feetToBodyJoint.createRotationalAxis();
	
	    this.speed = speed;
	    this.mass = mass;
	    this.model = model;
	    
	    characterMaterial = new Material("Character Material");
	    characterMaterial.setDensity(100f);
	    MutableContactInfo contactDetails = new MutableContactInfo();
	    contactDetails.setBounce(0);
	    contactDetails.setMu( 100 );
	    contactDetails.setMuOrthogonal( 10 );
	    contactDetails.setDampingCoefficient(0);
	    characterMaterial.putContactHandlingDetails( Material.DEFAULT, contactDetails );
	
	    createPhysics();
	    contactDetection();
	    
	    previousShootTime = 0;
		
		vectorToLookAt = new Vector3f();
		
		previousMoveDirection = world.getCore().getMoveDirection(id).clone();
	}

	/** Function <code>createPhysics</code> <p>
	 * 
	 * Create the physics geometry of a character: <br>
	 *  - <i>feet</i> node, a Sphere used for the movements (by rotation) <br>
	 *  - <i>body</i> node, a Capsule placed upon the feet that contains the character model <br>
	 *  - <i>rotationalAxis</i>, the axis that permit the movement (by rotating the sphere) <br>
	 *  <p>
	 *  Also initialize animation controller and set the <i>"idle"</i> animation
	 */
	void createPhysics() {
		// Create the feet
		PhysicsSphere feetGeometry = feet.createSphere("feet geometry");
		feetGeometry.setLocalScale(2);

		feet.setMaterial(characterMaterial);
		feet.computeMass();

		// Append feet to main Character Node
		characterNode.attachChild(feet);

		// Create the body
		PhysicsCapsule bodyGeometry = body.createCapsule("body geometry");
		bodyGeometry.setLocalScale(2);
		bodyGeometry.setLocalTranslation(0,5,0);
		// Set UP the orientation of the Body
		Quaternion quaternion = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		bodyGeometry.setLocalRotation(quaternion);
		// Setting up the body
		body.setAffectedByGravity(false);
		body.computeMass();
		body.attachChild( model );

		// Append body to main Character Node
		characterNode.attachChild(body);

		// Create the joint
		feetToBodyJoint.attach( body, feet );
		rotationalAxis.setRelativeToSecondObject(true);
		rotationalAxis.setAvailableAcceleration(0f);
		rotationalAxis.setDesiredVelocity(0f);

		// Set default mass
		feet.setMass(mass);

		/** initialize the animation */ 
		animationController = new GameAnimationController( model.getController(0) );
		setOnGround( false );
	}

	/** Function <code>update</code> <br>
	 * Update the physics character 
	 * @param time
	 */
	public void update( float time ) {
	    if( world.getCore().isAlive( id ) == true ) {
			if( enabled ) {
				feet.setActive(true);
				body.setActive(true);
				
				preventFall();
				onGround = false;
			    contactDetect.update(time);
			    
				world.getCore().updateState(id);
				if( world.getCore().getState(id) == State.ATTACK || 
					world.getCore().getState(id) == State.FINDATTACK || 
					world.getCore().getState(id) == State.GUARDATTACK ) {
					animationController.runAnimation( Animation.SHOOT );
					shooting = true;
					if( GameTimer.getTimeInSeconds() - previousShootTime > world.getCore().getWeapon(id).getLoadTime() ) {
						previousShootTime = GameTimer.getTimeInSeconds();
						shoot( world.getCore().getShootDirection(id) );
					}
				} else {
					shooting = false;
				}
			    
			    moveCharacter();
			    
			    // update core
			    world.getCore().setPosition( id, feet.getWorldTranslation() );
			} else {
				feet.setActive(false);
				body.setActive(false);
			}
	    } else {
	    	die();
	    }
	}

	public void moveCharacter() {
		Vector3f moveDirection = world.getCore().getMoveDirection( id );
		if( !moveDirection.equals( Vector3f.ZERO ) ) {
			if( Math.abs(moveDirection.angleBetween( previousMoveDirection )) >= (FastMath.DEG_TO_RAD * 2) ) {
				clearDynamics();
				previousMoveDirection.set( moveDirection );
				setMoving(false);
			} else {
//				if( feet.getLinearVelocity( null ).length() < 1 )
					move( moveDirection );
				setMoving( true );
			}
			lookAtAction( moveDirection );
		} else {
			if( getOnGround() ) 
				clearDynamics();
			setMoving( false );
			lookAtAction( null );
		}
	}

	void lookAtAction( Vector3f direction ) {
		if( direction == null ) {
			if( world.getCore().getState(id) == State.ATTACK || 
				world.getCore().getState(id) == State.FINDATTACK ||
				world.getCore().getState(id) == State.GUARDATTACK ) {
				Vector3f lookAtDirection = new Vector3f( world.getCore().getShootDirection(id) );
				vectorToLookAt.set( model.getWorldTranslation() );
				vectorToLookAt.addLocal( lookAtDirection.x, 0, lookAtDirection.z );
				model.lookAt( vectorToLookAt, Vector3f.UNIT_Y );
			}
		} else {
			vectorToLookAt.set( model.getWorldTranslation() );
			vectorToLookAt.addLocal( direction.x, 0, direction.z );
			model.lookAt( vectorToLookAt, Vector3f.UNIT_Y );
		}
	}

	public void shoot( Vector3f direction ) {
			Vector3f bulletPosition = world.getCore().
						getPosition(id).add( direction.mult( 10 ) );
			bulletPosition.addLocal( 0, 4, 0 );
			Bullet bullet = new Bullet( id, world, 
					world.getCore().getWeapon(id), bulletPosition );
			world.bullets.add( bullet );
			bullet.shoot(direction);
			
			world.shoot( feet.getWorldTranslation() );
		}

	/** Function <code>move</code> <br>
	 * Move the character in this direction (only if the character is on the ground)
	 * 
	 * @param direction - (Vector3f) the direction of the character's movement
	 */
	public void move( Vector3f direction ) { 
		try{
			// the rotational axis is orthogonal to the direction and
			// to the Y axis. It's calculated using cross product
			rotationalAxis.setDirection( direction.cross( new Vector3f(0,1,0) ) );
			rotationalAxis.setAvailableAcceleration( 100*speed );
			rotationalAxis.setDesiredVelocity( speed );
		} catch( Exception e ) {
			rotationalAxis.setDesiredVelocity(0f);
		}
	}
	
	public void die() {
        ParticleMesh exp = ExplosionFactory.getSmallExplosion(); 
		exp.setOriginOffset( feet.getWorldTranslation().clone() );
        world.getRootNode().attachChild(exp);
        exp.forceRespawn();
        
		GraphicalAmmoPackage ammoPack = new GraphicalAmmoPackage( id, 
				world, feet.getWorldTranslation() );
		world.ammoPackages.add( ammoPack );
		
    	clearDynamics();
    	
    	world.explode( feet.getWorldTranslation() );

    	body.detachAllChildren();
    	feet.detachAllChildren();
    	feet.delete();
    	body.delete();
    	world.getRootNode().detachChild( characterNode );
    	characterNode = null;
    	model = null;
	}

	/** Function <code>preventFall</code> <br>
	 *  prevent the falling of the character body due to physics
	 */
	public void preventFall() {
	    Quaternion quaternion = body.getLocalRotation();
	    Vector3f[] axes = new Vector3f[3];
	    quaternion.toAxes(axes);
	
	    quaternion.fromAxes(axes[0], Vector3f.UNIT_Y, axes[2]);
	    body.setLocalRotation(quaternion);
	    body.setAngularVelocity(Vector3f.ZERO);
	    body.updateWorldVectors();
	}

	/** Function <code>contactDetection</code> <p>
     * Detect when the player collide to the ground, and set the control variable onGround to true
     */
	void contactDetection() {
        SyntheticButton playerCollisionEventHandler = feet.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {
			public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == world.getGround() || contactInfo.getNode2() == world.getGround() ) {
                    onGround = true;
                }
            }
        };
        
        contactDetect.addAction( collisionAction, playerCollisionEventHandler, false );
    }

	/** Function <code>clearDynamics</code> <br>
     * Reset all dynamics of the physics character
     * and set him to rest (with the rest animation) 
     */
    public void clearDynamics() {
        feet.clearDynamics();
        rotationalAxis.setDesiredVelocity(0);
        rotationalAxis.setAvailableAcceleration(0);
        
        body.clearDynamics();
        
    	if( !shooting ) {
    		animationController.runAnimation( Animation.IDLE );
    	}
    }

	/** Function <code>getCharacterNode</code> <br>
     *  Return the main Node of the physics character
     * 
     * @return the main Node of the physics character
     */
    public Node getCharacterNode() {
        return characterNode;
    }

    /** Function <code>getCharacterBody</code> <br>
     *  Return the body Node of the physics character
     * 
     * @return the body Node of the physics character (a capsule)
     */
    public DynamicPhysicsNode getCharacterBody() {
        return body;
    }

    /** Function <code>getCharacterFeet</code> <br>
     *  Return the feet Node of the physics character 
     * 
     * @return the feet Node of the physics character (a sphere)
     */
    public DynamicPhysicsNode getCharacterFeet() {
        return feet;
    }

	/** Function <code>getModel</code> <br>
	 * 
	 * @return (Node) the character 3d model
	 */
    public Node getModel() {
        return model;
    }

	/** Function <code>getOnGround</code> <br>
	 * 
	 * @return <b>true</b> if the character is on the ground
	 */
	public boolean getOnGround() {
		return onGround;
	}
	
	/** Function <code>getAnimationController</code>
	 * 
	 * @return the animation controller
	 */
    public GameAnimationController getAnimationController() {
		return animationController;
	}
	
	/** Function <code>setOnGround</code> <p>
	 * If the boolean parameter is true, set the character's status to onGround
	 * @param onGround - (boolean)
	 */
	public void setOnGround( boolean onGround ) {
		this.onGround = onGround;
	}

	/** Function <code>setMovingForward</code> <p>
	 * If the boolean parameter is true, set the character's status to moving forward and activate the right animation
	 * @param moving - (boolean)
	 */
	public void setMoving( boolean moving ) {
		if( moving )
			animationController.runAnimation( Animation.WALK );
		world.getCore().setMoving( id, moving );
	}
}
