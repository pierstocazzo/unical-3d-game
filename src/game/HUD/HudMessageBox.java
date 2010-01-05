package game.HUD;

import game.common.GameTimer;
import utils.Loader;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class HudMessageBox {

	public static final int WELCOME1 = 0;
	public static final int WELCOME2 = 1;
	public static final int WELCOME3 = 2;
	public static final int WELCOME4 = 3;
	public static final int LEVEL2 = 4;	
	public static final int DIE = 5;
	public static final int GAMEOVER = 6;
	public static final int NOTHING = 7;
	
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
		createMessageBox(type);
	}
	
	/** Update current message */
	public void update() {
		if(type != NOTHING){
			switch (type) {
				case WELCOME1:
					if( time + 3 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						createMessageBox(WELCOME2);
					}
					break;
					
				case WELCOME2:
					if( time + 5 <= GameTimer.getTimeInSeconds() ){
						quad.removeFromParent();
						createMessageBox(WELCOME3);
					}
					break;
				case WELCOME3:
					if( time + 5 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						createMessageBox(WELCOME4);
					}
					break;
				case WELCOME4:
					if( time + 5 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
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
                case WELCOME1:path = "game/data/message/welcome1.jpg";break;//WELCOME
                case WELCOME2:path = "game/data/message/welcome2.jpg";break;//WELCOME
                case WELCOME3:path = "game/data/message/welcome3.jpg";break;//WELCOME
                case WELCOME4:path = "game/data/message/welcome4.jpg";break;//WELCOME
                case LEVEL2:path = "game/data/message/level2.jpg";break;//WELCOME
				case DIE:path = "game/data/message/energy0.jpg";break;//DIE
				case GAMEOVER:path = "game/data/message/gameOver.jpg";break;//GAMEOVER
            }
            time = GameTimer.getTimeInSeconds();
            /** add a texture */
            TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
            ts.setTexture( TextureManager.loadTexture( Loader.load( path ) , true ) );
		    ts.setEnabled(true);
		    quad.setRenderState(ts);
		    quad.updateRenderState();
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