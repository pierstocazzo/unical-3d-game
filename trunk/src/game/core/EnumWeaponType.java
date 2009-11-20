package game.core;

public enum EnumWeaponType {
	
	MP5 ( 10, 60000, false ),
	
	GATLING ( 10, 60000, true ),
	
	BAZOOKA ( 30, 10000, true );
	
	/** <code>WeaponType</code> field */
	int damage, power;
	boolean isHeavy;
	
	/**
	 * <code>WeaponType</code> constructor<br>
	 * Create a weapon type and set its attributes.
	 * @param damage - (int) the damage the weapon cause to a character
	 * @param power - (int) the shoot power of the weapon
	 * @param isHeavy - (bool) if true the character can't run when using this weapon
	 */
	EnumWeaponType ( int damage, int power, boolean isHeavy ) {
		this.damage = damage;
		this.power = power;
		this.isHeavy = isHeavy;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public int getDamage() {
		return this.damage;
	}
}
