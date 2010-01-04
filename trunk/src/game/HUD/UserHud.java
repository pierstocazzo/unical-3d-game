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
	
	Text fps;
	
	HudAlert hudAlert;
	
	HudAmmo hudAmmo;
	
	public HudMessageHandler hudMsg;

	public HudMessageBox hudMsgBox;
	
	/** 
	 * Control
	 * 
	 * @param graphicalWorld - (GraphicalWorld)
	 */
	public UserHud(GraphicalWorld graphicalWorld){
		this.gWorld = graphicalWorld;
		this.game = graphicalWorld.getCore();
		this.hudNode = graphicalWorld.hudNode;
		
		oldTicks = 0;
		hudScore = new HudScore(this);
		hudLife = new HudLife(this);
		hudAmmo = new HudAmmo(this);
		map = new WorldMap2D(graphicalWorld);
		
		level = Text.createDefaultTextLabel( "Level" );
    	level.setTextColor(ColorRGBA.black);
    	level.setLocalScale(1.2f);
    	level.setLocalTranslation( hudAmmo.backQuad.getLocalTranslation().x - hudAmmo.backQuad.getWidth()/2, 
    			hudAmmo.backQuad.getLocalTranslation().y + hudLife.backQuad.getHeight()/2 + 3, 0 );
    	graphicalWorld.getRootNode().attachChild(level);
    	
    	fps = Text.createDefaultTextLabel( "FPS" );
    	fps.setLocalTranslation( gWorld.getResolution().x - 200, gWorld.getResolution().y - 40, 0 );
    	gWorld.getRootNode().attachChild( fps );
    	hudAlert = new HudAlert(this);
    	hudMsg = new HudMessageHandler(this);
    	hudMsgBox = new HudMessageBox(HudMessageBox.WELCOME, this);
    }
	
	/**
	 * Update user hud
	 */
	public void update(){
		if(oldTicks + 500 <= (int)GameTimer.getTime())
		{
			oldTicks = (int) GameTimer.getTime();
			int value = gWorld.getCore().getLevel(gWorld.player.id);
			level.print("Level: "+Integer.toString(value));
			hudScore.update();
			hudLife.update();
			hudAlert.update();
			map.update();
		}
		fps.print( "Frame Rate: " + (int) GameTimer.getFrameRate() + "fps" );
    	hudAmmo.update();
    	hudMsg.update();
    	hudMsgBox.update();
	}
}
