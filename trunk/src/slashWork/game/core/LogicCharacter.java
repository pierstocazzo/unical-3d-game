package slashWork.game.core;

import java.io.Serializable;

import slashWork.game.enemyAI.Movement;

import com.jme.math.Vector3f;

/**
 * Abstract Class LogicCharacter
 */
@SuppressWarnings("serial")
public abstract class LogicCharacter implements Serializable {

	/** Idenfitier */
	String id;
	
	/** Current Life */
	int currentLife;
	
	/** Max Life - the maximum energy the character can reach */
	int maxLife;
	
	/** Current Score of the player */
	int score;
	
    /** Current Position Vector of the character */
	Vector3f position;
	
	/** the logic world in whitch the character live */
	LogicWorld world;

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
	 * Create a new character
	 * @param id - (String) Idenfier
	 * @param maxLife - (int) Maximum Life
	 * @param position - (Vector3d) the initial position
	 * @param world - (LogicWorld) the logic world in whitch the character live
	 */
	public LogicCharacter( String id, int maxLife, Vector3f position, LogicWorld world ) {
		this.id = id;
		this.maxLife = maxLife;
		this.currentLife = maxLife;
		this.score = 0;
		this.position = new Vector3f(position);
		this.world = world;
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
		if( currentLife < 0 ) {
			this.die();
		}
	}
	
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
	public void setPosition( Vector3f position ) {
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
		world.removeCharacter(id);
	}
	
	/** reset character movements */
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
    	this.rest = !movingForward;
        this.movingForward = movingForward;
    }

    public boolean getMovingBackward() {
        return movingBackward;
    }

    public void setMovingBackward( boolean movingBackward ) {
    	this.rest = !movingBackward;
        this.movingBackward = movingBackward;
    }

    public boolean getStrafingLeft() {
        return strafingLeft;
    }

    public void setStrafingLeft( boolean strafingLeft ) {
    	this.rest = !strafingLeft;
        this.strafingLeft = strafingLeft;
    }

    public boolean getStrafingRight() {
        return strafingRight;
    }

    public void setStrafingRight( boolean strafingRight ) {
    	this.rest = !strafingRight;
        this.strafingRight = strafingRight;
    }

    public boolean getJumping() {
        return jumping;
    }

    public void setJumping( boolean jumping ) {
    	this.rest = !jumping;
        this.jumping = jumping;
    }

    public boolean getOnGround() {
        return onGround;
    }

    public void setOnGround( boolean onGround ) {
        this.onGround = onGround;
    }

	public abstract Movement getNextMovement();
	
	public abstract WeaponType getCurrentWeapon();
}
