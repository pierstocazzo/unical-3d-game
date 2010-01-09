package game.menu;

public interface OptionsInterface {

	public abstract String getDefaultResolution();
	public abstract String getResolution();
	public abstract void setResolution();
	
	public abstract boolean getDefaultIsFullscreen();
	public abstract boolean isFullscreen();
	public abstract void setFullscreen();
	
	public abstract String GetDefaultXmlPath();
	public abstract String getXmlPath();
	public abstract void setXmlPath();
	
	public abstract String GetDefaultForwardKey();
	public abstract String getForwardKey();
	public abstract void setForwardKey();
	
	public abstract String GetDefaultBackwardKey();
	public abstract String getBackwardKey();
	public abstract void setBackwardKey();
	
	public abstract String GetDefaultStrafeRightKey();
	public abstract String getStrafeRightKey();
	public abstract void setStrafeRightKey();
	
	public abstract String GetDefaultStrafeLeftKey();
	public abstract String getStrafeLeftKey();
	public abstract void setStrafeLeftKey();
	
	public abstract String GetDefaultRunKey();
	public abstract String getRunKey();
	public abstract void setRunKey();
	
	public abstract String GetDefaultPauseKey();
	public abstract String getPauseKey();
	public abstract void setPauseKey();
}
