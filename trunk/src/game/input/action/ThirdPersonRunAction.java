package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

public class ThirdPersonRunAction extends KeyInputAction {

    private GameInputHandler handler;

    public ThirdPersonRunAction(GameInputHandler handler, float speed) {
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
