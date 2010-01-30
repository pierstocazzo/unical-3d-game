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
			convert( "src/game/data/meshes/vegetation" );
			convert( "src/game/data/meshes/items" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finish();
	}
	
	void convert( String directory ) throws Exception {
		BufferedReader in = new BufferedReader( 
				new InputStreamReader( Runtime.getRuntime().exec( "ls " + directory ).getInputStream() ) );

		String line;
		while( (line = in.readLine()) != null ) {
			System.out.println(line);
			if( line.matches( ".*3ds" ) ) {
				// convert in binary jme file
				String newFile = line.replaceFirst( "3ds", "jme" );
				ModelConverter.convert( directory + line, directory + newFile );
				
				// remove converted model
				Runtime.getRuntime().exec( "rm " + directory + line );
			} 
		}
	}
}
