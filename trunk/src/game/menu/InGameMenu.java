package game.menu;

import game.base.PhysicsGame;

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
	InGamePanel gamePanel;
	
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
		// get screen size and apply it to current frame
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width, screenSize.height);
		
		//get image background
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_SMOOTH);
		this.setUndecorated(true); 
	    
		//hide cursor
		setCursor( getToolkit().createCustomCursor(
				new BufferedImage( 3, 3, BufferedImage.TYPE_INT_ARGB ),
				new Point(), "null" ) );
		
		this.setTitle( "Game Menu" );
		createMenu();
		
		setVisible(true);
		
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * Create Game Menu
	 * It creates a panel that contains three component used in user choosing
	 */
	public void createMenu(){
		//create main panel
		JPanel borderPanel = new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent( Graphics g ){
				g.drawImage( background, 0, 0, this );
				super.paintComponent(g);
			}
		};
		
		borderPanel.setLayout( new BorderLayout() );
		borderPanel.setOpaque(false);
		setContentPane( borderPanel );
		gamePanel = new InGamePanel( this);
		borderPanel.add( gamePanel, BorderLayout.CENTER );
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize( new Dimension(screenSize.width/4, 1 ) );
		borderPanel.add( pVerticalEmpty1, BorderLayout.WEST );
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize( new Dimension(screenSize.width/4, 1 ) );
		borderPanel.add( pVerticalEmpty2, BorderLayout.EAST );
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize( new Dimension( 1, screenSize.height/4) );
		borderPanel.add( pHorizontalEmpty1, BorderLayout.SOUTH );
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize( new Dimension( 1, screenSize.height/4 ) );
		borderPanel.add( pHorizontalEmpty2, BorderLayout.NORTH );
		
		/**
		 * Custom Listener
		 * Used for intercept pressed keys
		 */
		class KeyHandler implements KeyListener{
			InGamePanel panel;
			/**
			 * Constructor
			 * 
			 * @param panel - Game Panel
			 */
			public KeyHandler( InGamePanel panel ){
				this.panel = panel;
			}
			@Override
			public void keyPressed( KeyEvent e ) {
				if( e.getKeyCode() == KeyEvent.VK_UP )
					panel.next();// go to next element
				if( e.getKeyCode() == KeyEvent.VK_DOWN )
					panel.prev();// go to previous element
				if( e.getKeyCode() == KeyEvent.VK_ENTER )
					// execute current element
					panel.executeSelectedItem();
				if( e.getKeyCode() == KeyEvent.VK_ESCAPE ){
					// return to game
					panel.current = 0;
					panel.executeSelectedItem();
				}
			}
			@Override
			public void keyReleased( KeyEvent e ) {}
			@Override
			public void keyTyped( KeyEvent e ) {}
		}
		
		this.addKeyListener( new KeyHandler(gamePanel) );
		this.setFocusable(true);
	}
}