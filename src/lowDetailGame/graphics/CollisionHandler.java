package lowDetailGame.graphics;

import lowDetailGame.input.ThirdPersonHandler;
import com.jme.scene.Node;

public class CollisionHandler {
	
	protected Node collisionNode;
	protected ThirdPersonHandler inputHandler;
	
	public CollisionHandler( ThirdPersonHandler inputHandler, Node collisionNode ) {
		this.inputHandler = inputHandler;
		this.collisionNode = collisionNode;
	}

	public void update() {
		/** Check for collision here */
		if( inputHandler.getPlayer().getModel().getChild("frontal").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanMoveForward(false);
			inputHandler.setGoingForward(false);
		} else if( inputHandler.getPlayer().getModel().getChild("backy").hasCollision( collisionNode, true )) {
			inputHandler.setCanMoveBackward(false);
			inputHandler.setGoingBackwards(false);
		} else {
			inputHandler.setCanMoveForward(true);
			inputHandler.setCanMoveBackward(true);
		}
	}
	
}
