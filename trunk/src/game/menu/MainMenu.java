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

package game.menu;

import game.common.GameConf;
import game.common.ImagesContainer;
import game.graphics.GraphicalWorld;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.*;

/**
 * Class Main Menu
 * It's the main frame. From this class we can launch or load a game match.
 * It's possible to know info about development team, to modify game option and exit.
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class MainMenu extends JFrame {
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** Reference to the current Panel */
	JPanel currentPanel;
	HashMap<String, JPanel> panelsContainer;

	/** Screen size informations */
	Dimension screenSize;

	GraphicalWorld game;
	
	JPanel paneContainer;
	Image background;
	
	GraphicsDevice device;
	
	boolean exclusiveFullScreen = false;
	
	LinkedHashMap<String, DisplayMode> displayModes;
	
	DisplayMode currentDM;
	
	/**
	 * Constructor of MainMenu Class
	 */
	@SuppressWarnings("serial")
	public MainMenu(){
		super();
		// hide frame border
		setUndecorated(true); 
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    
	    ImagesContainer.init();
	    
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getScreenDevices()[0];
		
		displayModes = new LinkedHashMap<String, DisplayMode>();
		for( DisplayMode dm : device.getDisplayModes() ) {
			if( dm.getWidth() >= 800 && dm.getHeight() >= 600 ) {
				String key = dm.getWidth() + "x" + dm.getHeight();
				displayModes.put( key, dm );
			}
		}
		
		int width = GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ); 
		int height = GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT );
		
		if( GameConf.getSetting( GameConf.IS_FULLSCREEN ).equals("true") && 
				device.isFullScreenSupported() ) {
			exclusiveFullscreen( displayModes.get( width + "x" + height ) );
		} else {
			normalFullscreen();
		}
		
		paneContainer = new JPanel() {
			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
			}
		};

		paneContainer.setLayout( new BorderLayout() );
		setContentPane(paneContainer);
		
		panelsContainer = new HashMap<String, JPanel>();
		panelsContainer.put("mainPanel", new MainPanel(this));
		panelsContainer.put("creditsPanel", new CreditsPanel(this));
		panelsContainer.put("optionsPanel", new OptionsPanel(this));
		panelsContainer.put("loadingPanel", new LoadingPanel(this) );
		panelsContainer.put("inGamePanel", new InGamePanel(this));
	    panelsContainer.put("savePanel", new SavePanel(this));
	}
	
	private void exclusiveFullscreen( DisplayMode displayMode ) {
		device.setFullScreenWindow(this);
		exclusiveFullScreen = true;
		device.setDisplayMode( displayMode );
		currentDM = displayMode;
		// get screen size informations
		screenSize = new Dimension( displayMode.getWidth(), 
				displayMode.getHeight() );
		setSize( screenSize );
		background = ImagesContainer.getBackground_with_FullScreen();
	}
	
	private void normalFullscreen() {
		device.setFullScreenWindow(null);
		exclusiveFullScreen = false;
		// get screen size informations
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		currentDM = new DisplayMode( screenSize.width, screenSize.height, -1, -1 );
		// set fullscreen
	    setSize( screenSize );
		background = ImagesContainer.getBackground_no_FullScreen();
	}
	
	public void start() {
		requestFocus();
		setVisible(true);
		setAlwaysOnTop(true);
		switchToPanel("mainPanel");
	}
	
	public void switchToPanel( String panel ){
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get( panel );
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	/** check if there are changes in the jme resolution and fullscreen mode
	 * if true apply them
	 * @param key  
	 */
	public void applyChanges( String key ) {
		int width = GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ); 
		int height = GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT );
		
		// if the user sets fullscreen enabled and a resolution differente from the current one
		// change resolution and go in exclusive fullscreen mode
		if( GameConf.getSetting( GameConf.IS_FULLSCREEN ).equals("true") ) {
			if( width != currentDM.getWidth() || height != currentDM.getHeight() ) {
				exclusiveFullscreen( displayModes.get(key) );
				reinit();
				repaint();
				validate();
			}
		} else { // if the user sets fullscreen disabled return to standard fullscreen mode
			if( exclusiveFullScreen == true ) {
				if( width != currentDM.getWidth() || height != currentDM.getHeight() ) {
					normalFullscreen();
					reinit();
					repaint();
					validate();
				}
			}
		}
	}
	
	public void reinit() {
		panelsContainer.put("mainPanel", new MainPanel(this));
		panelsContainer.put("creditsPanel", new CreditsPanel(this));
		panelsContainer.put("optionsPanel", new OptionsPanel(this));
		panelsContainer.put("loadingPanel", new LoadingPanel(this) );
		panelsContainer.put("inGamePanel", new InGamePanel(this));
	    panelsContainer.put("savePanel", new SavePanel(this));
	}
	
	public void setLoadingText( String text ) {
		((LoadingPanel) panelsContainer.get("loadingPanel")).setLoadingText( text );
	}
	
	public void setProgress( int progress ) {
		((LoadingPanel) panelsContainer.get("loadingPanel")).setProgress( progress );
	}

	public void setGame( GraphicalWorld game) {
		this.game = game;
	}

	public void hideMenu() {
		setVisible(false);
	}
}