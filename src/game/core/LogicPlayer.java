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

import game.common.Movement;
import game.common.WeaponType;
import game.core.ScoreManager.Score;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.jme.math.Vector3f;

/**
 * Class LogicPlayer
 * <br>
 * Logic representation of the player
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class LogicPlayer extends LogicCharacter implements Serializable {

	/** Player's equipment */
	LinkedHashMap<Integer, LogicWeapon> equipment;
	
	/** number of weapons in the player's equipment */
	int weaponCounter;
	
	/** Weapon in use */
	LogicWeapon currentWeapon;
	int currentWeaponIndex = 1;
	
	boolean reborn = false;
	
	Score score;
	
	/**
	 * LogicPlayer Constructor <br>
	 * Create a new Player
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicPlayer( String id, int maxLife , Vector3f position, LogicWorld world ) {
		super( id, maxLife, position, world );
		equipment = new LinkedHashMap<Integer, LogicWeapon>();
		this.weaponCounter = 0;
		/** Initially the player has just the AR15 */
		LogicWeapon ar15 = new LogicWeapon( super.id + "ar15", WeaponType.AR15 );
		LogicWeapon gatling = new LogicWeapon( super.id + "gatling", WeaponType.GATLING );
		
		LogicWeapon bazooka = new LogicWeapon( super.id + "bazooka", WeaponType.RPG );
		addWeapon( ar15 );
		addWeapon( gatling );
		addWeapon( bazooka );
		
		/** assign the mp5 as current weapon */
		currentWeapon = ar15;
		
		this.score = new Score();
	}
	
	/** Function <code>addWeapon</code><br>
	 * 
	 * Add a new weapon to the player equipment.
	 * @param weapon - (LogicWeapon) the weapon to add
	 */
	public void addWeapon( LogicWeapon weapon ) {
		// the player can't carry more than three weapons
		if( weaponCounter < 3 ) {
			weaponCounter = weaponCounter + 1;
			weapon.id = weapon.id + weaponCounter;
			equipment.put( weaponCounter, weapon );
		} 
		// advise the player he can't carry more than three weapons
	}
	
	/**
	 * Function <code>addAmmo</code><br>
	 * 
	 * Increase the  amount of ammo of a certain weapon
	 * @param weaponType - (WeaponType) Ammo weapon type
	 * @param quantity - (Integer) The quantity of ammo to add
	 */
	public boolean addAmmo( WeaponType weaponType, int quantity ) {
		for( LogicWeapon weapon : equipment.values() ) {
			if( weapon.type == weaponType ) {
				if( weapon.ammo >= weapon.magazineCapacity ) {
					return false;
				} else {
					weapon.addAmmo( quantity );
					if( weapon.ammo > weapon.magazineCapacity )
						weapon.ammo = weapon.magazineCapacity;
					return true;
				}
			}
		}
		return false;
	}
	
	/** 
	 * Just decrease ammunitions
	 */
	public boolean shoot() {
		if( currentWeapon.ammo > 0 ) { 
			currentWeapon.decreaseAmmo();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Movement getNextMovement() {
		return null;
	}

	@Override
	public WeaponType getCurrentWeapon() {
		return currentWeapon.type;
	}
	
	@Override
	/**
	 * Function called everytime a player die
	 */
	public void die( String shooterId ) {
		world.scoreManager.playerKilled(id);
		if(world.scoreManager.getScore(id)<=10){
			world.killed(id);
			super.die( shooterId );
		}
		else{
			reborn = true;
			reborn();
		}
	}
	
	/**
	 * Regenerate the life value of the player
	 * at the maximum value.
	 */
	public void reborn(){
		currentLife = maxLife;
	}
	
	/**
	 * Return the magazine capacity of
	 * current weapons
	 * @return
	 */
	public int getMaxAmmo() {
		return currentWeapon.magazineCapacity;
	}

	/**
	 * Switch at the next weapon that player carry on
	 */
	public void nextWeapon() {
		currentWeaponIndex++;
		if( currentWeaponIndex < 4 ) {
			currentWeapon = equipment.get(currentWeaponIndex);
		} else {
			currentWeaponIndex = 1;
			currentWeapon = equipment.get(currentWeaponIndex);
		}
	}
}
