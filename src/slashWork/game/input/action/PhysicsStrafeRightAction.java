package slashWork.game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import slashWork.game.input.PhysicsInputHandler;

/**
 *
 */
public class PhysicsStrafeRightAction extends InputAction {
    PhysicsInputHandler handler;

    public PhysicsStrafeRightAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
	        handler.getTarget().setStrafingRight(true);
    	} else {
    		handler.getTarget().setStrafingRight(false);
    	}
    }

}
