package game.enemyAI;

import java.util.Iterator;
import java.util.LinkedList;

public class MovementList{
	
	LinkedList<Movement> movements;
	Iterator<Movement> it;
	
	public MovementList( MovementType movementType ) {
		
		movements = new LinkedList<Movement>();
				
		switch ( movementType ) {
		case LARGE_PERIMETER:
			movements.add( new Movement( Direction.FORWARD, 40 ) );
			movements.add( new Movement( Direction.RIGHT, 40 ) );
			movements.add( new Movement( Direction.BACKWARD, 40 ) );
			movements.add( new Movement( Direction.LEFT, 40 ) );
			break;
			
		case SMALL_PERIMETER:
			movements.add( new Movement( Direction.FORWARD, 20 ) );
			movements.add( new Movement( Direction.RIGHT, 20 ) );
			movements.add( new Movement( Direction.BACKWARD, 20 ) );
			movements.add( new Movement( Direction.LEFT, 20 ) );
			break;
			
		case HORIZONTAL_SENTINEL:
			movements.add( new Movement( Direction.FORWARD, 40 ) );
			movements.add( new Movement( Direction.BACKWARD, 40 ) );
			break;
			
		case VERTICAL_SENTINEL:
			movements.add( new Movement( Direction.RIGHT, 40 ) );
			movements.add( new Movement( Direction.LEFT, 40 ) );
			break;
			
		case REST:
			movements.add( new Movement( Direction.REST, 0 ) );
			break;
		}
		
		it = movements.iterator();
	}
	
	public Movement getNextMovement() {
		if( it.hasNext() ) {
			return it.next();
		} else {
			it = movements.iterator();
			return it.next();
		}
	}
 
	
	public enum MovementType {
		SMALL_PERIMETER,
		
		LARGE_PERIMETER,
		
		HORIZONTAL_SENTINEL,
		
		VERTICAL_SENTINEL, 
		
		REST;
	}
}