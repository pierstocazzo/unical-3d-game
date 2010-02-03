/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.input;

import game.common.GameConfiguration;
import game.common.KeyConverter;
import game.graphics.GraphicalPlayer;
import game.input.action.FirstPersonAction;
import game.input.action.ShootAction;
import game.input.action.NextWeaponAction;
import game.input.action.BackwardAction;
import game.input.action.ForwardAction;
import game.input.action.ThirdPersonJoystickPlugin;
import game.input.action.LeftAction;
import game.input.action.RightAction;
import game.input.action.RunAction;

import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.MouseLookHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * <code>ThirdPersonHandler</code> defines an InputHandler for
 * a third person view game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameInputHandler extends InputHandler {
    GraphicalPlayer target;

    /** The camera this handler uses for determining action movement. */
    Camera cam;
    
	MouseLookHandler mouseLookHandler;
	
	ChaseCamera chaser;
	
	boolean firstPerson;
	
	ThirdPersonJoystickPlugin plugin = null;
    
	/** Actions */
    InputAction actionForward;
    InputAction actionBack;
    InputAction actionRight;
    InputAction actionLeft;
	InputAction actionForwardRun;
	InputAction actionShoot;
	InputAction actionFirstPerson;
	InputAction actionNextWeapon;
	
	/**
	 * Check if the player can do movements
	 */
	boolean canMoveForward;
	boolean canMoveBackward;
	boolean canStrafeLeft;
	boolean canStrafeRight;
    
	
    public GameInputHandler( GraphicalPlayer target, Camera cam ) {
        this.target = target;
        this.cam = cam;

        setActions();
        setupChaseCamera();
    }

    /** 
     * <code>setActions</code> sets the keyboard actions with the
     * corresponding key command.
     */
    protected void setActions() {
    	actionForwardRun = new RunAction( this );
        actionForward = new ForwardAction( this );
        actionBack = new BackwardAction( this );
        actionRight = new RightAction( this );
        actionLeft = new LeftAction( this );
        actionShoot = new ShootAction( this );
        actionFirstPerson = new FirstPersonAction( this );
        actionNextWeapon = new NextWeaponAction( this );
                
        addAction( actionForwardRun, DEVICE_KEYBOARD, KeyConverter.toKey(GameConfiguration.getRunKey()), AXIS_NONE, false );
        addAction( actionForward, DEVICE_KEYBOARD, KeyConverter.toKey(GameConfiguration.getForwardKey()), AXIS_NONE, false );
        addAction( actionBack, DEVICE_KEYBOARD, KeyConverter.toKey(GameConfiguration.getBackwardKey()), AXIS_NONE, false );
        addAction( actionRight, DEVICE_KEYBOARD, KeyConverter.toKey(GameConfiguration.getTurnRightKey()), AXIS_NONE, false );
        addAction( actionLeft, DEVICE_KEYBOARD, KeyConverter.toKey(GameConfiguration.getTurnLeftKey()), AXIS_NONE, false );
        addAction( actionShoot, DEVICE_MOUSE, MouseButtonBinding.LEFT_BUTTON, AXIS_NONE, false );
        addAction( actionFirstPerson, DEVICE_MOUSE, MouseButtonBinding.RIGHT_BUTTON, AXIS_NONE, false );
        addAction( actionNextWeapon, DEVICE_MOUSE, BUTTON_NONE, 2, false );
        
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    }

    /**
     * <code>update</code> updates the position and rotation of the target
     * based on the movement requested by the user.
     * 
     * @param time
     * @see com.jme.input.InputHandler#update(float)
     */
    public void update(float time) {
        if ( !isEnabled() ) return;
        
        doInputUpdate(time);
        
        /** switch to first person view when the mouse right bottom is down
         */
        if ( isFirstPerson() ) {
        	chaser.setEnabled(false);
        	mouseLookHandler.setEnabled(true);
        	mouseLookHandler.update(time);
        	Vector3f camPos = target.getCharacterNode().getLocalTranslation().clone().addLocal( 0, 6, 0 );
        	cam.setLocation( camPos );
        	cam.update();
        	target.hide( true );
        } else { /** return to third person view */
    		chaser.update( time );
    		chaser.setEnabled(true);
        	mouseLookHandler.setEnabled(false);
        	target.hide( false );
        	target.setShooting( false );
        }

    }

	protected void doInputUpdate(float time) {
        super.update(time);
        updateFromJoystick(time);
    }

    protected void updateFromJoystick(float time) {
        if (plugin == null) return;
        float xAmnt = plugin.getJoystick().getAxisValue(plugin.getXAxis());
        float yAmnt = plugin.getJoystick().getAxisValue(plugin.getYAxis());
        
        InputActionEvent evt = new InputActionEvent();
        if (xAmnt > 0) {
            evt.setTime(time*xAmnt);
            actionRight.performAction(evt);
        } else if (xAmnt < 0) {
            evt.setTime(time*-xAmnt);
            actionLeft.performAction(evt);            
        }
        
        if (yAmnt > 0) {
            evt.setTime(time*yAmnt);
            actionBack.performAction(evt);
        } else if (yAmnt < 0) {
            evt.setTime(time*-yAmnt);
            actionForward.performAction(evt);            
        }
    }

    protected void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = 5;
        chaser = new ChaseCamera( cam, target.getCharacterNode() );
        chaser.setTargetOffset(targetOffset);
    }

    public GraphicalPlayer getPlayer() {
    	return target;
    }

    public void setRunning( boolean running ) {
		target.setRunning(running);
	}

	/**
     * Internal method used to let the handler know that the target is currently
     * moving forward (via use of the forward key.)
     * 
     * @param forward
     */
    public void setGoingForward(boolean forward) {
        target.setWalkingForward(forward);
    }

    /**
     * Internal method used to let the handler know that the target is currently
     * moving backwards (via use of the back key.)
     * 
     * @param backwards
     */
    public void setGoingBackwards(boolean backwards) {
        target.setWalkingBackwards(backwards);
    }

    /**
     * @return Returns the joystick plugin or null if not set.
     */
    public ThirdPersonJoystickPlugin getJoystickPlugin() {
        return plugin;
    }

    /**
     * @param plugin The joystick plugin to set.
     */
    public void setJoystickPlugin(ThirdPersonJoystickPlugin plugin) {
        this.plugin = plugin;
    }

	public void setFirstPerson( boolean firstPerson ) {
		this.firstPerson = firstPerson;
	}


	public boolean isFirstPerson() {
		return firstPerson;
	}
	
	public void setShooting( boolean shooting ) {
		target.setShooting( shooting );
	}
	
	public boolean isCanMoveForward() {
		return canMoveForward;
	}

	public void setCanMoveForward(boolean canMoveForward) {
		this.canMoveForward = canMoveForward;
	}

	public boolean isCanMoveBackward() {
		return canMoveBackward;
	}

	public void setCanMoveBackward(boolean canMoveBackward) {
		this.canMoveBackward = canMoveBackward;
	}

	public boolean isCanStrafeLeft() {
		return canStrafeLeft;
	}

	public void setCanStrafeLeft(boolean canStrafeLeft) {
		this.canStrafeLeft = canStrafeLeft;
	}

	public boolean isCanStrafeRight() {
		return canStrafeRight;
	}

	public void setCanStrafeRight(boolean canStrafeRight) {
		this.canStrafeRight = canStrafeRight;
	}

	public void setTurningLeft(boolean b) {
		target.setTurningLeft(b);
	}

	public void setTurningRight(boolean b) {
		target.setTurningRight(b);
	}

	public GraphicalPlayer getTarget() {
		return target;
	}

}
