package game.common;

import java.io.Serializable;

/**
 * Class Movement
 * a single movement in a path
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class Movement implements Serializable {
	
	/** directionr of current movement */
	Direction direction;
	
	/** length of current movement */
	float length;
	
	/**
	 * Constructor
	 * 
	 * @param (Vector3f) direction
	 * @param (float) length
	 */
	public Movement( Direction direction, float length ){
		this.direction = direction;
		this.length = length;
	}
	
	/**
	 * Get direction of current movement
	 * 
	 * @return (Vector3f)
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Get length of current movement
	 * 
	 * @return (float)
	 */
	public float getLength() {
		return length;
	}
}