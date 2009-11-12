package game.core;

public enum EnumWeaponType {
	
	MP5 ( 1, 1000000, 1000, false ),
	
	GATLING ( 10, 1000000, 1000, true ),
	
	BAZOOKA ( 30, 100000, 500, true );
	
	/** <code>WeaponType</code> field */
	int damage, power, distance;
	boolean isHeavy;
	
	/**
	 * <code>WeaponType</code> constructor<br>
	 * Create a weapon type and set its attributes.
	 * @param damage - (int) the damage the weapon cause to a character
	 * @param power - (int) the shoot power of the weapon
	 * @param distance - (int) the maximum distance a bullet of this ammo can reach
	 * @param isHeavy - (bool) if true the character can't run when using this weapon
	 */
	EnumWeaponType ( int damage, int power, int distance, boolean isHeavy ) {
		this.damage = damage;
		this.power = power;
		this.distance = distance;
		this.isHeavy = isHeavy;
	}
}
