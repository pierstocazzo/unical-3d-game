package game.common;

import java.util.LinkedList;

import com.jme.math.Vector3f;

/**
 * Class PointPath
 * It can generate a movements list
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class PointPath {

	/** Point list */
	LinkedList<Point> pointList;
	
	/** movements list generate */
	public LinkedList<Movement> movementslist;
	
	/**
	 * Constructor
	 * 
	 * Initialize field
	 */
	public PointPath(){
		pointList = new LinkedList<Point>();
		movementslist = new LinkedList<Movement>();
	}
	
	public void add( Point point ){
		pointList.add( point );
	}
	
	public Point remove( int index ){
		return pointList.remove( index );
	}
	
	public Point get( int index ){
		return pointList.get( index );
	}
	
	public int size(){
		return pointList.size();
	}
	
	public LinkedList<Movement> generateMovementsList(){
		//se ho meno di due punti ritorno il path vuoto
		if( pointList.size() < 2 ){
			movementslist.add( new Movement( Direction.REST, 0));
			return movementslist;
		}
		
		for( int curr = 0; curr < pointList.size() ; curr++ ){
						
			Vector3f first = new Vector3f( pointList.get( curr ).x, 0, pointList.get( curr ).z );
			
			Vector3f second = null;
			
			if ( curr + 1 == pointList.size() ) 
				second = new Vector3f( pointList.get( 0 ).x, 0, pointList.get( 0 ).z );
			else
				second = new Vector3f( pointList.get( curr + 1 ).x, 0, pointList.get( curr + 1 ).z );
			
			Vector3f direction = first.negate().add(second);
			
			if(first.equals(second))
				direction = first.clone();
			
			movementslist.add( new Movement( direction, direction.length() ) );
			// sembra che la lista ha un elemento in più
		}
		return movementslist;
	}
}
