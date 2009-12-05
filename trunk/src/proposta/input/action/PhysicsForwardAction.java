/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package proposta.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import proposta.input.PhysicsInputHandler;

/**
 *
 * @author joseph
 */
public class PhysicsForwardAction extends InputAction {
    PhysicsInputHandler handler;
    Vector3f direction;

    public PhysicsForwardAction( PhysicsInputHandler handler ) {
        this.handler = handler;
        this.direction = new Vector3f();
    }

    public void performAction(InputActionEvent evt) {
    	if( evt.getTriggerPressed() ) {
	        handler.getTarget().setMovingForward(true);
    	} else {
    		handler.getTarget().setMovingForward(false);
    	}
        
    }
}
