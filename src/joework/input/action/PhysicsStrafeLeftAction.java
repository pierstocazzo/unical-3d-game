/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import joework.input.PhysicsInputHandler;

/**
 *
 * @author joseph
 */
public class PhysicsStrafeLeftAction extends InputAction {
    PhysicsInputHandler handler;
    Vector3f newRotationalAxis;

    public PhysicsStrafeLeftAction( PhysicsInputHandler handler, float speed ) {
        this.handler = handler;
        this.handler.getTarget().setSpeed(speed);
    }

    public void performAction(InputActionEvent evt) {
        handler.getTarget().setRest(false);
        handler.getTarget().setStrafingLeft(true);
        newRotationalAxis = handler.getCamera().getDirection().normalizeLocal();
        handler.getTarget().move(newRotationalAxis);
    }

}
