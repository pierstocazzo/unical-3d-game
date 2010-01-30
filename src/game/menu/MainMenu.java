package game.menu;

import game.common.GameConfiguration;
import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	/** Main Panel */
	JPanel currentPanel;
	HashMap<String, JPanel> panelsContainer;
	
	/** background image */
	Image background;
	
	/** Screen size informations */
	Dimension screenSize;
	
	/**
	 * Constructor of MainMenu Class
	 */
	public MainMenu(){
		super();
		
		// get screen size informations
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// apply screen size value to current frame
	    setBounds( 0, 0, screenSize.width, screenSize.height );
		
		GameConfiguration.init();
		ImagesContainer.init();
		
		// hide frame border
		setUndecorated(true); 
		setAlwaysOnTop(true);
		requestFocus();
		
		setTitle("Main Menu");
		
	    setResizable(false);
//	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    		
		panelsContainer = new HashMap<String, JPanel>();
		panelsContainer.put("mainPanel", new MainPanel(this));
		panelsContainer.put("creditsPanel", new CreditsPanel(this));
		
		// set a layout to main panel
		switchToMainPanel();
	}
	
	public void switchToMainPanel(){
		System.out.println("passo al main panel");
		getContentPane().removeAll();
		getContentPane().add( panelsContainer.get("mainPanel") );
		getContentPane().repaint();
	}

	public void switchToCreditsPanel() {
		System.out.println("passo al credits panel");
		getContentPane().removeAll();
		getContentPane().add( panelsContainer.get("creditsPanel") );
		getContentPane().repaint();
	}
}