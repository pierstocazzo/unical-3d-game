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
	DEFAULT ( 150, 100 ),
	
	/**
	 * The character is in alert because he has 
	 * seen something strange
	 */
	ALERT ( 200, 140 ),
	
	/**
	 * The character is attacking you, because he
	 * has seen you and he have to kill
	 * everybody unknown
	 */
	ATTACK ( 200, 180 ),
	
	/**
	 * The character after an attack don't see you
	 * and he come to find you
	 */
	FIND ( 200, 180 ),
	
	/**
	 * The character is in find and see you.
	 * He attack you and remember that 
	 * he must return to default position
	 */
	FINDATTACK( 200, 180 ), 
	
	/**
	 * Ever the character is careful.
	 * It's similar to alert state
	 */
	GUARD ( 200, 140 ),
	
	/**
	 * The character is in attack 
	 * but he remember he is a guard
	 * and not a simple soldier
	 */
	GUARDATTACK( 200, 180 );
	
	/** view range */
	int viewRange;
	
	/** action range */
	int actionRange;
	
	/**
	 * Constructor
	 * 
	 * @param (int) viewRange
	 * @param (int) actionRange
	 */
	State( int viewRange, int actionRange ) {
		this.viewRange = viewRange;
		this.actionRange = actionRange;
	}
	
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
}