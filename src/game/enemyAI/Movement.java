package game.enemyAI;

public class Movement {
	
	EnumDirection direction;
	float length;
	
	Movement( EnumDirection direction, float length ){
		this.direction = direction;
		this.length = length;
	}
	
	public EnumDirection getDirection() {
		return direction;
	}

	public float getLength() {
		return length;
	}
}