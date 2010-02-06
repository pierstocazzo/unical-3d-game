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

/** class <code>LogicEnergyPack</code>
 * <br>
 * Represents an energy pack the player can catch and use when
 * his energy (life) is few
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class LogicEnergyPack implements Serializable {
	
	/** Identifier */
	String id;
	
	/** the energy the pack can give to the player */
	int value;
	
	/** <code>LogicEnergyPack</code> constructor<br>
	 * Create a new energy pack
	 * @param value - (int) the energy the pack can give to the player
	 */
	public LogicEnergyPack( String id, int value ) {
		this.value = value;
		this.id = id;
	}

	/**
	 * Get life value associated
	 * 
	 * @return (int)
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set life value
	 * 
	 * @param (int) value
	 */
	public void setValue(int value) {
		this.value = value;
	}
}