package game.enemyAI;

import com.jme.math.Vector3f;

public class Movement {
	
	Direction direction;
	float length;
	
	Movement( Direction direction, float length ){
		this.direction = direction;
		this.length = length;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public Vector3f getRotationalAxis() {
		return direction.toVector().cross( Vector3f.UNIT_Y.negate() );
	}

	public float getLength() {
		return length;
	}
}