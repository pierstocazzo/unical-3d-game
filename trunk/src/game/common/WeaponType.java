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

package game.common;

import java.io.Serializable;

import game.common.GameConf;

/**
 * Class WeaponType
 * <br>
 * Describres various type of weapon
 * with it properties
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public enum WeaponType implements Serializable {
	
	/** mp5 weapon */
	AR15 (
		Integer.valueOf( GameConf.getParameter( GameConf.WEAPON_AR15_DAMAGE ) ),
		Integer.valueOf( GameConf.getParameter( GameConf.WEAPON_AR15_POWER ) ),
		Float.valueOf( GameConf.getParameter( GameConf.WEAPON_AR15_LOADTIME ) ),
		false
	),
	
	/** not used */
	GATLING (
		Integer.valueOf( GameConf.getParameter( GameConf.WEAPON_GATLING_DAMAGE ) ),
		Integer.valueOf( GameConf.getParameter( GameConf.WEAPON_GATLING_POWER ) ),
		Float.valueOf( GameConf.getParameter( GameConf.WEAPON_GATLING_LOADTIME ) ),
		true
	),
	
	/** not used */
	BAZOOKA (
		Integer.valueOf( GameConf.getParameter( GameConf.WEAPON_BAZOOKA_DAMAGE ) ),
		Integer.valueOf( GameConf.getParameter( GameConf.WEAPON_BAZOOKA_POWER ) ),
		Float.valueOf( GameConf.getParameter( GameConf.WEAPON_BAZOOKA_LOADTIME ) ),
		true
	);
	
	/** <code>WeaponType</code> field */
	int damage, power;

	float loadTime;
	
	/** not used */
	boolean isHeavy;
	
	/**
	 * <code>WeaponType</code> constructor<br>
	 * Create a weapon type and set its attributes.
	 * @param damage - (int) the damage the weapon cause to a character
	 * @param power - (int) the shoot power of the weapon
	 * @param isHeavy - (bool) if true the character can't run when using this weapon
	 */
	WeaponType ( int damage, int power, float loadTime, boolean isHeavy ) {
		this.damage = damage;
		this.power = power;
		this.isHeavy = isHeavy;
		this.loadTime = loadTime;
	}
	
	/**
	 * Return current weapon power
	 * 
	 * @return (int)
	 */
	public int getPower() {
		return this.power;
	}
	
	/**
	 * Return current weapon damage
	 * 
	 * @return (int)
	 */
	public int getDamage() {
		return this.damage;
	}

	/**
	 * Return current weapon load time
	 * 
	 * @return (long)
	 */
	public float getLoadTime() {
		return this.loadTime;
	}

	public boolean isHeavy() {
		return isHeavy;
	}
}