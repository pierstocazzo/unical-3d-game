package game.menu;

/**
 * Interface used for define useful method that communicate with other class
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public interface OptionsInterface {

	public abstract String getDefaultResolutionWidth();
	public abstract String getDefaultResolutionHeight();
	public abstract String getDefaultResolutionDepth();
	public abstract String getDefaultResolutionFrequency();
	
	public abstract String getResolutionWidth();
	public abstract String getResolutionHeight();
	public abstract String getResolutionDepth();
	public abstract String getResolutionFrequency();
	
	public abstract void setResolutionWidth( String value );
	public abstract void setResolutionHeight( String value );
	public abstract void setResolutionDepth( String value );
	public abstract void setResolutionFrequency( String value );
	
	public abstract boolean getDefaultIsFullscreen();
	public abstract boolean isFullscreen();
	public abstract void setFullscreen( boolean value );
	
	public abstract String getDefaultForwardKey();
	public abstract String getForwardKey();
	public abstract void setForwardKey( String value );
	
	public abstract String getDefaultBackwardKey();
	public abstract String getBackwardKey();
	public abstract void setBackwardKey( String value );
	
	public abstract String getDefaultStrafeRightKey();
	public abstract String getStrafeRightKey();
	public abstract void setStrafeRightKey( String value );
	
	public abstract String getDefaultStrafeLeftKey();
	public abstract String getStrafeLeftKey();
	public abstract void setStrafeLeftKey( String value );
	
	public abstract String getDefaultRunKey();
	public abstract String getRunKey();
	public abstract void setRunKey( String value );
	
	public abstract String getDefaultPauseKey();
	public abstract String getPauseKey();
	public abstract void setPauseKey( String value );
	
	public abstract boolean getDefaultSoundEnabled();
	public abstract boolean isSoundEnabled();
	public abstract void setSoundEnabled( boolean value );
}
