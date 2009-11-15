package game.enemyAI;

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

	public float getLength() {
		return length;
	}
}