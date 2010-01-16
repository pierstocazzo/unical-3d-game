package game.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class Logic Controller (Score and Levels)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class ScoreManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** Pointer to logic world */
	LogicWorld world;
	
	/** Container of score players */
	HashMap<String, Score> players;
	
	/**
	 * Constructor
	 * 
	 * @param (LogicWorld) world
	 */
	public ScoreManager( LogicWorld world ) {
		this.world = world;
		players = new HashMap<String, Score>();
	}

	/**
	 * Initialize score manager
	 */
	public void initScoreManager() {
		for( String id : world.getPlayersIds() )
			players.put( id, new Score() );
	}

	/**
	 * it decreases score and level after the player was killed
	 * 
	 * @param (String) playerId
	 */
	void playerKilled( String playerId ) {
		players.get(playerId).decreaseScore();
		update();
	}
	
	/**
	 * it increase score and level after the player killed an enemy
	 * 
	 * @param (String) playerId
	 */
	void enemyKilled( String playerId ) {
		try {
			players.get(playerId).increaseScore();
			update();
		} catch ( Exception e ) {}
	}
	
	/**
	 * Update score and level for each player
	 */
	void update() {
		for( String id : players.keySet() )
			switch( players.get(id).level ) {
			case 1: 
				changeParameters( id, 100, 15, 100, 16 );
				break;
			case 2: 
				changeParameters( id, 125, 20, 125, 12 );
				break;
			case 3: 
				changeParameters( id, 150, 25, 150, 8 );
				break;
			case 4: 
				changeParameters( id, 175, 30, 175, 4 );
				break;
			case 5: 
				changeParameters( id, 200, 35, 200, 0 );
				break;
			}
	}
	
	/**
	 * Useful function that change game parameter
	 * 
	 * @param (String) playerId
	 * @param (String) playerLife
	 * @param (int) enemyLife - max enemies life
	 * @param (int) maxAmmo - max ammo
	 * @param (int) enemyErrorAngle - error angle of enemy shooting
	 */
	private void changeParameters( String playerId, int playerLife, int enemyLife, int maxAmmo, int enemyErrorAngle ) {
		((LogicPlayer) world.characters.get(playerId)).maxAmmo = maxAmmo;
		world.characters.get(playerId).maxLife = playerLife;
		
		for( int i = 1; i <= world.enemyCounter; i++ ) {
			String enemyID = "enemy"+i;
			if( world.characters.get(enemyID) != null ) {
				world.characters.get(enemyID).currentLife = enemyLife;
				world.characters.get(enemyID).maxLife = enemyLife;
				((LogicEnemy) world.characters.get(enemyID)).errorAngle = enemyErrorAngle;
			}
		}
	}

	/**
	 * Reset score of every player
	 */
	void reset() {
		for( String id : players.keySet() ) {
			players.get(id).reset();
		}
	}
	
	/**
	 * Return score of indicated player
	 * 
	 * @param (String) id
	 * @return (int)
	 */
	public int getScore( String id ) {
		return players.get(id).score;
	}
	
	/**
	 * Return level of indicated player
	 * 
	 * @param (String) id
	 * @return (int)
	 */
	public int getLevel( String id ) {
		return players.get(id).level;
	}
	
	/**
	 * Class Score
	 * 
	 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
	 */
	public class Score implements Serializable {
		
		/** Identifier */
		private static final long serialVersionUID = 1L;
		
		/** score value */
		int score;
		
		/** level value */
		int level;
		
		/** useful variable */
		boolean showLevel2 = false;
		
		/**
		 * Constructor
		 */
		Score() {
			score = 0;
			level = 1;
		}
		
		/**
		 * Reset player value
		 */
		public void reset() {
			level = 1;
			score = 0;
		}
		
		/**
		 * Increase score after an enemy killing
		 */
		public void increaseScore() {
			
			switch ( level ) {
				case 1:
					score = score + 5;
					if( score >= 25 ){
						level = 2;
						showLevel2 = true;
					}
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
		
		/**
		 * Decrease player score after death
		 */
		public void decreaseScore() {
			score = score - (int)(score*0.8f);
			score = score - score % 5;
		}
	}

	/**
	 * Return true if a message was displayed, false otherwise
	 * 
	 * @param (String) id - player id
	 * @return (boolean)
	 */
	public boolean getShowLevel2(String id) {
		return players.get(id).showLevel2;
	}
}