package game.graphics;

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

	
	public CustomAnimationController( Controller controller ) {
		this.controller = (JointController) controller;
		runAnimation( Animation.IDLE );
		controller.setActive( true );
		controller.setRepeatType( AnimationController.RT_CYCLE );
		controller.setSpeed( 0.25f );
	}
	
	public void runAnimation( Animation animation ) {
		controller.setTimes( animation.startFrame, animation.endFrame );
		currentAnimation = animation;
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
		
		RUN ( 16, 26 ), 
		
		WALK ( 2, 14 ),
		
		IDLE ( 327, 360 ),
		
		JUMP ( 42, 54 );
		
		
		int startFrame; 
		
		int endFrame;

		Animation( int startFrame, int endFrame ) {
			this.startFrame = startFrame;
			this.endFrame = endFrame;
		}
	}
}
