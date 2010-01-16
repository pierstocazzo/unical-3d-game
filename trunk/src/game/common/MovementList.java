package game.common;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import game.common.Direction;
import game.common.Movement;

/**
 * Class {@link MovementList}
 * a closed path, a list of movements
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class MovementList implements Serializable{
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** Movements list */
	LinkedList<Movement> movements;
	
	/** index of current movement */
	int curr = 0;
	
	/**
	 * Constructor
	 * 
	 * @param (MovementType) movementType
	 */
	public MovementList( MovementType movementType ) {
		
		/** initialize movements list */
		movements = new LinkedList<Movement>();
		
		/** For each movement type add correspondent movements*/
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
				
			case DIAGONAL_SENTINEL_MAIN:
				movements.add( new Movement( Direction.RIGHT_BACKWARD, 40 ) );
				movements.add( new Movement( Direction.LEFT_FORWARD, 40 ) );
				break;
				
			case DIAGONAL_SENTINEL_SECONDARY:
				movements.add( new Movement( Direction.RIGHT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.LEFT_BACKWARD, 40 ) );
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
			
			case ELLIPTICAL_PERIMETER_HORIZONTAL:
				movements.add( new Movement( Direction.RIGHT_BACKWARD, 40 ) );
				movements.add( new Movement( Direction.BACKWARD, 40 ) );
				movements.add( new Movement( Direction.LEFT_BACKWARD, 40 ) );
				movements.add( new Movement( Direction.LEFT, 120 ) );
				movements.add( new Movement( Direction.LEFT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.FORWARD, 40 ) );
				movements.add( new Movement( Direction.RIGHT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.RIGHT, 120 ) );
				break;
				
			case REST:
				movements.add( new Movement( Direction.REST, 0 ) );
				break;
			
			case EIGHT_PATH_SMALL:
				movements.add( new Movement( Direction.RIGHT_BACKWARD, 70 ) );
				movements.add( new Movement( Direction.RIGHT, 20 ) );
				movements.add( new Movement( Direction.RIGHT_FORWARD, 20 ) );
				movements.add( new Movement( Direction.FORWARD, 20 ) );
				movements.add( new Movement( Direction.LEFT_FORWARD, 20 ) );
				movements.add( new Movement( Direction.LEFT, 20 ) );
				movements.add( new Movement( Direction.LEFT_BACKWARD, 70 ) );
				movements.add( new Movement( Direction.LEFT, 20 ) );
				movements.add( new Movement( Direction.LEFT_FORWARD, 20 ) );
				movements.add( new Movement( Direction.FORWARD, 20 ) );
				movements.add( new Movement( Direction.RIGHT_FORWARD, 20 ) );
				movements.add( new Movement( Direction.RIGHT, 20 ) );
				break;
				
			case EIGHT_PATH_LARGE:
				movements.add( new Movement( Direction.RIGHT_BACKWARD, 140 ) );
				movements.add( new Movement( Direction.RIGHT, 40 ) );
				movements.add( new Movement( Direction.RIGHT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.FORWARD, 40 ) );
				movements.add( new Movement( Direction.LEFT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.LEFT, 40 ) );
				movements.add( new Movement( Direction.LEFT_BACKWARD, 140 ) );
				movements.add( new Movement( Direction.LEFT, 40 ) );
				movements.add( new Movement( Direction.LEFT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.FORWARD, 40 ) );
				movements.add( new Movement( Direction.RIGHT_FORWARD, 40 ) );
				movements.add( new Movement( Direction.RIGHT, 40 ) );
				break;
		}
		// set current at first element
		curr = movements.indexOf(movements.getFirst());
	}
	
	/**
	 * Constructor
	 * It receives directly a list of move of movements
	 * 
	 * @param (LinkedList<Movement>) movements
	 */
	public MovementList(LinkedList<Movement> movements) {
		this.movements = movements;
		if( movements.size() > 0 )
			curr = movements.indexOf(movements.getFirst());
	}

	/**
	 * Get next movement
	 * 
	 * @return {@link Movement}
	 */
	public Movement getNextMovement() {
		if( curr + 1 >= movements.size())
			curr = movements.indexOf(movements.getFirst());
		else
			curr = curr + 1;
		return movements.get(curr);
	}
	
	/**
	 * Get movements size
	 * 
	 * @return (int) 
	 */
	public int size() {
		return movements.size();
	}
	
	/**
	 * Get movement with index i
	 * 
	 * @param (int) i
	 * @return (Movement)
	 */
	public Movement get( int i ) {
		return movements.get(i);
	}
	
	/**
	 * Enum {@link MovementType}
	 * 
	 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
	 */
	public enum MovementType implements Serializable{
		
		// standard movement type
		
		SMALL_PERIMETER,
		
		LARGE_PERIMETER,
		
		HORIZONTAL_SENTINEL,
		
		VERTICAL_SENTINEL, 
		
		CIRCLE_SENTINEL_SMALL,
		
		CIRCLE_SENTINEL_LARGE,
		
		DIAGONAL_SENTINEL_MAIN,
		
		DIAGONAL_SENTINEL_SECONDARY,
		
		ELLIPTICAL_PERIMETER_HORIZONTAL,
		
		EIGHT_PATH_SMALL,
		
		EIGHT_PATH_LARGE,
		
		REST;

		/**
		 * Get a random movement type
		 * 
		 * @return {@link MovementType}
		 */
		public static MovementType getRandom() {
			Random r = new Random();
			int num = r.nextInt() % 12;
			MovementType moveType = MovementType.REST;
			switch (num) {
				case 0:moveType = MovementType.CIRCLE_SENTINEL_LARGE;break;
				case 1:moveType = MovementType.CIRCLE_SENTINEL_SMALL;break;
				case 2:moveType = MovementType.DIAGONAL_SENTINEL_MAIN;break;
				case 3:moveType = MovementType.DIAGONAL_SENTINEL_SECONDARY;break;
				case 4:moveType = MovementType.EIGHT_PATH_LARGE;break;
				case 5:moveType = MovementType.EIGHT_PATH_SMALL;break;
				case 6:moveType = MovementType.ELLIPTICAL_PERIMETER_HORIZONTAL;break;
				case 7:moveType = MovementType.HORIZONTAL_SENTINEL;break;
				case 8:moveType = MovementType.LARGE_PERIMETER;break;
				case 9:moveType = MovementType.REST;break;
				case 10:moveType = MovementType.SMALL_PERIMETER;break;
				case 11:moveType = MovementType.VERTICAL_SENTINEL;break;
			}
			return moveType;
		}
	}
}