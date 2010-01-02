package lowDetailGame.graphics;

import lowDetailGame.common.GameTimer;
import lowDetailGame.graphics.CustomAnimationController.Animation;

import com.jme.bounding.BoundingBox;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.material.Material;

/** Class <code>PhysicsPlayer</code> <br>
 * 
 * Represents a graphical player 
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 * 
 * @see {@link game.input.PhysicsInputHandler}
 */
public class Player extends Character {
	
	Vector3f vectorToLookAt = new Vector3f();
	
	/**
	 * Character Collision node
	 */
	Node collision;
	
	/** PhysicsCharacter constructor <br>
     * Create a new character affected by physics. 
     * 
     * @param id - (String) the character's identifier
     * @param world - (GraphicalWorld) the graphical world in whitch the character live
     * @param speed - (float) character's movements speed
     * @param mass - (float) the mass of the character
     * @param model - (Node) the model to apply to the character
     */
    public Player( String id, GraphicalWorld world, float speed, float mass, Node model ) {
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
	    bodyGeometry.setLocalScale( 2.5f );
	    bodyGeometry.setLocalTranslation(0,3,0);
	    
	    // Set UP the orientation of the Body
	    quaternion = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
	    bodyGeometry.setLocalRotation(quaternion);
	    
	    body.setAffectedByGravity(false);
	    body.setMaterial( Material.GHOST );
	
	    characterNode.attachChild(body);
	    characterNode.attachChild(model);
	    
	    model.setModelBound( new BoundingBox() );
	    model.updateModelBound();
	    
	    /**
	     * Setting up character collision node
	     */
	    collision = new Node("collision");
	    world.getRootNode().attachChild(collision);
	    
	    Box frontal = new Box("frontal", new Vector3f(-1,0,-1), new Vector3f(1,10,-.9f));
	    frontal.clearTextureBuffers();
	    frontal.setModelBound(new BoundingBox());
		frontal.updateModelBound();
		frontal.setLocalTranslation(0,0,5);
		collision.attachChild(frontal);
		
//      frontal.setCullHint(CullHint.Always);
		
		Box backy = new Box("backy", new Vector3f(-1,0,-1), new Vector3f(1,10,-.9f));
		backy.clearTextureBuffers();
		backy.setModelBound(new BoundingBox());
		backy.updateModelBound();
		backy.setLocalTranslation(0,0,-2.5f);
		collision.attachChild(backy);
		
//		backy.setCullHint(CullHint.Always);
		
		Box left = new Box("left", new Vector3f(-1,0,-1.5f), new Vector3f(-.9f,10,1.5f));
	    left.clearTextureBuffers();
	    left.setModelBound(new BoundingBox());
		left.updateModelBound();
		left.setLocalTranslation(3,0,0);
		collision.attachChild(left);
	    
//		left.setCullHint(CullHint.Always);
		
		Box right = new Box("right", new Vector3f(-1,0,-1.5f), new Vector3f(-.9f,10,1.5f));
	    right.clearTextureBuffers();
	    right.setModelBound(new BoundingBox());
		right.updateModelBound();
		right.setLocalTranslation(-1.5f,0,0);
		collision.attachChild(right);
	    
//		right.setCullHint(CullHint.Always);
		
		/**
		 * end collision node
		 */
		
//	    model.attachChild( body );
		
	    /** initialize the animation */ 
		animationController = new CustomAnimationController( model.getController(0) );
//        setMovingForward( true );
	}

	public void lookAtAction( Vector3f direction ) {
    	vectorToLookAt.set( this.getCollision().getWorldTranslation() );
        vectorToLookAt.addLocal( direction.x, 0, direction.z );
    	this.getCollision().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
    }
	
	/** Function <code>update</code> <br>
	 * Update the physics character 
	 * @param time
	 */
	public void update( float time ) {
	    if( world.getCore().isAlive( id ) == true ) {
//		    contactDetect.update(time);
		    
	    	collision.getLocalTranslation().set( characterNode.getWorldTranslation() );
		    body.getWorldTranslation().set( characterNode.getWorldTranslation() );
		    
		    if( !world.getCore().isMoving(id) ) {
		    	animationController.runAnimation( Animation.IDLE );
		    }

		    if( shooting ) {
		    	if( GameTimer.getTimeInSeconds() - previousTime > 0.1f  ) {
		    		previousTime = GameTimer.getTimeInSeconds();
		    		shoot( world.getCam().getDirection() );
		    	}
		    }
		    
		    // update core
		    world.getCore().setPosition( id, characterNode.getWorldTranslation() );
	    } else {
	    	die();
	    }
	}
	
	public void hide( boolean b ) {
		if( b ) 
			model.removeFromParent();
		else
			characterNode.attachChild( model );
	}
	
	public void die() {
    	body.detachAllChildren();
    	body.delete();
    	world.getRootNode().detachChild( characterNode );
    	
    	world.characters.remove( id );
	}

	void contactDetection() {
        SyntheticButton playerCollisionEventHandler = body.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
            	body.getWorldTranslation().set( characterNode.getWorldTranslation() );
            }
        };
        
        contactDetect.addAction( collisionAction, playerCollisionEventHandler, false );
    }

	/** Function <code>rest()</code> <br>
     * Activate the idle animation
     */
    public void rest() {
    	animationController.runAnimation( Animation.IDLE );
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
    
    /** Function <code>getCollision</code> <br>
	 * 
	 * @return (Node) the collision node of the character
	 */
    public Node getCollision() {
        return collision;
    }
    
	/** Function <code>setModel</code> <br>
	 * Apply this model to the character
	 * 
	 * @param model - (Node) the model to apply to the character
	 */
    public void setModel( Node model ) {
        this.model = model;
    }

	public void setRunning(boolean running) {
    	if( running == true ) {
    		animationController.runAnimation( Animation.RUN );
    	}
		world.getCore().setMoving( id, running );
	}

	/** Function <code>setMovingForward</code> <p>
	 * If the boolean parameter is true, set the character's status to moving forward and activate the right animation
	 * @param moving - (boolean)
	 */
	public void setMoving( boolean moving ) {
    	if( moving == true ) {
    		animationController.runAnimation( Animation.WALK );
    	} 
		
		world.getCore().setMoving( id, moving );
	}

	/** Function <code>setJumping</code> <p>
	 * If the boolean parameter is true, set the character's status to jumping and activate the right animation
	 * @param jumping - (boolean)
	 */
	public void setJumping( boolean jumping ) {	
    	if( jumping == true ) {
    		animationController.runAnimation( Animation.JUMP );
    	}
		world.getCore().setJumping( id, jumping );
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
		if( world.getCore().shoot(id) ) {
			Bullet bullet = new Bullet( id , world, 
					world.getCore().getWeapon(id), 
					world.getCam().getLocation().add( world.getCam().getDirection().mult( 6 ) ) );
			world.bullets.put( bullet.id, bullet );
			bullet.shoot(direction);
			world.shoot( world.getCam().getLocation() );
		}
	}
}