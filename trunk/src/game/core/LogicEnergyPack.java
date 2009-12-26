package game.core;

import java.io.Serializable;

/** class <code>LogicEnergyPack</code> <br>
 * Represents an energy pack the player can catch and use when
 * his energy (life) is few
 */
@SuppressWarnings("serial")
public class LogicEnergyPack implements Serializable {
	
	String id;
	
	/** the energy the pack can give to the player */
	int value;
	
	/** <code>LogicEnergyPack</code> constructor<br>
	 * Create a new energy pack
	 * @param value - (int) the energy the pack can give to the player
	 */
	public LogicEnergyPack( String id, int value ) {
		this.value = value;
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
