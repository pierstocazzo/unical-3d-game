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

import game.common.GameConfiguration;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudLife
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudLife {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Life Value of Player */
	int lifeValue;
	
	/** Little text on the life bar */
	Text lifeNum;
	
	/** Background Quad of life Bar */
	Quad backQuad;
	
	/** Front Quad of life Bar */
	Quad frontQuad;
	
	/** Screen width */
	float screenWidth;
	
	/** Screen height */
	float screenHeight;
	
	/** Border bar parameter */
	int borderWeight = 3;
	
	/** Bar standard Lenght in pixel */
	int initialLenght;
	
	/** Constructor
	 * 
	 * @param (UserHud) - userHud
	 */
	public HudLife(UserHud userHud){
		this.userHud = userHud;
		// get screen informations
		screenWidth = userHud.world.getResolution().x;
    	screenHeight = userHud.world.getResolution().y;
    	
		createBar();

		// create a text label
    	lifeNum = Text.createDefaultTextLabel( "lifeNum" );
    	lifeNum.setTextColor(ColorRGBA.black);
    	lifeNum.setLocalScale(screenWidth/1100);
    	lifeNum.setLocalTranslation( backQuad.getLocalTranslation().x, 
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.hudNode.attachChild( lifeNum );
    	
    	//for first correct visualization
    	lifeValue = 100;
    	checkColor();
		frontQuad.resize( initialLenght, frontQuad.getHeight() );
		frontQuad.setLocalTranslation( screenWidth/40 + frontQuad.getWidth()/2 + borderWeight,
										backQuad.getLocalTranslation().y, 0 );
		lifeNum.print( GameConfiguration.getPhrase( "hud_life" ) + " " + Integer.toString(lifeValue) );
		lifeNum.setLocalTranslation( backQuad.getLocalTranslation().x - backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y - lifeNum.getHeight()/2, 0);
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){
		// get current life value
		lifeValue = userHud.game.getCurrentLife(userHud.world.player.id);
		checkColor();
		// resize frontQuad respect life value
		frontQuad.resize( initialLenght*lifeValue/userHud.game.getMaxLife(userHud.world.player.id), 
						frontQuad.getHeight() );
		frontQuad.setLocalTranslation( screenWidth/40 + frontQuad.getWidth()/2 + borderWeight,
										backQuad.getLocalTranslation().y, 0 );
		lifeNum.print( GameConfiguration.getPhrase( "hud_life" ) + " " + Integer.toString(lifeValue) );
		lifeNum.setLocalTranslation( backQuad.getLocalTranslation().x - backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y - lifeNum.getHeight()/2, 0);
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		// create back quad
		backQuad = new Quad( "backQuad", screenWidth/6 , screenHeight/30);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(screenWidth/40+backQuad.getWidth()/2,
    								screenHeight/40+backQuad.getHeight()/2, 0);
    	backQuad.lock();
    	userHud.hudNode.attachChild( backQuad );
    	
    	// create front quad
    	frontQuad = new Quad("frontQuad", backQuad.getWidth() - borderWeight*2 , 
    										backQuad.getHeight() - borderWeight*2);
    	initialLenght = (int) frontQuad.getWidth();
    	frontQuad.setDefaultColor(ColorRGBA.green);
    	frontQuad.setLocalTranslation(backQuad.getLocalTranslation().x,
    			backQuad.getLocalTranslation().y, 0);
    	userHud.hudNode.attachChild( frontQuad );
	}
	
	/**
	 * It checks value life and change quad color
	 */
	public void checkColor(){
		if(lifeValue > 60)
			frontQuad.setDefaultColor(ColorRGBA.green);
		else if(lifeValue > 25)
			frontQuad.setDefaultColor(ColorRGBA.yellow);
		else 
			frontQuad.setDefaultColor(ColorRGBA.red);
	}
}
