package game.core;

import game.enemyAI.MovementList;
import game.enemyAI.Movement;

import com.jme.math.Vector3f;

/**
 * Class LogicEnemy
 *
 */
public class LogicEnemy extends LogicCharacter {

	/** Enemy weapon */
	LogicWeapon weapon;
	
	/** enemyPath */
	MovementList movements;
	
	/** current movement the enemy is executing */
	Movement currentMovement;

	/**
	 * Constructor Oggetto LogicEnemy
	 * 
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicEnemy( String id, int maxLife, EnumWeaponType weapon, Vector3f position, MovementList movements ) {
		super( id, maxLife, position );
		/** create the enemy weapon */
		this.weapon = new LogicWeapon( super.id + "w", 1, weapon );
		this.movements = movements;
	}
	
	public Movement getNextMovement() {
		currentMovement = movements.getNextMovement();
		return currentMovement;
	}

	public Movement getCurrentMovement() {
		return currentMovement;
	}
}
