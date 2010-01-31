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
