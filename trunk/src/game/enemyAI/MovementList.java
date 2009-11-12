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
			movements.add( new Movement( EnumDirection.FORWARD, 40 ) );
			movements.add( new Movement( EnumDirection.RIGHT, 40 ) );
			movements.add( new Movement( EnumDirection.BACKWARD, 40 ) );
			movements.add( new Movement( EnumDirection.LEFT, 40 ) );
			break;
			
		case SMALL_PERIMETER:
			movements.add( new Movement( EnumDirection.FORWARD, 10 ) );
			movements.add( new Movement( EnumDirection.RIGHT, 10 ) );
			movements.add( new Movement( EnumDirection.BACKWARD, 10 ) );
			movements.add( new Movement( EnumDirection.LEFT, 10 ) );
			break;
			
			// modified
		case HORIZONTAL_SENTINEL:
			movements.add( new Movement( EnumDirection.FORWARD, 10 ) );
			movements.add( new Movement( EnumDirection.BACKWARD, 10 ) );
			break;
			
		case VERTICAL_SENTINEL:
			movements.add( new Movement( EnumDirection.LEFT, 40 ) );
			movements.add( new Movement( EnumDirection.RIGHT, 40 ) );
			break;
			
		default:
			movements.add( new Movement( EnumDirection.REST, 0 ) );
			break;
		}
		
		it = movements.iterator();
//		it.next();
	}
	
	public enum MovementType {
		SMALL_PERIMETER,
		
		LARGE_PERIMETER,
		
		HORIZONTAL_SENTINEL,
		
		VERTICAL_SENTINEL;
	}
	
	public Movement getNextMovement() {
		if( it.hasNext() ) {
			return it.next();
		} else {
			it = movements.iterator();
			return it.next();
		}
	}
 
}