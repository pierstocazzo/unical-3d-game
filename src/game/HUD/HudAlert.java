package game.HUD;

import game.common.GameTimer;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudAlert
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudAlert {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Little text on the alert bar */
	Text alert;
	
	int alertValue;
	
	/** Background Quad of life Bar */
	Quad backQuad;
	
	/** Front Quad of life Bar */
	Quad frontQuad;
	
	/** Screen width */
	int widthScreen;
	
	/** Screen height */
	int heightScreen;
	
	/** Border bar parameter */
	int borderWeight = 3;
	
	/** Bar standard Lenght in pixel */
	int initialLenght;
	
	/** Constructor
	 * 
	 * @param userHud
	 */
	public HudAlert(UserHud userHud){
		this.userHud = userHud;
		widthScreen = userHud.gWorld.settings.getWidth();
    	heightScreen = userHud.gWorld.settings.getHeight();
    	
		createBar();
		
		alert = Text.createDefaultTextLabel( "alertBar" );
    	alert.setTextColor(ColorRGBA.blue);
    	alert.setLocalTranslation( backQuad.getLocalTranslation().x-backQuad.getWidth()/2, 
    			backQuad.getLocalTranslation().y+backQuad.getHeight()/2, 0 );
    	userHud.gWorld.hudNode.attachChild( alert );
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){

		alertValue = getAlertLevel();
		alert.print("Alert: "+Integer.toString(alertValue));
		
		frontQuad.resize(initialLenght*alertValue/100, 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(widthScreen/40+frontQuad.getWidth()/2+borderWeight,
										frontQuad.getLocalTranslation().y, 0);
		checkColor();
	}
	
	public int getAlertLevel(){
		//calculate max alert level
		int max = 0;
		for( String id : userHud.game.getEnemiesIds() ){
			System.out.println("ALERT="+userHud.game.getAlertLevel(id));
			if(userHud.game.getAlertLevel(id) > max )
				max = (int) userHud.game.getAlertLevel(id);
		}
		
		int diff = (int) GameTimer.getTimeInSeconds() - max;
		if((20 - diff)*100/20 < 20)
			return 20;
		return (20 - diff)*100/20;
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		backQuad = new Quad("backQuad", widthScreen/4 , heightScreen/20);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(widthScreen/40+backQuad.getWidth()/2,
    			heightScreen-heightScreen/20-backQuad.getHeight()/2, 0);
    	userHud.gWorld.hudNode.attachChild( backQuad );
    	
    	frontQuad = new Quad("frontQuad", backQuad.getWidth() - borderWeight*2 , 
    										backQuad.getHeight() - borderWeight*2);
    	initialLenght = (int) frontQuad.getWidth();
    	frontQuad.setDefaultColor(ColorRGBA.green);
    	frontQuad.setLocalTranslation(backQuad.getLocalTranslation().x+borderWeight,
    			backQuad.getLocalTranslation().y, 0);
    	userHud.gWorld.hudNode.attachChild( frontQuad );
	}
	
	/**
	 * It checks value life and change quad color
	 */
	public void checkColor(){
		if(alertValue > 80)
			frontQuad.setDefaultColor(ColorRGBA.red);
		else if(alertValue > 20)
			frontQuad.setDefaultColor(ColorRGBA.yellow);
		else 
			frontQuad.setDefaultColor(ColorRGBA.green);
	}
}
