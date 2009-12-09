package proposta.graphics;

import proposta.graphics.CustomAnimationController;
import proposta.graphics.CustomAnimationController.Animation;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.material.Material;

/** Class <code>PhysicsCharacter</code> <br>
 * 
 * Represents a graphical character affected by physics, with the ability to move
 * by simply call the {@link #move( Vector3f direction )} function
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 * 
 * @see {@link game.input.PhysicsInputHandler}
 */
public class PhysicsPlayer extends PhysicsCharacter {

	/** the character identifier */
	String id;
  
    /** the main node of the character */
    Node characterNode;
    
	/** The body of the character: a Capsule, placed upon the feet, that contains the model */
    DynamicPhysicsNode body; 
    
    /** 3d model applied to the character */
    Node model;
    
	/** animation controller */
	CustomAnimationController animationController;
	
	/** the graphical world in which the character live */
    GraphicalWorld world;

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

	/** PhysicsCharacter constructor <br>
     * Create a new character affected by physics. 
     * 
     * @param id - (String) the character's identifier
     * @param world - (GraphicalWorld) the graphical world in whitch the character live
     * @param speed - (float) character's movements speed
     * @param mass - (float) the mass of the character
     * @param model - (Node) the model to apply to the character
     */
    public PhysicsPlayer( String id, GraphicalWorld world, float speed, float mass, Node model ) {
        
    	this.id = id;
    	
    	this.world = world;
        
        characterNode = new Node("character node");
        body = world.getPhysicsSpace().createDynamicNode();

        this.model = model;
        
        createPhysics();
        contactDetection();
        
        previousTime = 0;
    }

	void createPhysics() {
	    PhysicsCapsule bodyGeometry = body.createCapsule("body geometry");
	    bodyGeometry.setLocalScale(2.5f);
	    bodyGeometry.setLocalTranslation(0,4,0);
//	     Set UP the orientation of the Body
	    quaternion = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
	    bodyGeometry.setLocalRotation(quaternion);
	    
	    body.setAffectedByGravity(false);
	    
	    body.setMaterial( Material.GHOST );
//	    body.computeMass();
//	    body.attachChild( model );
	
	    characterNode.attachChild(body);
	    characterNode.attachChild(model);
//	    characterNode.setModelBound( new BoundingBox() );
//	    characterNode.updateModelBound();
	    
	    model.setModelBound( new BoundingBox() );
	    model.updateModelBound();
	    
//	    model.attachChild( body );
		
	    /** initialize the animation */ 
		animationController = new CustomAnimationController( model.getController(0) );
        
        setMovingForward( true );
	}

	/** Function <code>update</code> <br>
	 * Update the physics character 
	 * @param time
	 */
	public void update( float time ) {
	    if( world.getCore().isAlive( id ) == true ) {
			preventFall();
		
		    contactDetect.update(time);
		    
		    body.getWorldTranslation().set( characterNode.getWorldTranslation() );

		    if( shooting && firstPerson ) {
		    	if( world.timer.getTimeInSeconds() - previousTime > 0.2f  ) {
		    		previousTime = world.timer.getTimeInSeconds();
		    		shoot( world.getCam().getDirection() );
		    	}
		    }
		    
		    // update core
		    world.getCore().setCharacterPosition( id, characterNode.getWorldTranslation() );
	    } else {
	    	die();
	    }
	}

	public void hide( boolean b ) {
		if( b ) 
			characterNode.detachChild( model );
		else
			characterNode.attachChild( model );
	}
	
	public void die() {
    	clearDynamics();

    	body.detachAllChildren();
    	body.delete();
    	world.getRootNode().detachChild( characterNode );
    	
    	world.characters.remove( id );
	}

	/** Function <code>preventFall</code> <br>
	 *  prevent the falling of the character body due to physics
	 */
	public void preventFall() {
		body.clearDynamics();
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
        SyntheticButton playerCollisionEventHandler = body.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
            	
            	body.clearDynamics();
            	body.getWorldTranslation().set( characterNode.getWorldTranslation() );
            	
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == world.getGround() || contactInfo.getNode2() == world.getGround() ) {
                    world.getCore().setCharacterOnGround( id, true );
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
        
        body.clearDynamics();
        
    	if( animationController.getCurrentAnimation() != Animation.IDLE ) {
    		// activate the animation "idle"
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
    	if( animationController.getCurrentAnimation() != Animation.RUN && movingForward == true/* && getOnGround() == true*/ ) {
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
	
	/**
	 * 
	 * @param direction - (Vector3f) the direction of the shoot
	 */
	public void shoot( Vector3f direction ) {
		world.bulletsCounter = world.bulletsCounter + 1;
		PhysicsBullet bullet = new PhysicsBullet( "bullet" + world.bulletsCounter, world, 
				world.getCore().getCharacterWeapon(id), 
				world.getCam().getLocation().add( world.getCam().getDirection().mult( 6 ) ) );
		world.bullets.put( bullet.id, bullet );
		bullet.shoot(direction);
//		world.shoot.setWorldPosition( feet.getWorldTranslation() );
//		world.shoot.setVolume( 0.2f );
		world.shoot( world.getCam().getLocation() );
//		AudioManager.shoot.play();
	}
}
