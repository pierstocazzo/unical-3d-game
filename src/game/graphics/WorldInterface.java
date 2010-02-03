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

import game.common.Movement;
import game.common.State;
import game.common.WeaponType;

import java.util.Set;

import com.jme.math.Vector3f;

/**
 * Interface
 */
public interface WorldInterface {

	/** Function <code>getCurrentLife</code><br>
	 * 
	 * @param id - (String) the character's id
	 * @return current life of the character with this id
	 */
	public abstract int getCurrentLife( String id );
	
	/** Function <code>setCharacterPosition</code><br>
	 * Update the position of the character with this id
	 *  
	 * @param id - (String) character's id
	 * @param position - (Vector3f) new position
	 */
	public abstract void setPosition( String id, Vector3f position );

	/** Function <code>getCharacterPosition</code><br>
	 * return the position of the character with this id
	 *
	 * @param id - (String) the character's id
	 * @return the position of the character with this id
	 */
	public abstract Vector3f getPosition( String id );

	/** Function getPlayerId()
	 *  @return the identifiers of the logic characters
	 */
	public abstract Set<String> getCharactersIds();

	/** Function getPlayerId()
	 *  @return the identifiers of the logic players
	 */
	public abstract Set<String> getPlayersIds();

	/** Function getPlayerId()
	 *  @return the identifiers of the logic enemies
	 */
	public abstract Set<String> getEnemiesIds();
	
	/** Function shoot() <br>
	 * call the shoot method of logic character
	 * 
	 * @param id - (String) the character's id
	 */
	public abstract boolean shoot( String id );
	
	/** Function characterShoot() <br>
	 * call the isShooted method of logic character
	 * @param characterId 
	 */
	public abstract void shooted( String id, String characterId, int bulletDamage );
	
	/** Function isMoving <br>
	 * 
	 * @param id - (String) the character's id
	 * @return true if the character with this id is moving
	 */
	public abstract boolean isMoving( String id );

	/** Function setMoving<br>
	 * 
	 * @param id - (String) the character's id
	 * @param moving - (boolean) 
	 */
	public abstract void setMoving( String id, boolean moving );

	/** Function setJumping <br>
	 * 
	 * @param id
	 * @param jumping
	 */
	public abstract void setJumping( String id, boolean jumping );

	/**
	 *
	 * @param id - (String) the id of the enemy
	 */
	public abstract Movement getNextMovement( String id );

	/**
	 *
	 * @param id - (String) the id of the enemy
	 * @return
	 */
	public abstract Movement getCurrentMovement( String id );

	/**
	 * 
	 * @param id - (String) the id of the enemy
	 * @return The type of weapon of this character
	 */
	public abstract WeaponType getWeapon( String id );

	/**
	 * 
	 * @param id - (String) the id of the character
	 * @return true if this character is alive
	 */
	public abstract boolean isAlive( String id );

	/** 
	 * Update the state of this enemy
	 * @param id - (String) the id of the enemy
	 */
	public abstract void updateState( String id );

	/**
	 * 
	 * @param id - (String) the id of the enemy
	 * @return The shoot direction of this enemy
	 */
	public abstract Vector3f getShootDirection( String id );

	/** 
	 * 
	 * @param id - (String) the id of the enemy
	 * @return The current state of this enemy
	 */
	public abstract State getState( String id );

	/**
	 * 
	 * @param playerId
	 * @param ammoPackId
	 * @return
	 */
	public abstract boolean catchAmmoPack( String playerId, String ammoPackId );

	/**
	 * 
	 * @param playerId
	 * @param energyPackId
	 */
	public abstract boolean catchEnergyPack( String playerId, String energyPackId );

	/** Function kill <br>
	 * Increase the score of the player who kill an enemy
	 * @param id - (String) the player's id
	 */
	public abstract void kill( String id );
	
	/** Function killed <br>
	 * Decrease the score of the player who was killed
	 * @param id - (String) the player's id
	 */
	public abstract void killed( String id );

	/** Function getLevel
	 * 
	 * @param id - (String) the player's id
	 * @return the player's level
	 */
	public abstract int getLevel( String id );

	/** Function getScore
	 * 
	 * @param id - (String) the player's id
	 * @return the player's score
	 */
	public abstract int getScore( String id );

	/** 
	 * 
	 * @param playerId
	 * @return player's ammo
	 */
	public abstract int getAmmo( String playerId );

	/** Function getEnergyPackagesIds <br>
	 * 
	 * @return all energy packages ids
	 */
	public abstract Set<String> getEnergyPackagesIds();
	
	public abstract float getAlertLevel( String id );

	public abstract void setState( String id, State state );
	
	public abstract int getMaxLife( String id );
	
	public abstract int getMaxAmmo( String id );

	public abstract boolean firstFind( String id );
	
	public abstract void setFirstFind( String id, boolean firstFind );

	public abstract boolean comeBack( String id );

	public abstract void setComeBack( String id, boolean comeBack );
	
	public abstract boolean isReborn( String id );
	
	public abstract void setReborn( String id , boolean value );
	
	public abstract boolean showLevel2Message( String id );

	public abstract void setShowLevel2Message( String id, boolean b);

	public abstract Vector3f getMoveDirection( String id );

	public abstract void nextWeapon( String id );

	public abstract void canSeePlayer( String id, boolean b );

}
