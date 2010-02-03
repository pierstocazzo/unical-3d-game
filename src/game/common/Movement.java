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

import java.io.Serializable;

import com.jme.math.Vector3f;

/**
 * Class Movement
 * a single movement in a path
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class Movement implements Serializable {
	
	/** direction of current movement */
	Vector3f direction;
	
	/** length of current movement */
	float length;
	
	/**
	 * Constructor
	 * 
	 * @param (Vector3f) direction
	 * @param (float) length
	 */
	public Movement( Vector3f direction, float length ){
		this.direction = direction;
		this.length = length;
	}

	/**
	 * Get direction of current movement
	 * 
	 * @return (Vector3f)
	 */
	public Vector3f getDirection() {
		return direction.clone();
	}

	/**
	 * Get length of current movement
	 * 
	 * @return (float)
	 */
	public float getLength() {
		return length;
	}
}