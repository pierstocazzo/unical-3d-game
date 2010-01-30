package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;


public class Convertitore extends SimpleGame {
	
	public static void main(String[] args) {
		Logger.global.setLevel(Level.OFF);
		new Convertitore().start();
	}

	@Override
	protected void simpleInitGame() {
		
		try {
			BufferedReader in = new BufferedReader( 
				new InputStreamReader( Runtime.getRuntime().exec( "ls src/game/data/meshes/vegetation" ).getInputStream() ) );
			
			String line;
			while( (line = in.readLine()) != null ) {
				if( line.matches( ".*3ds" ) ) {
					String newFile = line.replaceFirst( "3ds", "jme" );
					System.out.println(line);
					ModelConverter.convert("game/data/meshes/vegetation/" + line, "game/data/meshes/vegetation/" + newFile );
					try {
						Runtime.getRuntime().exec( "rm src/game/data/meshes/vegetation/" + line );
					}catch (Exception e) {
						System.err.println("Non rimosso");
					}
				} 
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
//		ModelConverter.convert("game/data/models/soldier/sold2.ms3d", "game/data/models/soldier/enemy.jme" );
		finish();
	}

}
