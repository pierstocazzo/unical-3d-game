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

package game.common;

import java.io.Serializable;

/**
 * Enum {@link State}
 * Define states of enemies
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public enum State implements Serializable {
	
	/**
	 * The character is in default state
	 */
	DEFAULT,
	
	/**
	 * The character is in alert because he has 
	 * seen something strange
	 */
	ALERT,
	
	/**
	 * The character is attacking you, because he
	 * has seen you and he have to kill
	 * everybody unknown
	 */
	ATTACK,
	
	/**
	 * The character after an attack don't see you
	 * and he come to find you
	 */
	SEARCH,
	
	/**
	 * The character is in find and see you.
	 * He attack you and remember that 
	 * he must return to default position
	 */
	SEARCHATTACK, 
	
	/**
	 * Ever the character is careful.
	 * It's similar to alert state
	 */
	GUARD,
	
	/**
	 * The character is in attack 
	 * but he remember he is a guard
	 * and not a simple soldier
	 */
	GUARDATTACK;
	
	/* State fields */
	
	/** view range */
	int viewRange;
	
	/** action range */
	int actionRange;
	
	/**
	 * Get view range
	 * 
	 * @return (int)
	 */
	public int getViewRange() {
		return viewRange;
	}
	
	/**
	 * Get action range
	 * 
	 * @return (int)
	 */
	public int getActionRange() {
		return actionRange;
	}
	
	public static State toState( String type ) {
		State state;
		
		if( type.equals("DEFAULT") ) 
			state = State.DEFAULT;
		else if( type.equals("ALERT") ) 
			state = State.ALERT;
		else if( type.equals("ATTACK") ) 
			state = State.ATTACK;
		else if( type.equals("FIND") ) 
			state = State.SEARCH;
		else if( type.equals("FINDATTACK") ) 
			state = State.SEARCHATTACK;
		else if( type.equals("GUARD") ) 
			state = State.GUARD;
		else if( type.equals("GUARDATTACK") ) 
			state = State.GUARDATTACK;
		else
			state = State.DEFAULT;
		
		return state;
	}
	
	private static int get( String param ) {
		return GameConf.getIntParameter( param );
	}
	
	private void set( int viewRange, int actionRange ) {
		this.viewRange = viewRange;
		this.actionRange = actionRange;
	}

	public static void init() {
		DEFAULT.set( get( GameConf.STATE_DEFAULT_VIEW_RANGE ), get( GameConf.STATE_DEFAULT_ACTION_RANGE ) );
		ALERT.set( get( GameConf.STATE_ALERT_VIEW_RANGE ), get( GameConf.STATE_ALERT_ACTION_RANGE ) );
		ATTACK.set( get( GameConf.STATE_ATTACK_VIEW_RANGE ), get( GameConf.STATE_ATTACK_ACTION_RANGE ) );
		SEARCH.set( get( GameConf.STATE_SEARCH_VIEW_RANGE ), get( GameConf.STATE_SEARCH_VIEW_RANGE ) );
		SEARCHATTACK.set( get( GameConf.STATE_ATTACK_VIEW_RANGE ), get( GameConf.STATE_ATTACK_ACTION_RANGE ) );
		GUARD.set( get( GameConf.STATE_GUARD_VIEW_RANGE ), get( GameConf.STATE_GUARD_ACTION_RANGE ) );
	}
}