package game.core;

import game.enemyAI.Movement;

import com.jme.math.Vector3f;

/**
 * Abstract Class LogicCharacter
 */
public abstract class LogicCharacter {

	/** Idenfitier */
	String id;
	
	/** Current Life */
	int currentLife;
	
	/** Max Life - the maximum energy the character can reach */
	int maxLife;
	
	/** Current Score of the player */
	int score;
	
	/** Present LogicState */
	EnumCharacterState currentState;

    /** Current Position Vector of the character */
	public Vector3f position;

        /** initial position of the current path segment */
	Vector3f initialPosition;

	/** Control variables */
    boolean rest;
    boolean movingForward;
    boolean movingBackward;
    boolean strafingLeft;
    boolean strafingRight;
    boolean jumping;
    boolean onGround;

	/**
	 * Constructor LogicCharacter
	 * 
	 * @param id - (String) Idenfier
	 * @param maxLife - (int) Maximum Life
	 * @param position - (Vector3d) the initial position
	 */
	public LogicCharacter( String id, int maxLife, Vector3f position ) {
		this.id = id;
		this.maxLife = maxLife;
		this.currentLife = maxLife;
		this.currentState = EnumCharacterState.DEFAULT;
		this.score = 0;
		this.position = new Vector3f(position);
		//this.position.set( position );
                this.initialPosition = new Vector3f(position);
	}
	
	/**
	 * 	Method that increases the value currentLife respecting the limit setted by Maxlife
	 * 
	 * @param lifeToAdd - (int) Valore vita da aggiungere
	 */
	void addLife( int lifeToAdd ) {
		if( currentLife + lifeToAdd >= maxLife) {
			currentLife = maxLife;
		}
		else
			currentLife = currentLife + lifeToAdd;
	}
	
	/**
	 * Decrements the current life, leaving a consistent value
	 * 
	 * @param removedLife - (int) Valore vita da rimuovere
	 */
	void removeLife( int removedLife ){
		currentLife = currentLife - removedLife;
		if( currentLife <= 0 ) {
			this.die();
		}
	}
	
	/**
	 * Change the current state.
	 * 
	 * @param newState - (State) the new state
	 */
	void setState( EnumCharacterState newState ){
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

	void addScore( int toAdd ) {
		score = score + toAdd;
	}
	
	void decreaseScore( int toDecrease ) {
		score = score - toDecrease;
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
	public EnumCharacterState getCurrState() {
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
	
	
    /** Function 
     * 
     * @return
     */
	public Vector3f getPosition() {
		return position;
	}

	/** 
	 * 
	 * @param position
	 */
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}
	
	/** 
	 * 
	 * @param bulletDamage - (int) the bullet damage power
	 */
	public void isShooted( int bulletDamage ) {
		if( currentLife - bulletDamage <= 0 )
			die();
		else
			currentLife = currentLife - bulletDamage;
	}

	// Just do nothing
	public void shoot() {
		// to override
	}

	public void die() {
		currentState = EnumCharacterState.DEATH;
		// TODO
	}
	
	public void rest() {
        rest = true;
        movingForward = false;
        movingBackward = false;
        strafingLeft = false;
        strafingRight = false;
        jumping = false;
        onGround = false;
	}
	

    public boolean getRest() {
        return rest;
    }

    public void setRest( boolean rest ) {
        this.rest = rest;
    }

    public boolean getMovingForward() {
        return movingForward;
    }

    public void setMovingForward( boolean movingForward ) {
        this.movingForward = movingForward;
    }

    public boolean getMovingBackward() {
        return movingBackward;
    }

    public void setMovingBackward( boolean movingBackward ) {
        this.movingBackward = movingBackward;
    }

    public boolean getStrafingLeft() {
        return strafingLeft;
    }

    public void setStrafingLeft( boolean strafingLeft ) {
        this.strafingLeft = strafingLeft;
    }

    public boolean getStrafingRight() {
        return strafingRight;
    }

    public void setStrafingRight( boolean strafingRight ) {
        this.strafingRight = strafingRight;
    }

    public boolean getJumping() {
        return jumping;
    }

    public void setJumping( boolean jumping ) {
        this.jumping = jumping;
    }

    public boolean getOnGround() {
        return onGround;
    }

    public void setOnGround( boolean onGround ) {
        this.onGround = onGround;
    }

	public abstract Movement getNextMovement();

        public Vector3f getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition( Vector3f position ) {
		this.initialPosition.set(position);
	}
}
