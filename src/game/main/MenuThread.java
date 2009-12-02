package game.main;

import game.main.menu.MainMenu;

public class MenuThread implements Runnable {
	
	ThreadController threadController;

	@Override
	public void run() {
		threadController = new ThreadController();
		MainMenu menu = new MainMenu(threadController);
		menu.createMenu();
	}

}
