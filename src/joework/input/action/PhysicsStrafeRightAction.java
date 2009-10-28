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
public class PhysicsStrafeRightAction extends InputAction {
    PhysicsInputHandler handler;
    Vector3f newRotationalAxis;

    public PhysicsStrafeRightAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
        handler.getTarget().setRest(false);
        handler.getTarget().setStrafingRight(true);
        newRotationalAxis = handler.getCamera().getDirection().normalizeLocal().mult(-1);
        handler.getTarget().move(newRotationalAxis);
    }

}
