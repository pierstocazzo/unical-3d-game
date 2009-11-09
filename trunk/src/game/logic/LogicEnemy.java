package game.logic;

import com.jme.math.Vector3f;

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
	public LogicEnemy( String id, int currentLife, int maxLife, WeaponType weapon, Vector3f position ) {
		super( id, currentLife, maxLife, position );
		/** create the enemy weapon */
		this.weapon = new LogicWeapon( super.id + "w", 1, weapon );
	}
}
