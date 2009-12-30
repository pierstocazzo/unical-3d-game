package game.common;

import java.io.Serializable;

public enum State implements Serializable {
	
	/**
	 * The character is in default state
	 */
	DEFAULT ( 0, 100 ),
	
	/**
	 * The character is in alert because he has 
	 * seen something strange
	 */
	ALERT ( 120, 100 ),
	
	/**
	 * The character is attacking you, because he
	 * has seen you and he have to kill
	 * everybody unknown
	 */
	ATTACK ( 240, 200 ),
	
	FIND ( 120, 100 ),
	
	FINDATTACK( 240, 200 );
	
	/** view range */
	int viewRange;
	int actionRange;
	
	State( int viewRange, int actionRange ) {
		this.viewRange = viewRange;
		this.actionRange = actionRange;
	}
	
	public int getViewRange() {
		return viewRange;
	}
	
	public int getActionRange() {
		return actionRange;
	}
}
