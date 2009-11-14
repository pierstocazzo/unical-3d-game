package game.graphics;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.model.animation.JointController;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.material.Material;

public class PhysicsCharacter {

	/** the character identifier */
	String id;
	
	/** the physics parts material */
	static final Material characterMaterial;
    static {
        characterMaterial = new Material("Character Material");
        characterMaterial.setDensity(100f);
        MutableContactInfo contactDetails = new MutableContactInfo();
        contactDetails.setBounce(0);
        contactDetails.setMu( 100 );
        contactDetails.setMuOrthogonal( 10 );
        contactDetails.setDampingCoefficient(0);
        characterMaterial.putContactHandlingDetails( Material.DEFAULT, contactDetails );
    }
  
    /** the main node of the character */
    Node characterNode;
    
    /** physics parts of the character */
    DynamicPhysicsNode body, feet;
    
    /** model applied to the character */
    Node model;
    
	/** animation controller */
	JointController animationController;
	
	/** the graphical world in whitch the character live */
    GraphicalWorld world;
    
    StaticPhysicsNode ground;
    
    /** the joint that connect the feet to the body */
    Joint feetToBody;
    
    /** the joint that permit the rotation of the feet (feet is a Sphere) */
    RotationalJointAxis rotationalAxis;

    /** movements speed */
    float speed;
    
    /** physics character mass */
    float mass;

    /** the move direction */
    Vector3f moveDirection;
    
    /** jump direction */
    Vector3f jumpVector;
    
    /** an util that detect the contact between the character and the ground */
    InputHandler contactDetect = new InputHandler();
    
    /** util quaternion */
    Quaternion quaternion;
    
    /** PhysicsCharacter constructor <br>
     * Create a new character affected by physics. 
     * 
     * @param id - (String) the character's identifier
     * @param world - (GraphicalWorld) the graphical world in whitch the character live
     * @param direction (Vector3f) initial direction of the character
     * @param speed - (float) movements speed
     * @param mass - (float) the mass of the character
     * @param model - (Node) the model to apply to the character
     */
    public PhysicsCharacter( String id, GraphicalWorld world, Vector3f direction, float speed, float mass, Node model ) {
        
    	this.id = id;
    	
    	this.world = world;
    	this.ground = world.getGround();
        
        characterNode = new Node("character node");
        body = world.getPhysicsSpace().createDynamicNode();
        feet = world.getPhysicsSpace().createDynamicNode();
        feetToBody = world.getPhysicsSpace().createJoint();
        rotationalAxis = feetToBody.createRotationalAxis();

        this.moveDirection = direction;
        this.speed = speed;
        this.mass = mass;
        this.model = model;

        this.rest();

        quaternion = new Quaternion();

        createPhysics();
        contactDetection();
    }

    private void rest() {
    	world.getCore().characterRest(id);
	}

