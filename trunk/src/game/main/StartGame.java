package game.main;

import game.menu.MainMenu;
import game.common.*;

public class StartGame {

	public static void main( String[] args ) {
		GameConfiguration.init();
		State.init();
		MainMenu menu = new MainMenu();
		menu.setVisible(true);
	} 
} 	
