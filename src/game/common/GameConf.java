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

/** Class GameSettings used to read the xml configuration file<br>
 * 
 * Needed library: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameConf {
	
	/** Base path of the configuration file */
	private final static String SETTINGS_FILE = "src/game/data/game.conf";
	
	/**
	 * Default game settings
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
		return parameters.get( param );
	}
	
	public static int getIntParameter( String param ) {
		return Integer.valueOf( parameters.get( param ) );
	}
	
	public static String getPhrase( String phrase ) {
		return phrases.get( phrase );
	}
	
	public static Vector2f getPlayerPosition() {
		return playerPosition;
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