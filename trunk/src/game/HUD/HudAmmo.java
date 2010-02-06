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

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudAmmo
 * <br>
 * It updates the Ammo Counter 
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudAmmo {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Life Value of Player */
	int ammoValue;
	
	/** Little text on the life bar */
	Text ammoNum;
	
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
	public HudAmmo(UserHud userHud){
		this.userHud = userHud;
		// get screen informations
		screenWidth = userHud.world.getResolution().x;
    	screenHeight = userHud.world.getResolution().y;
    	
		createBar();

    	ammoNum = Text.createDefaultTextLabel( "ammoNum" );
    	ammoNum.setTextColor(ColorRGBA.black);
    	ammoNum.setLocalScale(screenWidth/1100);
    	ammoNum.setLocalTranslation( backQuad.getLocalTranslation().x, 
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.hudNode.attachChild( ammoNum );
    	//initialize for first correct visualization
    	ammoValue = 0;
		frontQuad.resize( initialLenght*ammoValue/100, frontQuad.getHeight() );
		frontQuad.setLocalTranslation( screenWidth/40 + frontQuad.getWidth()/2 + borderWeight,
										backQuad.getLocalTranslation().y, 0 );
		ammoNum.print( GameConf.getPhrase( GameConf.HUD_AMMO ) + " " + Integer.toString(ammoValue) );
		ammoNum.setLocalTranslation(backQuad.getLocalTranslation().x - backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y - ammoNum.getHeight()/2, 0 );
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){
		// get ammo value
		ammoValue = userHud.game.getAmmo(userHud.world.player.id);
		// resize front quad respect ammo value and max ammo value
		frontQuad.resize(initialLenght*ammoValue/userHud.game.getMaxAmmo(userHud.world.player.id), 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										backQuad.getLocalTranslation().y, 0);
		// print current ammo value
		ammoNum.print( GameConf.getPhrase( GameConf.HUD_AMMO ) + " " + Integer.toString(ammoValue) );
		ammoNum.setLocalTranslation(backQuad.getLocalTranslation().x-backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y-ammoNum.getHeight()/2, 0);
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		// create a back quad
		backQuad = new Quad( "backQuad", screenWidth/6 , screenHeight/30 );
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation( screenWidth/40 + backQuad.getWidth()/2,
    			userHud.hudLife.backQuad.getHeight()+userHud.hudLife.backQuad.getHeight()+10, 0);
    	backQuad.lock();
    	userHud.hudNode.attachChild( backQuad );
    	
    	// create a front quad
    	frontQuad = new Quad( "frontQuad", backQuad.getWidth() - borderWeight*2 , 
    										backQuad.getHeight() - borderWeight*2 );
    	initialLenght = (int) frontQuad.getWidth();
    	frontQuad.setDefaultColor(ColorRGBA.green);
    	frontQuad.setLocalTranslation( backQuad.getLocalTranslation().x,
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.hudNode.attachChild( frontQuad );
	}
}