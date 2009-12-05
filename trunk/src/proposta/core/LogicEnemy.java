package proposta.core;

import java.io.Serializable;

import proposta.enemyAI.MovementList;
import proposta.enemyAI.Movement;
import proposta.enemyAI.MovementList.MovementType;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Class LogicEnemy
 *
 */
@SuppressWarnings("serial")
public class LogicEnemy extends LogicCharacter implements Serializable {

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
		shootDirection.set( Vector3f.ZERO );
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
					// TODO inserire un timer per lo stato di allerta
					state = State.DEFAULT;
				}
				break;
			
			case ATTACK:
				if ( distance > state.getViewRange() ) {
					// TODO inserire un timer per stato di attacco
					state = State.ALERT;
				} else {
					calculateShootDirection( playerId );
				}
				break;
			}
		}
	}
	
	private void calculateShootDirection( String playerId ) {
		// ottengo la shootdirection esatta sottraendo il vettore posizione del 
		// nemico a quello del suo target (ovvero un player) 
		shootDirection.set( world.characters.get(playerId).position.subtract( position ).normalize() );
		// aggiungo un certo errore ruotando il vettore di un angolo random tra 0 e 10 gradi 
		// TODO gestire l'errore della shootdirection in base al livello
		float angle = FastMath.DEG_TO_RAD * ( FastMath.rand.nextFloat() % 10 );
		Quaternion q = new Quaternion().fromAngleAxis( angle, Vector3f.UNIT_Y );
		q.mult( shootDirection, shootDirection );
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
