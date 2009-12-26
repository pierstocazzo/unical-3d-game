package game.input.action;

import game.input.ThirdPersonHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class FirstPersonAction extends MouseInputAction {
	ThirdPersonHandler handler;

    public FirstPersonAction( ThirdPersonHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
    		handler.setFirstPerson( true );
    	} else {
    		handler.setFirstPerson( false );
    	}
    }
}
