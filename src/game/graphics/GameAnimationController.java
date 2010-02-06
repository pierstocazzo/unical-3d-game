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

package game.graphics;

import com.jme.animation.AnimationController;
import com.jme.scene.Controller;
import com.jmex.model.animation.JointController;

/** Class <code>CustomAnimationController</code> <br>
 * 
 * An custom animation controller to easily run animations
 * with a JointController
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameAnimationController {

	JointController controller;
	
	Animation currentAnimation;

	public GameAnimationController( Controller controller ) {
		this.controller = (JointController) controller;
		runAnimation( Animation.IDLE );
		controller.setActive( true );
		controller.setRepeatType( AnimationController.RT_WRAP );
		controller.setSpeed( currentAnimation.speed );
	}
	
	public void runAnimation( Animation animation ) {
		if( currentAnimation == animation )
			return;
		
		controller.setTimes( animation.startFrame, animation.endFrame );
		currentAnimation = animation;
		controller.setSpeed( currentAnimation.speed );
		if( currentAnimation == Animation.DIE ) {
			controller.setRepeatType( AnimationController.RT_CLAMP );
		}
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation( Animation currentAnimation ) {
		this.currentAnimation = currentAnimation;
	}

	public JointController getController() {
		return controller;
	}

	public void setController(JointController controller) {
		this.controller = controller;
	}
	
	/** A utility enum for easily set most used animations 
	 */
	 public enum Animation {
		
		RUN ( 46, 66, 1.2f ), 
		
		WALK ( 25, 45, 0.7f ),
		
		IDLE ( 1, 24, 0.3f ),
		
		JUMP ( 68, 68, 0.7f ),
		
		DIE ( 68, 68, 1f ),
		
		SHOOT ( 68, 68, 1f );
		
		int startFrame; 
		
		int endFrame;

		float speed;

		Animation( int startFrame, int endFrame, float speed ) {
			this.startFrame = startFrame;
			this.endFrame = endFrame;
			this.speed = speed;
		}
	}
}
