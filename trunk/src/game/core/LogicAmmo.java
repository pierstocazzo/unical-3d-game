package game.core;

import com.jme.math.Vector3f;

public class LogicAmmo extends LogicEnergyPack {
	
	private EnumWeaponType type;
	private int quantity;
	private Vector3f position;
	
	public LogicAmmo( EnumWeaponType type, int quantity, Vector3f position ){
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

	public EnumWeaponType getType() {
		return type;
	}

}
