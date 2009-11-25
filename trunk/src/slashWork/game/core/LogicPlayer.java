package slashWork.game.core;

import slashWork.game.enemyAI.Movement;

import java.util.HashMap;

import com.jme.math.Vector3f;

/**
 * Class LogicPlayer
 * 
 * @author Andrea
 */
public class LogicPlayer extends LogicCharacter {

	/** Player's equipment */
	HashMap< String, LogicWeapon > equipment = new HashMap<String, LogicWeapon>();
	
	/** current level of experience */
	int currentLevel;
	
	/** previous level of experience */
	int previousLevel;
	
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
		
		this.currentLevel = 0;
		this.previousLevel = 0;
		
		/** Initially the player has just the MP5 */
		addWeapon( new LogicWeapon( super.id + "w" + weaponCounter, 100, WeaponType.MP5 ) );
		
		/** assign the mp5 as current weapon */
		changeWeapon( "weapon" + weaponCounter );
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
			equipment.put( "weapon" + weaponCounter, weapon );
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
	public void shoot() {
		currentWeapon.decreaseAmmo();
	}

	@Override
	public Movement getNextMovement() {
		return null;
	}

	@Override
	public WeaponType getCurrentWeapon() {
		return currentWeapon.type;
	}
}
