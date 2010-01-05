package game.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Movement implements Serializable {
	
	Direction direction;
	float length;
	
	public Movement( Direction direction, float length ){
		this.direction = direction;
		this.length = length;
	}
	
	public Direction getDirection() {
		return direction;
	}

	public float getLength() {
		return length;
	}
}