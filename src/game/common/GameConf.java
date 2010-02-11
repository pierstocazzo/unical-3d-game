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

package game.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.jme.math.Vector2f;

/**
 * Class GameSettings used to read the xml configuration file
 * <br>
 * Needed library: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameConf {
	
	/** Base path of the configuration file */
	private final static String SETTINGS_FILE = "src/game/data/game.conf";
	
	/**
	 * Game settings keys
	 */
	public static String FORWARD_KEY = "forward_key";
	public static String BACKWARD_KEY = "backward_key";
	public static String TURNRIGHT_KEY = "turnright_key";
	public static String TURNLEFT_KEY = "turnleft_key";
	public static String RUN_KEY = "run_key";
	public static String PAUSE_KEY = "pause_key"; 
	
	// screen
	public static String RESOLUTION_WIDTH = "resolution_width";
	public static String RESOLUTION_HEIGHT = "resolution_height";
	public static String RESOLUTION_DEPTH = "resolution_depth";
	public static String RESOLUTION_FREQUENCY = "resolution_frequency";
	public static String IS_FULLSCREEN = "is_fullscreen";
	
	// Sound
	public static String SOUND_ENABLED = "sound_enabled";
	
	//XML path scene file
	public static String SCENE_FILE_PATH = "scene_file_path";
	
	/**
	 * Game parameter keys
	 */
	// General purpose parameters
	public static String MAX_ALERT_TIME = "maxAlertTime";
	public static String MAX_FIND_DISTANCE = "maxFindDistance";
	public static String MAX_NEIGHBORHOOD_RANGE = "maxNeighborhoodRange";
	public static String INITIAL_PLAYER_LIFE = "initialPlayerLife";
	public static String INITIAL_ENEMY_LIFE = "initialEnemyLife";
	public static String PLAYER_LIFE_INCREASE = "playerLifeIncrease";
	public static String ENEMY_LIFE_INCREASE = "enemyLifeIncrease";
	public static String PLAYER_LIFE_DECREASE = "playerLifeDecrease";
	public static String INITIAL_ENEMY_ACCURACY = "initialEnemyAccuracy";
	public static String ENEMY_ACCURACY_INCREASE = "enemyAccuracyIncrease";
	public static String INITIAL_ENERGY_PACK_VALUE = "initialEnergyPackValue";
	public static String INITIAL_AMMO_PACK_VALUE = "initialAmmoPackValue";
	public static String AMMO_PACK_INCREASE = "ammoPackIncrease";
	public static String ENERGY_PACK_INCREASE = "energyPackIncrease";
	public static String WALK_VELOCITY = "walkVelocity";
	public static String RUN_VELOCITY = "runVelocity";
	public static String SECOND_LEVEL_SCORE = "secondLevelScore";
	public static String SCORE_FACTOR = "scoreFactor";
	public static String SCORE_DECREASE_PERCENTAGE = "scoreDecreasePercentage";
	public static String ENERGY_PACK_NUMBER = "energyPackNumber";
	// States
	public static String STATE_DEFAULT_VIEW_RANGE = "stateDefaultViewRange";
	public static String STATE_DEFAULT_ACTION_RANGE = "stateDefaultActionRange";
	public static String STATE_ALERT_VIEW_RANGE = "stateAlertViewRange";
	public static String STATE_ALERT_ACTION_RANGE = "stateAlertActionRange";
	public static String STATE_ATTACK_VIEW_RANGE = "stateAttackViewRange";
	public static String STATE_ATTACK_ACTION_RANGE = "stateAttackActionRange";
	public static String STATE_GUARD_VIEW_RANGE = "stateGuardViewRange";
	public static String STATE_GUARD_ACTION_RANGE = "stateGuardActionRange";
	public static String STATE_SEARCH_VIEW_RANGE = "stateSearchViewRange";
	public static String STATE_SEARCH_ACTION_RANGE = "stateSearchActionRange";
	// Weapons
	// AR15
	public static String AR15_PROBABILITY = "ar15_probability";
	public static String INITIAL_AR15_CAPACITY = "initial_ar15_capacity";
	public static String AR15_AMMO_INCREASE = "ar15AmmoIncrease";
	public static String WEAPON_AR15_DAMAGE = "weapon_ar15_damage";
	public static String WEAPON_AR15_POWER = "weapon_ar15_power";
	public static String WEAPON_AR15_LOADTIME = "weapon_ar15_loadtime";
	// GATLING
	public static String GATLING_PROBABILITY = "gatling_probability";
	public static String INITIAL_GATLING_CAPACITY = "initial_gatling_capacity";
	public static String GATLING_AMMO_INCREASE = "gatlingAmmoIncrease";
	public static String WEAPON_GATLING_DAMAGE = "weapon_gatling_damage";
	public static String WEAPON_GATLING_POWER = "weapon_gatling_power";
	public static String WEAPON_GATLING_LOADTIME = "weapon_gatling_loadtime";
	// BAZOOKA
	public static String BAZOOKA_PROBABILITY = "bazooka_probability";
	public static String INITIAL_BAZOOKA_CAPACITY = "initial_bazooka_capacity";
	public static String BAZOOKA_AMMO_INCREASE = "bazookaAmmoIncrease";
	public static String WEAPON_BAZOOKA_DAMAGE = "weapon_bazooka_damage";
	public static String WEAPON_BAZOOKA_POWER = "weapon_bazooka_power";
	public static String WEAPON_BAZOOKA_LOADTIME = "weapon_bazooka_loadtime";
	
	/**
	 * Game phrases keys
	 */
	public static String LOADING_INIT = "loading_init";
	public static String LOADING_ENVIRONMENT = "loading_environment";
	public static String LOADING_PLAYERS = "loading_players";
	public static String LOADING_ENEMIES = "loading_enemies";
	public static String LOADING_INPUT = "loading_input";
	public static String HUD_NO_MESSAGE = "hud_no_message";
	public static String HUD_AMMO = "hud_ammo";
	public static String HUD_LIFE = "hud_life";
	public static String HUD_MAX_AMMO = "hud_max_ammo";
	public static String HUD_MAX_ENERGY = "hud_max_energy";
	public static String HUD_AMMO_FINISHED = "hud_ammo_finished";
	public static String HUD_LEVEL = "hud_level";
	public static String HUD_CURRENT_WEAPON = "hud_current_weapon";
	public static String HUD_NEW_LEVEL_1 = "hud_new_level_1";
	public static String HUD_NEW_LEVEL_2 = "hud_new_level_2";
	public static String HUD_NEW_LEVEL_3 = "hud_new_level_3";
	
	/** the xml document */
	static Document document;
	
	/** the root of the xml file */
	static Element root;
	
	/** Player's initial position */
	static Vector2f playerPosition;
	
	/**
	 * Default Values
	 */
	static final LinkedHashMap< String, String > defaultSettings = new LinkedHashMap<String, String>();
	static {
		defaultSettings.put( "forward_key", "W" );
		defaultSettings.put( "backward_key", "S" );
		defaultSettings.put( "turnright_key", "D" );
		defaultSettings.put( "turnleft_key", "A" );
		defaultSettings.put( "run_key", "LSHIFT" ); 
		defaultSettings.put( "pause_key", "P" ); 
		
		// screen
		defaultSettings.put( "resolution_width", "1024" );
		defaultSettings.put( "resolution_height", "768" );
		defaultSettings.put( "resolution_depth", "16" );
		defaultSettings.put( "resolution_frequency", "-1" );
		defaultSettings.put( "is_fullscreen", "false" );
		
		// Sound
		defaultSettings.put( "sound_enabled", "false" );
		
		//XML path scene file
		defaultSettings.put( "scene_file_path", "src/game/data/level/island.xml" );
	}
	
	/**
	 * User defined values
	 */
	
	// Main Settings
	static LinkedHashMap< String, String > settings;
	
	// Enemies information
	static List<EnemyInfo> enemies;
	
	// Global game parameters
	static LinkedHashMap< String, String > parameters;
	
	// Phrases used in the game
	static LinkedHashMap< String, String > phrases;
	
	/**
	 * Initialize game configuration regards the User Defined
	 * configuration files (see: SETTINGS_FILE ) 
	 */
	public static void init() {
		settings = new LinkedHashMap< String, String >();
		settings.putAll(defaultSettings);
		
		enemies = new LinkedList< EnemyInfo >();
		parameters = new LinkedHashMap< String, String >();
		phrases = new LinkedHashMap< String, String >();
		playerPosition = new Vector2f();
		
		// Load all settings
		load();
		
		/* 
		 * Change depth and frequency according to the operating system in use
		 */
		if( System.getProperties().getProperty("os.name").equals( "Linux" ) ) {
			settings.put( "resolution_depth", "24" );
			settings.put( "resolution_frequency", "50" );
		} else {
			settings.put( "resolution_depth", "32" );
			settings.put( "resolution_frequency", "60" );
		}
	}
	
	/**
	 * Load user defined values from XML file setting 
	 */
	@SuppressWarnings("unchecked")
	public static void load() {
		SAXBuilder builder = new SAXBuilder();
		
		/* Try to load user defined settings*/
		try {
			document = builder.build( new File( SETTINGS_FILE ) );
			root = document.getRootElement();
			
			for( Iterator<Element> it = root.getChild("settings").getChildren().iterator(); it.hasNext();  ) {
				Element element = it.next();
				settings.put( element.getAttributeValue("name"), element.getText() );
			}
			
			Element playerInfo = root.getChild("player");
			playerPosition.setX( Integer.valueOf( playerInfo.getAttributeValue("x") ) );
			playerPosition.setY( Integer.valueOf( playerInfo.getAttributeValue("z") ) );
			
			List<Element> enemy_list = root.getChild("enemies").getChildren();
			for( Element enemy : enemy_list ) {
				EnemyInfo enemyInfo = new EnemyInfo();
						
				enemyInfo.setPosX( Float.valueOf( enemy.getAttributeValue("x") ) );
				
				enemyInfo.setPosZ( Float.valueOf( enemy.getAttributeValue("z") ) );
				
				enemyInfo.setState( State.toState( enemy.getAttributeValue("state") ) );
				
				//load path's points
				PointPath pointPath = new PointPath();
				
				List<Element> point_list = enemy.getChildren();
				for( Element pointInfo : point_list ) {
					int x = Integer.valueOf( pointInfo.getAttributeValue("x") );
					int z = Integer.valueOf( pointInfo.getAttributeValue("z") );
					Point point = new Point( x, z );
					pointPath.add(point);
				}
				enemyInfo.setMovements( pointPath.generateMovementsList() );
				
				enemies.add( enemyInfo );
			}
			
			/**
			 * Load all parameters
			 */
			List<Element> parameter_list = root.getChild("parameters").getChildren();
			for( Element param : parameter_list ) {
				String key = param.getAttributeValue("name");
				String value = param.getAttributeValue("value");
				
				parameters.put( key, value );
			}
			
			/**
			 * Load all phrases
			 */
			List<Element> phrase_list = root.getChild("phrases").getChildren();
			for( Element phrase : phrase_list ) {
				String key = phrase.getAttributeValue("name");
				String value = phrase.getAttributeValue("value");
				
				phrases.put( key, value );
			}
			
		} catch (Exception e) {
			Logger.getAnonymousLogger().log( Level.SEVERE, 
					"Error loading the configuration file, make sure the path is correct." +
					"\nDefault configuration will be loaded" );
		}
	}
	
	/**
	 * Store the user defined settings into the XML file
	 */
	@SuppressWarnings("unchecked")
	public static void save() {
		List<Element> settingsList = root.getChild("settings").getChildren();
		
		for( Element setting : settingsList ) {
			setting.setText( settings.get( setting.getAttributeValue("name") ) );
		}
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat( Format.getPrettyFormat() );
		
		try {
			outputter.output( document, new FileOutputStream( SETTINGS_FILE ) );
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("Error saving game configuration");
		} 
	}
	
	public static String getDefaultSetting( String setting ) {
		return defaultSettings.get( setting );
	} 
	
	public static int getIntDefaultSetting( String setting ) {
		return Integer.valueOf( defaultSettings.get( setting ) );
	}
	
	public static String getSetting( String setting ) {
		return settings.get( setting );
	} 
	
	public static int getIntSetting( String setting ) {
		return Integer.valueOf( settings.get( setting ) );
	}
	
	public static String setSetting( String setting, String newValue ) {
		return settings.put( setting, newValue );
	}
	
	public static List<EnemyInfo> getEmemiesInfoList() {
		return enemies;
	}
	
	public static String getParameter( String param ) {
		try {
			return parameters.get( param );
		} catch (Exception e) {
			return "noparam";
		}
	} 
	
	public static int getIntParameter( String param ) {
		try {
			return Integer.valueOf( parameters.get( param ) );
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static String getPhrase( String phrase ) {
		try {
			return phrases.get( phrase );
		} catch (Exception e) {
			return "frase not found";
		}
	}
	
	public static Vector2f getPlayerPosition() {
		try {
			return playerPosition;
		} catch (Exception e) {
			return new Vector2f();
		}
	}
	
	/** Utility class used to store some informations 
	 * for the creation of enemies
	 */
	public static class EnemyInfo {
		float posX;
		float posZ;
		State state;
		LinkedList<Movement> movements;
		
		public void setPosX(float posX) {
			this.posX = posX;
		}
		public float getPosX() {
			return posX;
		}
		
		public void setPosZ(float posZ) {
			this.posZ = posZ;
		}
		
		public float getPosZ() {
			return posZ;
		}
		
		public void setState(State state) {
			this.state = state;
		}
		
		public State getState() {
			return state;
		}
		
		public void setMovements(LinkedList<Movement> movements) {
			this.movements = movements;
		}
		
		public LinkedList<Movement> getMovements() {
			return movements;
		}
	}
}