package game.HUD;

import game.common.GameTimer;
import game.graphics.GraphicalWorld;
import game.graphics.WorldInterface;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;

/**
 * Class UserHud
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class UserHud {
	
	/** Useful pointer */
	WorldInterface game;
	
	/** main Hud Node */
	Node hudNode;
	
	/** World pointer */
	GraphicalWorld gWorld;
	
	/** HudScore pointer */
	HudScore hudScore;
	
	/** HudLife pointer */
	HudLife hudLife;
	
	/** World Map 2d */
	WorldMap2D map;
	
	/** Level Label */
	Text level;
	
	/** variable used for controlled update */
	int oldTicks;
	
	/** Frame Rate Label */
	Text fps;
	
	/** Alert bar */
	HudAlert hudAlert;
	
	/** Ammmo bar */
	HudAmmo hudAmmo;
	
	/** small message handler */
	public static HudMessageHandler hudMsg;

	/** big message handler */
	public HudMessageBox hudMsgBox;
	
	/** 
	 * Constructor
	 * 
	 * @param graphicalWorld - (GraphicalWorld)
	 */
	public UserHud(GraphicalWorld graphicalWorld){
		this.gWorld = graphicalWorld;
		this.game = graphicalWorld.getCore();
		this.hudNode = graphicalWorld.hudNode;
		
		// initialize time variable and other objects
		oldTicks = 0;
		hudScore = new HudScore(this);
		hudLife = new HudLife(this);
		hudAmmo = new HudAmmo(this);
		map = new WorldMap2D(graphicalWorld);
		
		// create text label
		level = Text.createDefaultTextLabel( "Level" );
    	level.setTextColor(ColorRGBA.black);
    	level.setLocalScale(1.2f);
    	level.setLocalTranslation( hudAmmo.backQuad.getLocalTranslation().x - hudAmmo.backQuad.getWidth()/2, 
    			hudAmmo.backQuad.getLocalTranslation().y + hudLife.backQuad.getHeight()/2 + 3, 0 );
    	graphicalWorld.getRootNode().attachChild(level);
    	
    	// create text label
    	fps = Text.createDefaultTextLabel( "FPS" );
    	fps.setLocalTranslation( gWorld.getResolution().x - 200, gWorld.getResolution().y - 40, 0 );
    	gWorld.getRootNode().attachChild( fps );
    	
    	// create alert bar and two message handler
    	hudAlert = new HudAlert(this);
    	hudMsg = new HudMessageHandler(this);
    	hudMsgBox = new HudMessageBox(HudMessageBox.WELCOME1, this);
    }
	
	/**
	 * Update user hud
	 */
	public void update(){
		// some elements are updated every 500 ticks
		if(oldTicks + 500 <= (int)GameTimer.getTime())
		{
			oldTicks = (int) GameTimer.getTime();
			int value = gWorld.getCore().getLevel(gWorld.player.id);
			level.print("Level: "+Integer.toString(value));
			hudScore.update();
			hudLife.update();
			map.update();
		} 
		// the follows elements are updated ever 
		fps.print( "Frame Rate: " + (int) GameTimer.getFrameRate() + "fps" );
    	hudAmmo.update();
    	hudAlert.update();
    	hudMsg.update();
    	hudMsgBox.update();
	}
	
	/**
	 * Add a new small message
	 * 
	 * @param (int) type - message type
	 */
	public static void addMessage( int type ) {
		hudMsg.addMessage( type, 5, ColorRGBA.blue );
	}
}
