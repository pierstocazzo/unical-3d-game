package game.input.action;

import game.input.ThirdPersonHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

public class ThirdPersonRunAction extends KeyInputAction {

    private ThirdPersonHandler handler;

    public ThirdPersonRunAction(ThirdPersonHandler handler, float speed) {
        this.handler = handler;
        this.speed = speed;
    }

    public void performAction(InputActionEvent event) {
        if( event.getTriggerPressed() ) {
        	handler.setRunning(true);
        } else {
        	handler.setRunning(false);
        }
    }
}
