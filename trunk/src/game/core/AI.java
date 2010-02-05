/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.core;

import game.common.GameConf;
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
	
	int ALERT_RANGE = Integer.valueOf( GameConf.getParameter( GameConf.MAX_ALERT_TIME ) );

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
		
		enemy.updateTimeAlert();
		
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
			}

			// update current state
			switch ( enemy.state ) {
			case DEFAULT:
				// if he sees the player he goes in alert state
				if ( distance <= enemy.state.getViewRange() && enemy.canSeePlayer ) {
					enemy.state = State.ALERT;
					enemy.timeAlert = ALERT_RANGE;
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
					if( enemy.timeAlert == 0 ) {
						enemy.state = State.DEFAULT;
					}
				} else {
					enemy.timeAlert = ALERT_RANGE;
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
				enemy.timeAlert = ALERT_RANGE;
				break;

			case SEARCH:
				enemy.timeAlert = ALERT_RANGE;
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
					enemy.timeAlert = ALERT_RANGE;
				}
				break;
				
			case GUARD:
				 /* if he can see the player he attacks him
				  */
				if ( distance <= enemy.state.getViewRange() && enemy.canSeePlayer ) {
					enemy.state = State.GUARDATTACK;
					calculateShootDirection( id, playerId );
					enemy.timeAlert = ALERT_RANGE;
				}
				break;
				
			case GUARDATTACK:
				if( distance <= enemy.state.getActionRange() && enemy.canSeePlayer ) {
					calculateShootDirection( id, playerId );
					enemy.timeAlert = ALERT_RANGE;
				} else {
					enemy.state = State.GUARD;
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
			if( distance > Integer.valueOf( GameConf.getParameter( GameConf.MAX_FIND_DISTANCE ) ) ) {
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
