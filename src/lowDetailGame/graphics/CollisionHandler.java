package lowDetailGame.graphics;

import lowDetailGame.input.ThirdPersonHandler;

import com.jme.bounding.CollisionTree;
import com.jme.bounding.CollisionTreeManager;
import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionResults;
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
		if( inputHandler.getPlayer().getModel().getChild("front").hasCollision( collisionNode, false ) ) {
			inputHandler.setCanMoveForward(false);
			inputHandler.setGoingForward(false);
			System.out.println("COLLISIONE AVVENUTA!!!");
		} else {
			inputHandler.setCanMoveForward(true);
		}
		
		if( inputHandler.getPlayer().getModel().getChild("back").hasCollision( collisionNode, false ) ) {
			inputHandler.setCanMoveBackward(false);
			inputHandler.setGoingBackwards(false);
			System.out.println("COLLISIONE AVVENUTA!!!");
		} else {
			inputHandler.setCanMoveBackward(true);
		}
	}
	
}
