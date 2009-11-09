package game.logic;

import game.graphics.WorldInterface;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import com.jme.math.Vector3f;

public class LogicWorld implements WorldInterface {
	
	/** 
	 *  The player
	 */
	LogicPlayer player;
	
	/**
	 *  Hashmap of the characters
	 */
	HashMap< String, LogicEnemy > enemies;
		
	
	/** 
	 *  <code>LogicWorld</code> Constructor<br>
	 *  Initialize the game logic
	 */
	public LogicWorld() {
		enemies = new HashMap< String, LogicEnemy >();
	}
	
	/** create player */
	public void createPlayer( String id, int life, int maxLife, Vector3f position ) {
		player = new LogicPlayer( id, life, maxLife, position );
	}
	
	/**
	 * Function that create enemies in the number specified
	 * @param numberOfEnemies - (int) number of enemies to create
	 */
	public void createEnemies( int numberOfEnemies, Vector3f area ) {
		float x = area.getX();
		float z = area.getY();

		Random r = new Random();

		
		for( int i = 0; i < numberOfEnemies; i++ ) {
			
			float xRelative = x + r.nextInt(numberOfEnemies * 5);
			float zRelative = z + r.nextInt(numberOfEnemies * 5);
			
			Vector3f position = new Vector3f( xRelative, 30, zRelative );
			
			LogicEnemy enemy = new LogicEnemy( "enemy" + i, 50, 50, WeaponType.MP5, position );
			enemies.put( enemy.id, enemy );
		}
	}
	
	@Override 
	/** 
	 *  override interface method move
	 */
	public void move( String id, Vector3f position ) {
		enemies.get( id ).position = position;
	}
	
	/** 
	 * Print the position of all characters
	 * @return String to print
	 */
	public String printWorld() {
		String s = "World status: ";
		
		Set<String> keySet = enemies.keySet();
		
		s = s + "\n Player position: " + player.position;
		
		for( String key : keySet ){
			s = s + "\n" + enemies.get(key).id + " in position: " + enemies.get(key).position;
		}
		
		return s;
	}

	@Override
	public Set<String> getEnemiesId() {
		return enemies.keySet();
	}
	
	@Override
	public String getPlayerId() {
		return player.id;
	}

	@Override
	public HashMap<String, LogicEnemy> getEnemies() {
		return enemies;
	}
}
