package game.logic;

import com.jme.math.Vector3f;

public class LogicAmmo extends LogicEnergyPack {
	
	private WeaponType type;
	private int quantity;
	private Vector3f position;
	
	public LogicAmmo( WeaponType type, int quantity, Vector3f position ){
		this.type = type;
		this.quantity = quantity;
		this.position = position;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public Vector3f getPosition() {
		return position;
	}

	public WeaponType getType() {
		return type;
	}

}
