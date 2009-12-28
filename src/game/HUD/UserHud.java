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
	
	Text ammo;

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
		map = new WorldMap2D(graphicalWorld);
		
		level = Text.createDefaultTextLabel( "Level" );
    	level.setTextColor(ColorRGBA.blue);
    	level.setLocalTranslation( hudLife.life.getLocalTranslation().x, 
    			hudLife.life.getLocalTranslation().y + hudLife.life.getHeight(), 0 );
    	graphicalWorld.getRootNode().attachChild(level);
    	
    	fps = Text.createDefaultTextLabel( "FPS" );
    	fps.setLocalTranslation( gWorld.getResolution().x - 200, gWorld.getResolution().y - 40, 0 );
    	gWorld.getRootNode().attachChild( fps );
    	
    	ammo = Text.createDefaultTextLabel( "ammo" );
    	ammo.setLocalTranslation( gWorld.getResolution().x - 200, gWorld.getResolution().y - 60, 0 );
    	gWorld.getRootNode().attachChild( ammo );
    	
	}
	
	/**
	 * Update user hud
	 */
	public void update(){
		if(oldTicks + 1000 <= (int)GameTimer.getTime())
		{
			oldTicks = (int) GameTimer.getTime();
			int value = gWorld.getCore().getLevel(gWorld.player.id);
			level.print("Level: "+Integer.toString(value));
	    	fps.print( "Frame Rate: " + (int) GameTimer.getFrameRate() + "fps" );
	    	ammo.print( "Ammunitions: " + gWorld.getCore().getAmmo( gWorld.player.id ) );
			hudScore.update();
			hudLife.update();
			map.update();
		}
	}
}
