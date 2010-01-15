package game.menu;

import java.awt.BorderLayout;
import java.awt.Cursor;
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
	MainPanel centerPanel;
	
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
	    setBounds(0,0,screenSize.width, screenSize.height);
	    // get background image
		background = Toolkit.getDefaultToolkit().getImage( "src/game/data/images/menu/background.jpg" );
		// scale background image respect screen size
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_SMOOTH);
		// hide frame border
		setUndecorated(true); 
		this.setAlwaysOnTop(true);
		requestFocus();
		hideCursor();
		
		this.setTitle("Main Menu");
		centerPanel = new MainPanel(this);
		
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * Create a panel. This panel is displayed with its components at frame center
	 */
	public void createMenu(){
		
		/**
		 * Create a panel for mapping frame components
		 */
		JPanel mainPanel = new JPanel(){
			
			/** panel ID */
			private static final long serialVersionUID = 1L;

			/** 
			 * Override this method for paint background image before frame components
			 */
			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		// set a layout to main panel
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(false);
		this.setContentPane(mainPanel);
		// at main panel center add centerPanel
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		//add left vertical empty panel for spacing
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(screenSize.width/4, 1));
		mainPanel.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel for spacing
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(screenSize.width/4, 1));
		mainPanel.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel for spacing
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, screenSize.height/4));
		mainPanel.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel for spacing
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height/4));
		mainPanel.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
		/**
		 * Custom Listener.
		 * It intercepts key pressed and refresh center panel
		 * 
		 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
		 */
		class KeyHandler implements KeyListener{
			
			/** Main Panel */
			MainPanel panel;
			
			/**
			 * Constructor
			 * @param p - MainPanel
			 */
			public KeyHandler( MainPanel p){
				this.panel = p;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_UP)
					panel.next();
				if(e.getKeyCode()==KeyEvent.VK_DOWN)
					panel.prev();
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					panel.executeSelectedItem();
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		}
		
		addKeyListener( new KeyHandler(centerPanel));
		setFocusable(true);
		setVisible(true);
	}
	
	/**
	 * Hide system cursor
	 */
	public void hideCursor(){
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
	}
	
	/**
	 * Show system cursor
	 */
	public void showCursor(){
		setCursor(Cursor.getDefaultCursor());
	}
}