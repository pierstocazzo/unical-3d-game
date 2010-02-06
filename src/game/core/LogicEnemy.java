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
import game.common.GameTimer;
import game.common.Movement;
import game.common.MovementList;
import game.common.State;
import game.common.WeaponType;
import game.common.MovementList.MovementType;

import java.io.Serializable;
import java.util.LinkedList;

import com.jme.math.Vector3f;

/**
 * Class LogicEnemy
 *
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class LogicEnemy extends LogicCharacter implements Serializable {

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
	
	/** the time that the enemy remain in alert or attack state */
	float alertTime = 0;
	float oldTime = 0;
	
	/** true when the enemy is coming back from the state find */
	boolean comingBack = false;
	
	/** false when the enemy is in find status */
	boolean firstTimeInSearch = true;
	
	/** the max error of the shot */
	int errorAngle;

	public Vector3f initialFindPosition;

	/** define if there isn't anything that cause this enemy to not see the player */
	boolean canSeePlayer = true;

	/**
	 * <code>LogicEnemy</code> constructor<br>
	 * Create a new Enemy
	 * @param id - (String) Identifier
	 * @param maxLife - (int) Max Life
	 * @param weapon - (EnumWeaponType) the weapon of the enemy
	 * @param state - (EnumCharacterState) the initial state of enemy
	 * @param position - (Vector3f) the position of the enemy
	 * @param movements - (MovementList) the movement type that the enemy have to repeat
	 */
	public LogicEnemy( String id, int maxLife, WeaponType weapon, State state,
						Vector3f position, MovementType movements, LogicWorld world ) {
		super( id, maxLife, position, world );
		/** create the enemy weapon */
		this.weapon = new LogicWeapon( super.id + "w", weapon );
		this.state = state;
		this.movements = new MovementList( movements );
		this.shootDirection = new Vector3f();
		this.movementStartPosition = new Vector3f( position );
		this.movementStartPosition.setY(0);
		this.errorAngle = Integer.valueOf( GameConf.getParameter( GameConf.INITIAL_ENEMY_ACCURACY ) );
		this.currentMovement = this.movements.get(0);
		this.initialFindPosition = new Vector3f();
	}
	
	/**
	 * <code>LogicEnemy</code> constructor<br>
	 * Create a new Enemy
	 * @param id - (String) Identifier
	 * @param maxLife - (int) Max Life
	 * @param weapon - (EnumWeaponType) the weapon of the enemy
	 * @param state - (EnumCharacterState) the initial state of enemy
	 * @param position - (Vector3f) the position of the enemy
	 * @param movements - (LinkedList<Movement>) the movements the enemy have to repeat
	 */
	public LogicEnemy( String id, int maxLife, WeaponType weapon, State state,
		Vector3f position, LinkedList<Movement> movements, LogicWorld world ) {
		super( id, maxLife, position, world );
		/** create the enemy weapon */
		this.weapon = new LogicWeapon( super.id + "w", weapon );
		this.state = state;
		this.movements = new MovementList( movements );
		this.shootDirection = new Vector3f();
		this.movementStartPosition = new Vector3f( position );
		this.movementStartPosition.setY(0);
		this.errorAngle = Integer.valueOf( GameConf.getParameter( GameConf.INITIAL_ENEMY_ACCURACY ) );
		this.currentMovement = this.movements.get(0);
		this.initialFindPosition = new Vector3f();
	}
	
	/**
	 * Return next movement
	 */
	public Movement getNextMovement() {
		currentMovement = movements.getNextMovement();
		return currentMovement;
	}

	/**
	 * Get current movement
	 * 
	 * @return {@link Movement}
	 */
	public Movement getCurrentMovement() {
		return currentMovement;
	}
	
	/**
	 * Check if an enemy is in attack.
	 * 
	 * @return (boolean)
	 */
	public boolean someNeighborInAttack() {
		for( String id : world.getEnemiesIds() ) {
			State currState = world.getState( id );
			if( ( currState == State.ATTACK || currState == State.SEARCHATTACK || currState == State.GUARDATTACK )
					&& position.distance( world.getPosition( id ) ) <=  
					Integer.valueOf( GameConf.getParameter( GameConf.MAX_NEIGHBORHOOD_RANGE ) ) )
				return true;
		}
		return false;
	}
	
	/**
	 * Return current alert time
	 * 
	 * @return (float)
	 */
	public float getTimeAlert() {
		return alertTime;
	}
	
	public void updateTimeAlert(){
		float newTime = GameTimer.getTimeInSeconds();
		alertTime = alertTime - ( newTime - oldTime );
		oldTime = newTime;
		if( alertTime < 0 )
			alertTime = 0;
	}

	/**
	 * Check if the character is shooted.
	 * It perform also state change and life decrease
	 */
	@Override
	public void isShooted( int bulletDamage, String shooterId ) {
		if( state == State.SEARCH || state == State.SEARCHATTACK )
			state = State.SEARCHATTACK;
		else if( state == State.GUARD || state == State.GUARDATTACK )
			state = State.GUARDATTACK;
		else
			state = State.ATTACK;
		
		if( currentLife - bulletDamage <= 0 )
			die( shooterId );
		else
			currentLife = currentLife - bulletDamage;
	}
	
	/**
	 * Return current weapon
	 * 
	 * @return {@link WeaponType}
	 */
	@Override
	public WeaponType getCurrentWeapon() {
		return weapon.type;
	}

	/**
	 * Get current state
	 * 
	 * @return {@link State}
	 */
	public State getState() {
		return state;
	}

	/**
	 * Set enemy state
	 * 
	 * @param (State) state
	 */
	public void setState( State state ) {
		this.state = state;
	}

	/**
	 * Return shoot direction
	 * 
	 * @return {@link Vector3f}
	 */
	public Vector3f getShootDirection() {
		return shootDirection;
	}

	/**
	 * Set shoot direction
	 * 
	 * @param (Vector3f)
	 */
	public void setShootDirection(Vector3f shootDirection) {
		this.shootDirection = shootDirection;
	}
	
	/**
	 * Kill current enemy
	 * 
	 * @param (String) id
	 */
	@Override
	public void die( String shooterId ) {
		world.createAmmoPack( id, weapon.type, 
				world.ammoPackValue, position );
		world.kill(shooterId);
		super.die( shooterId );
	}

	@Override
	public boolean shoot() {
		return false;
	}

	@Override
	public boolean addAmmo(WeaponType weaponType, int quantity) {
		return false;
	}
	
	/**
	 * Set if a enemy can see the player
	 * 
	 * @param canSeePlayer
	 */
	public void canSeePlayer( boolean canSeePlayer ) {
		this.canSeePlayer = canSeePlayer;
	}
}