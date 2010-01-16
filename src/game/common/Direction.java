package game.common;

import com.jme.math.Vector3f;

/**
 * Enum Direction.
 * Define directions used in the game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class Direction {
	
	static Vector3f FORWARD = Vector3f.UNIT_Z ;
	static Vector3f BACKWARD = Vector3f.UNIT_Z.negate();
	static Vector3f RIGHT = Vector3f.UNIT_X.negate();
	static Vector3f LEFT = Vector3f.UNIT_X; 
	static Vector3f REST = Vector3f.ZERO;
	static Vector3f LEFT_FORWARD = Vector3f.UNIT_X.add(Vector3f.UNIT_Z);
	static Vector3f LEFT_BACKWARD = Vector3f.UNIT_X.add( Vector3f.UNIT_Z.negate() );
	static Vector3f RIGHT_FORWARD = Vector3f.UNIT_X.negate().add( Vector3f.UNIT_Z );
	static Vector3f RIGHT_BACKWARD =  Vector3f.UNIT_X.negate().add( Vector3f.UNIT_Z.negate() );
	
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
	
	public Direction setDirection( Vector3f direction ){
		System.out.println("Direzione : " + direction);
		this.direction = direction;
		return this;
	}
}
