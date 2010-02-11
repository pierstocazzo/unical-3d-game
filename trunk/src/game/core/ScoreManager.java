/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.core;

import game.common.GameConf;
import game.common.WeaponType;

import java.io.Serializable;

/**
 * Class Logic Controller (Score and Levels)
 * <br>
 * It manage the score level
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
			
			int initialPlayerLife = GameConf.getIntParameter( GameConf.INITIAL_PLAYER_LIFE );
			int initialEnemyLife = GameConf.getIntParameter( GameConf.INITIAL_ENEMY_LIFE );
			int playerLifeIncrease = GameConf.getIntParameter( GameConf.PLAYER_LIFE_INCREASE );
			int enemyLifeIncrease = GameConf.getIntParameter( GameConf.ENEMY_LIFE_INCREASE );
			int initialEnemyAccuracy = GameConf.getIntParameter( GameConf.INITIAL_ENEMY_ACCURACY );
			int enemyAccuracyIncrease = GameConf.getIntParameter( GameConf.ENEMY_ACCURACY_INCREASE );
			int initialAmmoPackValue = GameConf.getIntParameter( GameConf.INITIAL_AMMO_PACK_VALUE );
			int ammoPackIncrease = GameConf.getIntParameter( GameConf.AMMO_PACK_INCREASE );
			int initialEnergyPackValue = GameConf.getIntParameter( GameConf.INITIAL_ENERGY_PACK_VALUE );
			int energyPackIncrease = GameConf.getIntParameter( GameConf.ENERGY_PACK_INCREASE );
			
			
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
		world.characters.get(playerId).currentLife = playerLife;
		
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
			if( world.ammoPackages.get(id).type == WeaponType.RPG ) {
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
				weapon.magazineCapacity = Integer.valueOf( GameConf.getParameter( GameConf.INITIAL_AR15_CAPACITY ) )
				+ GameConf.getIntParameter( GameConf.AR15_AMMO_INCREASE ) * previousLevel;
				break;
				
			case GATLING: 
				weapon.magazineCapacity = Integer.valueOf( GameConf.getParameter( GameConf.INITIAL_GATLING_CAPACITY ) )
				+ GameConf.getIntParameter( GameConf.GATLING_AMMO_INCREASE ) * previousLevel;
				break;
				
			case RPG: 
				weapon.magazineCapacity = Integer.valueOf( GameConf.getParameter( GameConf.INITIAL_BAZOOKA_CAPACITY ) )
				+ GameConf.getIntParameter( GameConf.BAZOOKA_AMMO_INCREASE ) * previousLevel;
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
		
		int scoreFactor = Integer.valueOf( GameConf.getParameter( GameConf.SCORE_FACTOR ) );
		int scoreForSecondLevel = Integer.valueOf( GameConf.getParameter( GameConf.SECOND_LEVEL_SCORE ) );
		float scoreDecreasePercentage = Float.valueOf( GameConf.getParameter( GameConf.SCORE_DECREASE_PERCENTAGE ) );
		
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
		
		/**
		 * 
		 * @param level
		 * @return the score needed to switch to next level
		 */
		private int scoreForNextLevel( int level ) {
			/* base: return the value needed to switch to the second level */
			if ( level == 1 ) {
				return scoreForSecondLevel;
			}
			
			/* recursively call this function to calculate the score needed
			 * the dynamic function is: 
			 * 
			 * 		nextLevelScore = previousLevelScore + (scorePowerFactor * currentLevel)^2
			 * 
			 * es. score needed to switch to third level: 
			 * 
			 * 		1 - scoreForNextLevel( 2 ) = (5*2)^2 + scoreForNextLevel( 1 )
			 * 
			 * 		2 - scoreForNextLevel( 1 ) = 25
			 * 
			 * 		3 - scoreForNextLevel( 2 ) = (5*2)^2 + 25 = 125
			 * 		
			 */
			
			return (int) (Math.pow( scoreFactor*level, 2 ) + scoreForNextLevel( level - 1 ));
		}
		
		/**
		 * Increase score after an enemy killing
		 */
		public void increaseScore() {
			int factor = Integer.valueOf( GameConf.getParameter( GameConf.SCORE_FACTOR ) );
			
			score = score + factor*level;
			
			/* switch to next level if the score reached the needed value */
			if ( score >= scoreForNextLevel(level) ) {
				previousLevel = level;
				level = level + 1;
				
				/* just to display a tutorial message at the switch to level 2 */
				if ( level == 2 ) {
					showLevel2 = true;
				}
			}
		}
		
		/**
		 * Decrease player score after death
		 */
		public void decreaseScore() {
			/* decrease score */
			score = score - (int)(score*scoreDecreasePercentage);
			/* modulate score to be a multiple of scoreFactor */
			score = score - score % scoreFactor;
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