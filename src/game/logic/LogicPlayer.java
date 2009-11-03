package game.logic;

import java.util.HashMap;

/**
 * Class LogicPlayer
 * 
 * @author Andrea
 */
public class LogicPlayer extends LogicCharacter {

	HashMap< String, LogicWeapon > equipment;
	
	/**
	 * Constructor Object LogicPlayer
	 * 
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 * @param maxWeapons - (int) Max Weapons
	 */
	public LogicPlayer( String id, int currentLife, int maxLife ) {
		super( id, currentLife, maxLife );
	}
}
