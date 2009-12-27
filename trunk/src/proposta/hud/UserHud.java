package proposta.hud;

import proposta.graphics.GraphicalWorld;
import proposta.graphics.WorldInterface;

import com.jme.scene.Node;

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
	}
	
	/**
	 * Update user hud
	 */
	public void update(){
		if(oldTicks + 500 <= (int)gWorld.timer.getTime())
		{
			oldTicks = (int) gWorld.timer.getTime();
			hudScore.update();
			hudLife.update();
		}
	}
}
