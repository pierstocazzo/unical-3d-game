package andreaFolder.test;


public class testSwing implements Runnable{
	
	ThreadController tc;

	@Override
	public void run() {
		tc = new ThreadController();
		MainMenu menu = new MainMenu(tc);
	}

}
