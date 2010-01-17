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
	public int getBackwardKey() {
		return values.get( "backward_key" ).integerValue;
	}

	@Override
	public int getDefaultBackwardKey() {
		return defaultValues.get( "backward_key" ).integerValue;
	}

	@Override
	public int getDefaultForwardKey() {
		return defaultValues.get( "forward_key" ).integerValue;
	}

	@Override
	public boolean getDefaultIsFullscreen() {
		return defaultValues.get( "is_fullscreen" ).booleanValue;
	}

	@Override
	public int getDefaultPauseKey() {
		return defaultValues.get( "pause_key" ).integerValue;
	}

	@Override
	public int getDefaultResolutionHeight() {
		return defaultValues.get( "resolution_height" ).integerValue;
	}

	@Override
	public int getDefaultResolutionWidth() {
		return defaultValues.get( "resolution_width" ).integerValue;
	}

	@Override
	public int getDefaultRunKey() {
		return defaultValues.get( "run_key" ).integerValue;
	}

	@Override
	public boolean getDefaultSoundEnabled() {
		return defaultValues.get( "sound_enabled" ).booleanValue;
	}

	@Override
	public int getDefaultStrafeLeftKey() {
		return defaultValues.get( "strafeleft_key" ).integerValue;
	}

	@Override
	public int getDefaultStrafeRightKey() {
		return defaultValues.get( "straferight_key" ).integerValue;
	}

	@Override
	public int getForwardKey() {
		return values.get( "forward_key" ).integerValue;
	}

	@Override
	public int getPauseKey() {
		return values.get( "pause_key" ).integerValue;
	}

	@Override
	public int getResolutionHeight() {
		return values.get( "resolution_height" ).integerValue;
	}

	@Override
	public int getResolutionWidth() {
		return values.get( "resolution_width" ).integerValue;
	}

	@Override
	public int getRunKey() {
		return values.get( "run_key" ).integerValue;
	}

	@Override
	public int getStrafeLeftKey() {
		return values.get( "strafeleft_key" ).integerValue;
	}

	@Override
	public int getStrafeRightKey() {
		return values.get( "straferight_key" ).integerValue;
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
	public void setBackwardKey(int value) {
		values.put( "backward_key", new Value(value) );
	}

	@Override
	public void setForwardKey(int value) {
		values.put( "forward_key", new Value(value) );
	}

	@Override
	public void setFullscreen(boolean value) {
		values.put( "is_fullscreen", new Value(value) );
	}

	@Override
	public void setPauseKey(int value) {
		values.put( "pause_key", new Value(value) );
	}

	@Override
	public void setResolutionHeight(int value) {
		values.put( "resolution_height", new Value(value) );
	}

	@Override
	public void setResolutionWidth(int value) {
		values.put( "resolution_width", new Value(value) );
	}

	@Override
	public void setRunKey(int value) {
		values.put( "run_key", new Value(value) );
	}

	@Override
	public void setSoundEnabled(boolean value) {
		values.put( "sound_enabled", new Value(value) );
	}

	@Override
	public void setStrafeLeftKey(int value) {
		values.put( "strafeleft_key", new Value(value) );
	}

	@Override
	public void setStrafeRightKey(int value) {
		values.put( "straferight_key", new Value(value) );
	}

	@Override
	public int getDefaultResolutionDepth() {
		return defaultValues.get( "resolution_depth" ).integerValue;
	}

	@Override
	public int getDefaultResolutionFrequency() {
		return defaultValues.get( "resolution_frequency" ).integerValue;
	}

	@Override
	public int getResolutionDepth() {
		return values.get( "resolution_depth" ).integerValue;
	}

	@Override
	public int getResolutionFrequency() {
		return values.get( "resolution_frequency" ).integerValue;
	}

	@Override
	public void setResolutionDepth(int value) {
		values.put("resolution_depth", new Value(value));
	}

	@Override
	public void setResolutionFrequency(int value) {
		values.put("resolution_frequency", new Value(value));
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
