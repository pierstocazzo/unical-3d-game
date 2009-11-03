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
	int currentAmmo;
	
	WeaponType type;
	
	/**
	 * Constructor
	 * 
	 * @param i - Identifier
	 * @param pw - Power of every bullet
	 * @param currBull - Aviable bullets
	 * @param isH - If it is heavy
	 */
	public LogicWeapon( String id, int currentAmmo, WeaponType type ) {
		this.id = id;
		this.currentAmmo = currentAmmo;
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
	public int getCurrBullets() {
		return currentAmmo;
	}

	/**
	 * Add new bullets
	 * 
	 * @param newBullets
	 */
	public void addBullets(int newBullets) {
		if( currentAmmo > 0 )
			this.currentAmmo += newBullets;
	}
}
