/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.input;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
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
    Camera cam;
    Vector3f prevCamDirection;

    protected InputAction forwardAction;
    protected InputAction backwardAction;
    protected InputAction strafeLeftAction;
    protected InputAction strafeRightAction;
    protected InputAction jumpAction;

    public PhysicsInputHandler(PhysicsCharacter target, Camera cam) {
        this.target = target;
        this.cam = cam;
        this.prevCamDirection = new Vector3f();
        updateKeyBinding(); // to improve
        setActions();
    }

    private void setActions() {
        forwardAction = new PhysicsForwardAction(this);
        backwardAction = new PhysicsBackwardAction(this, target.getSpeed()/2);
        strafeLeftAction = new PhysicsStrafeLeftAction(this, target.getSpeed()/2);
        strafeRightAction = new PhysicsStrafeRightAction(this, target.getSpeed()/2);
        jumpAction = new PhysicsJumpAction(this);

        addAction(forwardAction, PROP_KEY_FORWARD, true);
        addAction(backwardAction, PROP_KEY_BACKWARD, true);
        addAction(strafeLeftAction, PROP_KEY_STRAFE_LEFT, true);
        addAction(strafeRightAction, PROP_KEY_STRAFE_RIGHT, true);
        addAction(jumpAction, PROP_KEY_JUMP, true);
    }

    public void updateKeyBinding() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
        keyboard.set(PROP_KEY_FORWARD, KeyInput.KEY_W);
        keyboard.set(PROP_KEY_BACKWARD, KeyInput.KEY_S);
        keyboard.set(PROP_KEY_STRAFE_LEFT, KeyInput.KEY_A);
        keyboard.set(PROP_KEY_STRAFE_RIGHT, KeyInput.KEY_D);
        keyboard.set(PROP_KEY_JUMP, KeyInput.KEY_SPACE);
    }

    @Override
    public void update(float time) {
        if ( !isEnabled() ) return;
        
        /**
         * Process all input triggers and change internal state variables
         */
        doInputUpdate(time);
        
        
        /* 
         *   Compute rotation for the target
         */
//        Vector3f prev = prevCamDirection.normalize();
//        prev.setY(0);
//        Vector3f current = cam.getDirection().normalize();
//        current.setY(0);
//        
//        float cos = prev.dot( current );
//        
//        float angle = FastMath.acos( cos );
//        
//        System.out.println("L'angolo e'. " + angle);
//        
//        prevCamDirection.set(cam.getDirection());
//
//        Quaternion q = new Quaternion().fromAngleAxis( angle, Vector3f.UNIT_Y );
//        target.rotateBody(q);
        
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
