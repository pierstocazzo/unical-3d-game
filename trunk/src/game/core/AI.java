package game.core;

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
		float maxAlertTime = LogicEnemy.ALERT_RANGE;
		float distance;
		enemy.shootDirection.set( Vector3f.ZERO );
		
		// check state of other enemies
		for ( String playerId : world.getPlayersIds() ) {
			distance = enemy.position.distance( world.getPosition( playerId ) );

			if ( enemy.enemyNextInAttack() ){
				if(enemy.state == State.GUARD || enemy.state == State.GUARDATTACK)
					enemy.state = State.GUARDATTACK;
				else if(enemy.state == State.FIND || enemy.state == State.FINDATTACK)
					enemy.state = State.FINDATTACK;
				else
					enemy.state = State.ATTACK;
				enemy.alertTime = GameTimer.getTimeInSeconds();
			}

			// update current state
			switch ( enemy.state ) {
			case DEFAULT:
				enemy.alertTime = GameTimer.getTimeInSeconds() - 20;
				// if player enter in enemy view range he go in alert
				if ( distance <= enemy.state.getViewRange() ) {
					enemy.state = State.ALERT;
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;

			case ALERT:
				// if player enter in enemy action range he go in attack
				if ( distance <= enemy.state.getActionRange() ) {
					enemy.state = State.ATTACK;
					updateState( id );
				} else if ( distance > enemy.state.getViewRange() ) {
					/* if the player goes away from the actionRange of this enemy, 
					 * he remains in alert state for "ALERT_RANGE" seconds
					 */
					if( GameTimer.getTimeInSeconds() - enemy.alertTime > maxAlertTime ) {
						enemy.state = State.DEFAULT;
					}
				}
				// if player leaves enemy view range, he go in alert
				if ( distance <= enemy.state.getViewRange() ) 
					enemy.alertTime = GameTimer.getTimeInSeconds();
				break;

			case ATTACK:
				// if player lefts enemy action range he go in find
				if ( distance > enemy.state.getViewRange() ) {
					enemy.state = State.FIND;
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				calculateShootDirection( id, playerId );
				enemy.alertTime = GameTimer.getTimeInSeconds();
				break;

			case FIND:
				enemy.alertTime = GameTimer.getTimeInSeconds();
				//if player enter enemy action range he go in findattack
				//because he remember that he was in find
				if ( distance <= enemy.state.getActionRange() )
					enemy.state = State.FINDATTACK;
				break;

			case FINDATTACK:
				// if player lefts enemy view range he return in find
				if ( distance > enemy.state.getViewRange() ) {
					enemy.state = State.FIND;
				} else {
					calculateShootDirection( id, playerId );
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;
				
			case GUARD:
				/** It is a particular state. It don't result in alert bar
				 * if player enter enemy action range he go in alert
				 */
				if ( distance <= enemy.state.getActionRange() )
					enemy.state = State.GUARDATTACK;
				break;
				
			case GUARDATTACK:
				/** if player lefts enemy view range he return in guard */
				if ( distance > enemy.state.getViewRange() ) {
					enemy.alertTime = 0;
					enemy.state = State.GUARD;
				} else {
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
		// Add a random error with rotating of direction vector of an angle between 0 and 10 grades
		float angle = FastMath.DEG_TO_RAD * ( FastMath.rand.nextFloat() % enemy.errorAngle );
		// generate if angle is positive or negative
		int tmpValue = FastMath.rand.nextInt() % 2;
		if ( tmpValue == 0 )
			angle = angle * (-1);
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

		if( enemy.state == State.FIND ) {
			if( enemy.firstFind ) {
				findDirection.set( enemy.shootDirection );
				findDirection.setY(0);
				enemy.initialFindPosition.set( currentPosition );
				enemy.firstFind = false;
			}
			
			distance = Math.abs( currentPosition.distance( enemy.initialFindPosition ) );
			if( distance > 80 ) {
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
