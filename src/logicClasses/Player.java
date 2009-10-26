package logicClasses;

/**
 * Class Player
 * 
 * @author Andrea
 */
public class Player extends Character {

	/** Weapon's Container */
	private Equipment equipment;
	
	/**
	 * Constructor Object Player
	 * 
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 * @param maxWeapons - (int) Max Weapons
	 */
	public Player( String id, int currentLife, int maxLife, int maxWeapons) {
		super( id, currentLife, maxLife );
		equipment = new Equipment(maxWeapons);
	}

	/**
	 * It gets the equipment
	 * 
	 * @return equipment - (Equipment) Weapon's Container
	 */
	public Equipment getEquipment(){
		return equipment;
	}
	

}
