package proposta.hud;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudLife
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudLife {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Life Value of Player */
	int lifeValue;
	
	/** Little text on the life bar */
	Text life;
	
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
	public HudLife(UserHud userHud){
		this.userHud = userHud;
		widthScreen = userHud.gWorld.settings.getWidth();
    	heightScreen = userHud.gWorld.settings.getHeight();
    	
		createBar();
		
		life = Text.createDefaultTextLabel( "Score" );
    	life.setTextColor(ColorRGBA.blue);
    	life.setLocalTranslation( backQuad.getLocalTranslation().x-backQuad.getWidth()/2, 
    			backQuad.getLocalTranslation().y+backQuad.getHeight()/2, 0 );
    	userHud.gWorld.hudNode.attachChild( life );
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){
		lifeValue = userHud.game.getCharacterLife(userHud.gWorld.player.id);
		life.print("Life: "+Integer.toString(lifeValue));
		
		frontQuad.resize(initialLenght*lifeValue/100, 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(widthScreen/40+frontQuad.getWidth()/2+borderWeight,
										frontQuad.getLocalTranslation().y, 0);
		checkColor();
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		backQuad = new Quad("backQuad", widthScreen/4 , heightScreen/20);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(widthScreen/40+backQuad.getWidth()/2,
    								heightScreen/40+backQuad.getHeight()/2, 0);
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
		if(lifeValue > 60)
			frontQuad.setDefaultColor(ColorRGBA.green);
		else if(lifeValue > 25)
			frontQuad.setDefaultColor(ColorRGBA.yellow);
		else 
			frontQuad.setDefaultColor(ColorRGBA.red);
	}
}
