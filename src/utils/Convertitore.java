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

package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.jme.app.SimpleGame;


public class Convertitore extends SimpleGame {
	
	public static void main(String[] args) {
		new Convertitore().start();
	}

	@Override
	protected void simpleInitGame() {
		try {
//			convert( "src/game/data/meshes/vegetation/" );
			convert( "game/data/meshes/items/" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finish();
	}
	
	void convert( String directory ) throws Exception {
		BufferedReader in = new BufferedReader( 
				new InputStreamReader( Runtime.getRuntime().exec( "ls " + "src/" + directory ).getInputStream() ) );

		String line;
		while( (line = in.readLine()) != null ) {
			
			if( line.matches( ".*3ds" ) ) {
				System.out.println(line);
				System.out.println(directory + line);
//				 convert in binary jme file
				line.replaceFirst( "\n", "" );
				String newFile = line.replaceFirst( "3ds", "jme" );
				ModelConverter.convert( directory + line, directory + newFile );
				
				// remove converted model
				Runtime.getRuntime().exec( "rm " + "src/" + directory + line );
				
				System.out.println( line + " converted" );
			} 
		}
	}
}
