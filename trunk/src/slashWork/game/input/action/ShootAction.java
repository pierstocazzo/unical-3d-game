package slashWork.game.input.action;

import slashWork.game.input.PhysicsInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class ShootAction extends MouseInputAction {
	
    PhysicsInputHandler handler;
    
    public ShootAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	if ( handler.getTarget().isFirstPerson() && evt.getTriggerPressed() ) {
    		handler.getTarget().shoot( handler.getCamera().getDirection() );
    	}
    }
}
