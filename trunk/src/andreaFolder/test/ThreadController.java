package andreaFolder.test;

public class ThreadController {
	//messaggio tra i thread
	public boolean close = false;

	public synchronized void waitThread(){
		System.out.println("Sveglio l'altro");
		notify();
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void notifyCloseGame(){
		close = true;
		notify();
	}
}
