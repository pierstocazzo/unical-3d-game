package game.input.action;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

import game.input.PhysicsInputHandler;

public class FirstPersonAction extends MouseInputAction {
    PhysicsInputHandler handler;

    public FirstPersonAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
    		handler.getTarget().setFirstPerson( true );
    	} else {
    		handler.getTarget().setFirstPerson( false );
    	}
    }
}
