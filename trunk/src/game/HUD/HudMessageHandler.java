package game.HUD;

import java.util.ArrayList;

import com.jme.renderer.ColorRGBA;

/**
 * Class HudScore
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudMessageHandler {
	
	public static final int MAX_AMMO = 0;
	public static final int MAX_ENERGY = 1;
	public static final int AMMO_FINISHED = 2;
	public static final int NEW_LEVEL = 3;
	
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
		this.userHud = userHud;
		weight = (int) userHud.gWorld.getResolution().x ;
		height = (int) userHud.gWorld.getResolution().y;
		
		messageList = new ArrayList<HudMessage>();
	}
	
	/**
	 * Add a new message
	 * 
	 * @param text (String)
	 * @param seconds (int)
	 * @param color (ColorRGBA)
	 */
	public void addMessage(int type, int seconds, ColorRGBA color){
		String text = "Missing Message";
		switch (type) {
			case MAX_AMMO:text="Non puoi prendere ulteriori munizioni.";break;
			case MAX_ENERGY:text="Non puoi acquisire ulteriore energia.";break;
			case AMMO_FINISHED:text="Hai finito tutte le munizioni, cercane negli avanposti nemici che hai ripulito.";break;
			case NEW_LEVEL:
				int level = userHud.gWorld.getCore().getLevel(userHud.gWorld.player.id);
				int maxEnergy = userHud.gWorld.getCore().getMaxLife(userHud.gWorld.player.id);
				int maxAmmo = userHud.gWorld.getCore().getMaxAmmo(userHud.gWorld.player.id);
				text="Sei passato al livello "+level+", la tua energia massima è "+
					maxEnergy+" e potrai portare fino a "+maxAmmo+" munizioni.";
				break;
		}
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
