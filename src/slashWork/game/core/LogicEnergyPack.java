package slashWork.game.core;

import java.io.Serializable;

import com.jme.math.Vector3f;

/** class <code>LogicEnergyPack</code> <br>
 * Represents an energy pack the player can catch and use when
 * his energy (life) is few
 */
@SuppressWarnings("serial")
public class LogicEnergyPack implements Serializable {
	
	String id;
	
	/** the position of the energy pack */
	Vector3f position;
	
	/** the energy the pack can give to the player */
	int value;
	
	/** <code>LogicEnergyPack</code> constructor<br>
	 * Create a new energy pack
	 * @param position - (Vector3f) the position of the energy pack
	 * @param value - (int) the energy the pack can give to the player
	 */
	public LogicEnergyPack( String id, Vector3f position, int value ) {
		this.position = position;
		this.value = value;
		this.id = id;
	}

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
