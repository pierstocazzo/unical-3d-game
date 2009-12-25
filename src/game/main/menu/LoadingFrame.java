package game.main.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Class SaveMenu
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class LoadingFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	Image background;
	JProgressBar pb;
	
	public LoadingFrame(){
		super();
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		
		this.setUndecorated(true); 
	    
		//hide cursor
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
		
		this.setTitle("Loading Game");
		
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
		this.setContentPane(b);
		
		pb = new JProgressBar(0, 100);
        pb.setValue(0);
        pb.setOpaque(false);
        pb.setForeground(Color.red);
        b.add(pb, BorderLayout.CENTER);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(150, 1));
		b.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(150, 1));
		b.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, 100));
		b.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, 600));
		b.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
		Dimension screenSize = 
	        Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width, screenSize.height);
	    setResizable(false);
//	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void setProgress(int num){
		pb.setValue(num);
	}
	
}