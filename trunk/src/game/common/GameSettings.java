package game.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.jme.input.KeyInput;

/** Class GameSettings used to read the xml configuration file<br>
 * 
 * Needed library: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author Giuseppe Leone 
 */
public class GameSettings {
	
	public static String SETTINGS_FILE = "src/game/data/settings.xml";
	
	/** the xml document */
	static Document document;
	
	/** the root of the xml file */
	static Element root;
	
	/**
	 * Default Values
	 */
	static HashMap< String, Value > defaultValues;
	
	/**
	 * User defined values
	 */
	static HashMap< String, Value > values;
	
	public static void init() {
		/* Initialize default values */
		initializeDefaultValues();
		
		/**
		 * Load user defined values from XML file setting 
		 */
		load();
	}
	
	public static void initializeDefaultValues() {
		/**
		 * Let's create new HashMaps!
		 */
		defaultValues = new HashMap< String, Value >();
		
		/**
		 * ### INTEGER VALUES ###
		 */
		
		// Keyboard
		defaultValues.put( "run_key", new Value(KeyInput.KEY_LSHIFT) ); 
		defaultValues.put( "forward_key", new Value(KeyInput.KEY_W) );
		defaultValues.put( "backward_key", new Value(KeyInput.KEY_S) );
		defaultValues.put( "straferight_key", new Value(KeyInput.KEY_D) );
		defaultValues.put( "strafeleft_key", new Value(KeyInput.KEY_A) );
		defaultValues.put( "pause_key", new Value(KeyInput.KEY_P) ); 
		
		// Resolution
		defaultValues.put( "resolution_width", new Value(1280) );
		defaultValues.put( "resolution_height", new Value(800) );
		defaultValues.put( "resolution_depth", new Value(16) );
		defaultValues.put( "resolution_frequency", new Value(50) );
		
		/**
		 * ### BOOLEAN VALUES ###
		 */
		
		// Resolution
		defaultValues.put( "is_fullscreen", new Value(false) );
		
		// Sound
		defaultValues.put( "sound_enabled", new Value(false) );
		
		//XML path scene file
		defaultValues.put( "scene_file_path", new Value("src/game/data/level/island.xml") );
		
		/**
		 * ### STRING VALUES ###
		 */
		
		// Phrases
		// TODO
		
		/**
		 * Set actual values as default values
		 */
		values = new HashMap< String, Value >();
		values.putAll(defaultValues);
	}
	
