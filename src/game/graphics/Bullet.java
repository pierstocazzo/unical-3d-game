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

import game.common.WeaponType;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.material.Material;

/**
 * A graphical bullet
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class Bullet {
	/** the bullet physicsNode */
	DynamicPhysicsNode physicsBullet;
	
	/** an freeCamInput handler used to detect bullet's collisions */
	InputHandler contactDetect = new InputHandler();
	
	/** the graphical world witch contains the bullet */
	GraphicalWorld world;
	
	/** the initial position of the bullet */
	Vector3f position;
	
	/** the type of the weapon witch shoot the bullet */
	WeaponType weaponType;
	
	/** the id of the character who shoot this bullet */
	String shooterId;
	
	boolean enabled;
	
	Sphere bullet;
	
	/** PhysicsBullet constructor <br>
	 * 
	 * @param shooterId - (String) the id of the character who shoot this bullet
	 * @param world - (GraphicalWorld) the graphical world witch contains the bullet
	 * @param direction - (Vector3d) the direction of the shoot 
	 * @param weaponType - (WeaponType) the type of the weapon witch shoot the bullet
	 * @param position - (Vector3f) the position where to create the bullet
	 */
	public Bullet( String shooterId, GraphicalWorld world, WeaponType weaponType, Vector3f position ) {
		this.shooterId = shooterId;
		this.world = world;
		this.position = new Vector3f( position );
		this.weaponType = weaponType;
		this.enabled = true;
	}
	
	/**
	 * Execute the graphical shoot of the bullet
	 * 
	 * @param direction
	 */
	public void shoot( Vector3f direction ) {
		physicsBullet = world.getPhysicsSpace().createDynamicNode();
		physicsBullet.setMaterial( Material.GHOST );
		world.getRootNode().attachChild( physicsBullet );
		physicsBullet.getLocalTranslation().set( position );
		
		bullet = new Sphere( "bullet", 10, 10, 0.05f );
		physicsBullet.attachChild( bullet );
		physicsBullet.generatePhysicsGeometry();
		bullet.lockBounds();
		bullet.lockMeshes();
		bullet.lockShadows();
		physicsBullet.addForce( direction.mult( weaponType.getPower() ) );
		bullet.setModelBound(new BoundingBox() );
		bullet.updateModelBound();
		contactDetection();
	}
	
	/** Function used to detect a contact between the bullet and something
	 *  when a contact is detected, the bullet is detached from the rootnode
	 */
	void contactDetection() {
        SyntheticButton collisionHandler = physicsBullet.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {

			public void performAction( InputActionEvent evt ) {
        		ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
        		physicsBullet.removeFromParent();
        		physicsBullet.delete();
        		enabled = false;
        		/** Control if the bullet hit a character */
        		for( GraphicalCharacter character : world.characters ) {
    				if( ( contactInfo.getNode1() == character.getCharacterBody() ||
    					  contactInfo.getNode2() == character.getCharacterBody() ) ||
    					( contactInfo.getNode1() == character.getCharacterFeet() ||
    					  contactInfo.getNode2() == character.getCharacterFeet() ) ){

    					world.getCore().shooted( character.id, shooterId, weaponType.getDamage() );
    				}
        		}
        	}
        };
        
        /** the action to do when a collision is detected */
        contactDetect.addAction( collisionAction, collisionHandler, false );
    }
	
	/** ContactDetect update
	 * @param time
	 */
	public void update( float time ) {
		contactDetect.update(time);
	}
}
