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

import utils.Loader;
import game.common.GameConf;
import game.common.GameTimer;
import game.graphics.GraphicalWorld;
import game.graphics.WorldInterface;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.font2d.Text2D;

/**
 * Class UserHud
 * <br>
 * Main UserHUD 
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class UserHud {
	
	/** Useful pointer */
	WorldInterface game;
	
	/** main Hud Node */
	public Node hudNode;
	
	/** World pointer */
	GraphicalWorld world;
	
	/** HudScore pointer */
	HudScore hudScore;
	
	/** HudLife pointer */
	HudLife hudLife;
	
	/** World Map 2d */
	WorldMap2D map;
	
	/** Level Label */
	Text level;
	
	/** Current weapon */
	Text weapon;
	
	/** variable used for controlled update */
	int oldTicks;
	
	/** Frame Rate Label */
	Text fps;
	
	/** Alert bar */
	HudAlert hudAlert;
	
	/** Ammmo bar */
	HudAmmo hudAmmo;
	
	/** small message handler */
	public static HudMessageHandler hudMsg;

	/** big message handler */
	public HudMessageBox hudMsgBox;

	/** crosshair */
	Quad crosshair;
	
	/** pause */
	Text pauseText;
	
	/** 
	 * Constructor
	 * 
	 * @param graphicalWorld - (GraphicalWorld)
	 */
	public UserHud(GraphicalWorld graphicalWorld){
		this.world = graphicalWorld;
		this.game = graphicalWorld.getCore();
		this.hudNode = new Node("Hud");
		
		world.getRootNode().attachChild( hudNode );
		hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		// initialize time variable and other objects
		oldTicks = 0;
		hudScore = new HudScore(this);
		hudLife = new HudLife(this);
		hudAmmo = new HudAmmo(this);
		map = new WorldMap2D( graphicalWorld, this );
		
		pauseText = Text2D.createDefaultTextLabel("Pause");
		pauseText.setLocalScale( GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ) / 600 );
		pauseText.print("Pause");
		pauseText.setLocalTranslation( GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH )/2 - pauseText.getWidth()/2,
				GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT )/2 - pauseText.getHeight()/2, 0 );
		pauseText.lock();
		
		// create text label
		level = Text.createDefaultTextLabel( "Level" );
    	level.setTextColor(ColorRGBA.black);
    	level.setLocalScale(.8f);
    	level.setLocalTranslation( hudAmmo.backQuad.getLocalTranslation().x - hudAmmo.backQuad.getWidth()/2, 
    			hudAmmo.backQuad.getLocalTranslation().y + hudLife.backQuad.getHeight()/2 + 20, 0 );
    	graphicalWorld.getRootNode().attachChild(level);
    	
    	// create text label
		weapon = Text.createDefaultTextLabel( "Weapon" );
		weapon.setTextColor(ColorRGBA.black);
		weapon.setLocalScale(.8f);
		weapon.setLocalTranslation( hudAmmo.backQuad.getLocalTranslation().x - hudAmmo.backQuad.getWidth()/2, 
    			hudAmmo.backQuad.getLocalTranslation().y + hudLife.backQuad.getHeight()/2 + 3, 0 );
    	graphicalWorld.getRootNode().attachChild(weapon);
    	
    	// create text label
    	fps = Text.createDefaultTextLabel( "FPS" );
    	fps.setLocalTranslation( world.getResolution().x - 100, world.getResolution().y - 40, 0 );
    	world.getRootNode().attachChild( fps );
    	
    	// create alert bar and two message handler
    	hudAlert = new HudAlert(this);
    	hudMsg = new HudMessageHandler(this);
    	hudMsgBox = new HudMessageBox(this);
    	
    	if(!graphicalWorld.isLoaded)
    		hudMsgBox.createMessageBox(HudMessageBox.WELCOME1);
    	
    	setCrosshair();
    }
	
	/**
	 * Update user hud
	 */
	public void update(){
		int value = world.getCore().getLevel(world.player.id);
		level.print( GameConf.getPhrase( GameConf.HUD_LEVEL ) + " " + Integer.toString(value));
		weapon.print( GameConf.getPhrase( GameConf.HUD_CURRENT_WEAPON ) + " " + world.getCore().getWeapon(world.player.id));
		hudScore.update();
		hudLife.update();
		map.update();
		// the follows elements are updated ever 
		fps.print( (int) GameTimer.getFrameRate() + "fps" );
    	hudAmmo.update();
    	hudAlert.update();
    	hudMsg.update();
    	hudMsgBox.update();
	}
	
	/**
	 * Add a new small message
	 * 
	 * @param (int) type - message type
	 */
	public static void addMessage( int type ) {
		hudMsg.addMessage( type, 5, ColorRGBA.blue );
	}

	public void showCrosshair( boolean show ) {
		if( show ) {
			hudNode.attachChild( crosshair );
		} else {
			crosshair.removeFromParent();
		}
	}
	
	public void setCrosshair() {
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	    as.setBlendEnabled(true);
	    as.setTestEnabled(false);
	    as.setSourceFunction( SourceFunction.SourceAlpha );
	    as.setDestinationFunction( DestinationFunction.OneMinusSourceAlpha );
	    as.setEnabled(true);
		TextureState cross = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		cross.setTexture( TextureManager.loadTexture( Loader.load(
		                "game/data/images/crosshair.png" ), false ) );
		cross.setEnabled(true);
		crosshair = new Quad( "quad", 30, 30 );
		crosshair.setLocalTranslation( DisplaySystem.getDisplaySystem().getWidth() / 2,
				DisplaySystem.getDisplaySystem().getHeight() / 2, 0 );
		crosshair.setRenderState(as);
		crosshair.setRenderState(cross);
		crosshair.lock();
		
		hudNode.attachChild( crosshair );
		hudNode.updateGeometricState( 0.0f, true );
		hudNode.updateRenderState();
	}
	
	public void showPause( boolean show ) {
		if( show ) 
			hudNode.attachChild( pauseText );
		else
			pauseText.removeFromParent();
	}
}
