package game.main;

import game.main.menu.MainMenu;

public class StartGame {

	public static void main( String[] args ) {
		MainMenu menu = new MainMenu();
		menu.createMenu();
		menu.setVisible(true);
	} 
}