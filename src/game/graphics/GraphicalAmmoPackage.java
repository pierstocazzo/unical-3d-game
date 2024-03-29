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

import game.HUD.HudMessageHandler;
import game.HUD.UserHud;
import utils.Util;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

/**
 * A graphical ammo package
 * <br>
 * The graphical (physics) representation of ammo package 
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GraphicalAmmoPackage {
 
	/**
	 * The identifier of AmmoPackage
	 */
	String id;
	
	/**
	 * The static physics node where attach the pack
	 */
	StaticPhysicsNode physicsPack;
	
	/**
	 * The box that represents the pack
	 */
	Box pack;
	
	/**
	 * Pointer to graphical world
	 */
	GraphicalWorld world;
	
	/**
	 * Position of ammo package
	 */
	Vector3f position;
	
	/**
	 * Contact detection handler
	 */
	InputHandler contactDetect;
	
	boolean contact = false;

	boolean enabled;
	
	/**
	 * Constructor for GraphicalAmmoPackage
	 * 
	 * @param (String) id - The Identifier
	 * @param (GraphicalWorld) world - pointer to GraphicalWorld
	 * @param (Vector3f) position - the position of pack
	 */
	GraphicalAmmoPackage( String id, GraphicalWorld world, Vector3f position ) {
		this.id = id;
		this.world = world;
		this.position = new Vector3f( position );
		this.contactDetect = new InputHandler();
		createPhysics();
		contactDetector();
		enabled = true;
	}
	
	/**
	 * Initialize the physics of ammo package
	 */
	public void createPhysics() {
		physicsPack = world.getPhysicsSpace().createStaticNode();
		world.getRootNode().attachChild( physicsPack );
		physicsPack.getLocalTranslation().set( position );
		pack = new Box( id, new Vector3f(), 1, 1, 1 );
		
		Texture texture = TextureManager.loadTexture( Util.load( "game/data/textures/ammo.jpg" ),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear);
	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    ts.setEnabled(true);
	    ts.setTexture(texture);
		pack.setRenderState(ts);
		pack.updateRenderState();
		pack.lockShadows();
		pack.lockMeshes();
		physicsPack.attachChild( pack );
		physicsPack.generatePhysicsGeometry();
	}
	
	/**
	 * Verify a contact between it and a character 
	 */
	public void contactDetector() {
        SyntheticButton ammoPackCollisionHandler = physicsPack.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {

        	public void performAction( InputActionEvent evt ) {
        		ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
        		
        		for( GraphicalCharacter character : world.characters ) {
    				if ( contactInfo.getNode1() == character.getCharacterBody() || 
    					 contactInfo.getNode2() == character.getCharacterBody() ) {

    					if( world.getCore().catchAmmoPack( character.id, id ) ) {
    						deletePackage();
    						return;
    					} else {
    						UserHud.addMessage( HudMessageHandler.MAX_AMMO  );
    						System.out.println(id);
    					}
    				}
        		}
        	}
        };
        
        contactDetect.addAction( collisionAction, ammoPackCollisionHandler, false );
	}

	/**
	 * Detach from rootNode
	 */
	public void deletePackage() {
		physicsPack.removeFromParent();
		physicsPack.detachAllChildren();
		physicsPack.delete();
		enabled = false;
	}
	
	/**
	 * Update the contact handler
	 * @param time
	 */
	public void update(float time) {
		if( physicsPack.isActive() )
			contactDetect.update(time);
	}
}
