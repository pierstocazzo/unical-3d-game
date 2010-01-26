package game.common;

import java.io.Serializable;

import game.common.GameConfiguration;

/**
 * Class WeaponType
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public enum WeaponType implements Serializable {
	
	/** mp5 weapon */
	AR15 (
		Integer.valueOf( GameConfiguration.getParameter("weapon_ar15_damage") ),
		Integer.valueOf( GameConfiguration.getParameter("weapon_ar15_power") ),
		Integer.valueOf( GameConfiguration.getParameter("weapon_ar15_loadtime") ),
		false
	),
	
	/** not used */
	GATLING (
		Integer.valueOf( GameConfiguration.getParameter("weapon_gatling_damage") ),
		Integer.valueOf( GameConfiguration.getParameter("weapon_gatling_power") ),
		Integer.valueOf( GameConfiguration.getParameter("weapon_gatling_loadtime") ),
		true
	),
	
	/** not used */
	BAZOOKA (
		Integer.valueOf( GameConfiguration.getParameter("weapon_bazooka_damage") ),
		Integer.valueOf( GameConfiguration.getParameter("weapon_bazooka_power") ),
		Integer.valueOf( GameConfiguration.getParameter("weapon_bazooka_loadtime") ),
		true
	);
	
	/** <code>WeaponType</code> field */
	int damage, power, loadTime;
	
	/** not used */
	boolean isHeavy;
	
	/**
	 * <code>WeaponType</code> constructor<br>
	 * Create a weapon type and set its attributes.
	 * @param damage - (int) the damage the weapon cause to a character
	 * @param power - (int) the shoot power of the weapon
	 * @param isHeavy - (bool) if true the character can't run when using this weapon
	 */
	WeaponType ( int damage, int power, int loadTime, boolean isHeavy ) {
		this.damage = damage;
		this.power = power;
		this.isHeavy = isHeavy;
		this.loadTime = loadTime;
	}
	
	/**
	 * Return current weapon power
	 * 
	 * @return (int)
	 */
	public int getPower() {
		return this.power;
	}
	
	/**
	 * Return current weapon damage
	 * 
	 * @return (int)
	 */
	public int getDamage() {
		return this.damage;
	}

	/**
	 * Return current weapon load time
	 * 
	 * @return (long)
	 */
	public long getLoadTime() {
		return this.loadTime;
	}
}