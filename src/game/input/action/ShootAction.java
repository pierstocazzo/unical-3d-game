/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import game.input.PhysicsInputHandler;

/**
 *
 * @author joseph
 */
public class ShootAction extends InputAction {
    PhysicsInputHandler handler;

    public ShootAction( PhysicsInputHandler handler ) {
        this.handler = handler;
    }

    public void performAction(InputActionEvent evt) {
    	handler.getTarget().shoot( handler.getCamera().getDirection() );
    }
}