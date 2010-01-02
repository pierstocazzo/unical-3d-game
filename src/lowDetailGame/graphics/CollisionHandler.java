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
		/** Check for front or back collision here */
		if( inputHandler.getPlayer().getCollision().getChild("frontal").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanMoveForward(false);
			inputHandler.setGoingForward(false);
		} else if( inputHandler.getPlayer().getCollision().getChild("backy").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanMoveBackward(false);
			inputHandler.setGoingBackwards(false);
		} else {
			inputHandler.setCanMoveForward(true);
			inputHandler.setCanMoveBackward(true);
		}
		
		/** Check for lateral collision */
		if( inputHandler.getPlayer().getCollision().getChild("left").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanStrafeLeft(false);
			inputHandler.setTurningLeft(false);
		} else if( inputHandler.getPlayer().getCollision().getChild("right").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanStrafeRight(false);
			inputHandler.setTurningRight(false);
		} else {
			inputHandler.setCanStrafeLeft(true);
			inputHandler.setCanStrafeRight(true);
		}
	}
	
}
