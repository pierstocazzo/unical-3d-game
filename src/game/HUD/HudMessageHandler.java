/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.HUD;

import game.common.GameConf;
import game.common.GameTimer;

import java.util.ArrayList;

import com.jme.renderer.ColorRGBA;

/**
 * Class HudMessageHandler
 * <br>
 * It manage and display all the messages in the game
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
		String text = GameConf.getPhrase( GameConf.HUD_NO_MESSAGE );
		switch (type) {
			case MAX_AMMO:
				text = GameConf.getPhrase( GameConf.HUD_MAX_AMMO );
				break;
			case MAX_ENERGY:
				text = GameConf.getPhrase( GameConf.HUD_MAX_ENERGY );
				break;
			case AMMO_FINISHED:
				text = GameConf.getPhrase( GameConf.HUD_AMMO_FINISHED );
				break;
			case NEW_LEVEL:
				int level = userHud.world.getCore().getLevel(userHud.world.player.id);
				int maxEnergy = userHud.world.getCore().getMaxLife(userHud.world.player.id);
				text = GameConf.getPhrase( GameConf.HUD_NEW_LEVEL_1 ) + " " + level + " "
					+ GameConf.getPhrase( GameConf.HUD_NEW_LEVEL_2 ) + " " + maxEnergy + " "
					+ GameConf.getPhrase( GameConf.HUD_NEW_LEVEL_3 );
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