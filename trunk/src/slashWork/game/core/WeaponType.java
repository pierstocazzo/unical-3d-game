package slashWork.game.core;

public enum WeaponType {
	
	MP5 ( 1, 30000, 20, false ),
	
	GATLING ( 2, 30000, 20, true ),
	
	BAZOOKA ( 30, 10000, 1000, true );
	
	/** <code>WeaponType</code> field */
	int damage, power, loadTime;
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
	
	public int getPower() {
		return this.power;
	}
	
	public int getDamage() {
		return this.damage;
	}

	public long getLoadTime() {
		return this.loadTime;
	}
}
