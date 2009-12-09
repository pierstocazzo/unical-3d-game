package slashWork.game.graphics;

import com.jme.animation.AnimationController;
import com.jme.scene.Controller;
import com.jmex.model.animation.JointController;

/** Class <code>CustomAnimationController</code> <br>
 * 
 * An custom animation controller to easily run animations
 * with a JointController
 */
public class CustomAnimationController {

	JointController controller;
	
	Animation currentAnimation;

	Animation previousAntimation;

	
	public CustomAnimationController( Controller controller ) {
		this.controller = (JointController) controller;
		runAnimation( Animation.IDLE );
		controller.setActive( true );
		controller.setRepeatType( AnimationController.RT_WRAP );
		controller.setSpeed( 0.4f );
	}
	
	public void runAnimation( Animation animation ) {
		controller.setTimes( animation.startFrame, animation.endFrame );
		previousAntimation = currentAnimation;
		currentAnimation = animation;
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
		
		RUN ( 1, 14 ), 
		
		WALK ( 1, 14 ),
		
		IDLE ( 251, 276 ),
		
		JUMP ( 103, 111 ),
		
		DIE ( 230, 251 ),
		
		SHOOT ( 63, 64 );
		
		int startFrame; 
		
		int endFrame;

		Animation( int startFrame, int endFrame ) {
			this.startFrame = startFrame;
			this.endFrame = endFrame;
		}
	}
}
