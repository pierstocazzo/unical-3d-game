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

import java.io.Serializable;

import com.jme.math.Vector3f;

/** Class <code>LogicAmmo</code> <br>
 * Represent pack of ammunitions the player can catch and use
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class LogicAmmoPack implements Serializable {

	/** type of the weapon which can use this ammo */
	WeaponType type;
	
	/** quantity of ammunition */
	int value;
	
	/** the position of the ammo pack */
	Vector3f position;
	
	/** <code>LogicAmmo</code> constructor <br>
	 * Create a new ammo pack
	 * 
	 * @param type - (EnumWeaponType) type of the weapon whitch can use this ammo
	 * @param quantity - (int) quantity of ammunition the pack contains
	 * @param position - (Vector3f) position of the ammo pack
	 */
	public LogicAmmoPack( WeaponType type, int quantity, Vector3f position ){
		this.type = type;
		this.value = quantity;
		this.position = position;
	}
	
	/**
	 * Return ammo quantity
	 * 
	 * @return (int)
	 */
	public int getQuantity() {
		return value;
	}

	/**
	 * Return the position of current ammo pack
	 * 
	 * @return {@link Vector3f}
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Return weapon type of current ammo
	 * 
	 * @return {@link WeaponType}
	 */
	public WeaponType getType() {
		return type;
	}
}