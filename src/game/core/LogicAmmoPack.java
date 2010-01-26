package game.core;

import game.common.WeaponType;

import java.io.Serializable;

import com.jme.math.Vector3f;

/** Class <code>LogicAmmo</code> <br>
 * Represent pack of ammunitions the player can catch and use
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class LogicAmmoPack implements Serializable {

	/** type of the weapon which can use this ammo */
	private WeaponType type;
	
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
	public LogicAmmoPack( WeaponType type, int quantity, Vector3f position ){
		this.type = type;
		this.quantity = quantity;
		this.position = position;
	}
	
	/**
	 * Return ammo quantity
	 * 
	 * @return (int)
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Return the position of current ammo pack
	 * 
	 * @return {@link Vector3f}
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Return weapon type of current ammo
	 * 
	 * @return {@link WeaponType}
	 */
	public WeaponType getType() {
		return type;
	}
}