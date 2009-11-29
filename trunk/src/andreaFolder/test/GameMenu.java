package andreaFolder.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Class GameMenu
 *  
 * Frame of game menu
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameMenu extends JFrame {
	
	/** Game Panel */
	GamePanel p;
	/** Monitor for thread management */
	ThreadController tc;
	/** Pointer to Main Menu ( a Frame ) */
	MainMenu mm;
	/** backgroung wallpaper */
	Image sfondo;
	
	/**
	 * Constructor of GameMenu
	 * 
	 * @param tc - Thread Monitor
	 * @param mm - Main Menu
	 */
	public GameMenu(ThreadController tc, MainMenu mm){
		super();
		this.tc = tc;
		this.mm = mm;
		
		//get image background
		sfondo = Toolkit.getDefaultToolkit().getImage("src/andreaFolder/shark.jpg");
		
		this.setSize(1000, 600);
//		setDefaultLookAndFeelDecorated(true);
//		this.setUndecorated(true); 
		
//		this.setResizable(true);
//		this.setAlwaysOnTop(true);

//		Dimension screenSize = 
//	        Toolkit.getDefaultToolkit().getScreenSize();
//	    setBounds(0,0,screenSize.width, screenSize.height);
	    
		//hide cursor
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
		
		this.setTitle("Game Menu");
		createMenu();
		
		//set full screen
//		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//	    device.setFullScreenWindow(this);
	}
	
	/**
	 * Create Game Menu
	 */
	public void createMenu(){
		//create main panel
		JPanel b = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				g.drawImage(sfondo, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		b.setLayout(new BorderLayout());
		b.setOpaque(false);
		this.setContentPane(b);
		p = new GamePanel(this,mm);
		b.add(p, BorderLayout.CENTER);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(350, 1));
		b.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(350, 1));
		b.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, 100));
		b.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, 250));
		b.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
		this.setVisible(true);
		
		/**
		 * Custom Listener
		 * 
		 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
		 */
		class KeyHandler implements KeyListener{
			GamePanel panel;
			/**
			 * Constructor
			 * 
			 * @param p - Game Panel
			 */
			public KeyHandler( GamePanel p){
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
		
		this.addKeyListener( new KeyHandler(p));
		this.setFocusable(true);
	}
}