package game.common;

import java.io.Serializable;
import java.util.LinkedList;

import game.common.Direction;
import game.common.Movement;

public class MovementList implements Serializable{
	private static final long serialVersionUID = 1L;
	
	LinkedList<Movement> movements;
//	Iterator<Movement> it;
	int curr = 0;
	 
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
		
//		it = movements.iterator();
		curr = movements.indexOf(movements.getFirst());
	}
	
	public Movement getNextMovement() {
//		if( it.hasNext() ) {
//			return it.next();
//		} else {
//			it = movements.iterator();
//			return it.next();
//		}
		if( curr + 1 >= movements.size())
			curr = movements.indexOf(movements.getFirst());
		else
			curr = curr + 1;
		return movements.get(curr);
	}
 
	
	public enum MovementType implements Serializable{
		SMALL_PERIMETER,
		
		LARGE_PERIMETER,
		
		HORIZONTAL_SENTINEL,
		
		VERTICAL_SENTINEL, 
		
		REST;
	}
}