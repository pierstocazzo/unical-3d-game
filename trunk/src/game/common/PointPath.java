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

import java.util.LinkedList;

import com.jme.math.Vector3f;

/**
 * Class PointPath
 * <br>
 * It can generate a movements list from a list
 * of Point
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class PointPath {

	/** Point list */
	LinkedList<Point> pointList;
	
	/** movements list generate */
	public LinkedList<Movement> movementslist;
	
	/**
	 * Constructor
	 * 
	 * Initialize field
	 */
	public PointPath(){
		pointList = new LinkedList<Point>();
		movementslist = new LinkedList<Movement>();
	}
	
	public void add( Point point ){
		pointList.add( point );
	}
	
	public Point remove( int index ){
		return pointList.remove( index );
	}
	
	public Point get( int index ){
		return pointList.get( index );
	}
	
	public int size(){
		return pointList.size();
	}
	
	/**
	 * Function <code>generateMovementsList</code>
	 * <br>
	 * Starts from a set of point to generate a complete
	 * list of movements
	 * @return LinkedList<Movement>
	 */
	public LinkedList<Movement> generateMovementsList(){
		/**
		 * Return a valid path only if there are 
		 * 2 point at least.
		 */
		if( pointList.size() < 2 ){
			movementslist.add( new Movement( Direction.REST, 0));
			return movementslist;
		}
		
		for( int curr = 0; curr < pointList.size() ; curr++ ){
						
			Vector3f first = new Vector3f( pointList.get( curr ).x, 0, pointList.get( curr ).z );
			
			Vector3f second = null;
			
			if ( curr + 1 == pointList.size() ) 
				second = new Vector3f( pointList.get( 0 ).x, 0, pointList.get( 0 ).z );
			else
				second = new Vector3f( pointList.get( curr + 1 ).x, 0, pointList.get( curr + 1 ).z );
			
			Vector3f direction = first.negate().add(second);
			
			if(first.equals(second))
				direction = first.clone();
			
			movementslist.add( new Movement( direction, direction.length() ) );
		}
		return movementslist;
	}
}
