package game.graphics;

import java.util.List;

import game.input.ThirdPersonHandler;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;

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
		if( hasTriangleCollision( (TriMesh) player.getChild("frontal"), collisionNode ) ) {
			inputHandler.setCanMoveForward(false);
			inputHandler.setGoingForward(false);
		} else if( hasTriangleCollision( (TriMesh) player.getChild("backy"), collisionNode ) ) {
			inputHandler.setCanMoveBackward(false);
			inputHandler.setGoingBackwards(false);
		} else {
			inputHandler.setCanMoveForward(true);
			inputHandler.setCanMoveBackward(true);
		}
		
		/** Check for lateral collision */
		if( hasTriangleCollision( (TriMesh) player.getChild("left"), collisionNode ) ) {
			inputHandler.setCanStrafeLeft(false);
			inputHandler.setTurningLeft(false);
		} else if( hasTriangleCollision( (TriMesh) player.getChild("right"), collisionNode ) ) {
			inputHandler.setCanStrafeRight(false);
			inputHandler.setTurningRight(false);
		} else {
			inputHandler.setCanStrafeLeft(true);
			inputHandler.setCanStrafeRight(true);
		}
	}
	public boolean hasTriangleCollision( Node n1, Node nodeWithSharedNodes )
	{
		List<TriMesh> geosN1 = n1.descendantMatches( TriMesh.class );
		for (TriMesh triN1 : geosN1)
		{
			if (hasTriangleCollision(triN1, nodeWithSharedNodes))
				return true;
		}
		return false;
	}
	
	public boolean hasTriangleCollision(TriMesh sp,Node nodeWithSharedNodes)
	{
		List<TriMesh> geosN2 = nodeWithSharedNodes.descendantMatches(TriMesh.class);
		
		for (TriMesh triN2 : geosN2)
		{
			if (triN2.hasTriangleCollision(sp))
				return true;
		}
		return false;
	}
}
