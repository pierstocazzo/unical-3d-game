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

package game.core;

import game.common.WeaponType;
import game.common.*;
import java.io.Serializable;

/**
 * Class <code>LogicWeapon</code><br>
 * Represent the game weapons
 */
@SuppressWarnings("serial")
public class LogicWeapon implements Serializable {
	
	/** Identifier */
	String id;
	
	/** residue ammo */
	int ammo;
	
	/** weapon magazine's capacity */
	int magazineCapacity;
	
	/** Weapon's type */
	WeaponType type;
	
	/**
	 * <code>LogicWeapon</code> Constructor
	 * 
	 * @param id - (String) Identifier
	 * @param ammo - (int) initial ammunitions quantity
	 * @param type - (WeaponType) the weapon's type
	 */
	public LogicWeapon( String id, WeaponType type ) {
		this.id = id;
		this.type = type;
		
		switch( type ) {
		case AR15: 
			this.magazineCapacity = Integer.valueOf( GameConfiguration.getParameter("initial_ar15_capacity") );
			break;
			
		case GATLING: 
			this.magazineCapacity = Integer.valueOf( GameConfiguration.getParameter("initial_gatling_capacity") );
			break;
			
		case BAZOOKA: 
			this.magazineCapacity = Integer.valueOf( GameConfiguration.getParameter("initial_bazooka_capacity") );
			break;
		}
		
		this.ammo = this.magazineCapacity;
	}

	/**
	 * It get weapon's power
	 * 
	 * @return power
	 */
	public int getPower() {
		return type.getPower();
	}

	/**
	 * It gets damage of every bullet
	 * 
	 * @return damage
	 */
	public int getDamage() {
		return type.getDamage();
	}

	/**
	 * It returns remaining bullets
	 * 
	 * @return currBullets
	 */
	public int getAmmo() {
		return ammo;
	}

	/**
	 * Add ammo
	 * 
	 * @param ammo
	 */
	public void addAmmo( int addedAmmo ) {
		ammo = ammo + addedAmmo;
	}
	
	/**
	 * Decrease ammo
	 */
	public void decreaseAmmo() {
		if( ammo > 0 )
			ammo = ammo - 1;
	}
}
