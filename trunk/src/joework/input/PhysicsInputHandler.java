/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.input;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jmex.physics.StaticPhysicsNode;
import joework.controller.PhysicsCharacter;
import joework.input.action.PhysicsBackwardAction;
import joework.input.action.PhysicsForwardAction;
import joework.input.action.PhysicsJumpAction;
import joework.input.action.PhysicsStrafeLeftAction;
import joework.input.action.PhysicsStrafeRightAction;

/**
 *
 * @author joseph
 */
public class PhysicsInputHandler extends InputHandler {

    public static final String PROP_KEY_FORWARD = "fwdKey";
    public static final String PROP_KEY_BACKWARD = "bkdKey";
    public static final String PROP_KEY_STRAFE_LEFT = "strLeftKey";
    public static final String PROP_KEY_STRAFE_RIGHT = "strRightKey";
    public static final String PROP_KEY_JUMP = "jmpKey";

    PhysicsCharacter target;
    StaticPhysicsNode terrain;
    Camera cam;

    Vector3f prevCamDirection;

    protected PhysicsForwardAction forwardAction;
    protected PhysicsBackwardAction backwardAction;
    protected PhysicsStrafeLeftAction strafeLeftAction;
    protected PhysicsStrafeRightAction strafeRightAction;
    protected PhysicsJumpAction jumpAction;

    public PhysicsInputHandler(PhysicsCharacter target, Camera cam) {
        this.target = target;
        this.cam = cam;
        this.prevCamDirection = new Vector3f();

        updateKeyBinding();
        setActions();
    }

    private void setActions() {
        forwardAction = new PhysicsForwardAction(this, 600);
        backwardAction = new PhysicsBackwardAction(this);
        strafeLeftAction = new PhysicsStrafeLeftAction(this);
        strafeRightAction = new PhysicsStrafeRightAction(this);
        jumpAction = new PhysicsJumpAction(this);
        // more here
        
        addAction(forwardAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_W, InputHandler.AXIS_NONE, true);
        addAction(backwardAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_S, InputHandler.AXIS_NONE, true);
        addAction(strafeLeftAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_A, InputHandler.AXIS_NONE, true);
        addAction(strafeRightAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_D, InputHandler.AXIS_NONE, true);
        addAction(jumpAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, true);
        // more here

    }

    public void updateKeyBinding() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
        keyboard.set(PROP_KEY_FORWARD, KeyInput.KEY_W);
        // more here
    }

    @Override
    public void update(float time) {
        if ( !isEnabled() ) return;

        /**
         * Process all input triggers and change internal state variables
         */
        doInputUpdate(time);

        /**
         * If the player is on the floor clear dynamics
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
