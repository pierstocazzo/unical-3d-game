package game.menu;

/**
 * Interface used for define useful method that communicate with other class
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public interface OptionsInterface {

	public abstract String getDefaultResolution();
	public abstract String getResolution();
	public abstract void setResolution( String value );
	
	public abstract boolean getDefaultIsFullscreen();
	public abstract boolean isFullscreen();
	public abstract void setFullscreen( boolean value );
	
	public abstract String GetDefaultXmlPath();
	public abstract String getXmlPath();
	public abstract void setXmlPath( String value );
	
	public abstract String GetDefaultForwardKey();
	public abstract String getForwardKey();
	public abstract void setForwardKey( String value );
	
	public abstract String GetDefaultBackwardKey();
	public abstract String getBackwardKey();
	public abstract void setBackwardKey( String value );
	
	public abstract String GetDefaultStrafeRightKey();
	public abstract String getStrafeRightKey();
	public abstract void setStrafeRightKey( String value );
	
	public abstract String GetDefaultStrafeLeftKey();
	public abstract String getStrafeLeftKey();
	public abstract void setStrafeLeftKey( String value );
	
	public abstract String GetDefaultRunKey();
	public abstract String getRunKey();
	public abstract void setRunKey( String value );
	
	public abstract String GetDefaultPauseKey();
	public abstract String getPauseKey();
	public abstract void setPauseKey( String value );
	
	public abstract boolean getDefaultSoundEnabled();
	public abstract boolean isSoundEnabled();
	public abstract boolean setSoundEnabled( boolean value );
}
