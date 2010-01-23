package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class ShootAction extends MouseInputAction {
	
	GameInputHandler handler;
    
    public ShootAction( GameInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if ( handler.isFirstPerson() && evt.getTriggerPressed() ) {
    		handler.setShooting( true );
    	} else {
    		handler.setShooting( false );
    	}
    }
}
