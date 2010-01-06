package game.HUD;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudLife
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudAmmo {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Life Value of Player */
	int ammoValue;
	
	/** Little text on the life bar */
	Text ammoNum;
	
	/** Background Quad of life Bar */
	Quad backQuad;
	
	/** Front Quad of life Bar */
	Quad frontQuad;
	
	/** Screen width */
	float screenWidth;
	
	/** Screen height */
	float screenHeight;
	
	/** Border bar parameter */
	int borderWeight = 3;
	
	/** Bar standard Lenght in pixel */
	int initialLenght;
	
	/** Constructor
	 * 
	 * @param userHud
	 */
	public HudAmmo(UserHud userHud){
		this.userHud = userHud;
		screenWidth = userHud.gWorld.getResolution().x;
    	screenHeight = userHud.gWorld.getResolution().y;
    	
		createBar();

    	ammoNum = Text.createDefaultTextLabel( "lifeNum" );
    	ammoNum.setTextColor(ColorRGBA.black);
    	ammoNum.setLocalScale(screenWidth/1100);
    	ammoNum.setLocalTranslation( backQuad.getLocalTranslation().x, 
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.gWorld.hudNode.attachChild( ammoNum );
    	//for first correct visualization
    	ammoValue = 0;
		frontQuad.resize(initialLenght*ammoValue/100, 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										backQuad.getLocalTranslation().y, 0);
		ammoNum.print("Ammo: "+Integer.toString(ammoValue));
		ammoNum.setLocalTranslation(backQuad.getLocalTranslation().x-backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y-ammoNum.getHeight()/2, 0);
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){
		ammoValue = userHud.game.getAmmo(userHud.gWorld.player.id);
		frontQuad.resize(initialLenght*ammoValue/userHud.game.getMaxAmmo(userHud.gWorld.player.id), 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										backQuad.getLocalTranslation().y, 0);
		ammoNum.print("Ammo: "+Integer.toString(ammoValue));
		ammoNum.setLocalTranslation(backQuad.getLocalTranslation().x-backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y-ammoNum.getHeight()/2, 0);
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		backQuad = new Quad("backQuad", screenWidth/6 , screenHeight/30);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(screenWidth/40+backQuad.getWidth()/2,
    			userHud.hudLife.backQuad.getHeight()+userHud.hudLife.backQuad.getHeight()+10, 0);
    	backQuad.lock();
    	userHud.gWorld.hudNode.attachChild( backQuad );
    	
    	frontQuad = new Quad("frontQuad", backQuad.getWidth() - borderWeight*2 , 
    										backQuad.getHeight() - borderWeight*2);
    	initialLenght = (int) frontQuad.getWidth();
    	frontQuad.setDefaultColor(ColorRGBA.green);
    	frontQuad.setLocalTranslation(backQuad.getLocalTranslation().x,
    			backQuad.getLocalTranslation().y, 0);
    	userHud.gWorld.hudNode.attachChild( frontQuad );
	}
}
