package game.logic;

import com.jme.math.Vector3f;
import game.physics.Characterable;

/**
 * Abstract Class LogicCharacter
 * 
 * @author Andrea
 */
public abstract class LogicCharacter implements Characterable {

	/** Idenfitier */
	String id;
	
	/** Current Life */
	int currentLife;
	
	/** Max Life */
	int maxLife;
	
	/** Score */
	int score;
	
	/** Present LogicState */
	int currentState;

        /** Current Position Vector */
        Vector3f position;
	
	/**
	 * Constructor LogicCharacter
	 * 
	 * @param id - (String) Idenfier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicCharacter( String id, int currentLife, int maxLife ) {
		this.id = id;
		this.currentLife = currentLife;
		this.maxLife = maxLife;
		this.currentState = LogicState.DEFAULT;
		this.score = 0;
                this.position = new Vector3f();
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
			currentState = LogicState.DEATH;
		}
	}
	
	// DA RIVEDERE
	/**
	 * Change the current state. Please fit only defined states in class LogicState
	 * 
	 * @param newState - (int) New LogicState
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

        public void refresh( Vector3f position ) {
            this.position.set(position);
            System.out.println("Posizione corrente logica: " + this.position.toString());
        }

}
