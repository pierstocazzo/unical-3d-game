/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.core;

import java.io.Serializable;

import game.common.Movement;
import game.common.WeaponType;

import com.jme.math.Vector3f;

/**
 * Abstract Class LogicCharacter
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
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
	boolean addLife( int lifeToAdd ) {
		if( currentLife >= maxLife ) 
			return false;
		else {
			currentLife = currentLife + lifeToAdd;
			if( currentLife > maxLife )
				currentLife = maxLife;
			return true;
		}
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
	 * Set the value of maximum life
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
	
    /** Return current position
     * 
     * @return
     */
	public Vector3f getPosition() {
		return position;
	}

	/** 
	 * Set current position
	 * 
	 * @param position
	 */
	public void setPosition( Vector3f position ) {
		this.position.set(position);
	}
	
	/** 
	 * Calculate life damage if the character has hit
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

	/**
	 * True - if character is shooting
	 * False - otherwise
	 * @return
	 */
	public abstract boolean shoot() ;

	/**
	 * A character deaths
	 * 
	 * @param shooterId
	 */
	public void die( String shooterId ) {
		world.removeCharacter(id);
	}
	
	/** 
	 * Reset character movements 
	 */
	public void rest() {
        moving = false;
        jumping = false;
	}

	/**
	 * True - the character is moving
	 * False - otherwise
	 * 
	 * @return (boolean)
	 */
    public boolean isMoving() {
        return moving;
    }

    /**
     * Set move value
     * 
     * @param (boolean) moving
     */
    public void setMoving( boolean moving ) {
        this.moving = moving;
    }

    /**
     * True - the character is jumping
     * False - otherwise
     * 
     * @return (boolean)
     */
    public boolean getJumping() {
        return jumping;
    }

    /**
     * Set jumping value
     * 
     * @param (boolean) jumping
     */
    public void setJumping( boolean jumping ) {
        this.jumping = jumping;
    }

    /**
     * Return next movement
     * 
     * @return {@link Movement}
     */
	public abstract Movement getNextMovement();
	
	/**
	 * Return current weapon type
	 * 
	 * @return {@link WeaponType}
	 */
	public abstract WeaponType getCurrentWeapon();

	/**
	 * Add an ammo pack to character equipment
	 * 
	 * @param  {@link WeaponType}
	 * @param (int) quantity
	 * @return (boolean)
	 */
	public abstract boolean addAmmo( WeaponType weaponType, int quantity ) ;
}
