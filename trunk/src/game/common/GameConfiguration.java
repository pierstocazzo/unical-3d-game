package game.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/** Class GameSettings used to read the xml configuration file<br>
 * 
 * Needed library: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author Giuseppe Leone 
 */
public class GameConfiguration {
	
	public static String SETTINGS_FILE = "src/game/data/settings.xml";
	
	/** the xml document */
	static Document document;
	
	/** the root of the xml file */
	static Element root;
	
	/**
	 * Default Values
	 */
	static LinkedHashMap< String, String > defaultValues = new LinkedHashMap<String, String>();
	static {
		defaultValues.put( "forward_key", "W" );
		defaultValues.put( "backward_key", "S" );
		defaultValues.put( "turnright_key", "D" );
		defaultValues.put( "turnleft_key", "A" );
		defaultValues.put( "run_key", "LSHIFT" ); 
		defaultValues.put( "pause_key", "P" ); 
		
		// screen
		defaultValues.put( "resolution_width", "1024" );
		defaultValues.put( "resolution_height", "768" );
		defaultValues.put( "resolution_depth", "16" );
		defaultValues.put( "resolution_frequency", "-1" );
		defaultValues.put( "is_fullscreen", "false" );
		
		// Sound
		defaultValues.put( "sound_enabled", "false" );
		
		//XML path scene file
		defaultValues.put( "scene_file_path", "src/game/data/level/island.xml" );
	}
	
	/**
	 * User defined values
	 */
	static LinkedHashMap< String, String > values;
	
	public static void init() {
		values = new LinkedHashMap< String, String >();
		values.putAll(defaultValues);

		load();
		
		/* 
		 * Change depth and frequency according to the operating system in use
		 */
		if( System.getProperties().getProperty("os.name").equals( "Linux" ) ) {
			values.put( "resolution_depth", "24" );
			values.put( "resolution_frequency", "50" );
		} else {
			values.put( "resolution_depth", "32" );
			values.put( "resolution_frequency", "60" );
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
				Attribute name = element.getAttribute("name");

				values.put( name.getValue(), element.getText() );
			}
		} catch (Exception e) {
			Logger.getAnonymousLogger().log( Level.SEVERE, 
					"Error loading the settings file, make sure the path is correct." +
					"\nDefault settings will be loaded" );
		}
	}
	
	/**
	 * Store the user defined settings into a XML file
	 */
	public static void save() {
		try {
			root = new Element("game_configuration");
			document = new Document(root);
			
			root.addContent( new Element("settings") );
			root.addContent( new Element("parameters") );
			
			for( String key : values.keySet() ) {
				Element setting = new Element( "setting" );
				setting.setAttribute("name", key);
				setting.setText( values.get(key) );
				root.getChild("settings").addContent(setting);
			}
			
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat( Format.getPrettyFormat() );
			outputter.output( document, new FileOutputStream( SETTINGS_FILE ) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getBackwardKey() {
		return values.get( "backward_key" );
	}

	public static String getDefaultBackwardKey() {
		return defaultValues.get( "backward_key" );
	}

	public static String getDefaultForwardKey() {
		return defaultValues.get( "forward_key" );
	}

	public static String getDefaultIsFullscreen() {
		return defaultValues.get( "is_fullscreen" );
	}

	public static String getDefaultPauseKey() {
		return defaultValues.get( "pause_key" );
	}

	public static String getDefaultResolutionHeight() {
		return defaultValues.get( "resolution_height" );
	}

	public static String getDefaultResolutionWidth() {
		return defaultValues.get( "resolution_width" );
	}

	public static String getDefaultRunKey() {
		return defaultValues.get( "run_key" );
	}

	public static String getDefaultSoundEnabled() {
		return defaultValues.get( "sound_enabled" );
	}

	public static String getDefaultTurnLeftKey() {
		return defaultValues.get( "turnleft_key" );
	}

	public static String getDefaultTurnRightKey() {
		return defaultValues.get( "turnright_key" );
	}

	public static String getForwardKey() {
		return values.get( "forward_key" );
	}

	public static String getPauseKey() {
		return values.get( "pause_key" );
	}

	public static String getResolutionHeight() {
		return values.get( "resolution_height" );
	}

	public static String getResolutionWidth() {
		return values.get( "resolution_width" );
	}

	public static String getRunKey() {
		return values.get( "run_key" );
	}

	public static String getTurnLeftKey() {
		return values.get( "turnleft_key" );
	}

	public static String getTurnRightKey() {
		return values.get( "turnright_key" );
	}

	public static String isFullscreen() {
		return values.get( "is_fullscreen" );
	}

	public static String isSoundEnabled() {
		return values.get( "sound_enabled" );
	}

	public static void setBackwardKey( String value ) {
		values.put( "backward_key", value );
	}

	public static void setForwardKey(String value) {
		values.put( "forward_key", value );
	}

	public static void setFullscreen(String value) {
		values.put( "is_fullscreen", value );
	}

	public static void setPauseKey(String value) {
		values.put( "pause_key", value );
	}

	public static void setResolutionHeight(String value) {
		values.put( "resolution_height", value );
	}

	public static void setResolutionWidth(String value) {
		values.put( "resolution_width", value );
	}

	public static void setRunKey(String value) {
		values.put( "run_key", value );
	}

	public static void setSoundEnabled(String value) {
		values.put( "sound_enabled", value );
	}

	public static void setTurnLeftKey(String value) {
		values.put( "turnleft_key", value );
	}

	public static void setTurnRightKey(String value) {
		values.put( "turnright_key", value );
	}

	public static String getDefaultResolutionDepth() {
		return defaultValues.get( "resolution_depth" );
	}

	public static String getDefaultResolutionFrequency() {
		return defaultValues.get( "resolution_frequency" );
	}

	public static String getResolutionDepth() {
		return values.get( "resolution_depth" );
	}

	public static String getResolutionFrequency() {
		return values.get( "resolution_frequency" );
	}

	public static void setResolutionDepth(String value) {
		values.put("resolution_depth", value );
	}

	public static void setResolutionFrequency(String value) {
		values.put("resolution_frequency", value );
	}
	
	public static String getDefaultSceneFilePath() {
		return defaultValues.get( "scene_file_path" );
	}
	
	public static String getSceneFilePath() {
		return values.get( "scene_file_path" );
	}
	
	public static void setSceneFilePath( String path ) {
		values.put( "scene_file_path", path );
	}
}
