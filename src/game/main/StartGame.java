package game.main;

public class StartGame {

	public static void main(String[] args) {
		Thread menuThread = new Thread(new MenuThread());
		menuThread.start();
	}
} 
