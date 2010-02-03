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

import java.util.List;

import game.input.GameInputHandler;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;

public class CollisionHandler {
	
	/**
	 * Root node that contain all collidable object in the scene
	 */
	protected Node collisionNode;
	
	/**
	 * InputHandler of the player
	 */
	protected GameInputHandler inputHandler;
	
	/**
	 * The graphical node of the player
	 */
	protected Node player;
	
	/**
	 * CollisionHandler constructor
	 * 
	 * @param inputHandler
	 * @param collisionNode
	 */
	public CollisionHandler( GameInputHandler inputHandler, Node collisionNode ) {
		this.inputHandler = inputHandler;
		this.collisionNode = collisionNode;
		this.player = inputHandler.getPlayer().getCollision();
	}

	/**
	 * Function <code>update</code>
	 * this function handles the collision between
	 * player and all collidable objects
	 */
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
	
	/**
	 * Function <code>hasTriangleCollision</code>
	 * Check eventually collision
	 * 
	 * @param node1
	 * @param node2
	 * @return
	 */
	public boolean hasTriangleCollision( Node node1, Node node2 ) {
		List<TriMesh> node1Geometries = node1.descendantMatches( TriMesh.class );
		
		for ( TriMesh node1Geometry : node1Geometries ) {
			if ( hasTriangleCollision( node1Geometry, node2 ) )
				return true;
		}
		return false;
	}
	
	/**
	 * Function <code>hasTriangleCollision</code>
	 * Check eventually collision
	 * 
	 * @param node1Geometry
	 * @param node2
	 * @return
	 */
	public static boolean hasTriangleCollision( TriMesh node1Geometry, Node node2 ) {
		// list all mesh children of the given node
		List<TriMesh> node2Geometries = node2.descendantMatches( TriMesh.class );
		
		// check for collision of each of this meshes with the given mesh
		// return true as soon as it finds a mesh child of the given node colliding with the given mesh
		for ( TriMesh node2Geometry : node2Geometries ) {
			if ( node2Geometry.hasTriangleCollision( node1Geometry ) )
				return true;
		}
		// if no mesh child of node2 collide with the given geometry return false
		return false;
	}
}
