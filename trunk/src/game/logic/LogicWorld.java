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
		
		characters = new HashMap<String, LogicCharacter>();
		
		/** create player */
		player = new LogicPlayer( "player", 100, 100 );
		characters.put( player.id, player );
		
		/** create enemies */
		for( int i = 0; i < 20; i++ ) {
			LogicEnemy enemy = new LogicEnemy( "enemy" + i, 50, 50, WeaponType.MP5 );
			characters.put( enemy.id, enemy );
		}
		
		// TODO items init
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
		String s = "World Situation: ";
		
		Set<String> keySet = characters.keySet();
		
		for( String key : keySet ){
			s = s + "\n" + characters.get(key).id + " in position: " + characters.get(key).position;
		}
		
		return s;
	}

}
