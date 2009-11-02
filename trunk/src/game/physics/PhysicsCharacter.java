package game.physics;

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
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsCapsule;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.material.Material;

/**
 *
 * @author joseph
 */
public class PhysicsCharacter {

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

    StaticPhysicsNode ground;
    Joint feetToBody;
    RotationalJointAxis rotationalAxis;

    float speed, mass;

    InputHandler contactDetect = new InputHandler();

    boolean rest;
    boolean movingForward;
    boolean movingBackward;
    boolean strafingLeft;
    boolean strafingRight;
    boolean jumping;
    boolean onGround;

    Quaternion quaternion;
    Vector3f moveDirection, jumpVector;


    public PhysicsCharacter( StaticPhysicsNode ground, PhysicsSpace physicsSpace, Vector3f direction, float speed, float mass, Node model ) {
        this.ground = ground;

        characterNode = new Node("character node");
        body = physicsSpace.createDynamicNode();
        feet = physicsSpace.createDynamicNode();
        feetToBody = physicsSpace.createJoint();
        rotationalAxis = feetToBody.createRotationalAxis();

        this.moveDirection = direction;
        this.speed = speed;
        this.mass = mass;
        this.model = model;

        rest = true;
        movingForward = false;
        movingBackward = false;
        strafingLeft = false;
        strafingRight = false;
        jumping = false;
        onGround = false;

        quaternion = new Quaternion();

        createPhysics();
        contactDetection();
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
                    onGround = true;
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
        rest = true;
        movingForward = false;
        movingBackward = false;
        strafingLeft = false;
        strafingRight = false;
        jumping = false;
        onGround = false;

        preventFall();

        contactDetect.update(time);
        body.rest();
    }

    private void preventFall() {
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

        if( onGround ) {
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

    public boolean getRest() {
        return rest;
    }

    public void setRest( boolean rest ) {
        this.rest = rest;
    }

    public boolean getMovingForward() {
        return movingForward;
    }

    public void setMovingForward( boolean movingForward ) {
        this.movingForward = movingForward;
    }

    public boolean getMovingBackward() {
        return movingBackward;
    }

    public void setMovingBackward( boolean movingBackward ) {
        this.movingBackward = movingBackward;
    }

    public boolean getStrafingLeft() {
        return strafingLeft;
    }

    public void setStrafingLeft( boolean strafingLeft ) {
        this.strafingLeft = strafingLeft;
    }

    public boolean getStrafingRight() {
        return strafingRight;
    }

    public void setStrafingRight( boolean strafingRight ) {
        this.strafingRight = strafingRight;
    }

    public boolean getJumping() {
        return jumping;
    }

    public void setJumping( boolean jumping ) {
        this.jumping = jumping;
    }

    public boolean getOnGround() {
        return onGround;
    }

    public void setOnGround( boolean onGround ) {
        this.onGround = onGround;
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
}