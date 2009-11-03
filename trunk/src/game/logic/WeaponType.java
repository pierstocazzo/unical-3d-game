package game.logic;

public enum WeaponType {
	
	MP5 ( 1, 1000000, 1000, false ),
	
	MACHINEGUN ( 10, 1000000, 1000, true ),
	
	BAZOOKA ( 30, 100000, 500, true );
	
	int damage, power, distance;
	
	boolean isHeavy;
	
	WeaponType ( int damage, int power, int distance, boolean isHeavy ) {
		this.damage = damage;
		this.power = power;
		this.distance = distance;
		this.isHeavy = isHeavy;
	}
}
