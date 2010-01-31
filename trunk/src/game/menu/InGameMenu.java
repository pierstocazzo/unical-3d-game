package game.menu;

import game.base.PhysicsGame;
import game.common.GameConfiguration;
import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class InGameMenu
 *  
 * Frame of game menu; in this frame user can choose three different actions
 * Resume ( return to game ), Save ( Save current game), Exit ( Kill current game without saving )
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class InGameMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/** Game Panel */
	JPanel currentPanel;
	HashMap<String, JPanel> panelsContainer;
	
	/** background image */
	Image background;

	/** Pointer to Game Class */
	PhysicsGame game;
	
	/** Screen information */
	Dimension screenSize;
	
	/**
	 * Constructor of GameMenu
	 * @param physicsGame 
	 * 
	 * @param mainMenu - Main Menu
	 */
	public InGameMenu( PhysicsGame game ){
		super();
		this.game = game;
		
		panelsContainer = new HashMap<String, JPanel>();
		
		/** Get screen size */
		if(GameConfiguration.isFullscreen().equals("true")){
			screenSize = new Dimension( 
					Integer.valueOf(GameConfiguration.getResolutionWidth()).intValue(), 
					Integer.valueOf(GameConfiguration.getResolutionHeight()).intValue() );
			background = ImagesContainer.getBackground_with_FullScreen();
		}
		else{
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			background = ImagesContainer.getBackground_no_FullScreen();
		}

		setBounds(0,0,screenSize.width, screenSize.height);
		setUndecorated(true); 
		setTitle( "Game Menu" );
		setVisible(true);
	    setResizable(false);
	    
	    panelsContainer.put("inGamePanel", new InGamePanel(this));
	    panelsContainer.put("savePanel", new SavePanel(this));
	    
	    switchToInGamePanel();
//	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void switchToInGamePanel(){
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("inGamePanel");
		add( currentPanel );
		repaint();
		validate();
	}
	
	public void switchToSavePanel(){
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("savePanel");
		add( currentPanel );
		repaint();
		validate();
	}
}