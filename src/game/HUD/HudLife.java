package game.HUD;

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
	Text lifeNum;
	
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
	public HudLife(UserHud userHud){
		this.userHud = userHud;
		screenWidth = userHud.gWorld.getResolution().x;
    	screenHeight = userHud.gWorld.getResolution().y;
    	
		createBar();
		
		life = Text.createDefaultTextLabel( "life" );
    	life.setTextColor(ColorRGBA.blue);
    	life.setLocalTranslation( backQuad.getLocalTranslation().x-backQuad.getWidth()/2, 
    			backQuad.getLocalTranslation().y+backQuad.getHeight()/2, 0 );
    	life.print("Life");
    	life.lock();
    	userHud.gWorld.hudNode.attachChild( life );
    	lifeNum = Text.createDefaultTextLabel( "lifeNum" );
    	lifeNum.setTextColor(ColorRGBA.black);
    	lifeNum.setLocalScale(2);
    	lifeNum.setLocalTranslation( backQuad.getLocalTranslation().x, 
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.gWorld.hudNode.attachChild( lifeNum );
    	//for first correct visualization
    	lifeValue = 100;
    	checkColor();
		frontQuad.resize(initialLenght*lifeValue/100, 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										backQuad.getLocalTranslation().y, 0);
		lifeNum.print(Integer.toString(lifeValue));
		lifeNum.setLocalTranslation(backQuad.getLocalTranslation().x-lifeNum.getWidth()/2,
				backQuad.getLocalTranslation().y-lifeNum.getHeight()/2, 0);
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){
		lifeValue = userHud.game.getCurrentLife(userHud.gWorld.player.id);
		checkColor();
		frontQuad.resize(initialLenght*lifeValue/userHud.game.getMaxLife(userHud.gWorld.player.id), 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										backQuad.getLocalTranslation().y, 0);
		lifeNum.print(Integer.toString(lifeValue));
		lifeNum.setLocalTranslation(backQuad.getLocalTranslation().x-lifeNum.getWidth()/2,
				backQuad.getLocalTranslation().y-lifeNum.getHeight()/2, 0);
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		backQuad = new Quad("backQuad", screenWidth/4 , screenHeight/20);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(screenWidth/40+backQuad.getWidth()/2,
    								screenHeight/40+backQuad.getHeight()/2, 0);
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
