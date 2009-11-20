package slashWork.game.input.action;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

import slashWork.game.input.PhysicsInputHandler;

public class FirstPersonAction extends MouseInputAction {
    PhysicsInputHandler handler;

    public FirstPersonAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	// usefull gotcha
    	handler.getTarget().setFirstPerson( !handler.getTarget().isFirstPerson() );
    }
}
