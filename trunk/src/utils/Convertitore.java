package utils;

import com.jme.app.SimpleGame;


public class Convertitore extends SimpleGame {
	
	public static void main(String[] args) {
		new Convertitore().start();
	}

	@Override
	protected void simpleInitGame() {
		ModelConverter.convert("game/data/models/soldier/sold2.ms3d", "game/data/models/soldier/enemy.jme" );
		finish();
	}

}
