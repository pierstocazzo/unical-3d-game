package game.graphics;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
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

	String id;
	
    static final Material characterMaterial;
    static {
        characterMaterial = new Material("Character Material");
        characterMaterial.setDensity(1f);
        MutableContactInfo contactDetails = new MutableContactInfo();
        contactDetails.setBounce(0);
        contactDetails.setMu(10);
        contactDetails.setMuOrthogonal(10);
        contactDetails.setDampingCoefficient(0);
        characterMaterial.putContactHandlingDetails( Material.DEFAULT, contactDetails );
    }

    Node characterNode;
    DynamicPhysicsNode body, feet;
    Node model;

    GraphicalWorld world;
    StaticPhysicsNode ground;
    Joint feetToBody;
    RotationalJointAxis rotationalAxis;

    float speed, mass;

    InputHandler contactDetect = new InputHandler();

    Quaternion quaternion;
    Vector3f moveDirection, jumpVector;
    
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

        // Set the jump vector
        jumpVector = new Vector3f(0, mass*400, 0);
    }

    public void update( float time ) {
        this.rest();
    	
    	preventFall();

        contactDetect.update(time);
        body.rest();
        
        // update core
        world.getCore().moveCharacter( id, feet.getWorldTranslation() );
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
	
	public void setOnGround(boolean b) {
		world.getCore().setCharacterOnGround(id, b);
	}
	
	public void setRest(boolean b) {
		world.getCore().setCharacterRest( id, b );
	}

	public void setMovingBackward(boolean b) {
		world.getCore().setCharacterMovingBackward( id, b );
		
	}

	public void setMovingForward(boolean b) {
		world.getCore().setCharacterMovingForward(id, b);
	}

	public void setJumping(boolean b) {
		world.getCore().setCharacterJumping(id, b);
	}

	public void setStrafingLeft(boolean b) {
		world.getCore().setCharacterStrafingLeft(id, b);
	}

	public void setStrafingRight(boolean b) {
		world.getCore().setCharacterStrafingRight(id, b);
	}
}