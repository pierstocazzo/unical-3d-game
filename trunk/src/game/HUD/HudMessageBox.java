package game.HUD;

import utils.Loader;

import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import game.common.GameTimer;

public class HudMessageBox {

	public static int WELCOME = 0;
	public static int COMMAND = 1;
	public static int DIE = 2;
	public static int GAMEOVER = 3;
	
	int type;
	Quad quadMsg;
	UserHud userHud;
	boolean used = false;
	
	public HudMessageBox(int type, UserHud userHud){
		this.type = type;
		this.userHud = userHud;
		createMessageBox();
	}
	
	public void createMessageBox(){
		quadMsg = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
		quadMsg.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
		userHud.gWorld.hudNode.attachChild(quadMsg);
		String path = "game/data/message/sfondo.jpg";
		switch (type) {
			case 0:path = "game/data/message/sfondo.jpg";break;//WELCOME
			case 1:path = "game/data/message/sfondo.jpg";break;//COMMAND
			case 2:path = "game/data/message/sfondo.jpg";break;//DIE
			case 3:path = "game/data/message/sfondo.jpg";break;//GAMEOVER
		}
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture( TextureManager.loadTexture( Loader.load( path ), true ) );
        ts.setEnabled(true);
        quadMsg.setRenderState(ts);
	}
	
	public void update(){
		if(!used){
				userHud.gWorld.pause = true;
				used = true;
		}
		if(used && !userHud.gWorld.pause)
			userHud.gWorld.hudNode.detachChild(quadMsg);
	}
}
