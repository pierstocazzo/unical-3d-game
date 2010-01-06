package game.graphics;

import game.input.ThirdPersonHandler;
import com.jme.scene.Node;

public class CollisionHandler {
	
	protected Node collisionNode;
	protected ThirdPersonHandler inputHandler;
	protected Node player;
	
	public CollisionHandler( ThirdPersonHandler inputHandler, Node collisionNode ) {
		this.inputHandler = inputHandler;
		this.collisionNode = collisionNode;
		this.player = inputHandler.getPlayer().getCollision();
	}

	public void update() {
		/** Check for front or back collision here */
		if( player.getChild("frontal").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanMoveForward(false);
			inputHandler.setGoingForward(false);
		} else if( player.getChild("backy").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanMoveBackward(false);
			inputHandler.setGoingBackwards(false);
		} else {
			inputHandler.setCanMoveForward(true);
			inputHandler.setCanMoveBackward(true);
		}
		
		/** Check for lateral collision */
		if( player.getChild("left").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanStrafeLeft(false);
			inputHandler.setTurningLeft(false);
		} else if( player.getChild("right").hasCollision( collisionNode, true ) ) {
			inputHandler.setCanStrafeRight(false);
			inputHandler.setTurningRight(false);
		} else {
			inputHandler.setCanStrafeLeft(true);
			inputHandler.setCanStrafeRight(true);
		}
	}
}
