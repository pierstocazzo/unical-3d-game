package game.core;

import java.util.HashMap;

import com.jme.math.Vector3f;

/**
 * Class LogicPlayer
 * 
 * @author Andrea
 */
public class LogicPlayer extends LogicCharacter {

	/** Player's equipment */
	HashMap< Integer, LogicWeapon > equipment;
	
	int currentLevel;
	int previousLevel;
	
	int weaponCounter;
	
	/** Weapon in use */
	LogicWeapon currentWeapon;
	
	/**
	 * Constructor Object LogicPlayer
	 * 
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicPlayer( String id, int currentLife, int maxLife , Vector3f position) {
		super( id, currentLife, maxLife, position );
		
		this.weaponCounter = 0;
		
		this.currentLevel = 0;
		this.previousLevel = 0;
		
		/** Initially the player has just the MP5 */
		addWeapon( new LogicWeapon( "mp5", 100, EnumWeaponType.MP5 ) );
		changeWeapon( 1 );
	}
	
	/** Function <code>addWeapon</code><br>
	 * 
	 * Add a new weapon to the player equipment.
	 * @param weapon - (LogicWeapon) the weapon to add
	 */
	public void addWeapon( LogicWeapon weapon ) {
		// the player can't carry more than three weapons
		if( weaponCounter >= 3 ) {
			equipment.put( weaponCounter, weapon );
			weaponCounter = weaponCounter + 1;
		} 
		// advise the player he can't carry more than three weapons
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
			// don't change the current weapon and advise
			// that the desired weapon isn't in the equipment
		}
	}
	
	/** 
	 * Just decrease ammunitions
	 */
	public void shoot() {
		currentWeapon.decreaseAmmo();
	}
}
