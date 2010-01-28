package utils.test;

import java.util.LinkedList;
import java.util.List;

public class mavaffanculo {
	
	private static int calculateNextScore( int level ) {
		if ( level == 1 ) {
			return 25;
		}
		
		return (int) (Math.pow( 5*level, 2 ) + calculateNextScore( level - 1 ));
	}
	
	public static void main(String[] args) {
		System.out.println( calculateNextScore(200) );
		List<Integer> interi = new LinkedList<Integer>();
		interi.add(1);
		interi.add(2);
		interi.add(3);
		System.out.println( interi.size() );
		
		for( int i=0; i<interi.size(); i++ ) {
			System.out.println( interi.get(i) + " " );
		}
	}
	
}
