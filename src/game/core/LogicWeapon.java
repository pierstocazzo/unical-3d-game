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
