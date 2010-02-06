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

package game.common;

import com.jme.math.Vector3f;

/**
 * Enum Direction.
 * <br>
 * Define directions used in the game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class Direction {
	
	static Vector3f FORWARD = Vector3f.UNIT_Z ;
	static Vector3f BACKWARD = Vector3f.UNIT_Z.negate();
	static Vector3f RIGHT = Vector3f.UNIT_X.negate();
	static Vector3f LEFT = Vector3f.UNIT_X; 
	static Vector3f REST = Vector3f.ZERO;
	static Vector3f LEFT_FORWARD = Vector3f.UNIT_X.add(Vector3f.UNIT_Z);
	static Vector3f LEFT_BACKWARD = Vector3f.UNIT_X.add( Vector3f.UNIT_Z.negate() );
	static Vector3f RIGHT_FORWARD = Vector3f.UNIT_X.negate().add( Vector3f.UNIT_Z );
	static Vector3f RIGHT_BACKWARD =  Vector3f.UNIT_X.negate().add( Vector3f.UNIT_Z.negate() );
	
	/** current direction */
	Vector3f direction;
	
	/**
	 * Constructor
	 * 
	 * @param (Vector3f) direction
	 */
	Direction( Vector3f direction ){
		this.direction = direction;
	}
	
	/**
	 * Return vector direction
	 * 
	 * @return (Vector3f)
	 */
	public Vector3f toVector() {
		return direction;
	}
	
	/**
	 * Set the direction
	 * @param direction
	 * @return
	 */
	public Direction setDirection( Vector3f direction ){
//		System.out.println("Direction : " + direction);
		this.direction = direction;
		return this;
	}
}
