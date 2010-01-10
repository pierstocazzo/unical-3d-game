package game.common;

import java.io.Serializable;
import java.util.LinkedList;

import game.common.Direction;
import game.common.Movement;

public class MovementList implements Serializable{
	private static final long serialVersionUID = 1L;
	
	LinkedList<Movement> movements;
	int curr = 0;
	 
	public MovementList( MovementType movementType ) {
		
		movements = new LinkedList<Movement>();
				
		switch ( movementType ) {
		case LARGE_PERIMETER:
			movements.add( new Movement( Direction.FORWARD, 35 ) );
			movements.add( new Movement( Direction.RIGHT_FORWARD, 10 ) );
			movements.add( new Movement( Direction.RIGHT, 35 ) );
			movements.add( new Movement( Direction.RIGHT_BACKWARD, 10 ) );
			movements.add( new Movement( Direction.BACKWARD, 35 ) );
			movements.add( new Movement( Direction.LEFT_BACKWARD, 10 ) );
			movements.add( new Movement( Direction.LEFT, 35 ) );
			movements.add( new Movement( Direction.LEFT_FORWARD, 10 ) );
			break;
			
		case SMALL_PERIMETER:
			movements.add( new Movement( Direction.FORWARD, 17 ) );
			movements.add( new Movement( Direction.RIGHT_FORWARD, 5 ) );
			movements.add( new Movement( Direction.RIGHT, 17 ) );
			movements.add( new Movement( Direction.RIGHT_BACKWARD, 5 ) );
			movements.add( new Movement( Direction.BACKWARD, 17 ) );
			movements.add( new Movement( Direction.LEFT_BACKWARD, 5 ) );
			movements.add( new Movement( Direction.LEFT, 17 ) );
			movements.add( new Movement( Direction.LEFT_FORWARD, 5 ) );
			break;
			
		case HORIZONTAL_SENTINEL:
			movements.add( new Movement( Direction.FORWARD, 40 ) );
			movements.add( new Movement( Direction.BACKWARD, 40 ) );
			break;
			
		case VERTICAL_SENTINEL:
			movements.add( new Movement( Direction.RIGHT, 40 ) );
			movements.add( new Movement( Direction.LEFT, 40 ) );
			break;
			
		case CIRCLE_SENTINEL_SMALL:
			movements.add( new Movement( Direction.RIGHT_BACKWARD, 20 ) );
			movements.add( new Movement( Direction.BACKWARD, 20 ) );
			movements.add( new Movement( Direction.LEFT_BACKWARD, 20 ) );
			movements.add( new Movement( Direction.LEFT, 20 ) );
			movements.add( new Movement( Direction.LEFT_FORWARD, 20 ) );
			movements.add( new Movement( Direction.FORWARD, 20 ) );
			movements.add( new Movement( Direction.RIGHT_FORWARD, 20 ) );
			movements.add( new Movement( Direction.RIGHT, 20 ) );
			break;
			
		case CIRCLE_SENTINEL_LARGE:
			movements.add( new Movement( Direction.RIGHT_BACKWARD, 40 ) );
			movements.add( new Movement( Direction.BACKWARD, 40 ) );
			movements.add( new Movement( Direction.LEFT_BACKWARD, 40 ) );
			movements.add( new Movement( Direction.LEFT, 40 ) );
			movements.add( new Movement( Direction.LEFT_FORWARD, 40 ) );
			movements.add( new Movement( Direction.FORWARD, 40 ) );
			movements.add( new Movement( Direction.RIGHT_FORWARD, 40 ) );
			movements.add( new Movement( Direction.RIGHT, 40 ) );
			break;
			
		case REST:
			movements.add( new Movement( Direction.REST, 0 ) );
			break;
		}
		
		curr = movements.indexOf(movements.getFirst());
	}
	
	public Movement getNextMovement() {
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
		
		CIRCLE_SENTINEL_SMALL,
		
		CIRCLE_SENTINEL_LARGE,
		
		REST;
	}
}