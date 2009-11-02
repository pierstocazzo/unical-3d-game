package game.input.action;

import game.input.PhysicsInputHandler;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class LookAtAction extends InputAction {
	
    PhysicsInputHandler handler;
	Camera cam;
	
    public LookAtAction( PhysicsInputHandler handler, Camera cam ) {
    	this.handler = handler;
    	this.cam = cam;
    }

    public void performAction( InputActionEvent evt ) {
        if ( evt.getTriggerDelta() != 0f ) {
            Vector3f v = new Vector3f( handler.getTarget().getModel().getWorldTranslation().x,
                                       handler.getTarget().getModel().getWorldTranslation().y,
                                       handler.getTarget().getModel().getWorldTranslation().z );
            v.addLocal( cam.getDirection().x, 0 ,cam.getDirection().z );
            handler.getTarget().getModel().lookAt( v , Vector3f.UNIT_Y );
        }
    }
}
