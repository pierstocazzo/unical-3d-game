package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class SwitchWeapon extends MouseInputAction {
	GameInputHandler handler;

    public SwitchWeapon( GameInputHandler handler ) {
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
