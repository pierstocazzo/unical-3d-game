/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.input.thirdpersonphysics.action;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Vector3f;
import joework.input.thirdpersonphysics.ThirdPersonPhysicsHandler;

/**
 *
 * @author joseph
 */
public class BackwardAction extends KeyInputAction {

    private Vector3f rot;
    private ThirdPersonPhysicsHandler handler;

    /**
     * Constructor creates a new <code>ThirdPersonForwardAction</code> object.
     * During construction, the character to direct and the speed at which to
     * move the character is set.
     *
     * @param handler
     *            the associated handler
     * @param speed
     *            the speed at which the target can move forward
     */
    public BackwardAction(ThirdPersonPhysicsHandler handler, float speed) {
        this.handler = handler;
        this.speed = speed;
        rot = new Vector3f();
    }

    /**
     * <code>performAction</code> moves the node along it's positive
     * direction vector at a speed of movement speed * time. Where time is
     * the time between frames and 1 corresponds to 1 second.
     * @see com.jme.input.action.InputActionInterface#performAction(InputActionEvent)
     */
    public void performAction(InputActionEvent event) {
        if (handler.getPermitter() != null && !handler.getPermitter().canBeMoved())
            return;
        handler.setGoingBackwards(true);
        Vector3f loc = handler.getTarget().getLocalTranslation();
        if (handler.isCameraAlignedMovement()) {
            rot.set(handler.getCamera().getDirection());
            rot.y = 0;
        } else {
            handler.getTarget().getLocalRotation().getRotationColumn(2, rot);
        }
        rot.normalizeLocal();
        loc.subtractLocal(rot.multLocal((speed * event.getTime())));
    }

}