package logicClasses;
/**
 * Abstract Class Character
 * 
 * @author Andrea
 */
public abstract class Character {

	/** Idenfitier */
	String id;
	
	/** Current Life */
	private int currentLife;
	
	/** Max Life */
	private int maxLife;
	
	/** Score */
	private int score;
	
	/** Present State */
	private int currentState;
	
	/**
	 * Constructor Character
	 * 
	 * @param id - (String) Idenfier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public Character( String id, int currentLife, int maxLife ) {
		this.id = id;
		this.currentLife = currentLife;
		this.maxLife = maxLife;
		this.currentState = State.DEFAULT;
		this.score = 0;
	}
	
	/**
	 * 	Method that increases the value currentLife respecting the limit set by Maxlife
	 * 
	 * @param newLife - (int) Valore vita da aggiungere
	 */
	void addLife( int newLife ) {
		if( currentLife + newLife >= maxLife) {
			currentLife = maxLife;
		}
		else
			currentLife = currentLife + newLife;
	}
	
	// DA RIVEDERE
	/**
	 * Decrements the current life, leaving a consistent value
	 * 
	 * @param removedLife - (int) Valore vita da rimuovere
	 */
	void removeLife( int removedLife ){
		currentLife = currentLife - removedLife;
		if( currentLife <= 0 ) {
			currentLife = 0;
			currentState = State.DEATH;
		}
	}
	
	// DA RIVEDERE
	/**
	 * Change the current state. Please fit only defined states in class State
	 * 
	 * @param newState - (int) New State
	 */
	void setState( int newState ){
		currentState = newState;
	}
	
	// DA RIVEDERE 
	/**
	 * Set new Score
	 * 
	 * @param newScore - (int) New Score
	 */
	void setScore( int newScore ){
		if( newScore > 0 )
			score = newScore;
	}

	/**
	 * It gets current Life
	 * 
	 * @return currentLife 
	 */
	public int getCurrentLife() {
		return currentLife;
	}

	/**
	 * It gets max life
	 * 
	 * @return maxLife
	 */
	public int getMaxLife() {
		return maxLife;
	}

	/**
	 * Sets the value of maximum life
	 * 
	 * @param maxLife - (int) nuovo valore di massima vita
	 */
	public void setMaxLife( int newMaxLife ) {
		if( newMaxLife > 0) {
			this.maxLife = newMaxLife;
			if( currentLife > maxLife )
				this.currentLife = maxLife;
		}
	}

	/**
	 * It return current state
	 * 
	 * @return currentState
	 */
	public int getCurrState() {
		return currentState;
	}

	/**
	 * It return current score
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}
}
