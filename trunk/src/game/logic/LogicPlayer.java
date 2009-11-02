package game.logic;

/**
 * Class LogicPlayer
 * 
 * @author Andrea
 */
public class LogicPlayer extends LogicCharacter {

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
