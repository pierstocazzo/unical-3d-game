package slashWork.game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import slashWork.game.input.PhysicsInputHandler;

/**
 *
 */
public class PhysicsJumpAction extends InputAction {
    PhysicsInputHandler handler;
    
    public PhysicsJumpAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction( InputActionEvent evt ) {
    	if( handler.getTarget().getOnGround() ) {
    		handler.getTarget().getCharacterFeet().addForce( handler.getTarget().getJumpVector() );
    		handler.getTarget().setOnGround( false );
    	}
    }
}
