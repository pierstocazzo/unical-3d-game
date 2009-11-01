package joework.input;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.renderer.Camera;
import joework.controller.PhysicsCharacter;
import joework.input.action.PhysicsBackwardAction;
import joework.input.action.PhysicsForwardAction;
import joework.input.action.PhysicsJumpAction;
import joework.input.action.PhysicsStrafeLeftAction;
import joework.input.action.PhysicsStrafeRightAction;
import joework.input.action.LookAtAction;

public class PhysicsInputHandler extends InputHandler {

    public static final String PROP_KEY_FORWARD = "fwdKey";
    public static final String PROP_KEY_BACKWARD = "bkdKey";
    public static final String PROP_KEY_STRAFE_LEFT = "strLeftKey";
    public static final String PROP_KEY_STRAFE_RIGHT = "strRightKey";
    public static final String PROP_KEY_JUMP = "jmpKey";

    PhysicsCharacter target;
    Camera cam;

    InputAction forwardAction;
    InputAction backwardAction;
    InputAction strafeLeftAction;
    InputAction strafeRightAction;
    InputAction jumpAction;
    
    /** 
     *  to turn the target to always look in cam direction
     */
	LookAtAction lookAtAction;

    
    public PhysicsInputHandler(PhysicsCharacter target, Camera cam) {
        this.target = target;
        this.cam = cam;
        updateKeyBinding(); // to improve
        setActions();
    }

    private void setActions() {
        forwardAction = new PhysicsForwardAction( this );
        backwardAction = new PhysicsBackwardAction( this, target.getSpeed()/2 );
        strafeLeftAction = new PhysicsStrafeLeftAction( this, target.getSpeed()/2 );
        strafeRightAction = new PhysicsStrafeRightAction( this, target.getSpeed()/2 );
        jumpAction = new PhysicsJumpAction( this );
        lookAtAction = new LookAtAction( target.getCharacterBody(), cam );
        
        addAction( forwardAction, PROP_KEY_FORWARD, true );
        addAction( backwardAction, PROP_KEY_BACKWARD, true );
        addAction( strafeLeftAction, PROP_KEY_STRAFE_LEFT, true );
        addAction( strafeRightAction, PROP_KEY_STRAFE_RIGHT, true );
        addAction( jumpAction, PROP_KEY_JUMP, true );
               
        addAction( lookAtAction, InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_NONE, InputHandler.AXIS_ALL, false );
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
}
