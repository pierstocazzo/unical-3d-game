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
import com.jmex.physics.DynamicPhysicsNode;
import joework.input.action.PhysicsForwardAction;

/**
 *
 * @author joseph
 */
public class PhysicsInputHandler extends InputHandler {

    public static final Vector3f PROP_DEFAULT_TORQUE = new Vector3f(-60000,0,0);

    public static final String PROP_KEY_FORWARD = "fwdKey";

    DynamicPhysicsNode target;
    Camera cam;

    Vector3f newCamDirection, prevLocation;

    boolean rest = true;
    boolean movingForward;
    boolean movingBackward;
    boolean rotatingLeft;
    boolean rotatingRight;
    boolean jumping;

    boolean targetOnFloor = false;

    protected PhysicsForwardAction forwardAction;

    public PhysicsInputHandler(DynamicPhysicsNode target, Camera cam) {
        this.target = target;
        this.cam = cam;
        this.newCamDirection = new Vector3f();

        prevLocation = new Vector3f(100,0,100);

        updateKeyBinding();
        setActions();
    }

    private void setActions() {
        forwardAction = new PhysicsForwardAction(this, PROP_DEFAULT_TORQUE);
        // rotateLeftAction = new PhysicsLeftAction(this);
        
        // more here
        addAction(forwardAction, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_W, InputHandler.AXIS_NONE, true);
        // more here

    }

    public void updateKeyBinding() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
        keyboard.set(PROP_KEY_FORWARD, KeyInput.KEY_W);
        // more here
    }

    @Override
    public void update(float time) {
        targetOnFloor = false;

        super.update(time);

        newCamDirection.setX( target.getLocalTranslation().x );
        newCamDirection.setY( target.getLocalTranslation().y + 20 );
        newCamDirection.setZ( target.getLocalTranslation().z + 100 );

        cam.setLocation(newCamDirection);
        cam.update();

        if ( rest && targetOnFloor ) {
            target.clearDynamics();
        }

        rest = true;
    }

    public DynamicPhysicsNode getTarget() {
        return target;
    }

    public void setRest( boolean rest ) {
        this.rest = rest;
    }

    public void setTargetOnFloor( boolean onFloor ) {
        targetOnFloor = onFloor;
    }

    public void setMovingForward( boolean moving ) {
        movingForward = moving;
    }

    public boolean getTargetOnFloor() {
        return targetOnFloor;
    }

    public boolean getRest() {
        return rest;
    }

    public Camera getCamera() {
        return cam;
    }

}
