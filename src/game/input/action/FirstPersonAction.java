package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class FirstPersonAction extends MouseInputAction {
	GameInputHandler handler;

    public FirstPersonAction( GameInputHandler handler ) {
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
