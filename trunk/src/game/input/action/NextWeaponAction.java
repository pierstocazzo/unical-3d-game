package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class NextWeaponAction extends MouseInputAction {
	GameInputHandler handler;

    public NextWeaponAction( GameInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if( handler.isFirstPerson() ) {
	    	handler.getTarget().nextWeapon();
    	}
    }
}
