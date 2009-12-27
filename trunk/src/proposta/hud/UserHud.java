package proposta.hud;

import java.util.Random;

import proposta.graphics.GraphicalWorld;
import proposta.graphics.WorldInterface;

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
	 * @param gWorld - (GraphicalWorld)
	 */
	public UserHud(GraphicalWorld gWorld){
		this.gWorld = gWorld;
		this.game = gWorld.getCore();
		this.hudNode = gWorld.hudNode;
		
		oldTicks = 0;
		hudScore = new HudScore(this);
		hudLife = new HudLife(this);
		
		level = Text.createDefaultTextLabel( "Level" );
    	level.setTextColor(ColorRGBA.blue);
    	level.setLocalTranslation( hudLife.life.getLocalTranslation().x, 
    			hudLife.life.getLocalTranslation().y + hudLife.life.getHeight(), 0 );
    	gWorld.getRootNode().attachChild(level);
	}
	
	/**
	 * Update user hud
	 */
	public void update(){
		if(oldTicks + 500 <= (int)gWorld.timer.getTime())
		{
			oldTicks = (int) gWorld.timer.getTime();
			Random r = new Random();
			level.print("Level: "+Integer.toString(r.nextInt()));
			hudScore.update();
			hudLife.update();
		}
	}
}
