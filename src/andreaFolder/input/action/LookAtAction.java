package andreaFolder.input.action;


import andreaFolder.input.PhysicsInputHandler;

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
            Vector3f v = new Vector3f( handler.getTarget().getModel().getWorldTranslation() );
            v.addLocal( cam.getDirection().negate().x, 0 ,cam.getDirection().negate().z );
            handler.getTarget().getModel().lookAt( v , Vector3f.UNIT_Y );
        }
    }
}
