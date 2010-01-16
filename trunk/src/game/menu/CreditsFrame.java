package game.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class CreditsFrame
 * Used for displaying game credits 
 * (Development Team, reference libraries, copied code, projects reference)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class CreditsFrame extends JFrame {
	/** Class ID */
	private static final long serialVersionUID = 1L;

	/** Useful pointer to MainMenu frame */
	MainMenu mainMenu;
	
	/** 
	 * Constructor Class CreditsFrame
	 * 
	 * @param mainMenu (MainMenu)
	 */
	public CreditsFrame( MainMenu mainMenu ){
		super();
		this.mainMenu = mainMenu;

		/** Get screen size */
		Dimension screenSize = 
	        Toolkit.getDefaultToolkit().getScreenSize();
		// applied screen size to frame
		setBounds(0,0,screenSize.width, screenSize.height);
		setUndecorated(true); 
	    
		//hide cursor
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
		
		setTitle("Credits Game");
		
		// Create a main panel that contains background image
		JPanel creditsFramePanel = new JPanel();
		
		// Applied a borderLayout
		creditsFramePanel.setLayout(new BorderLayout());
		creditsFramePanel.setOpaque(false);
		setContentPane(creditsFramePanel);
        
		JLabel photo = new JLabel();
		Image img = Toolkit.getDefaultToolkit().getImage( "src/game/data/images/menu/credits16_9.jpg" );
		img = img.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
		photo.setIcon(new ImageIcon(img));
        
        creditsFramePanel.add(photo,BorderLayout.CENTER);
		
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    
	    /**
		 * Custom Listener for intercept pressed keys
		 */
		class KeyHandler implements KeyListener{
			/** Useful pointer to CreditsFrame object */
			CreditsFrame creditsFrame;
			/**
			 * Constructor KeyHandler
			 * 
			 * @param creditsFrame (CreditsFrame)
			 */
			public KeyHandler( CreditsFrame creditsFrame ){
				this.creditsFrame = creditsFrame;
			}
			
			/**
			 * Redefine keyPressed method for close current frame
			 * and reopen mainFrame
			 */
			@Override
			public void keyPressed( KeyEvent e ) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE ){
					setVisible(false);
					creditsFrame.mainMenu.setVisible(true);
				}
			}
			@Override
			public void keyReleased( KeyEvent e ) {}
			@Override
			public void keyTyped( KeyEvent e ) {}
		}
		
		this.addKeyListener( new KeyHandler(this) );
		this.setFocusable(true);
	}
}