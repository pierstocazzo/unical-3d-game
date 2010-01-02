package game.HUD;

import java.awt.Font;
import java.util.ArrayList;

import utils.Loader;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.font2d.Text2D;
import com.jmex.font3d.Font3D;
import com.jmex.font3d.Text3D;

/**
 * Class HudScore
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudMessageHandler {
	
	/** User Hud */
	UserHud userHud;
	/** useful variable */
	int weight;
	/** useful variable */
	int height;
	
	ArrayList<HudMessage> messageList;
	
	HudMessage msg;
	
	/**
	 * Constructor
	 * 
	 * @param userHud - (UserHud)
	 */
	public HudMessageHandler(UserHud userHud){
		//inizializza valore score
		this.userHud = userHud;
		weight = (int) userHud.gWorld.getResolution().x ;
		height = (int) userHud.gWorld.getResolution().y;
		
		messageList = new ArrayList<HudMessage>();
		
		addMessage("Welcome to Game!", 3, ColorRGBA.randomColor());
		addMessage("An enemy says: I splat you!", 5, ColorRGBA.randomColor());
		addMessage("For survive, we have to kill all enemy...", 7, ColorRGBA.randomColor());
		addMessage("Remember: If you die, you lose... Bye bye", 9, ColorRGBA.randomColor());
	}
	
	public void addMessage(String text, int seconds, ColorRGBA color){
		HudMessage msg = new HudMessage(text, seconds, color, userHud);
		messageList.add(msg);
	}
	
	/**
	 * Update Messages
	 */
	public void update(){
		for( int i = 0; i < messageList.size(); i++){
			HudMessage msg = messageList.get(i);
			msg.update(weight/20,(int) (height*3/4 - i*msg.getHeight() * 2) );
			if(!msg.enabled)
				messageList.remove(i);
			else
				messageList.get(i).show();
		}
	}
}
