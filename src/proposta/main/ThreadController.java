package proposta.main;

public class ThreadController {
	//message between two thread
	public boolean close = false;

	public synchronized void waitThread(){
		// debug print
//		System.out.println( "Sveglio l'altro" );
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
