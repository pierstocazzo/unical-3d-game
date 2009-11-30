package game.main;

import game.main.menu.MainMenu;


public class MenuThread implements Runnable{
	
	ThreadController tc;

	@Override
	public void run() {
		tc = new ThreadController();
		MainMenu menu = new MainMenu(tc);
		menu.createMenu();
	}

}
