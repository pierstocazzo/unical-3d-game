/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import java.util.logging.Logger;
import joework.input.PhysicsInputHandler;

/**
 *
 * @author joseph
 */
public class PhysicsForwardAction extends InputAction {
    static final Logger logger = Logger.getLogger(PhysicsForwardAction.class.getName());

    PhysicsInputHandler handler;
    Vector3f torque;

    public PhysicsForwardAction(PhysicsInputHandler handler, Vector3f torque ) {
        this.torque = torque;
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
        logger.info("Key Forward Pressed");
        handler.setRest(false);
        handler.setMovingForward(true);
        handler.getTarget().addTorque(torque);
    }

}
