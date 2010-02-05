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
import game.common.State;
import game.sound.SoundManager;
import game.sound.SoundManager.SoundType;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudAlert
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudAlert {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Little text on the alert bar */
	Text alertLabel;
	
	/** Max current alert value */
	float alertValue;
	
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
	
	/** Bar standard Length in pixel */
	int initialLenght;
	
	/** Useful variable */
	State stateColor;
	
	final int ALERT_RANGE = Integer.valueOf( GameConf.getParameter( GameConf.MAX_ALERT_TIME ) );
	
	/** Constructor
	 * 
	 * @param (UserHud) - userHud
	 */
	public HudAlert(UserHud userHud){
		this.userHud = userHud;
		// get screen informations
		screenWidth = userHud.world.getResolution().x;
    	screenHeight = userHud.world.getResolution().y;
    	
		createBar();
		
		// create a new label
    	alertLabel = Text.createDefaultTextLabel( "alertLabel" );
    	alertLabel.setTextColor(ColorRGBA.black);
    	alertLabel.setLocalScale(screenWidth/1200);
    	alertLabel.setLocalTranslation( backQuad.getLocalTranslation().x, 
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.hudNode.attachChild( alertLabel );
    	
    	//initialize for correct first visualization
    	alertValue = 0;
    	frontQuad.resize( 0, frontQuad.getHeight() );
		frontQuad.setLocalTranslation( screenWidth/40 + frontQuad.getWidth()/2 + borderWeight,
										frontQuad.getLocalTranslation().y, 0 );
		alertLabel.setLocalTranslation( backQuad.getLocalTranslation().x - backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y + backQuad.getHeight()/2, 0);
		alertLabel.print("Alert Level");
   	}
	
	/**
	 * It updates Life bar informations.
	 * Alert bar color is variable: 
	 * 		YELLOW if an enemy is in ALERT or FIND state
	 * 		RED if an enemy is in ATTACK, FINDATTACK or GUARDATTACK state
	 */
	public void update(){
		//initialize variable for color management
		stateColor = State.DEFAULT;
		alertValue = getAlertLevel();
		//if an enemy is in alert state
		if( stateColor == State.ALERT ){
			frontQuad.setDefaultColor(ColorRGBA.yellow);
			if( userHud.world.isAudioEnabled() )
				SoundManager.playSound( SoundType.ALERTMUSIC, null );
		}
		//if an enemy is in attack state
		if( stateColor == State.ATTACK ){
			frontQuad.setDefaultColor(ColorRGBA.red);
			if(userHud.world.isAudioEnabled())
				SoundManager.playSound( SoundType.ATTACKMUSIC, null );
		}
		//if all enemy are in default state
		if( stateColor == State.DEFAULT )
			checkColor();
		frontQuad.resize( initialLenght*alertValue/100, frontQuad.getHeight() );
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										frontQuad.getLocalTranslation().y, 0);
	}
	
	public float getAlertLevel(){
		stateColor = State.DEFAULT;
		//calculate max alert level
		float max = 0;
//		System.out.println("Ciclo sui nemici");
		for( String id : userHud.game.getEnemiesIds() ){
//			System.out.println("entro ciclo "+id);
			State currState = userHud.game.getState(id);
			if(userHud.game.getAlertLevel(id) > max )
				max = userHud.game.getAlertLevel(id);
			if(currState == State.GUARD)
				max = 0;
			//check if current enemy is in attack
			if(currState == State.ATTACK || currState == State.SEARCHATTACK || currState == State.GUARDATTACK)
				stateColor = State.ATTACK;
			//check if current enemy is in alert e nobody is in attack
			if((currState == State.ALERT || currState == State.SEARCH)
					&& stateColor != State.ATTACK)
				stateColor = State.ALERT;
		}
//		System.out.println("Massimo = "+max);
		//check difference
		return max*100/ALERT_RANGE;
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		//create back quad
		backQuad = new Quad("backQuad", screenWidth/6 , screenHeight/30);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(screenWidth/40+backQuad.getWidth()/2,
    			screenHeight-screenHeight/20-backQuad.getHeight()/2, 0);
    	backQuad.lock();
    	userHud.hudNode.attachChild( backQuad );
    	
    	//create front quad
    	frontQuad = new Quad("frontQuad", backQuad.getWidth() - borderWeight*2 , 
    										backQuad.getHeight() - borderWeight*2 );
    	initialLenght = (int) frontQuad.getWidth();
    	frontQuad.setDefaultColor( ColorRGBA.green );
    	frontQuad.setLocalTranslation( backQuad.getLocalTranslation().x,
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.hudNode.attachChild( frontQuad );
    	
	}
	
	/**
	 * It checks value life and change quad color
	 */
	public void checkColor(){
		if( alertValue > 80 )
			frontQuad.setDefaultColor(ColorRGBA.red);
		else if( alertValue > 20 )
			frontQuad.setDefaultColor(ColorRGBA.yellow);
		else 
			frontQuad.setDefaultColor(ColorRGBA.green);
	}
}
