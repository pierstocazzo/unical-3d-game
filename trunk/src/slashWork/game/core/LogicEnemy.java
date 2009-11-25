package slashWork.game.core;

import slashWork.game.enemyAI.MovementList;
import slashWork.game.enemyAI.Movement;
import slashWork.game.enemyAI.MovementList.MovementType;

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
	
	/** current state */
	State state;
	
	/** where to shoot */
	Vector3f shootDirection;

	/**
	 * <code>LogicEnemy</code> constructor<br>
	 * Create a new Enemy
	 * @param id - (String) Identifier
	 * @param maxLife - (int) Max Life
	 * @param weapon - (EnumWeaponType) the weapon of the enemy
	 * @param state - (EnumCharacterState) the initial state of enemy
	 * @param position - (Vector3f) the position of the enemy
	 * @param movements - (MovementList) the movements the enemy have to repeate
	 */
	public LogicEnemy( String id, int maxLife, WeaponType weapon, State state,
						Vector3f position, MovementType movements, LogicWorld world ) {
		super( id, maxLife, position, world );
		/** create the enemy weapon */
		this.weapon = new LogicWeapon( super.id + "w", 1, weapon );
		this.state = state;
		this.movements = new MovementList( movements );
		this.shootDirection = new Vector3f();
	}
	
	public Movement getNextMovement() {
		currentMovement = movements.getNextMovement();
		return currentMovement;
	}

	public Movement getCurrentMovement() {
		return currentMovement;
	}
	
	public void updateState() {
		float distance;
		
		for ( String playerId : world.getPlayersId() ) {
			distance = position.distance( world.getCharacterPosition( playerId ) );
			
			switch ( state ) {
			case DEFAULT:
				if ( distance <= state.getActionRange() ) {
					state = State.ALERT;
				}
				break;
			
			case ALERT:
				if ( distance <= state.getViewRange() ) {
					state = State.ATTACK;
				} else if ( distance > state.getActionRange() ) {
					// TODO inserire un timer
					state = State.DEFAULT;
				}
				break;
			
			case ATTACK:
				if ( distance > state.getViewRange() ) {
					// TODO inserire un timer
					state = State.ALERT;
				} else {
					shootDirection.set( position.subtract( world.characters.get(playerId).position ).normalize().negate() );
				}
				break;
			}
		}
	}
	
	@Override
	public void isShooted( int bulletDamage ) {
		state = State.ATTACK;
		
		if( currentLife - bulletDamage <= 0 )
			die();
		else
			currentLife = currentLife - bulletDamage;
	}
	
	@Override
	public WeaponType getCurrentWeapon() {
		return weapon.type;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState( State state ) {
		this.state = state;
	}

	/**
	 * @return the shootDirection
	 */
	public Vector3f getShootDirection() {
		return shootDirection;
	}

	/**
	 * @param shootDirection the shootDirection to set
	 */
	public void setShootDirection(Vector3f shootDirection) {
		this.shootDirection = shootDirection;
	}
	
	public void die() {
		world.createAmmoPack( id + "ammoPack", weapon.type, 20, position );
		super.die();
	}
}
