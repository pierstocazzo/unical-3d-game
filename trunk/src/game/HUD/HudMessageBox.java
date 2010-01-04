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
	Quad quad;
	UserHud userHud;
	boolean pauseUsed = false;
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
		createMessageBox(WELCOME);
	}
	
	/** Update current message */
	public void update() {
		if(type != NOTHING){
			switch (type) {
				case WELCOME:
					if( time + 1 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						createMessageBox(COMMAND);
					}
					break;
					
				case COMMAND:
					checkPause();
					if(!changed){
						userHud.gWorld.pause = true;
						changed = true;
					}
					break;
					
				case DIE:
					break;
					
				case GAMEOVER:
					if( time + 3 <= GameTimer.getTimeInSeconds() )
						userHud.gWorld.finish();
					break;
			}
		}
	}

    /**
     * Create a new message box
     * 
     * @param type (int) Type of Message
     */
    public void createMessageBox(int type){
            this.type = type;
            quad = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
            quad.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
            userHud.gWorld.hudNode.attachChild(quad);
            String path = "game/data/message/sfondo.jpg";
            switch (type) {
                case WELCOME:path = "game/data/message/sfondo.jpg";break;//WELCOME
                case COMMAND:path = "game/data/images/map.jpg";break;//COMMAND
				case DIE:path = "game/data/message/sfondo.jpg";break;//DIE
				case GAMEOVER:path = "game/data/message/sfondo.jpg";break;//GAMEOVER
            }
            time = GameTimer.getTimeInSeconds();
            /** add a texture */
            TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
            ts.setTexture( TextureManager.loadTexture( Loader.load( path ) , true ) );
		    ts.setEnabled(true);
		    quad.setRenderState(ts);
		    quad.updateRenderState();
		    if(type == GAMEOVER)
		    	System.out.println("CREATO MSG GAMEOVER");
    }
	
    /**
     * Check and Set pause
     */
	public void checkPause(){
		if(!pauseUsed){
			userHud.gWorld.pause = true;
			pauseUsed = true;
		}
		if(pauseUsed && !userHud.gWorld.pause)
			userHud.gWorld.hudNode.detachChild(quad);
	}
}