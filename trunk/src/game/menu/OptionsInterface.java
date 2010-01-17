package game.menu;

/**
 * Interface used for define useful method that communicate with other class
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public interface OptionsInterface {

	public abstract int getDefaultResolutionWidth();
	public abstract int getDefaultResolutionHeight();
	public abstract int getDefaultResolutionDepth();
	public abstract int getDefaultResolutionFrequency();
	
	public abstract int getResolutionWidth();
	public abstract int getResolutionHeight();
	public abstract int getResolutionDepth();
	public abstract int getResolutionFrequency();
	
	public abstract void setResolutionWidth( int value );
	public abstract void setResolutionHeight( int value );
	public abstract void setResolutionDepth( int value );
	public abstract void setResolutionFrequency( int value );
	
	public abstract boolean getDefaultIsFullscreen();
	public abstract boolean isFullscreen();
	public abstract void setFullscreen( boolean value );
	
	public abstract int getDefaultForwardKey();
	public abstract int getForwardKey();
	public abstract void setForwardKey( int value );
	
	public abstract int getDefaultBackwardKey();
	public abstract int getBackwardKey();
	public abstract void setBackwardKey( int value );
	
	public abstract int getDefaultStrafeRightKey();
	public abstract int getStrafeRightKey();
	public abstract void setStrafeRightKey( int value );
	
	public abstract int getDefaultStrafeLeftKey();
	public abstract int getStrafeLeftKey();
	public abstract void setStrafeLeftKey( int value );
	
	public abstract int getDefaultRunKey();
	public abstract int getRunKey();
	public abstract void setRunKey( int value );
	
	public abstract int getDefaultPauseKey();
	public abstract int getPauseKey();
	public abstract void setPauseKey( int value );
	
	public abstract boolean getDefaultSoundEnabled();
	public abstract boolean isSoundEnabled();
	public abstract void setSoundEnabled( boolean value );
}
