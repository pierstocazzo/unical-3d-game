package proposta.enemyAI;

import com.jme.math.Vector3f;

public enum Direction {
	
	FORWARD ( Vector3f.UNIT_Z ),
	
	BACKWARD ( Vector3f.UNIT_Z.negate() ),
	
	RIGHT ( Vector3f.UNIT_X.negate() ),
	
	LEFT ( Vector3f.UNIT_X ), 
	
	REST (Vector3f.UNIT_X );
	
	Vector3f direction;
	
	Direction( Vector3f direction ){
		this.direction = direction;
	}
	
	public Vector3f toVector() {
		return direction;
	}
}
