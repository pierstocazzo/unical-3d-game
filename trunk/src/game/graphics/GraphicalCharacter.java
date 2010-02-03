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

import com.jme.input.InputHandler;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;

public class GraphicalCharacter {

	/** the character identifier */
	public String id;
  
    /** the main node of the character */
    Node characterNode;
    
	/** The body of the character: a Capsule that contains the model */
    DynamicPhysicsNode body; 
    
    /** 3d model applied to the character */
    Node model;
    
	/** animation controller */
	GameAnimationController animationController;
	
	/** the graphical world in which the character live */
    GraphicalWorld world;
    
    /** an util handler that detect the contact between the character and the ground */
    InputHandler contactDetect = new InputHandler();
    
    /** true if the character is shooting */
    boolean shooting = false;

    /** when was shooted the last bullet */
	float previousShootTime;

	/** set if the character will update or not */
	boolean enabled = true;
	
	/**
	 * @return the main character Node
	 */
	public Node getCharacterNode() {
		return characterNode;
	}

	public void update(float time) {
		// overridden 
	}

	/** 
	 * @return the physics body node
	 */
	public PhysicsNode getCharacterBody() {
		return body;
	}

	/** 
	 * @return enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled 
	 */
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public void hideModel() {
		model.removeFromParent();
	}

	public void showModel() {
		body.attachChild(model);
	}

	public PhysicsNode getCharacterFeet() {
		return body;
	}
}