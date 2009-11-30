package game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import game.input.PhysicsInputHandler;

/**
 *
 */
public class PhysicsStrafeLeftAction extends InputAction {
    PhysicsInputHandler handler;

    public PhysicsStrafeLeftAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
	        handler.getTarget().setStrafingLeft(true);
    	} else {
    		handler.getTarget().setStrafingLeft(false);
    	}
    }
}
