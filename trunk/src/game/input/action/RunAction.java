package game.input.action;

import game.input.GameInputHandler;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

public class RunAction extends KeyInputAction {

    private GameInputHandler handler;

    public RunAction(GameInputHandler handler) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent event) {
        if( event.getTriggerPressed() ) {
        	handler.setRunning(true);
        } else {
        	handler.setRunning(false);
        }
    }
}
