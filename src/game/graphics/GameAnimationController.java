package game.graphics;

import com.jme.animation.AnimationController;
import com.jme.scene.Controller;
import com.jmex.model.animation.JointController;

/** Class <code>CustomAnimationController</code> <br>
 * 
 * An custom animation controller to easily run animations
 * with a JointController
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
		
		RUN ( 15, 28, 0.7f ), 
		
		WALK ( 1, 14, 0.4f ),
		
		IDLE ( 251, 276, 0.3f ),
		
		JUMP ( 103, 111, 0.7f ),
		
		DIE ( 230, 251, 1f ),
		
		SHOOT ( 63, 64, 0.4f );
		
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
