package game.HUD;

import game.common.GameTimer;
import utils.Loader;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Class HudMessageBox
 * This class is used for manage and show big message on screen
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudMessageBox {

	/** Standard messages */
	public static final int WELCOME1 = 0;
	public static final int WELCOME2 = 1;
	public static final int WELCOME3 = 2;
	public static final int WELCOME4 = 3;
	public static final int LEVEL2 = 4;	
	public static final int DIE = 5;
	public static final int GAMEOVER = 6;
	public static final int VICTORY = 7;
	public static final int NOTHING = 8;
	
	/** Type of current message */
	int type;
	
	/** A quad that contains a message */
	Quad quad;
	
	/** Ponter used for get info */
	UserHud userHud;
	
	/** life time of current message */
	float time = 0;
	
	/** useful variable */
	boolean usedLevel2 = false;
	
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
		// if current type isn't nothing
		if(type != NOTHING){
			switch (type) {
			
				case WELCOME1:
					// first tutorial message
					if( time + 10 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						// switch to other message
						createMessageBox(WELCOME2);
					}
					break;
					
				case WELCOME2:
					// second tutorial message
					if( time + 10 <= GameTimer.getTimeInSeconds() ){
						quad.removeFromParent();
						// switch to other message
						createMessageBox(WELCOME3);
					}
					break;
					
				case WELCOME3:
					// third tutorial message
					if( time + 10 <= GameTimer.getTimeInSeconds() ) {
						quad.removeFromParent();
						// switch to other message
						createMessageBox(WELCOME4);
					}
					break;
					
				case WELCOME4:
					// fourth tutorial message
					if( time + 10 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
						// tutorial messages finished
					break;
					
				case DIE:
					// die message - the player reborn
					if( time + 5 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
					break;
					
				case LEVEL2:
					// switch to second level for the first time
					if( time + 5 <= GameTimer.getTimeInSeconds() )
						quad.removeFromParent();
					break;
					
				case VICTORY:
					// victory message
					if( time + 10 <= GameTimer.getTimeInSeconds() )
						userHud.gWorld.finish();
					break;
					
				case GAMEOVER:
					// game over message
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
		// check if level2 message was used
		if( type == LEVEL2 && usedLevel2 )
			return;
		// check if it would create a level2 message
		if( type == LEVEL2 )
			usedLevel2 = true;
        this.type = type;
        // create a quad that contains request message
        quad = new Quad("messageBox", userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y/3);
        // quad moved
        quad.setLocalTranslation(userHud.gWorld.getResolution().x/2, userHud.gWorld.getResolution().y*3/5, 0);
        userHud.gWorld.hudNode.attachChild(quad);
        // select file image of request message
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
        // get current time
        time = GameTimer.getTimeInSeconds();
        /** add a texture */
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setTexture( TextureManager.loadTexture( Loader.load( path ) , true ) );
	    ts.setEnabled(true);
	    quad.setRenderState(ts);
	    quad.updateRenderState();
    }
	
    /**
     * Switch the current message to next (Used only for initial tutorial)
     */
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