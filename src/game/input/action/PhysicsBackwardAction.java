package game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import game.input.PhysicsInputHandler;

/**
 *
 */
public class PhysicsBackwardAction extends InputAction {
    PhysicsInputHandler handler;

    public PhysicsBackwardAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
	        handler.getTarget().setMovingBackward(true);
    	} else {
    		handler.getTarget().setMovingBackward(false);
    	}
    }

}
