package game.core;

import game.common.GameConfiguration;
import game.common.GameTimer;
import game.common.State;

import java.io.Serializable;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * AI
 * Class used for manage artificial intelligence of enemies
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class AI implements Serializable {
	
	/** Pointer to logic world */
	LogicWorld world;
	
	/** direction when enemy is in find state */
	Vector3f findDirection;
	
	/** current movement direction */
	Vector3f moveDirection;

	/**
	 * Constructor
	 * 
	 * @param (LogicWorld) world
	 */
	public AI( LogicWorld world ) {
		//initialize field
		this.world = world;
		this.findDirection = new Vector3f();
		this.moveDirection = new Vector3f();
		moveDirection.set( Vector3f.ZERO );
	}
	
	/** 
	 * Update state of a particular enemy
	 * 
	 * @param (String) id
	 */
	public void updateState( String id ) {

		// initialize useful variable
		LogicEnemy enemy = (LogicEnemy) world.characters.get( id );
		float distance;
		enemy.shootDirection.set( Vector3f.ZERO );
		
		for ( String playerId : world.getPlayersIds() ) {
			distance = enemy.position.distance( world.getPosition( playerId ) );
			// check state of other enemies: if one of the near enemies 
			if ( enemy.someNeighborInAttack() ) {
				if( enemy.state == State.GUARD || enemy.state == State.GUARDATTACK )
					enemy.state = State.GUARDATTACK;
				else if(enemy.state == State.SEARCH || enemy.state == State.SEARCHATTACK)
					enemy.state = State.SEARCHATTACK;
				else
					enemy.state = State.ATTACK;
				enemy.alertTime = GameTimer.getTimeInSeconds();
			}

			// update current state
			switch ( enemy.state ) {
			case DEFAULT:
				// if he sees the player he goes in alert state
				if ( distance <= enemy.state.getViewRange() && enemy.canSeePlayer ) {
					enemy.state = State.ALERT;
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;

			case ALERT:
				// if the player is near enough and he can see him, he goes in attack state
				if ( distance <= enemy.state.getActionRange() && enemy.canSeePlayer ) {
					enemy.state = State.ATTACK;
					calculateShootDirection( id, playerId );
				} else if ( distance > enemy.state.getViewRange() || !enemy.canSeePlayer ) {
					/* if he don't see the player
					 * he remains in alert state for some time
					 * then he return in default state
					 */
					if( GameTimer.getTimeInSeconds() - enemy.alertTime > 
							Integer.valueOf( GameConfiguration.getParameter("maxAlertTime") ) ) {
						enemy.state = State.DEFAULT;
					}
				} else {
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;

			case ATTACK:
				// if player hides himself or leaves the enemy view range, the enemy 
				// goes in search of him
				if ( distance > enemy.state.getViewRange() || !enemy.canSeePlayer  ) {
					enemy.state = State.SEARCH;
				} 
				// in any case he calculate the shoot direction
				calculateShootDirection( id, playerId );
				enemy.alertTime = GameTimer.getTimeInSeconds();
				break;

			case SEARCH:
				enemy.alertTime = GameTimer.getTimeInSeconds();
				// if he sees the player he goes in searchattack state
				// (different from Attack just to remember that he was in search state before)
				if ( distance <= enemy.state.getActionRange() && enemy.canSeePlayer )
					enemy.state = State.SEARCHATTACK;
				break;

			case SEARCHATTACK:
				// if he doesen't see the player he keeps searching him
				if ( distance > enemy.state.getViewRange() || !enemy.canSeePlayer ) {
					enemy.state = State.SEARCH;
				} else {
					calculateShootDirection( id, playerId );
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;
				
			case GUARD:
				 /* if he can see the player he attacks him
				  */
				if ( distance <= enemy.state.getViewRange() && enemy.canSeePlayer ) {
					enemy.state = State.GUARDATTACK;
					calculateShootDirection( id, playerId );
				}
				break;
				
			case GUARDATTACK:
				/* if he can't see the player he returns in guard state */
				if ( distance > enemy.state.getViewRange() || !enemy.canSeePlayer ) {
					enemy.alertTime = 0;
					enemy.state = State.GUARD;
				} else {
					// else he attacks him
					calculateShootDirection( id, playerId );
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;
			}
		}
	}

	/**
	 * Calculate player direction for shoot it.
	 * The direction has an error that decrease at increase of player level
	 * 
	 * @param (String) enemyId
	 * @param (String) playerId
	 */
	private void calculateShootDirection( String enemyId, String playerId ) {
		LogicEnemy enemy = (LogicEnemy) world.characters.get( enemyId );
		enemy.shootDirection.set( world.characters.get(playerId).position.subtract( enemy.position ).normalize() );
		// Add a random error with rotating of direction vector of an angle between 0 and a max error angle
		float angle = FastMath.DEG_TO_RAD * ( FastMath.rand.nextFloat() % enemy.errorAngle );
		// generate if angle is positive or negative
		int tmpValue = FastMath.rand.nextInt() % 2;
		if ( tmpValue == 0 )
			angle = angle * (-1);
		// rotate the shoot direction vector of this angle
		Quaternion q = new Quaternion().fromAngleAxis( angle, Vector3f.UNIT_Y );
		q.mult( enemy.shootDirection, enemy.shootDirection );
	}
	
	/**
	 * Get direction of current movement
	 * 
	 * @param (String) id
	 * @return (Vector3f) direction
	 */
	public Vector3f getMoveDirection( String id ) {
		
		LogicEnemy enemy = (LogicEnemy) world.characters.get(id);
		
		float distance;
		
		/** utility vectors */
		Vector3f currentPosition = new Vector3f( enemy.position );
		currentPosition.setY(0);

		if( enemy.state == State.SEARCH ) {
			if( enemy.firstFind ) {
				findDirection.set( enemy.shootDirection );
				findDirection.setY(0);
				enemy.initialFindPosition.set( currentPosition );
				enemy.firstFind = false;
			}
			
			distance = Math.abs( currentPosition.distance( enemy.initialFindPosition ) );
			if( distance > Integer.valueOf( GameConfiguration.getParameter("maxFindDistance") ) ) {
				if( enemy.comingBack == false ) {
					findDirection.negateLocal();
					enemy.comingBack = true;
				}
			}

			if( enemy.comingBack == true && distance <= 7 && distance >= 0 ) {
				enemy.comingBack = false;
				enemy.firstFind = true;
				enemy.state = State.ALERT;
				enemy.initialFindPosition.set( currentPosition );
			}
			
			return findDirection;
		} 
		else if( enemy.state == State.DEFAULT )
		{
			/** The enemy will move only if he is in default state. When he attack or is in alert he rests
			 */
			if( !enemy.currentMovement.getDirection().equals(Vector3f.ZERO) ) {
				/** calculate distance between the current movement's start position and
				 *  the current character position
				 */
				distance = Math.abs( currentPosition.distance( enemy.movementStartPosition ) );
				/** If this distance is major than the lenght specified in the movement 
				 *  start the next movement
				 */
				if( distance >= enemy.currentMovement.getLength() ) {
					enemy.movementStartPosition.set( currentPosition );
					enemy.getNextMovement();
				}
				
				moveDirection.set( enemy.currentMovement.getDirection() );

				return moveDirection;
			} 
		} 
		return Vector3f.ZERO;
	}
}
