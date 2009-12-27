package game.HUD;

import game.common.GameTimer;
import game.graphics.GraphicalWorld;
import game.graphics.WorldInterface;

import java.util.Random;

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
	
	Text level;
	
	/** variable used for controlled update */
	int oldTicks;

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
		
		level = Text.createDefaultTextLabel( "Level" );
    	level.setTextColor(ColorRGBA.blue);
    	level.setLocalTranslation( hudLife.life.getLocalTranslation().x, 
    			hudLife.life.getLocalTranslation().y + hudLife.life.getHeight(), 0 );
    	graphicalWorld.getRootNode().attachChild(level);
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
		}
	}
}
