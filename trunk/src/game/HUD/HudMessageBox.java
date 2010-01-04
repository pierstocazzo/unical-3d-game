package game.HUD;

import java.net.URL;

import utils.Loader;

import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import game.common.GameTimer;

public class HudMessageBox {

	public static final int WELCOME = 0;
	public static final int COMMAND = 1;
	public static final int DIE = 2;
	public static final int GAMEOVER = 3;
	public static final int NOTHING = 4;
	
	int type;
	Quad quadMsg;
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
	public HudMessageBox(int type, UserHud userHud){
		this.userHud = userHud;
		createMessageBox(type);
	}
	
	/**
	 * Create a new message box
	 * 
	 * @param type (Int) Type of Message
	 */
	public void createMessageBox(int type){
		this.type = type;
		quadMsg = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
		quadMsg.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
		userHud.gWorld.hudNode.attachChild(quadMsg);
		String path = "game/data/message/sfondo.jpg";
		switch (type) {
			case WELCOME:path = "game/data/message/sfondo.jpg";
					time = GameTimer.getTimeInSeconds();break;//WELCOME
			case COMMAND:path = "game/data/images/map.jpg";
					time = GameTimer.getTimeInSeconds();break;
//			case DIE:path = "game/data/message/sfondo.jpg";break;//DIE
//			case GAMEOVER:path = "game/data/message/sfondo.jpg";break;//GAMEOVER
//			case NOTHING:path = "game/data/message/sfondo.jpg";break;//NOTHING
		}
		/** add a texture */
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture( TextureManager.loadTexture( Loader.load( path ) , true ) );
        ts.setEnabled(true);
        quadMsg.setRenderState(ts);
	}
	
	/** Update current message */
	public void update(){
		if( type != NOTHING) {
//			checkPause();
			if(type == WELCOME){
				System.out.println("WELCOME");
				if(time + 1 <= GameTimer.getTimeInSeconds()){
					userHud.gWorld.hudNode.detachChild(quadMsg);
					createMessageBox(COMMAND);
				}
			}
			else if(type == COMMAND){
//				checkPause();
				System.out.println("COMMAND");
				if(time + 1 <= GameTimer.getTimeInSeconds() && !changed){
					changed = true;
					userHud.gWorld.hudNode.detachChild(quadMsg);
				}
			}
		}
	}
	
	public void checkPause(){
		if(!used){
			userHud.gWorld.pause = true;
			used = true;
		}
		if(used && !userHud.gWorld.pause)
			userHud.gWorld.hudNode.detachChild(quadMsg);
	}
}
