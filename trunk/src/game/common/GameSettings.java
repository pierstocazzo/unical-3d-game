package game.common;

import game.menu.OptionsInterface;

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

/** Class GameSettings used to read the xml configuration file<br>
 * 
 * Needed library: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author Giuseppe Leone 
 */
public class GameSettings implements OptionsInterface {
	
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
		defaultValues.put( "run_key", new Value(42) ); // default is 42
		defaultValues.put( "forward_key", new Value(17) );
		defaultValues.put( "backward_key", new Value(31) );
		defaultValues.put( "straferight_key", new Value(32) );
		defaultValues.put( "strafeleft_key", new Value(30) );
		defaultValues.put( "pause_key", new Value(17) ); // Not used in the game yet.
		
		// Resolution
		defaultValues.put( "resolution_width", new Value(640) );
		defaultValues.put( "resolution_height", new Value(480) );
		defaultValues.put( "resolution_depth", new Value(16) );
		defaultValues.put( "resolution_frequency", new Value(50) );
		
		/**
		 * ### BOOLEAN VALUES ###
		 */
		
		// Resolution
		defaultValues.put( "is_fullscreen", new Value(false) );
		
		// Sound
		defaultValues.put( "sound_enabled", new Value(false) );
		
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
	
	public static void load() {
		SAXBuilder builder = new SAXBuilder();
		
		/* Try to load user defined settings*/
		try {
			document = builder.build( new File( SETTINGS_FILE ) );
			root = document.getRootElement();
			
			for( Iterator it = root.getChildren().iterator(); it.hasNext();  ) {
				Element element = (Element) it.next();
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
	
	@Override
	public String getBackwardKey() {
		return KeyConverter.toString(values.get( "backward_key" ).integerValue);
	}

	@Override
	public String getDefaultBackwardKey() {
		return KeyConverter.toString(defaultValues.get( "backward_key" ).integerValue);
	}

	@Override
	public String getDefaultForwardKey() {
		return KeyConverter.toString(defaultValues.get( "forward_key" ).integerValue);
	}

	@Override
	public boolean getDefaultIsFullscreen() {
		return defaultValues.get( "is_fullscreen" ).booleanValue;
	}

	@Override
	public String getDefaultPauseKey() {
		return KeyConverter.toString(defaultValues.get( "pause_key" ).integerValue);
	}

	@Override
	public String getDefaultResolutionHeight() {
		return KeyConverter.toString(defaultValues.get( "resolution_height" ).integerValue);
	}

	@Override
	public String getDefaultResolutionWidth() {
		return KeyConverter.toString(defaultValues.get( "resolution_width" ).integerValue);
	}

	@Override
	public String getDefaultRunKey() {
		return KeyConverter.toString(defaultValues.get( "run_key" ).integerValue);
	}

	@Override
	public boolean getDefaultSoundEnabled() {
		return defaultValues.get( "sound_enabled" ).booleanValue;
	}

	@Override
	public String getDefaultStrafeLeftKey() {
		return KeyConverter.toString(defaultValues.get( "strafeleft_key" ).integerValue);
	}

	@Override
	public String getDefaultStrafeRightKey() {
		return KeyConverter.toString(defaultValues.get( "straferight_key" ).integerValue);
	}

	@Override
	public String getForwardKey() {
		return KeyConverter.toString(values.get( "forward_key" ).integerValue);
	}

	@Override
	public String getPauseKey() {
		return KeyConverter.toString(values.get( "pause_key" ).integerValue);
	}

	@Override
	public String getResolutionHeight() {
		return KeyConverter.toString(values.get( "resolution_height" ).integerValue);
	}

	@Override
	public String getResolutionWidth() {
		return KeyConverter.toString(values.get( "resolution_width" ).integerValue);
	}

	@Override
	public String getRunKey() {
		return KeyConverter.toString(values.get( "run_key" ).integerValue);
	}

	@Override
	public String getStrafeLeftKey() {
		return KeyConverter.toString(values.get( "strafeleft_key" ).integerValue);
	}

	@Override
	public String getStrafeRightKey() {
		return KeyConverter.toString(values.get( "straferight_key" ).integerValue);
	}

	@Override
	public boolean isFullscreen() {
		return values.get( "is_fullscreen" ).booleanValue;
	}

	@Override
	public boolean isSoundEnabled() {
		return values.get( "sound_enabled" ).booleanValue;
	}

	@Override
	public void setBackwardKey(String value) {
		values.put( "backward_key", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setForwardKey(String value) {
		values.put( "forward_key", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setFullscreen(boolean value) {
		values.put( "is_fullscreen", new Value(value) );
	}

	@Override
	public void setPauseKey(String value) {
		values.put( "pause_key", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setResolutionHeight(String value) {
		values.put( "resolution_height", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setResolutionWidth(String value) {
		values.put( "resolution_width", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setRunKey(String value) {
		values.put( "run_key", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setSoundEnabled(boolean value) {
		values.put( "sound_enabled", new Value(value) );
	}

	@Override
	public void setStrafeLeftKey(String value) {
		values.put( "strafeleft_key", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public void setStrafeRightKey(String value) {
		values.put( "straferight_key", new Value(KeyConverter.toKey(value)) );
	}

	@Override
	public String getDefaultResolutionDepth() {
		return KeyConverter.toString(defaultValues.get( "resolution_depth" ).integerValue);
	}

	@Override
	public String getDefaultResolutionFrequency() {
		return KeyConverter.toString(defaultValues.get( "resolution_frequency" ).integerValue);
	}

	@Override
	public String getResolutionDepth() {
		return KeyConverter.toString(values.get( "resolution_depth" ).integerValue);
	}

	@Override
	public String getResolutionFrequency() {
		return KeyConverter.toString(values.get( "resolution_frequency" ).integerValue);
	}

	@Override
	public void setResolutionDepth(String value) {
		values.put("resolution_depth", new Value(KeyConverter.toKey(value)));
	}

	@Override
	public void setResolutionFrequency(String value) {
		values.put("resolution_frequency", new Value(KeyConverter.toKey(value)));
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
