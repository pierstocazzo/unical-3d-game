package game.HUD;

import game.common.GameTimer;
import utils.Loader;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class HudMessageBox {

	public static final int WELCOME = 0;
	public static final int COMMAND = 1;
	public static final int DIE = 2;
	public static final int GAMEOVER = 3;
	public static final int NOTHING = 4;
	
	int type;
	Quad quadWelcome;
	Quad quadMap;
	Quad quadDie;
	Quad quadGameOver;
	UserHud userHud;
	boolean used = false;
	boolean changed = false;
	float time = 0;
	
	/**
	 * Constructor 
	 * 
	 * @param type (int) A Type of Message
	 * @param userHud (UserHud) Useful pointer to UserHud
	 */
	public HudMessageBox( int type, UserHud userHud ) {
		this.userHud = userHud;
		this.type = type;
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
		quadWelcome = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
		quadWelcome.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
		ts.setTexture( TextureManager.loadTexture( Loader.load( "game/data/message/sfondo.jpg" ), true ) );
		quadWelcome.setRenderState(ts);
		
		TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts1.setEnabled(true);
		quadMap = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
		quadMap.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
		ts1.setTexture( TextureManager.loadTexture( Loader.load( "game/data/images/map.jpg" ), true ) );
		quadMap.setRenderState(ts1);
		
		quadDie = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
		quadDie.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
		
		
		quadGameOver = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
		quadGameOver.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
		
		switchTo(type);
	}
	
	/**
	 * Create a new message box
	 * 
	 * @param type (Int) Type of Message
	 */
	public void switchTo( int newtype ) {
		this.type = newtype;
		
		switch (type) {
		
		case WELCOME:
			time = GameTimer.getTimeInSeconds();
			userHud.gWorld.hudNode.attachChild(quadWelcome);
			quadWelcome.updateRenderState();
			break;
			
		case COMMAND:
			time = GameTimer.getTimeInSeconds();
			userHud.gWorld.hudNode.attachChild(quadMap);
			quadMap.updateRenderState();
			break;
			
		case DIE:
			break;
			
		case GAMEOVER:
			break;
			
		case NOTHING:
			break;
		}
	}
	
	/** Update current message */
	public void update() {
		switch (type) {
		
		case WELCOME:
			if( time + 1 <= GameTimer.getTimeInSeconds() ) {
				quadWelcome.removeFromParent();
				switchTo(COMMAND);
			}
			break;
			
		case COMMAND:
			if( time + 1 <= GameTimer.getTimeInSeconds() && !changed ) {
				quadMap.removeFromParent();
				switchTo(WELCOME);
			}
			break;
			
		case DIE:
			break;
			
		case GAMEOVER:
			break;
			
		case NOTHING:
			break;
		}
	}
	
	public void checkPause(){
		if(!used){
			userHud.gWorld.pause = true;
			used = true;
		}
		if(used && !userHud.gWorld.pause)
			userHud.gWorld.hudNode.detachChild(quadWelcome);
	}
}
