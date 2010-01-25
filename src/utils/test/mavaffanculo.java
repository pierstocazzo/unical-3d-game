package utils.test;

public class mavaffanculo {
	
	private static int calculateNextScore( int level ) {
		if ( level == 1 ) {
			return 25;
		}
		
		return (int) (Math.pow( 5*level, 2 ) + calculateNextScore( level - 1 ));
	}
	
	public static void main(String[] args) {
		System.out.println( calculateNextScore(200) );
	}
	
}
