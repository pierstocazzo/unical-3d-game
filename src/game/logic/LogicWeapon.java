package game.logic;

/**
 * Classe LogicWeapon
 * 
 * @author Andrea
 *
 */
public class LogicWeapon {
	
	/** Identifier */
	String id;
	
	/** Remaining ammo */
	int ammo;
	
	/** Weapon type */
	WeaponType type;
	
	/**
	 * <code>LogicWeapon</code> Constructor
	 * 
	 * @param id - (String) Identifier
	 * @param ammo - (int) character's ammo
	 * @param type - (WeaponType) the weapon type
	 */
	public LogicWeapon( String id, int ammo, WeaponType type ) {
		this.id = id;
		this.ammo = ammo;
		this.type = type;
	}

	/**
	 * It get weapon's power
	 * 
	 * @return power
	 */
	public int getPower() {
		return type.power;
	}

	/**
	 * It gets damage of every bullet
	 * 
	 * @return damage
	 */
	public int getDamage() {
		return type.damage;
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
		if( ammo > 0 )
			ammo = ammo + addedAmmo;
	}
	
	public void decreaseAmmo() {
		ammo = ammo - 1;
	}
}
