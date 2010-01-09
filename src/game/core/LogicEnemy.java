package game.core;

import game.common.Movement;
import game.common.MovementList;
import game.common.State;
import game.common.WeaponType;
import game.common.MovementList.MovementType;

import java.io.Serializable;

import com.jme.math.Vector3f;

/**
 * Class LogicEnemy
 *
 */
@SuppressWarnings("serial")
public class LogicEnemy extends LogicCharacter implements Serializable {

	static public final int ALERT_RANGE = 20;
	
	/** Enemy's weapon */
	LogicWeapon weapon;
	
	/** list of the enemy's movements */
	MovementList movements;
	
	/** current movement the enemy is executing */
	Movement currentMovement;
	
	/** the start position of the current movement */
	Vector3f movementStartPosition;
	
	/** current state */
	State state;
	
	/** where to shoot */
	Vector3f shootDirection;
	
	/** the time the enemy remain in alert or attack state */
	float alertTime;
	
	/** true when the enemy is coming back from the state find */
	boolean comingBack = false;
	
	/** false when the enemy is in find status */
	boolean firstFind = true;
	
	/** the max error of the shot */
	int errorAngle;


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
		this.movementStartPosition = new Vector3f( position );
		this.movementStartPosition.setY(0);
		this.errorAngle = 10;
		this.currentMovement = this.movements.getNextMovement();
	}
	
	public Movement getNextMovement() {
		currentMovement = movements.getNextMovement();
		return currentMovement;
	}

	public Movement getCurrentMovement() {
		return currentMovement;
	}
	
	boolean enemyNextInAttack() {
		boolean inAttack = false;
		for( int i = 1; i <= world.enemyCounter; i++ ) {
			if( world.characters.get( "enemy"+i ) != null )
				if(world.getState( "enemy"+i ) == State.ATTACK && !id.equals( "enemy"+i ) &&
						position.distance( world.getPosition("enemy"+i)) <=  50 )
					return true;
		}
		return inAttack;
	}
	
	public float getAlertTime() {
		return alertTime;
	}

	@Override
	public void isShooted( int bulletDamage, String shooterId ) {
		if(state == State.FIND || state == State.FINDATTACK)
			state = State.FINDATTACK;
		else
			state = State.ATTACK;
		
		if( currentLife - bulletDamage <= 0 )
			die( shooterId );
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
	
	@Override
	public void die( String shooterId ) {
		world.ammoPackCounter++;
		world.createAmmoPack( "ammo" + world.ammoPackCounter, weapon.type, 20, position );
		world.kill(shooterId);
		super.die( shooterId );
	}
}
