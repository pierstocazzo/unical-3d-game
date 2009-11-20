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

    public void performAction(InputActionEvent evt) {
        handler.getTarget().setRest(false);

        if ( handler.getTarget().getOnGround() == true ) {
            handler.getTarget().setJumping(true);
            handler.getTarget().setOnGround(false);
            handler.getTarget().getCharacterFeet().addForce( handler.getTarget().getJumpVector() );
        }
    }

}
