package game.logic;

public enum State {
	
	/**
	 * The character is a guard, so he just rests
	 * in his position 
	 */
	GUARD,
	
	/**
	 * The character is a sentinel, so he walks
	 * describing a path 
	 */
	SENTINEL,
	
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
	 * The character is dead
	 */
	DEATH;
}
