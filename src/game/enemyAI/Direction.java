package game.enemyAI;

import com.jme.math.Vector3f;

public enum Direction {
	
	FORWARD ( Vector3f.UNIT_Z ),
	
	BACKWARD ( Vector3f.UNIT_Z.negate() ),
	
	RIGHT ( Vector3f.UNIT_X.negate() ),
	
	LEFT ( Vector3f.UNIT_X ), 
	
	REST (Vector3f.ZERO );
	
	Vector3f direction;
	Vector3f rotationalAxis;
	
	Direction( Vector3f direction ){
		this.direction = direction;
		this.rotationalAxis = direction.cross( Vector3f.UNIT_Y.negate() );
	}
	
	public Vector3f getRotationalAxis(){
		return rotationalAxis;
	}
	
	public Vector3f getDirectionVector() {
		return direction;
	}
}
