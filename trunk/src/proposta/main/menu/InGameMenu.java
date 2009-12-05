package proposta.main.menu;

import proposta.main.ThreadController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class GameMenu
 *  
 * Frame of game menu
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class InGameMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/** Game Panel */
	InGamePanel gamePanel;
	
	/** Monitor for thread management */
	ThreadController threadController;
	
	/** Pointer to Main Menu ( a Frame ) */
	MainMenu mainMenu;
	
	/** background wallpaper */
	Image background;
	
	
	/**
	 * Constructor of GameMenu
	 * 
	 * @param threadController - Thread Monitor
	 * @param mainMenu - Main Menu
	 */
	public InGameMenu( ThreadController threadController, MainMenu mainMenu ){
		super();
		this.threadController = threadController;
		this.mainMenu = mainMenu;
		
		//get image background
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		
//		this.setSize(1000, 600);
//		setDefaultLookAndFeelDecorated(true);
		this.setUndecorated(true); 
		
//		this.setResizable(true);
//		this.setAlwaysOnTop(true);

//		Dimension screenSize = 
//	        Toolkit.getDefaultToolkit().getScreenSize();
//	    setBounds(0,0,screenSize.width, screenSize.height);
	    
		//hide cursor
		setCursor( getToolkit().createCustomCursor(
				new BufferedImage( 3, 3, BufferedImage.TYPE_INT_ARGB ),
				new Point(), "null" ) );
		
		this.setTitle( "Game Menu" );
		createMenu();
		
		//set full screen
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	    device.setFullScreenWindow(this);
	}
	
	/**
	 * Create Game Menu
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
		gamePanel = new InGamePanel( this, mainMenu );
		borderPanel.add( gamePanel, BorderLayout.CENTER );
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize( new Dimension( 350, 1 ) );
		borderPanel.add( pVerticalEmpty1, BorderLayout.WEST );
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize( new Dimension( 350, 1 ) );
		borderPanel.add( pVerticalEmpty2, BorderLayout.EAST );
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize( new Dimension( 1, 100 ) );
		borderPanel.add( pHorizontalEmpty1, BorderLayout.SOUTH );
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize( new Dimension( 1, 250 ) );
		borderPanel.add( pHorizontalEmpty2, BorderLayout.NORTH );
		
		this.setVisible(true);
		
		/**
		 * Custom Listener
		 */
		class KeyHandler implements KeyListener{
			InGamePanel panel;
			/**
			 * Constructor
			 * 
			 * @param p - Game Panel
			 */
			public KeyHandler( InGamePanel p ){
				this.panel = p;
			}
			@Override
			public void keyPressed( KeyEvent e ) {
				if( e.getKeyCode() == KeyEvent.VK_UP )
					panel.next();
				if( e.getKeyCode() == KeyEvent.VK_DOWN )
					panel.prev();
				if( e.getKeyCode() == KeyEvent.VK_ENTER )
					panel.executeSelectedItem();
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