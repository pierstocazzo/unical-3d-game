package game.core;

import game.enemyAI.MovementList;
import game.enemyAI.Movement;

import com.jme.math.Vector3f;

/**
 * Class LogicEnemy
 *
 */
public class LogicEnemy extends LogicCharacter {

	/** Enemy's weapon */
	LogicWeapon weapon;
	
	/** list of the enemy's movements */
	MovementList movements;
	
	/** current movement the enemy is executing */
	Movement currentMovement;

	/**
	 * <code>LogicEnemy</code> constructor<br>
	 * Create a new Enemy
	 * @param id - (String) Identifier
	 * @param maxLife - (int) Max Life
	 * @param weapon - (EnumWeaponType) the weapon of the enemy
	 * @param position - (Vector3f) the position of the enemy
	 * @param movements - (MovementList) the movements the enemy have to repeate
	 */
	public LogicEnemy( String id, int maxLife, EnumWeaponType weapon, 
						Vector3f position, MovementList movements, LogicWorld world ) {
		super( id, maxLife, position, world );
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
	
	@Override
	public void die() {
		super.die();
		// when an enemy died he releases an ammo pack (in his position)
		world.createAmmoPack( this.weapon.type, 10, this.position );
	}
}
