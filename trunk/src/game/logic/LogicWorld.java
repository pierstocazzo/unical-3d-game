package game.logic;

import game.graphics.WorldInterface;

import java.util.HashMap;
import java.util.Set;

import com.jme.math.Vector3f;

public class LogicWorld implements WorldInterface {
	
	/** 
	 *  The player
	 */
	LogicCharacter player;
	
	/**
	 *  Hashmap of the characters
	 */
	HashMap< String, LogicCharacter > characters;
		
	
	/** 
	 *  <code>LogicWorld</code> Constructor<br>
	 *  Initialize the game logic
	 */
	public LogicWorld() {
		characters = new HashMap< String, LogicCharacter >();
	}
	
	/** create player */
	public void createPlayer( String id, int life, int maxLife ) {
		player = new LogicPlayer( id, life, maxLife );
	}
	
	/**
	 * Function that create enemies in the number specified
	 * @param numberOfEnemies - (int) number of enemies to create
	 */
	public void createEnemies( int numberOfEnemies ) {
		for( int i = 0; i < numberOfEnemies; i++ ) {
			LogicEnemy enemy = new LogicEnemy( "enemy" + i, 50, 50, WeaponType.MP5 );
			characters.put( enemy.id, enemy );
		}
	}
	
	@Override 
	/** 
	 *  override interface method move
	 */
	public void move( String id, Vector3f position ) {
		characters.get( id ).position = position;
	}
	
	/** 
	 * Print the position of all characters
	 * @return String to print
	 */
	public String printWorld() {
		String s = "World status: ";
		
		Set<String> keySet = characters.keySet();
		
		s = s + "\n Player position: " + player.position;
		
		for( String key : keySet ){
			s = s + "\n" + characters.get(key).id + " in position: " + characters.get(key).position;
		}
		
		return s;
	}

	@Override
	public Set<String> getCharactersId() {
		return characters.keySet();
	}
	
	@Override
	public String getPlayerId() {
		return player.id;
	}
}
