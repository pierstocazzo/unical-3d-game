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

import game.common.GameTimer;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;

/**
 * Class HudMessage
 * <br>
 * Contains a single message that can be displayed in the game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudMessage {
	
	/** message text label */
	Text message;
	
	/** String message */
	String messageString;
	
	/** time a creation */
	float time;
	
	/** check variable */
	public boolean enabled = true;
	
	/** message life time */
	int seconds;
	
	/** useful pointer */
	UserHud userHud;
	
	/** Position x of label */
	int x = 0;
	
	/** position y of label */
	int y = 0;
	
	/**
	 * Constructor of Class HudMessage
	 * 
	 * @param (String) messageString - text displayed
	 * @param (int) seconds - message life time
	 * @param (ColorRGBA) color - message color
	 * @param (UserHud) userHud - used for get data
	 */
	public HudMessage(String messageString, int seconds, ColorRGBA color, UserHud userHud){
		// create a text label
		message = Text.createDefaultTextLabel(messageString);
		message.setTextColor(color);
		message.setLocalScale(userHud.world.getResolution().x/1200);
		message.lock();
		userHud.hudNode.attachChild(message);
		
		// initialize time
		time = GameTimer.getTimeInSeconds();
		this.messageString = messageString;
		this.seconds = seconds;
		this.userHud = userHud;
	}
	
	/**
	 * Update message and check its life time.
	 * 
	 * @param (int) x - New x position of text label
	 * @param (int) y - new y position of text label
	 */
	public void update(int x, int y){
		// check life time
		if(GameTimer.getTimeInSeconds() > time + seconds){
			// disable current message
			enabled = false;
			messageString = "";
			message.print(messageString);
		}
		// set new position
		this.x = x;
		this.y = (int) (y - message.getHeight()/2);
	}
	
	/**
	 * Show the message after check if it is enabled
	 */
	public void show(){
		if(enabled){
			message.setLocalTranslation(x,y,0);
			message.print(messageString);
		}
	}
	
	/** 
	 * Get message height 
	 * 
	 * @return (float) - message height
	 */
	public float getHeight(){
		return message.getHeight();
	}
}
