package game.graphics;


import game.enemyAI.Movement;

import game.core.LogicAmmoPack;
import game.core.State;
import game.core.WeaponType;

import java.util.HashMap;
import java.util.Set;

import com.jme.math.Vector3f;

/**
 * Interface
 */
public interface WorldInterface {

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

	public abstract Set<String> getCharactersIds();

	/** Function getPlayerId()
	 *  returns the identificator of the logic player
	 */
	public abstract Set<String> getPlayersIds();

	/** Function getPlayerId()
	 *  returns the identificators of the logic characters
	 */
	public abstract Set<String> getEnemiesIds();
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @param quantity
	 * @param position
	 */
	public abstract void createAmmoPack( String id, WeaponType type, int quantity, Vector3f position );

	/** Function characterShoot()
	 * call the shoot method of logic character
	 */
	public abstract void shoot( String id );
	
	/** Function characterShoot()
	 * call the isShooted method of logic character
	 * @param characterId 
	 */
	public abstract void shooted( String id, String characterId, int bulletDamage );
	
	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean isMoving( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setMoving(String id, boolean b);


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

	/** Function <code>printWorld</code><br>
	 * Print the world current situation
	 */
	public abstract String printWorld();
	
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

	public abstract boolean catchAmmoPack( String playerId, String ammoPackId );

	public abstract void catchEnergyPack( String playerId, String energyPackId );

	public abstract LogicAmmoPack getAmmoPack( String ammoPackId );

	public abstract HashMap<String, Vector3f> getEnergyPackagesPositions();
	
	public abstract void kill( String id );
	
	public abstract void killed( String id );

}
