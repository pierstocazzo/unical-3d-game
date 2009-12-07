package proposta.graphics;

import proposta.core.State;
import proposta.enemyAI.Direction;
import proposta.enemyAI.Movement;

import game.graphics.CustomAnimationController;
import game.graphics.CustomAnimationController.Animation;

import java.awt.Color;

import jmetest.TutorialGuide.ExplosionFactory;
import utils.Loader;
import utils.TextLabel2D;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.BillboardNode;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.material.Material;

public class PhysicsEnemy extends PhysicsCharacter  {
	
	Movement currentMovement;
	
	/** Helper movement vectors */
	private Vector3f currentPosition;
	private Vector3f initialPosition;
	
	/** utility Vector used for the look at action */
	Vector3f vectorToLookAt;
	
	
	/** the character identifier */
	String id;
	
	/** the physics character material */
	Material characterMaterial;
  
    /** the main node of the character */
    Node characterNode;
    
    /** The feet of the character: a Sphere that permit the movements by rotating 
     * (rotation controlled by a RotationalJointAxis) */
	DynamicPhysicsNode feet;

	/** The body of the character: a Capsule, placed upon the feet, that contains the model */
    DynamicPhysicsNode body; 
    
    /** 3d model applied to the character */
    Node model;
    
	/** animation controller */
	CustomAnimationController animationController;
	
	/** the graphical world in which the character live */
    GraphicalWorld world;
    
    /** the joint that connect the feet to the body */
    Joint feetToBodyJoint;
    
    /** the joint that permit the rotation of the feet sphere */
    RotationalJointAxis rotationalAxis;

    /** movements speed */
    float speed;
    
    /** physics character mass */
    float mass;

    /** the current movement's direction */
    Vector3f moveDirection;
    
    /** the force applied to the character to jump */
    Vector3f jumpVector;
    
    /** an util handler that detect the contact between the character and the ground */
    InputHandler contactDetect = new InputHandler();
    
    /** Utility quaternion */
    Quaternion quaternion;
    
    /** character firstPerson view */
    boolean firstPerson = false;
    
    /** true if the character is shooting */
    boolean shooting = false;

	float previousTime;

    	
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
	
		this.id = id;
		
		this.world = world;
	    
	    characterNode = new Node("character node");
	    body = world.getPhysicsSpace().createDynamicNode();
	    feet = world.getPhysicsSpace().createDynamicNode();
	    feetToBodyJoint = world.getPhysicsSpace().createJoint();
	    rotationalAxis = feetToBodyJoint.createRotationalAxis();
	
	    this.moveDirection = new Vector3f( Vector3f.ZERO );
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
	
	    quaternion = new Quaternion();
	    
	    createPhysics();
	    contactDetection();
	    
	    previousTime = 0;
		
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
		    quaternion.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		    bodyGeometry.setLocalRotation(quaternion);
		    // Setting up the body
		    body.setAffectedByGravity(false);
		    body.computeMass();
		    body.attachChild( model );
		    
		    // Append body to main Character Node
		    characterNode.attachChild(body);
		
		    // Create the joint
	//	    rotationalAxis.setDirection(moveDirection);
		    feetToBodyJoint.attach( body, feet );
		    rotationalAxis.setRelativeToSecondObject(true);
		    rotationalAxis.setAvailableAcceleration(0f);
		    rotationalAxis.setDesiredVelocity(0f);
		
		    // Set default mass
		    feet.setMass(mass);
		
		    // Set the jump direction
		    jumpVector = new Vector3f(0, mass*1000, 0);
		    
		    /** initialize the animation */ 
			animationController = new CustomAnimationController( model.getController(0) );
			
