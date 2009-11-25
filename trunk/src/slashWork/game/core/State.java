package slashWork.game.core;

public enum State {
	
	/**
	 * The character is in default state
	 */
	DEFAULT ( 0, 100 ),
	
	/**
	 * The character is in alert because he has 
	 * seen something strange
	 */
	ALERT ( 80, 200 ),
	
	/**
	 * The character is attacking you, because he
	 * has seen you and he have to kill
	 * everybody unknown
	 */
	ATTACK ( 180, 180 );
	
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
