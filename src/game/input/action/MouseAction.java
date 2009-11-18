package game.input.action;

import game.input.PhysicsInputHandler;

import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

public class MouseAction extends MouseInputAction {
	
    PhysicsInputHandler handler;
    MouseInput mouseInput;
    
    public MouseAction( PhysicsInputHandler handler ) {
        this.handler = handler;
        
        this.mouseInput = MouseInput.get();
    }

    public void performAction(InputActionEvent evt) {
    	if ( handler.getTarget().isFirstPerson() ) {
    		handler.getTarget().shoot( handler.getCamera().getDirection() );
    	}
    }
}
