package game.common;

import com.jme.math.Vector3f;

/**
 * Enum Direction.
 * Define directions used in the game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public enum Direction {
	
	FORWARD ( Vector3f.UNIT_Z ),
	
	BACKWARD ( Vector3f.UNIT_Z.negate() ),
	
	RIGHT ( Vector3f.UNIT_X.negate() ),
	
	LEFT ( Vector3f.UNIT_X ), 
	
	REST (Vector3f.UNIT_X ),
	
	LEFT_FORWARD ( Vector3f.UNIT_X.add(Vector3f.UNIT_Z) ),
	
	LEFT_BACKWARD ( Vector3f.UNIT_X.add( Vector3f.UNIT_Z.negate() ) ),
	
	RIGHT_FORWARD ( Vector3f.UNIT_X.negate().add( Vector3f.UNIT_Z ) ),
	
	RIGHT_BACKWARD (  Vector3f.UNIT_X.negate().add( Vector3f.UNIT_Z.negate() ) );
	
	/** current direction */
	Vector3f direction;
	
	/**
	 * Constructor
	 * 
	 * @param (Vector3f) direction
	 */
	Direction( Vector3f direction ){
		this.direction = direction;
	}
	
	/**
	 * Return vector direction
	 * 
	 * @return (Vector3f)
	 */
	public Vector3f toVector() {
		return direction;
	}
}
