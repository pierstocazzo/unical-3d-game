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
	public static final int VICTORY = 7;
	public static final int NOTHING = 8;
	
	int type;
	Quad quad;
	UserHud userHud;
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
					if( time + 10 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						createMessageBox(WELCOME2);
					}
					break;
					
				case WELCOME2:
					if( time + 10 <= GameTimer.getTimeInSeconds() ){
						quad.removeFromParent();
						createMessageBox(WELCOME3);
					}
					break;
				case WELCOME3:
					if( time + 10 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						createMessageBox(WELCOME4);
					}
					break;
				case WELCOME4:
					if( time + 10 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
					break;
				case DIE:
					if( time + 5 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
					break;
				case LEVEL2:
					if( time + 5 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
					break;
				case VICTORY:
					if( time + 10 <= GameTimer.getTimeInSeconds() )
						userHud.gWorld.finish();
					break;
				case GAMEOVER:
					if( time + 3 <= GameTimer.getTimeInSeconds() )
						userHud.gWorld.finish();
					break;
			}
		}
		
		if(userHud.gWorld.getCore().isReborn(userHud.gWorld.player.id)){
			userHud.gWorld.getCore().setReborn(userHud.gWorld.player.id, false);
			createMessageBox(DIE);
		}
		else if(userHud.gWorld.getCore().showLevel2Message(userHud.gWorld.player.id)){
			userHud.gWorld.getCore().setShowLevel2Message(userHud.gWorld.player.id,false);
			createMessageBox(LEVEL2);
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
                case WELCOME1:path = "game/data/message/welcome1.jpg";break;//WELCOME1
                case WELCOME2:path = "game/data/message/welcome2.jpg";break;//WELCOME2
                case WELCOME3:path = "game/data/message/welcome3.jpg";break;//WELCOME3
                case WELCOME4:path = "game/data/message/welcome4.jpg";break;//WELCOME4
                case LEVEL2:path = "game/data/message/Level2.jpg";break;//WELCOME
				case DIE:path = "game/data/message/energy0.jpg";break;//DIE
				case VICTORY:path = "game/data/message/end.jpg";break;//DIE
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
	
    public void switchToNext(){
    	if(type!=NOTHING && type!=GAMEOVER && type!=VICTORY)
    		quad.removeFromParent();
    	switch (type) {
    		case WELCOME1: createMessageBox(WELCOME2);break;
    		case WELCOME2: createMessageBox(WELCOME3);break;
    		case WELCOME3: createMessageBox(WELCOME4);break;
    		case WELCOME4: type = NOTHING;break;
    		case DIE: type = NOTHING;break;
    		case LEVEL2: type = NOTHING;break;
    	}
    }
}