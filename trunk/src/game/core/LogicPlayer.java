package game.core;

import game.common.Movement;
import game.common.WeaponType;

import java.io.Serializable;
import java.util.HashMap;

import com.jme.math.Vector3f;

/**
 * Class LogicPlayer
 * 
 * @author Andrea
 */
@SuppressWarnings("serial")
public class LogicPlayer extends LogicCharacter implements Serializable {

	/** Player's equipment */
	HashMap< String, LogicWeapon > equipment = new HashMap<String, LogicWeapon>();
	
	/** number of weapons in the player's equipment */
	int weaponCounter;
	
	/** Weapon in use */
	LogicWeapon currentWeapon;
	
	/**
	 * LogicPlayer Constructor <br>
	 * Create a new Player
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicPlayer( String id, int maxLife , Vector3f position, LogicWorld world ) {
		super( id, maxLife, position, world );
		
		this.weaponCounter = 0;
		
		/** Initially the player has just the AR15 */
		LogicWeapon ar15 = new LogicWeapon( super.id + "w", 100, WeaponType.AR15 );
		addWeapon( ar15 );
		
		/** assign the mp5 as current weapon */
		changeWeapon( ar15.id );
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
			equipment.put( weapon.id, weapon );
		} 
		// advise the player he can't carry more than three weapons
	}
	
	/** Function <code>changeWeapon</code><br>
	 * 
	 * Switch to another weapon.
	 * @param weaponKey - (int) the key of the weapon to use.
	 */
	public void changeWeapon( String weaponKey ) {
		try {
			currentWeapon = equipment.get( weaponKey );
		} catch( Exception e ) {
			// don't change the current weapon and advise
			// that the desired weapon isn't in the equipment
		}
	}
	
	/** 
	 * Just decrease ammunitions
	 */
    @Override
	public boolean shoot() {
		currentWeapon.decreaseAmmo();
		if( currentWeapon.ammo > 0 ) 
			return true;
		else 
			return false;
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
	public void die( String shooterId ) {
		world.killed(id);
		super.die( shooterId );
	}
}
