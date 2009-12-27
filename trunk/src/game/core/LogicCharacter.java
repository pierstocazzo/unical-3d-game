package game.core;

import java.io.Serializable;

import game.common.Movement;
import game.common.WeaponType;

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
	
    /** Current Position Vector of the character */
	Vector3f position;
	
	/** the logic world in whitch the character live */
	LogicWorld world;

	/** Control variables */
    boolean moving;
    boolean jumping;

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
	 * @param shooterId 
	 */
	public void isShooted( int bulletDamage, String shooterId ) {
		if( currentLife - bulletDamage <= 0 )
			die( shooterId );
		else
			currentLife = currentLife - bulletDamage;
	}

	// Just do nothing
	public boolean shoot() {
		return false;
		// to override
	}

	public void die( String shooterId ) {
		world.removeCharacter(id);
	}
	
	/** reset character movements */
	public void rest() {
        moving = false;
        jumping = false;
	}

    public boolean isMoving() {
        return moving;
    }

    public void setMoving( boolean moving ) {
        this.moving = moving;
    }

    public boolean getJumping() {
        return jumping;
    }

    public void setJumping( boolean jumping ) {
        this.jumping = jumping;
    }

	public abstract Movement getNextMovement();
	
	public abstract WeaponType getCurrentWeapon();
}
