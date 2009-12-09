package slashWork.game.main;

import slashWork.game.main.menu.InGameMenu;
import slashWork.game.main.menu.LoadingFrame;

public class ThreadController {
	//message between two thread
	public boolean close = false;
	InGameMenu menu;
	public LoadingFrame loadingFrame;
	public GameThread gameThread;
	
	public ThreadController(LoadingFrame loadingFrame, GameThread gameThread) {
		this.loadingFrame = loadingFrame;
		this.gameThread = gameThread;
	}

	public synchronized void notifyCloseGame(){
		close = true;
		WakeUpGame();
	}
	
	public synchronized void gameWait(){
		menu = new InGameMenu(this);
		menu.setVisible(true);
		menu.toFront();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		menu.removeAll();
//		menu.setVisible(false);
	}

	public synchronized void WakeUpGame() {
		notify();
		try {
			wait(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
