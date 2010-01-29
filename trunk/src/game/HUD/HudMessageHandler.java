package game.HUD;

import game.common.GameConfiguration;
import game.common.GameTimer;

import java.util.ArrayList;

import com.jme.renderer.ColorRGBA;

/**
 * Class HudMessageHandler
 * 
 * Small Message Handler
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudMessageHandler {
	
	/** Standard messages with their times */
	public static final int MAX_AMMO = 0;
	float time_max_ammo = 0;
	public static final int MAX_ENERGY = 1;
	float time_max_energy = 0;
	public static final int AMMO_FINISHED = 2;
	float time_ammo_finished = 0;
	public static final int NEW_LEVEL = 3;
	float time_new_level = 0;
	
	/** User Hud */
	UserHud userHud;
	
	/** useful variable */
	int weight;
	
	/** useful variable */
	int height;
	
	/** Messages list */
	ArrayList<HudMessage> messageList;
	
	/** Current message */
	HudMessage msg;
	
	/** initial level */
	private int oldLevel = 1;
	
	/**
	 * Constructor
	 * 
	 * @param userHud - (UserHud)
	 */
	public HudMessageHandler(UserHud userHud){
		this.userHud = userHud;
		// get and save screen informations
		weight = (int) userHud.world.getResolution().x ;
		height = (int) userHud.world.getResolution().y;
		
		messageList = new ArrayList<HudMessage>();
	}
	
	/**
	 * Add a new message
	 * 
	 * @param text (String) - message text
	 * @param seconds (int) - life time
	 * @param color (ColorRGBA) - message color
	 */
	public void addMessage(int type, int seconds, ColorRGBA color){
		//check time
		switch (type) {
		
			case MAX_AMMO:
				if(time_max_ammo + 2 >= GameTimer.getTimeInSeconds())
					return;
				time_max_ammo = GameTimer.getTimeInSeconds();
				break;
				
			case MAX_ENERGY:
				if(time_max_energy + 2 >= GameTimer.getTimeInSeconds())
					return;
				time_max_energy = GameTimer.getTimeInSeconds();
				break;
				
			case AMMO_FINISHED:
				if(time_ammo_finished + 2 >= GameTimer.getTimeInSeconds())
					return;
				time_ammo_finished = GameTimer.getTimeInSeconds();
				break;
				
			case NEW_LEVEL:
				break;
		}
		
		// get string associated
		String text = GameConfiguration.getPhrase( "hud_no_message" );
		switch (type) {
			case MAX_AMMO:
				text = GameConfiguration.getPhrase( "hud_max_ammo" );
				break;
			case MAX_ENERGY:
				text = GameConfiguration.getPhrase( "hud_max_energy" );
				break;
			case AMMO_FINISHED:
				text = GameConfiguration.getPhrase( "hud_ammo_finished" );
				break;
			case NEW_LEVEL:
				int level = userHud.world.getCore().getLevel(userHud.world.player.id);
				int maxEnergy = userHud.world.getCore().getMaxLife(userHud.world.player.id);
				int maxAmmo = userHud.world.getCore().getMaxAmmo(userHud.world.player.id);
				text = GameConfiguration.getPhrase( "hud_new_level_1" ) + " " + level + " "
					+ GameConfiguration.getPhrase( "hud_new_level_2" ) + " " + maxEnergy + " "
					+ GameConfiguration.getPhrase( "hud_new_level_3" ) + " " + maxAmmo + " "
				    + GameConfiguration.getPhrase( "hud_new_level_4" );
				break;
		}
		
		HudMessage msg = new HudMessage(text, seconds, color, userHud);
		messageList.add(msg);
	}
	
	/**
	 * Update Messages list
	 */
	public void update(){
		for( int i = 0; i < messageList.size(); i++){
			// save temporary message
			HudMessage msg = messageList.get(i);
			// update message with its new position
			msg.update(weight/20,(int) (height*3/4 - i*msg.getHeight() * 2) );
			// if it is disabled clean it else show it
			if(!msg.enabled)
				messageList.remove(i);
			else
				messageList.get(i).show();
		}
		
		checkLevel();
	}
	
	/** 
	 * Check current level
	 */
	public void checkLevel(){
		// get level player
		int currLevel = userHud.world.getCore().getLevel(userHud.world.player.id);
		if( currLevel != oldLevel )
			addMessage( NEW_LEVEL, 3, ColorRGBA.blue );
		oldLevel = currLevel;
	}
}