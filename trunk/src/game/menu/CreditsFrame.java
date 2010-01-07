package game.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
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
 * Class SaveMenu
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class CreditsFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	Image background;
	MainMenu mm;
	
	public CreditsFrame( MainMenu mm ){
		super();
		this.mm = mm;

		Dimension screenSize = 
	        Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width, screenSize.height);
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_DEFAULT);
		setUndecorated(true); 
	    
		//hide cursor
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
		
		setTitle("Credits Game");
		
		JPanel b = new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		b.setLayout(new BorderLayout());
		b.setOpaque(false);
		setContentPane(b);
		
		JPanel flow = new JPanel();
		flow.setLayout(new FlowLayout(FlowLayout.CENTER));
		flow.setOpaque(false);
		
		JLabel title = new JLabel("Development Team");
		title.setFont(new Font("arial", Font.BOLD, 24));
		title.setAlignmentX(CENTER_ALIGNMENT);
        flow.add(title);
        
        JLabel space = new JLabel();
        space.setPreferredSize(new Dimension(screenSize.width*6/8, 1));
        flow.add(space);
        
        JLabel title2 = new JLabel("07/01/2009 - Università della Calabria");
		title2.setSize(screenSize.width*6/8, screenSize.height/16);
        flow.add(title2);
        
		JLabel photo = new JLabel();
		Image img = Toolkit.getDefaultToolkit().getImage( "src/game/data/images/menu/team.jpg" );
		img = img.getScaledInstance(screenSize.width*5/8, screenSize.height*5/8, Image.SCALE_DEFAULT);
		photo.setIcon(new ImageIcon(img));
		
		flow.add(photo);
		
		JLabel label = new JLabel("Giuseppe Leone, Salvatore Loria, Andrea Martire");
		label.setFont(new Font("arial", Font.BOLD, 18));
		label.setSize(screenSize.width*6/8, screenSize.height/16);
        flow.add(label);
        
        b.add(flow,BorderLayout.CENTER);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(screenSize.width/8, 1));
		b.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(screenSize.width/8, 1));
		b.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, screenSize.height/8));
		b.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height/8));
		b.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
	    setResizable(false);
//	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    
	    /**
		 * Custom Listener
		 */
		class KeyHandler implements KeyListener{
			CreditsFrame cm;
			public KeyHandler( CreditsFrame cm ){
				this.cm = cm;
			}
			@Override
			public void keyPressed( KeyEvent e ) {
				if( e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE ){
					setVisible(false);
					cm.mm.setVisible(true);
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