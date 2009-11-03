package game.logic;

import java.util.HashMap;

/**
 * Class LogicPlayer
 * 
 * @author Andrea
 */
public class LogicPlayer extends LogicCharacter {

	/** Player's equipment */
	HashMap< Integer, LogicWeapon > equipment;
	
	int weaponCounter;
	
	LogicWeapon currentWeapon;
	
	/**
	 * Constructor Object LogicPlayer
	 * 
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 * @param maxWeapons - (int) Max Weapons
	 */
	public LogicPlayer( String id, int currentLife, int maxLife ) {
		super( id, currentLife, maxLife );
		
		weaponCounter = 0;
		
		/** Initially the player has just the MP5 */
		addWeapon( new LogicWeapon( "mp5", 100, WeaponType.MP5 ) );
		changeWeapon( 1 );
	}
	
	/** Function <code>addWeapon</code><br>
	 * 
	 * Add a new weapon to the player equipment.
	 * @param weapon - (LogicWeapon) the weapon to add
	 */
	public void addWeapon( LogicWeapon weapon ) {
		equipment.put( weaponCounter, weapon );
		weaponCounter = weaponCounter + 1;
	}
	
	/** Function <code>changeWeapon</code><br>
	 * 
	 * Switch to another weapon.
	 * @param weaponKey - (int) the key of the weapon to use.
	 */
	public void changeWeapon( int weaponKey ) {
		try {
			currentWeapon = equipment.get( weaponKey );
		} catch( Exception e ) {
			// just don't change the current weapon
		}
	}
}