	@SuppressWarnings("unchecked")
	public static void load() {
		SAXBuilder builder = new SAXBuilder();
		
		/* Try to load user defined settings*/
		try {
			document = builder.build( new File( SETTINGS_FILE ) );
			root = document.getRootElement();
			
			for( Iterator<Element> it = root.getChildren().iterator(); it.hasNext();  ) {
				Element element = it.next();
				Attribute name = element.getAttribute("name");
				Attribute type = element.getAttribute("type");
				
				/**
				 * Check the correct type of setting
				 */
				if ( type.getValue().equals("integer") ) {
					// Is an Integer
					values.put( name.getValue(), new Value( Integer.valueOf(element.getText()) ) );
				} else if ( type.getValue().contains("boolean") ) {
					// Is a Boolean
					values.put( name.getValue(), new Value( Boolean.valueOf(element.getText()) ) );
				} else {
					// Is a String
					values.put( name.getValue(), new Value(element.getText().toString()) );
				}
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
			root = new Element("settings");
			document = new Document(root);
			
			for( String key : values.keySet() ) {
				Element item = new Element( "item" );
				item.setAttribute("name", key);
				/**
				 * Check for the correct element type
				 */
				if ( values.get(key).integerValue != null ) {
					item.setAttribute("type", "integer");
					item.setText( values.get(key).integerValue.toString() );
				} else if ( values.get(key).booleanValue != null ) {
					item.setAttribute("type", "boolean");
					item.setText( values.get(key).booleanValue.toString() );
				} else {
					item.setAttribute("type", "string");
					item.setText( values.get(key).stringValue );
				}
				root.addContent(item);
			}
			
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat( Format.getPrettyFormat() );
			outputter.output( document, new FileOutputStream( SETTINGS_FILE ) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getBackwardKey() {
		return KeyConverter.toString(values.get( "backward_key" ).integerValue);
	}

	public static String getDefaultBackwardKey() {
		return KeyConverter.toString(defaultValues.get( "backward_key" ).integerValue);
	}

	public static String getDefaultForwardKey() {
		return KeyConverter.toString(defaultValues.get( "forward_key" ).integerValue);
	}

	public static String getDefaultIsFullscreen() {
		if ( defaultValues.get( "is_fullscreen" ).booleanValue )
			return "YES";
		return "NO";
	}

	public static String getDefaultPauseKey() {
		return KeyConverter.toString(defaultValues.get( "pause_key" ).integerValue);
	}

	public static String getDefaultResolutionHeight() {
		return defaultValues.get( "resolution_height" ).stringValue;
	}

	public static String getDefaultResolutionWidth() {
		return defaultValues.get( "resolution_width" ).stringValue;
	}

	public static String getDefaultRunKey() {
		return KeyConverter.toString(defaultValues.get( "run_key" ).integerValue);
	}

	public static String getDefaultSoundEnabled() {
		if ( defaultValues.get( "sound_enabled" ).booleanValue )
			return "YES";
		return "NO";
	}

	public static String getDefaultStrafeLeftKey() {
		return KeyConverter.toString(defaultValues.get( "strafeleft_key" ).integerValue);
	}

	public static String getDefaultStrafeRightKey() {
		return KeyConverter.toString(defaultValues.get( "straferight_key" ).integerValue);
	}

	public static String getForwardKey() {
		return KeyConverter.toString(values.get( "forward_key" ).integerValue);
	}

	public static String getPauseKey() {
		return KeyConverter.toString(values.get( "pause_key" ).integerValue);
	}

	public static String getResolutionHeight() {
		return values.get( "resolution_height" ).stringValue;
	}

	public static String getResolutionWidth() {
		return values.get( "resolution_width" ).stringValue;
	}

	public static String getRunKey() {
		return KeyConverter.toString(values.get( "run_key" ).integerValue);
	}

	public static String getStrafeLeftKey() {
		return KeyConverter.toString(values.get( "strafeleft_key" ).integerValue);
	}

	public static String getStrafeRightKey() {
		return KeyConverter.toString(values.get( "straferight_key" ).integerValue);
	}

	public static String isFullscreen() {
		if (values.get( "is_fullscreen" ).booleanValue ) 
			return "YES";
		return "NO";
	}

	public static String isSoundEnabled() {
		if ( values.get( "sound_enabled" ).booleanValue )
			return "YES";
		return "NO";
	}

	public static void setBackwardKey(String value) {
		values.put( "backward_key", new Value(KeyConverter.toKey(value)) );
	}

	public static void setForwardKey(String value) {
		values.put( "forward_key", new Value(KeyConverter.toKey(value)) );
	}

	public static void setFullscreen(String value) {
		if ( value == "YES")
			values.put( "is_fullscreen", new Value(true) );
		else
			values.put( "is_fullscreen", new Value(false) );
	}

	public static void setPauseKey(String value) {
		values.put( "pause_key", new Value(KeyConverter.toKey(value)) );
	}

	public static void setResolutionHeight(String value) {
		values.put( "resolution_height", new Value( value ) );
	}

	public static void setResolutionWidth(String value) {
		values.put( "resolution_width", new Value( value ) );
	}

	public static void setRunKey(String value) {
		values.put( "run_key", new Value(KeyConverter.toKey(value)) );
	}

	public static void setSoundEnabled(String value) {
		if ( value == "YES" )
			values.put( "sound_enabled", new Value(true) );
		else
			values.put( "sound_enabled", new Value(false) );
	}

	public static void setStrafeLeftKey(String value) {
		values.put( "strafeleft_key", new Value(KeyConverter.toKey(value)) );
	}

	public static void setStrafeRightKey(String value) {
		values.put( "straferight_key", new Value(KeyConverter.toKey(value)) );
	}

	public static String getDefaultResolutionDepth() {
		return defaultValues.get( "resolution_depth" ).stringValue;
	}

	public static String getDefaultResolutionFrequency() {
		return defaultValues.get( "resolution_frequency" ).stringValue;
	}

	public static String getResolutionDepth() {
		return values.get( "resolution_depth" ).stringValue;
	}

	public static String getResolutionFrequency() {
		return values.get( "resolution_frequency" ).stringValue;
	}

	public static void setResolutionDepth(String value) {
		values.put("resolution_depth", new Value(value));
	}

	public static void setResolutionFrequency(String value) {
		values.put("resolution_frequency", new Value(value));
	}
	
	public static String getDefaultSceneFilePath() {
		return defaultValues.get( "scene_file_path" ).stringValue;
	}
	
	public static String getSceneFilePath() {
		return values.get( "scene_file_path" ).stringValue;
	}
	
	public static void setSceneFilePath( String path ) {
		values.put( "scene_file_path", new Value( path ) );
	}
	
	/**
	 * A generic value(Integer|Boolean|String) container
	 * 
	 * @author Giuseppe Leone
	 */
	public static class Value {
		Integer integerValue;
		Boolean booleanValue;
		String stringValue;
		
		public Value( Integer value ) {
			this.integerValue = value;
			this.booleanValue = null;
			this.stringValue = null;
		}
		
		public Value( Boolean value ) {
			this.integerValue = null;
			this.booleanValue = value;
			this.stringValue = null;
		}
		
		public Value( String value ) {
			this.integerValue = null;
			this.booleanValue = null;
			this.stringValue = value;
		}
	}

}
