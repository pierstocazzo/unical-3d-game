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
		return GameConfiguration.getIntParameter( param );
	}
	
	private void set( int viewRange, int actionRange ) {
		this.viewRange = viewRange;
		this.actionRange = actionRange;
	}

	public static void init() {
		DEFAULT.set( get("stateDefaultViewRange"), get("stateDefaultActionRange") );
		ALERT.set( get("stateAlertViewRange"), get("stateAlertActionRange") );
		ATTACK.set( get("stateAttackViewRange"), get("stateAttackActionRange") );
		SEARCH.set( get("stateSearchViewRange"), get("stateSearchActionRange") );
		SEARCHATTACK.set( get("stateAttackViewRange"), get("stateAttackActionRange") );
		GUARD.set( get("stateGuardViewRange"), get("stateGuardActionRange") );
	}
}