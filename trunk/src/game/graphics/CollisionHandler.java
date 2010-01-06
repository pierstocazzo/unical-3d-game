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
	
	public boolean hasTriangleCollision( Node node1, Node node2 ) {
		List<TriMesh> node1Geometries = node1.descendantMatches( TriMesh.class );
		
		for ( TriMesh node1Geometry : node1Geometries ) {
			if ( hasTriangleCollision( node1Geometry, node2 ) )
				return true;
		}
		return false;
	}
	
	public boolean hasTriangleCollision( TriMesh node1Geometry, Node node2 ) {
		// list all mesh children of the given node
		List<TriMesh> node2Geometries = node2.descendantMatches( TriMesh.class );
		
		// ceck for collision of each of this meshes with the given mesh
		// return true as soon as it finds a mesh child of the given node colliding with the given mesh
		for ( TriMesh node2Geometry : node2Geometries ) {
			if ( node2Geometry.hasTriangleCollision( node1Geometry ) )
				return true;
		}
		// if no mesh child of node2 collide with the given geometry return false
		return false;
	}
}