	        rest();
	        setRest( false );
	        setOnGround( false );
		}

	/** Function <code>update</code> <br>
	 * Update the physics character 
	 * @param time
	 */
	public void update( float time ) {
	    if( world.getCore().isAlive( id ) == true ) {
			preventFall();
		
		    contactDetect.update(time);
		    
		    body.rest();
		    lookAtAction();
		    
			world.getCore().updateEnemyState(id);
			if( world.getCore().getEnemyState(id) == State.ATTACK ) {
				animationController.runAnimation( Animation.SHOOT );
				shooting = true;
				if( world.timer.getTimeInSeconds() - previousTime > 0.2f /*world.getCore().getCharacterWeapon(id).getLoadTime() == 0*/ ) {
					previousTime = world.timer.getTimeInSeconds();
					shoot( world.getCore().getEnemyShootDirection(id) );
				}
			} else {
				shooting = false;
			}
		    
		    moveCharacter();
		    
		    // update core
		    world.getCore().setCharacterPosition( id, feet.getWorldTranslation() );
	    } else {
	    	die();
	    }
	}

	public void moveCharacter() {
		float distance;
		
		/** The enemy will move only if he is in default state. When he attack or is in alert he rest
		 */
		if( currentMovement.getDirection() != Direction.REST 
			&& world.getCore().getEnemyState(id) != State.ATTACK
			&& world.getCore().getEnemyState(id) != State.ALERT ) {
			
			setMovingForward( true );
			/** move the character in the direction specified in the current movement */
			move( currentMovement.getDirection().toVector() );
			        
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
				clearDynamics();
				
				initialPosition.set( currentPosition );
				currentMovement = world.getCore().getEnemyNextMovement( id );
			}
		} else {
			if( getOnGround() )
				clearDynamics();
		}
	}

	void lookAtAction() {
		Vector3f lookAtDirection = new Vector3f( world.getCore().getEnemyShootDirection(id) );
		if( lookAtDirection.equals( Vector3f.ZERO ) ) {
	        vectorToLookAt.set( this.getModel().getWorldTranslation() );
	        moveDirection.set( currentMovement.getDirection().toVector() );
	        vectorToLookAt.addLocal( moveDirection.x, 0, moveDirection.z );
	        this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
		} else {
	        vectorToLookAt.set( this.getModel().getWorldTranslation() );
	        vectorToLookAt.addLocal( lookAtDirection.x, 0, lookAtDirection.z );
	        this.getModel().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
		}
		
		lookAtDirection = null;
	}

	public void shoot( Vector3f direction ) {
			world.bulletsCounter = world.bulletsCounter + 1;
			Vector3f bulletPosition = world.getCore().
						getCharacterPosition(id).add( direction.mult( 5 ) );
			bulletPosition.addLocal( 0, 2, 0 );
			PhysicsBullet bullet = new PhysicsBullet( "bullet" + world.bulletsCounter, world, 
					world.getCore().getCharacterWeapon(id), bulletPosition );
			world.bullets.put( bullet.id, bullet );
			bullet.shoot(direction);
			
	//		world.shoot.setWorldPosition( feet.getWorldTranslation() );
	//		world.shoot.play();
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
			rotationalAxis.setDirection( moveDirection.cross( Vector3f.UNIT_Y ) );
			rotationalAxis.setAvailableAcceleration( 30*speed );
			rotationalAxis.setDesiredVelocity( speed );
		} catch( Exception e ) {
			rotationalAxis.setDesiredVelocity(0f);
		}
	}

	public void hide( boolean b ) {
		if( b ) 
			body.detachChild( model );
		else
			body.attachChild( model );
	}
	
	public void die() {
        ParticleMesh exp = ExplosionFactory.getSmallExplosion(); 
		exp.setOriginOffset( feet.getWorldTranslation().clone() );
//		  non si sa come ma da problemi...
//        exp.setLocalScale( 0.3f); 
        world.getRootNode().attachChild(exp);
        exp.forceRespawn();
		
		PhysicsAmmoPackage ammo = new PhysicsAmmoPackage( id + "ammoPack", 
				world, feet.getWorldTranslation().clone().add( new Vector3f(0,15,0) ) );
		world.ammoPackages.put( ammo.id, ammo );
		
    	clearDynamics();
//    	world.explosion.setWorldPosition( feet.getWorldTranslation() );
//    	world.explosion.setVolume( 5 );
//    	world.explosion.play();

    	body.detachAllChildren();
    	feet.detachAllChildren();
    	feet.delete();
    	body.delete();
    	world.getRootNode().detachChild( characterNode );
    	
    	world.characters.remove( id );
	}

	/** Function <code>preventFall</code> <br>
	 *  prevent the falling of the character body due to physics
	 */
	public void preventFall() {
	    quaternion = body.getLocalRotation();
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
                    world.getCore().setCharacterOnGround( id, true );
                }
            }
        };
        
        contactDetect.addAction( collisionAction, playerCollisionEventHandler, false );
    }

    /** Function <code>rest</code> <p>
	 * Set the character to the rest status
	 */
	public void rest() {
		world.getCore().characterRest(id);
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
        
    	if( animationController.getCurrentAnimation() != Animation.IDLE && !shooting ) {
    		// activate the animation "idle"
    		animationController.runAnimation( Animation.IDLE );
    	}
    }

    /**
	 * @return the moveDirection
	 */
	public Vector3f getMoveDirection() {
		return moveDirection;
	}

	/**
	 * @param moveDirection the moveDirection to set
	 */
	public void setMoveDirection( Vector3f moveDirection ) {
		this.moveDirection.set( moveDirection );
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

	/** Function <code>getModel</code> <p>
	 * 
	 * @return (float) the character's movements speed
	 */
    public float getSpeed() {
        return speed;
    }
    
	/** Function <code>setSpeed</code> <br>
	 * Set the character's movements speed
	 * @param speed - (Float) the movement speed
	 */
    public void setSpeed( float speed ) {
        this.speed = speed;
    }

    /** Function <code>getJumpVector</code> <br>
     * 
     * @return (Vector3f) the jump vector
     */
    public Vector3f getJumpVector() {
        return jumpVector;
    }

	/** Function <code>getModel</code> <br>
	 * 
	 * @return (Node) the character 3d model
	 */
    public Node getModel() {
        return model;
    }

	/** Function <code>setModel</code> <br>
	 * Apply this model to the character
	 * 
	 * @param model - (Node) the model to apply to the character
	 */
    public void setModel( Node model ) {
        this.model = model;
    }

	/** Function <code>getOnGround</code> <p>
	 * 
	 * @return <b>true</b> if the character is in rest status
	 */
	public boolean getRest() {
		return world.getCore().getCharacterRest(id);
	}

	/** Function <code>getOnGround</code> <br>
	 * 
	 * @return <b>true</b> if the character is on the ground
	 */
	public boolean getOnGround() {
		return world.getCore().getCharacterOnGround(id);
	}
	
	/** Function <code>getAnimationController</code>
	 * 
	 * @return the animation controller
	 */
    public CustomAnimationController getAnimationController() {
		return animationController;
	}
	
	/** Function <code>setOnGround</code> <p>
	 * If the boolean parameter is true, set the character's status to onGround
	 * @param onGround - (boolean)
	 */
	public void setOnGround( boolean onGround ) {
		world.getCore().setCharacterOnGround( id, onGround );
	}
	
	/** Function <code>setRest</code> <p>
	 * If the boolean parameter is true, set the character's status to rest
	 * @param rest - (boolean)
	 */
	public void setRest( boolean rest ) {		
		world.getCore().setCharacterRest( id, rest );
	}

	/** Function <code>setMovingBackward</code> <p>
	 * If the boolean parameter is true, set the character's status to moving backward and activate the right animation
	 * @param movingBackward - (boolean)
	 */
	public void setMovingBackward( boolean movingBackward ) {
		world.getCore().setCharacterMovingBackward( id, movingBackward );
	}

	/** Function <code>setMovingForward</code> <p>
	 * If the boolean parameter is true, set the character's status to moving forward and activate the right animation
	 * @param movingForward - (boolean)
	 */
	public void setMovingForward( boolean movingForward ) {
    	// activate the animation only if the previous animation was different and if the character is on the ground
    	if( animationController.getCurrentAnimation() != Animation.RUN && movingForward == true && getOnGround() == true ) {
    		// activate the animation "run" 
    		animationController.runAnimation( Animation.RUN );
    	}
		
		world.getCore().setCharacterMovingForward( id, movingForward );
	}

	/** Function <code>setJumping</code> <p>
	 * If the boolean parameter is true, set the character's status to jumping and activate the right animation
	 * @param jumping - (boolean)
	 */
	public void setJumping( boolean jumping ) {	
		// if the character is jumping
    	if( animationController.getCurrentAnimation() != Animation.JUMP && jumping == true ) {
    		// activate the animation "jump"
    		animationController.runAnimation( Animation.JUMP );
    	}
		world.getCore().setCharacterJumping( id, jumping );
	}

	/** Function <code>setStrafingLeft</code> <p>
	 * If the boolean parameter is true, set the character's status to strafing left
	 * @param strafingLeft - (boolean)
	 */
	public void setStrafingLeft( boolean strafingLeft ) {
		world.getCore().setCharacterStrafingLeft( id, strafingLeft );
	}

	/** Function <code>setStrafingRight</code> <p>
	 * If the boolean parameter is true, set the character's status to strafing right
	 * @param strafingRight - (boolean)
	 */
	public void setStrafingRight( boolean strafinRight ) {
		world.getCore().setCharacterStrafingRight( id, strafinRight );
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFirstPerson() {
		return firstPerson;
	}

	/** 
	 * 
	 * @param firstPerson
	 */
	public void setFirstPerson( boolean firstPerson ) {
		this.firstPerson = firstPerson;
	}

	/**
	 * @return true if the character is shooting
	 */
	public boolean isShooting() {
		return shooting;
	}

	/**
	 * @param shooting 
	 */
	public void setShooting( boolean shooting ) {
		this.shooting = shooting;
	}
}
