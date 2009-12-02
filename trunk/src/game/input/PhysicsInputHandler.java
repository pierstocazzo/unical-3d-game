package game.input;

import game.input.action.FirstPersonAction;
import game.input.action.PhysicsBackwardAction;
import game.input.action.PhysicsForwardAction;
import game.input.action.PhysicsJumpAction;
import game.input.action.PhysicsStrafeLeftAction;
import game.input.action.PhysicsStrafeRightAction;
import game.input.action.ShootAction;

import java.util.HashMap;

import game.graphics.PhysicsCharacter;

import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseLookHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class PhysicsInputHandler extends InputHandler {
    PhysicsCharacter target;
    Camera cam;
    ChaseCamera chaser; 
    
    InputAction forwardAction;
    InputAction backwardAction;
    InputAction strafeLeftAction;
    InputAction strafeRightAction;
    InputAction jumpAction;
    InputAction freeCamAction;
    MouseInputAction shootAction;
	MouseInputAction firstPersonAction;
	
	MouseLookHandler mouseLookHandler;
	
	Vector3f targetOffSet;
	
    public PhysicsInputHandler( PhysicsCharacter target, Camera cam ) {
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    	
    	this.target = target;
        this.cam = cam;
        this.chaser = new ChaseCamera( cam, target.getCharacterNode() );
        this.targetOffSet = new Vector3f();
        this.targetOffSet.setY(5);
        
        setActions();
        setupChaseCamera();
    }

    private void setActions() {
        forwardAction = new PhysicsForwardAction( this );
        backwardAction = new PhysicsBackwardAction( this );
        strafeLeftAction = new PhysicsStrafeLeftAction( this );
        strafeRightAction = new PhysicsStrafeRightAction( this );
        jumpAction = new PhysicsJumpAction( this );
        shootAction = new ShootAction( this );
        firstPersonAction = new FirstPersonAction( this );
        freeCamAction = new InputAction() {
			
			@Override
			public void performAction(InputActionEvent evt) {
				if( evt.getTriggerPressed() )
					target.toggleFreeCam();
			}
		};
        
		addAction( freeCamAction, DEVICE_KEYBOARD, KeyInput.KEY_C, AXIS_NONE, false );
        addAction( forwardAction, DEVICE_KEYBOARD, KeyInput.KEY_W, AXIS_NONE, false );
        addAction( backwardAction, DEVICE_KEYBOARD, KeyInput.KEY_S, AXIS_NONE, false );
        addAction( strafeLeftAction, DEVICE_KEYBOARD, KeyInput.KEY_A, AXIS_NONE, false );
        addAction( strafeRightAction, DEVICE_KEYBOARD, KeyInput.KEY_D, AXIS_NONE, false );
        addAction( jumpAction, DEVICE_KEYBOARD, KeyInput.KEY_SPACE, AXIS_NONE, false );
        addAction( shootAction, DEVICE_MOUSE, MouseButtonBinding.LEFT_BUTTON, AXIS_NONE, false );
        addAction( firstPersonAction, DEVICE_MOUSE, MouseButtonBinding.RIGHT_BUTTON, AXIS_NONE, false );
    }
    
    @Override
    public void update(float time) {
        if ( !isEnabled() ) 
        	return;
        
        /** Process all input triggers and change internal state variables */
        super.update(time);
        
        /** switch to first person view when the mouse right bottom is down
         */
        if ( target.isFirstPerson() ) {
        	chaser.setEnabled(false);
        	mouseLookHandler.setEnabled(true);
        	mouseLookHandler.update(time);
        	cam.setLocation( target.getCharacterFeet().getWorldTranslation().add(this.targetOffSet) );
        	target.hide( true );
        } else { /** return to third person view */
    		chaser.update( time );
    		chaser.setEnabled(true);
        	mouseLookHandler.setEnabled(false);
        	target.hide( false );
        }
    }

    public PhysicsCharacter getTarget() {
        return target;
    }

    public Camera getCam() {
        return cam;
    }

    protected void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = getTarget().getCharacterBody().getLocalTranslation().y + 5;
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "30");
        props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "10");
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        props.put(ThirdPersonMouseLook.PROP_MAXASCENT, "" + 20 * FastMath.DEG_TO_RAD);
        props.put(ThirdPersonMouseLook.PROP_MINASCENT, "" + 5 * FastMath.DEG_TO_RAD);
        props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0, 30 * FastMath.DEG_TO_RAD));
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
//        props.put(ThirdPersonMouseLook.PROP_INVERTEDY, false );
        chaser = new ChaseCamera(cam, getTarget().getCharacterBody(), props);
        chaser.setMaxDistance(50);
        chaser.setMinDistance(15);
    }
}
