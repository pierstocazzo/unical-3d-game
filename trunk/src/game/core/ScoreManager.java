package game.core;

import game.common.GameConfiguration;
import game.common.WeaponType;

import java.io.Serializable;

/**
 * Class Logic Controller (Score and Levels)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class ScoreManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** Pointer to logic world */
	LogicWorld world;
	
	/**
	 * Constructor
	 * 
	 * @param (LogicWorld) world
	 */
	public ScoreManager( LogicWorld world ) {
		this.world = world;
	}

	/**
	 * it decreases score and level after the player was killed
	 * 
	 * @param (String) playerId
	 */
	void playerKilled( String playerId ) {
		((LogicPlayer)world.characters.get(playerId)).score.decreaseScore();
		update();
	}
	
	/**
	 * it increase score and level after the player killed an enemy
	 * 
	 * @param (String) playerId
	 */
	void enemyKilled( String playerId ) {
		try {
			((LogicPlayer)world.characters.get(playerId)).score.increaseScore();
			update();
		} catch ( Exception e ) {}
	}
	
	/**
	 * Update level for each player
	 */
	void update() {
		for( String id : world.getPlayersIds() ) {
			
			int level = ((LogicPlayer)world.characters.get(id)).score.level;
			int previousLevel = ((LogicPlayer)world.characters.get(id)).score.previousLevel;
			
			int initialPlayerLife = Integer.valueOf( GameConfiguration.getParameter("initialPlayerLife") );
			int initialEnemyLife = Integer.valueOf( GameConfiguration.getParameter("initialEnemyLife") );
			int playerLifeIncrease = Integer.valueOf( GameConfiguration.getParameter("playerLifeIncrease") );
			int enemyLifeIncrease = Integer.valueOf( GameConfiguration.getParameter("enemyLifeIncrease") );
			int initialEnemyAccuracy = Integer.valueOf( GameConfiguration.getParameter("initialEnemyAccuracy") );
			int enemyAccuracyIncrease = Integer.valueOf( GameConfiguration.getParameter("enemyAccuracyIncrease") );
			int initialAmmoPackValue = Integer.valueOf( GameConfiguration.getParameter("initialAmmoPackValue") );
			int ammoPackIncrease = Integer.valueOf( GameConfiguration.getParameter("ammoPackIncrease") );
			int initialEnergyPackValue = Integer.valueOf( GameConfiguration.getParameter("initialEnergyPackValue") );
			int energyPackIncrease = Integer.valueOf( GameConfiguration.getParameter("energyPackIncrease") );
			
			
			if ( level != previousLevel ) {
				changeParameters(
						id,
						initialPlayerLife + playerLifeIncrease * previousLevel,
						initialEnemyLife + enemyLifeIncrease * previousLevel,
						initialEnemyAccuracy - enemyAccuracyIncrease * previousLevel,
						initialEnergyPackValue + energyPackIncrease * previousLevel,
						initialAmmoPackValue + ammoPackIncrease * previousLevel
				);
				
				((LogicPlayer)world.characters.get(id)).score.previousLevel = level;
			}
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
	 * @param (int) energyPackValue
	 * @param (int) ammoPackValue
	 */
	private void changeParameters( String playerId, int playerLife, int enemyLife, 
			int enemyErrorAngle, int energyPackValue, int ammoPackValue ) {
		world.characters.get(playerId).maxLife = playerLife;
		
		int previousLevel = ((LogicPlayer)world.characters.get(playerId)).score.previousLevel;
		
		for( String id : world.getEnemiesIds() ) {
			world.characters.get(id).currentLife = enemyLife;
			world.characters.get(id).maxLife = enemyLife;
			
			if( enemyErrorAngle >= 0 ) {
				((LogicEnemy) world.characters.get(id)).errorAngle = enemyErrorAngle;
			}
		}
		
		world.ammoPackValue = ammoPackValue;
		for( String id : world.ammoPackages.keySet() ) {
			if( world.ammoPackages.get(id).type == WeaponType.BAZOOKA ) {
				world.ammoPackages.get(id).value = (int) (ammoPackValue * 0.1);
			} else {
				world.ammoPackages.get(id).value = ammoPackValue;
			}
		}
		
		for( String id : world.energyPackages.keySet() ) {
			world.energyPackages.get(id).value = energyPackValue;
		}
		
		for ( LogicWeapon weapon : ((LogicPlayer)world.characters.get(playerId)).equipment.values() ) {
			switch( weapon.type ) {
			case AR15: 
				weapon.magazineCapacity = Integer.valueOf( GameConfiguration.getParameter("initial_ar15_capacity") )
				+ Integer.valueOf(GameConfiguration.getParameter("ar15AmmoIncrease")) * previousLevel;
				break;
				
			case GATLING: 
				weapon.magazineCapacity = Integer.valueOf( GameConfiguration.getParameter("initial_gatling_capacity") )
				+ Integer.valueOf(GameConfiguration.getParameter("gatlingAmmoIncrease")) * previousLevel;
				break;
				
			case BAZOOKA: 
				weapon.magazineCapacity = Integer.valueOf( GameConfiguration.getParameter("initial_bazooka_capacity") )
				+ Integer.valueOf(GameConfiguration.getParameter("bazookaAmmoIncrease")) * previousLevel;
				break;
			}
		}
	}

	/**
	 * Reset score of every player
	 */
	void reset() {
		for( String id : world.getPlayersIds() ) {
			((LogicPlayer)world.characters.get(id)).score.reset();
		}
	}
	
	/**
	 * Return score of indicated player
	 * 
	 * @param (String) id
	 * @return (int)
	 */
	public int getScore( String id ) {
		return ((LogicPlayer)world.characters.get(id)).score.score;
	}
	
	/**
	 * Return level of indicated player
	 * 
	 * @param (String) id
	 * @return (int)
	 */
	public int getLevel( String id ) {
		return ((LogicPlayer)world.characters.get(id)).score.level;
	}
	
	/**
	 * Class Score
	 * 
	 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
	 */
	public static class Score implements Serializable {
		
		/** Identifier */
		private static final long serialVersionUID = 1L;
		
		/** score value */
		int score;
		
		/** level values */
		int previousLevel;
		int level;
		
		/** useful variable */
		boolean showLevel2 = false;
		
		/**
		 * Constructor
		 */
		Score() {
			score = 0;
			previousLevel = 1;
			level = 1;
		}
		
		/**
		 * Reset player value
		 */
		public void reset() {
			level = 1;
			previousLevel = 1;
			score = 0;
		}
		
		private int calculateNextScore( int level ) {
			if ( level == 1 ) {
				return 25;
			}
			
			return (int) (Math.pow( 5*level, 2 ) + calculateNextScore( level - 1 ));
		}
		
		/**
		 * Increase score after an enemy killing
		 */
		public void increaseScore() {
			score = score + 5*level;
			if ( score >= calculateNextScore(level) ) {
				previousLevel = level;
				level = level + 1;
				
				if ( level == 2 ) {
					showLevel2 = true;
				}
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
		try {
			return ((LogicPlayer)world.characters.get(id)).score.showLevel2;
		} catch (Exception e) {
			return false;
		}
	}
}