package game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import game.input.PhysicsInputHandler;

public class FirstPersonAction extends InputAction {
    PhysicsInputHandler handler;

    public FirstPersonAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	handler.getTarget().setFirstPerson( true );
    }
}
