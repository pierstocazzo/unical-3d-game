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

package utils.test;

import java.util.LinkedList;
import java.util.List;

public class mavaffanculo {
	
	private static int calculateNextScore( int level ) {
		if ( level == 1 ) {
			return 25;
		}
		
		return (int) (Math.pow( 5*level, 2 ) + calculateNextScore( level - 1 ));
	}
	
	public static void main(String[] args) {
		System.out.println( calculateNextScore(200) );
		List<Integer> interi = new LinkedList<Integer>();
		interi.add(1);
		interi.add(2);
		interi.add(3);
		System.out.println( interi.size() );
		
		for( int i=0; i<interi.size(); i++ ) {
			System.out.println( interi.get(i) + " " );
		}
	}
	
}
