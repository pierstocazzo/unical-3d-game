package andreaFolder.test;

public class test {

	public static void main(String[] args) {
		Thread menuThread = new Thread(new testSwing());
		menuThread.start();
	}
}
//non rileva bene il tasto esce da dentro il gioco
