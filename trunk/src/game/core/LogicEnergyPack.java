package game.core;

import com.jme.math.Vector3f;

public class LogicEnergyPack {
	
	private Vector3f position;
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	
}
