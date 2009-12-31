package lowDetailGame.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class Logic Controller (Score and Levels)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class ScoreManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	LogicWorld world;
	HashMap<String, Score> players;
	
	float previousTime;
	
	public ScoreManager( LogicWorld world ) {
		this.world = world;
		this.previousTime = 0;
		players = new HashMap<String, Score>();
	}

	public void initScoreManager() {
		for( String id : world.getPlayersIds() ) {
			players.put( id, new Score() );
		}
	}

	/**
	 * it decreases score and level after the player was killed
	 */
	void playerKilled( String playerId ) {
		players.get(playerId).decreaseScore();
		update();
	}
	
	/**
	 * it increase score and level after the player killed an enemy
	 */
	void enemyKilled( String playerId ) {
		try {
			players.get(playerId).increaseScore();
			update();
		} catch ( Exception e ) {
			// just do nothing when the bullet who kill an enemy came from an other enemy
		}
	}
	
	void update() {
		for( String id : players.keySet() )
			switch( players.get(id).level ) {
			case 1: 
				((LogicPlayer) world.characters.get(id)).maxAmmo = 100;
				break;
			case 2: 
				((LogicPlayer) world.characters.get(id)).maxAmmo = 120;
				break;
			case 3: 
				((LogicPlayer) world.characters.get(id)).maxAmmo = 150;
				break;
			case 4: 
				((LogicPlayer) world.characters.get(id)).maxAmmo = 180;
				break;
			case 5: 
				((LogicPlayer) world.characters.get(id)).maxAmmo = 200;
				break;
			}
	}
	
	void reset() {
		for( String id : players.keySet() ) {
			players.get(id).reset();
		}
	}
	
	public int getScore( String id ) {
		return players.get(id).score;
	}
	
	public int getLevel( String id ) {
		return players.get(id).level;
	}
	
	
	
	public class Score implements Serializable {
		private static final long serialVersionUID = 1L;
		
		int score;
		int level;
		
		Score() {
			score = 0;
			level = 1;
		}
		
		public void reset() {
			level = 1;
			score = 0;
		}
		
		public void increaseScore() {
			switch ( level ) {
			case 1:
				score = score + 5;
				if( score >= 25 ) 
					level = 2;
				break;
			case 2: 
				score = score + 10;
				if( score >= 125 )
					level = 3;
				break;
			case 3:
				score = score + 15;
				if( score >= 350 )
					level = 4;
				break;
			case 4:
				score = score + 20;
				if( score >= 750 )
					level = 5;
				break;
			case 5: 
				score = score + 25;
				break;
			}
		}
		
		public void decreaseScore() {
			score = score -  (int)(score*0.8f);
		}
	}
}