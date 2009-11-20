/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package slashWork.game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import slashWork.game.input.PhysicsInputHandler;

/**
 *
 * @author joseph
 */
public class PhysicsBackwardAction extends InputAction {
    PhysicsInputHandler handler;
    Vector3f direction;

    public PhysicsBackwardAction( PhysicsInputHandler handler, float speed ) {
        this.handler = handler;
        this.handler.getTarget().setSpeed( speed );
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
	        handler.getTarget().setRest(false);
	        handler.getTarget().setMovingBackward(true);
	        direction = handler.getCamera().getDirection().negateLocal();
	        handler.getTarget().move( direction );
    	} else {
    		System.out.println("FUCK");
    	}
    }

}
