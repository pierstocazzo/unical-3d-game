package game.common;

import java.io.Serializable;

import com.jme.math.Vector3f;

/**
 * Class Movement
 * a single movement in a path
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class Movement implements Serializable {
	
	/** directionr of current movement */
	Vector3f direction;
	
	/** length of current movement */
	float length;
	
	/**
	 * Constructor
	 * 
	 * @param (Vector3f) direction
	 * @param (float) length
	 */
	public Movement( Vector3f direction, float length ){
		this.direction = direction;
		this.length = length;
	}

	/**
	 * Get direction of current movement
	 * 
	 * @return (Vector3f)
	 */
	public Vector3f getDirection() {
		return direction.clone();
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