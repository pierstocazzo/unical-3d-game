package slashWork.game.input;

import java.util.HashMap;

import slashWork.game.graphics.PhysicsCharacter;
import slashWork.game.input.action.FirstPersonAction;
import slashWork.game.input.action.ShootAction;
import slashWork.game.input.action.PhysicsBackwardAction;
import slashWork.game.input.action.PhysicsForwardAction;
import slashWork.game.input.action.PhysicsJumpAction;
import slashWork.game.input.action.PhysicsStrafeLeftAction;
import slashWork.game.input.action.PhysicsStrafeRightAction;

import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseLookHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.MouseInputAction;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class PhysicsInputHandler extends InputHandler {

    public static final String PROP_KEY_FORWARD = "fwdKey";
    public static final String PROP_KEY_BACKWARD = "bkdKey";
    public static final String PROP_KEY_STRAFE_LEFT = "strLeftKey";
    public static final String PROP_KEY_STRAFE_RIGHT = "strRightKey";
    public static final String PROP_KEY_JUMP = "jmpKey";

    PhysicsCharacter target;
    Camera cam;
    ChaseCamera chaser; 
    
    InputAction forwardAction;
    InputAction backwardAction;
    InputAction strafeLeftAction;
    InputAction strafeRightAction;
    InputAction jumpAction;
    MouseInputAction shootAction;
	MouseInputAction firstPersonAction;
	
	MouseLookHandler mouseLookHandler;
	
	Vector3f targetOffSet;
	
    public PhysicsInputHandler( PhysicsCharacter target, Camera cam ) {
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    	
    	this.target = target;
        this.cam = cam;
        this.chaser = new ChaseCamera( cam, target.getCharacterNode() );
        this.targetOffSet = new Vector3f();
        this.targetOffSet.setY(5);
        
        updateKeyBinding();
        setActions();
        setupChaseCamera();
    }

    private void setActions() {
        forwardAction = new PhysicsForwardAction( this );
        backwardAction = new PhysicsBackwardAction( this, target.getSpeed()/2 );
        strafeLeftAction = new PhysicsStrafeLeftAction( this, target.getSpeed()/2 );
        strafeRightAction = new PhysicsStrafeRightAction( this, target.getSpeed()/2 );
        jumpAction = new PhysicsJumpAction( this );
        shootAction = new ShootAction( this );
        firstPersonAction = new FirstPersonAction( this );
        
        addAction( forwardAction, DEVICE_KEYBOARD, KeyInput.KEY_W, AXIS_NONE, true );
        addAction( backwardAction, DEVICE_KEYBOARD, KeyInput.KEY_S, AXIS_NONE, true );
        addAction( strafeLeftAction, DEVICE_KEYBOARD, KeyInput.KEY_A, AXIS_NONE, true );
        addAction( strafeRightAction, DEVICE_KEYBOARD, KeyInput.KEY_D, AXIS_NONE, true );
        addAction( jumpAction, DEVICE_KEYBOARD, KeyInput.KEY_SPACE, AXIS_NONE, false );
        addAction( shootAction, DEVICE_MOUSE, MouseButtonBinding.LEFT_BUTTON, AXIS_NONE, false );
        addAction( firstPersonAction, DEVICE_MOUSE, MouseButtonBinding.RIGHT_BUTTON, AXIS_NONE, false );
    }

    public void updateKeyBinding() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
        
        keyboard.set( PROP_KEY_FORWARD, KeyInput.KEY_W );
        keyboard.set( PROP_KEY_BACKWARD, KeyInput.KEY_S );
        keyboard.set( PROP_KEY_STRAFE_LEFT, KeyInput.KEY_A );
        keyboard.set( PROP_KEY_STRAFE_RIGHT, KeyInput.KEY_D );
        keyboard.set( PROP_KEY_JUMP, KeyInput.KEY_SPACE );
    }
    
    @Override
    public void update(float time) {
        if ( !isEnabled() ) return;
        
        /**
         * Process all input triggers and change internal state variables
         */
        doInputUpdate(time);
        
        /**
         * If the player is on the floor and it isn't moving, clear dynamics
         */
        if ( target.getRest() && target.getOnGround() ) {
            target.clearDynamics();
        }
        
        /** swith to first person view when the muose right bottom is down */
        if ( target.isFirstPerson() ) {
        	chaser.setEnabled(false);
        	mouseLookHandler.setEnabled(true);
        	mouseLookHandler.update(time);
        	cam.setLocation( target.getCharacterFeet().getWorldTranslation().add(this.targetOffSet) );
        } else { /** return to third person view */
    		chaser.update( time );
    		chaser.setEnabled(true);
        	mouseLookHandler.setEnabled(false);
        }
    }

    public void doInputUpdate(float time) {
        super.update(time);
    }

    public PhysicsCharacter getTarget() {
        return target;
    }

    public Camera getCamera() {
        return cam;
    }

    protected void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = getTarget().getCharacterBody().getLocalTranslation().y + 5;
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "30");
        props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "10");
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        props.put(ThirdPersonMouseLook.PROP_MAXASCENT, ""+90 * FastMath.DEG_TO_RAD);
        props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0, 30 * FastMath.DEG_TO_RAD));
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        chaser = new ChaseCamera(cam, getTarget().getCharacterBody(), props);
        chaser.setMaxDistance(50);
        chaser.setMinDistance(15);
    }
}
