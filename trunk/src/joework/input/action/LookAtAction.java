package joework.input.action;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jmex.physics.DynamicPhysicsNode;

public class LookAtAction extends InputAction {
	
    DynamicPhysicsNode target;
	Camera cam;
	
    public LookAtAction( DynamicPhysicsNode target, Camera cam ) {
    	this.target = target;
    	this.cam = cam;
    }

    public void performAction( InputActionEvent evt ) {
        if ( evt.getTriggerDelta() != 0f ) {
            Vector3f v = new Vector3f( target.getWorldTranslation().x,
                                       target.getWorldTranslation().y,
                                       target.getWorldTranslation().z );
            v.addLocal( cam.getDirection().x, 0 ,cam.getDirection().z );
            target.lookAt( v , Vector3f.UNIT_Y );
        }
    }
}