	void contactDetection() {
        /**
         * Detect the ground collision
         */
        SyntheticButton playerCollisionEventHandler = feet.getCollisionEventHandler();
        contactDetect.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == ground || contactInfo.getNode2() == ground ) {
                    world.getCore().setCharacterOnGround( id, true );
                }
            }
        }, playerCollisionEventHandler, false );
    }

    void createPhysics() {
        /**
         * Create the physics geometry of character
         */

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
        rotationalAxis.setDirection(moveDirection);
        feetToBody.attach(body, feet);
        rotationalAxis.setRelativeToSecondObject(true);
        rotationalAxis.setAvailableAcceleration(0f);
        rotationalAxis.setDesiredVelocity(0f);

        // Set default mass
        feet.setMass(mass);

        // Set the jump direction
        jumpVector = new Vector3f(0, mass*400, 0);
        
        /** initialize the animation */ 
		animationController = (JointController) model.getController(0);
		// set the start and end frame to execute
		// the initial animation is "idle"
		animationController.setTimes( 327, 360 );
		// set the repeate type of the animation
		animationController.setRepeatType( JointController.RT_CYCLE );
		// activate the animation 
		animationController.setActive(true);
		// set the speed of the animation
		animationController.setSpeed( 0.25f );
    }

    public void update( float time ) {
        this.rest();
    	
    	preventFall();

        contactDetect.update(time);
        body.rest();
        
        // update core
        world.getCore().setCharacterPosition( id, feet.getWorldTranslation() );
    }

    public void preventFall() {
        quaternion = body.getLocalRotation();
        Vector3f[] axes = new Vector3f[3];
        quaternion.toAxes(axes);

        quaternion.fromAxes(axes[0], Vector3f.UNIT_Y, axes[2]);
        body.setLocalRotation(quaternion);
        body.setAngularVelocity(Vector3f.ZERO);
        body.updateWorldVectors();
    }

    public void move( Vector3f direction ) {
        moveDirection.set(direction);
        
        if( world.getCore().getCharacterOnGround( id ) ) {
            if( moveDirection != Vector3f.ZERO ){
                try{
                    rotationalAxis.setDirection( moveDirection );
                    rotationalAxis.setAvailableAcceleration( 15*speed );
                    rotationalAxis.setDesiredVelocity( speed );
                } catch( Exception e ) {
                    rotationalAxis.setDesiredVelocity(0f);
                }
            }
        }
    }

    public void clearDynamics() {
        feet.clearDynamics();
        rotationalAxis.setDesiredVelocity(0);
        rotationalAxis.setAvailableAcceleration(0);
        body.clearDynamics();
        
    	if( animationController.getMaxTime() != (360 / animationController.FPS) ) {
    		// activate the animation "run"
    		animationController.setTimes( 327, 360 );
    	}
    }

    public Node getCharacterNode() {
        return characterNode;
    }

    public DynamicPhysicsNode getCharacterBody() {
        return body;
    }

    public DynamicPhysicsNode getCharacterFeet() {
        return feet;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed( float speed ) {
        this.speed = speed;
    }

    public Vector3f getJumpVector() {
        return jumpVector;
    }

    public Node getModel() {
        return model;
    }

    public void setModel( Node model ) {
        this.model = model;
    }

	public boolean getRest() {
		return world.getCore().getCharacterRest(id);
	}

	public boolean getOnGround() {
		return world.getCore().getCharacterOnGround(id);
	}
	
	public void setOnGround( boolean onGround ) {
		world.getCore().setCharacterOnGround( id, onGround );
	}
	
	public void setRest( boolean rest ) {
		world.getCore().setCharacterRest( id, rest );
	}

	public void setMovingBackward( boolean movingBackward ) {
		world.getCore().setCharacterMovingBackward( id, movingBackward );
	}

	public void setMovingForward( boolean movingForward ) {
    	// ATTIVO L'ANIMAZIONE SOLTANTO SE QUELLA PRECEDENTE ERA DIVERSA e se il character � sul terreno
    	if( animationController.getMaxTime() != (26 / animationController.FPS) && movingForward == true && getOnGround() == true ) {
    		// activate the animation "run"
    		animationController.setTimes( 16, 26 );
    	}
		
		world.getCore().setCharacterMovingForward( id, movingForward );
	}

	public void setJumping( boolean jumping ) {	
		// if the character is jumping
    	if( animationController.getMaxTime() != (40 / animationController.FPS) && jumping == true ) {
    		// activate the animation "jump"
    		animationController.setTimes( 28, 40 );
    	}
		world.getCore().setCharacterJumping( id, jumping );
	}

	public void setStrafingLeft( boolean strafingLeft ) {
		world.getCore().setCharacterStrafingLeft( id, strafingLeft );
	}

	public void setStrafingRight( boolean strafinRight ) {
		world.getCore().setCharacterStrafingRight( id, strafinRight );
	}
	
    public JointController getAnimationController() {
		return animationController;
	}
}