/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package andreaFolder.input.action;

import andreaFolder.input.PhysicsInputHandler;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;

/**
 *
 * @author joseph
 */
public class PhysicsStrafeRightAction extends InputAction {
    PhysicsInputHandler handler;
    Vector3f newRotationalAxis;

    public PhysicsStrafeRightAction( PhysicsInputHandler handler, float speed ) {
        this.handler = handler;
        this.handler.getTarget().setSpeed(speed);
    }

    public void performAction(InputActionEvent evt) {
        handler.getTarget().setRest(false);
        handler.getTarget().setStrafingRight(true);
        newRotationalAxis = handler.getCamera().getDirection().crossLocal( Vector3f.UNIT_Y );
        handler.getTarget().move(newRotationalAxis);
    }

}
