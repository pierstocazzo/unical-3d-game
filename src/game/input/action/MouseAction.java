package game.input.action;

import game.input.PhysicsInputHandler;

import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
import com.jme.input.controls.binding.MouseButtonBinding;

public class MouseAction extends MouseInputAction {
	
    PhysicsInputHandler handler;
    MouseInput mouseInput;
    
    public MouseAction( PhysicsInputHandler handler ) {
        this.handler = handler;
        
        this.mouseInput = MouseInput.get();
    }

    public void performAction(InputActionEvent evt) {
    	if ( mouseInput.isButtonDown( MouseButtonBinding.RIGHT_BUTTON ) ) {
    		handler.getTarget().setFirstPerson(true);
    	}
    	if ( mouseInput.isButtonDown( MouseButtonBinding.LEFT_BUTTON ) && handler.getTarget().isFirstPerson() ) {
    		handler.getTarget().shoot( handler.getCamera().getDirection() );
    	}
    }
}
