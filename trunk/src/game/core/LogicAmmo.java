package game.core;

import com.jme.math.Vector3f;

/** Class <code>LogicAmmo</code> <br>
 * Represent pack of ammunitions the player can catch and use
 */
public class LogicAmmo {
	/** type of the weapon whitch can use this ammo */
	private EnumWeaponType type;
	
	/** quantity of ammunition */
	private int quantity;
	
	/** the position of the ammo pack */
	private Vector3f position;
	
	/** <code>LogicAmmo</code> constructor <br>
	 * Create a new ammo pack
	 * 
	 * @param type - (EnumWeaponType) type of the weapon whitch can use this ammo
	 * @param quantity - (int) quantity of ammunition the pack contains
	 * @param position - (Vector3f) position of the ammo pack
	 */
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
