package game.logic;

/**
 * Class LogicEnemy
 *
 */
public class LogicEnemy extends LogicCharacter {

	/** Enemy weapon */
	LogicWeapon weapon;
	
	// TODO enemyPath
	
	/**
	 * Constructor Oggetto LogicEnemy
	 * 
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicEnemy( String id, int currentLife, int maxLife, WeaponType weapon ) {
		super( id, currentLife, maxLife );
		/** create the enemy weapon */
		this.weapon = new LogicWeapon( super.id + "w", 1, weapon );
	}
}
