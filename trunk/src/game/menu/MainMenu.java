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
	
	DisplayMode currentDM;
	
	boolean exclusiveFullScreen = false;
	
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
	    
		GraphicsEnvironment env = GraphicsEnvironment.
        getLocalGraphicsEnvironment();
		device = env.getScreenDevices()[0];
		
		ImagesContainer.init();
		
		if( GameConf.getSetting( GameConf.IS_FULLSCREEN ).equals("true") ) {
			device.setFullScreenWindow( this );
			exclusiveFullScreen = true;
			DisplayMode displayMode;
			
			if( System.getProperties().getProperty("os.name").equals("Linux") ) {
				displayMode = new DisplayMode( 
						GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ), 
						GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT ), 
						DisplayMode.BIT_DEPTH_MULTI, 
						GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ) );
			} else {
				displayMode = new DisplayMode( 
						GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ), 
						GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT ), 
						GameConf.getIntSetting( GameConf.RESOLUTION_DEPTH ), 
						GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ) );
			}
			
			device.setDisplayMode( displayMode );
			currentDM = displayMode;
			// get screen size informations
			screenSize = new Dimension( displayMode.getWidth(), 
					displayMode.getHeight() );
			setSize( screenSize );
			background = ImagesContainer.getBackground_with_FullScreen();
		} else {
			exclusiveFullScreen = false;
			// get screen size informations
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			// set fullscreen
		    setBounds( 0, 0, screenSize.width, screenSize.height );
			background = ImagesContainer.getBackground_no_FullScreen();
			currentDM = new DisplayMode( screenSize.width, screenSize.height, 
					GameConf.getIntSetting( GameConf.RESOLUTION_DEPTH ), 
					GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ) );
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
	 */
	public void applyChanges() {
		DisplayMode displayMode = new DisplayMode( 
			GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ), 
			GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT ), 
			GameConf.getIntSetting( GameConf.RESOLUTION_DEPTH ), 
			GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ) 
		);
				
		// if the user sets fullscreen enabled and a resolution differente from the current one
		// change resolution and go in exclusive fullscreen mode
		if( GameConf.getSetting( GameConf.IS_FULLSCREEN ).equals("true") ) {
			if( displayMode.getWidth() != currentDM.getWidth() ||
					displayMode.getHeight() != currentDM.getHeight() ) {
				setVisible(false);
				device.setFullScreenWindow( this );
				exclusiveFullScreen = true;
				background = ImagesContainer.getBackground_with_FullScreen();
				device.setDisplayMode( displayMode );
				
				// get screen size informations
				screenSize = new Dimension( displayMode.getWidth(), 
						displayMode.getHeight() );
				setSize( screenSize );
				reinit();
				validate();
				currentDM = displayMode;
				repaint();
				setVisible(true);
			}
		} else { // if the user sets fullscreen disabled return to standard fullscreen mode
			if( exclusiveFullScreen == true ) {
				setVisible(false);
				device.setFullScreenWindow(null);
				background = ImagesContainer.getBackground_no_FullScreen();
				
				// get screen size informations
				screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				setSize( screenSize );
				reinit();
				validate();
				repaint();
				setVisible(true);
				currentDM = new DisplayMode( screenSize.width, screenSize.height, 
						GameConf.getIntSetting( GameConf.RESOLUTION_DEPTH ), 
						GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ));
				exclusiveFullScreen = false;
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