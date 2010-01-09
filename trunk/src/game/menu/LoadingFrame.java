package game.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
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
	JLabel textLoading;
	
	public LoadingFrame(){
		super();

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
		
		setTitle("Loading Game");
		
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
		flow.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/8));
		flow.setOpaque(false);
		//elimino spaziatura tra componenti del flow layout
		flow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		b.add(flow,BorderLayout.CENTER);
		
		//creo jlabel per esprimere ciò che si sta caricando
		textLoading = new JLabel("Loading...");
		textLoading.setFont(new Font("Times New Roman", Font.BOLD, 32));
		textLoading.setForeground(Color.red);
		textLoading.setHorizontalAlignment(JLabel.CENTER);
		textLoading.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/16));
		flow.add(textLoading);
		
		pb = new JProgressBar(0, 100);
		pb.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/16));
        pb.setValue(0);
        pb.setOpaque(false);
        pb.setForeground(Color.red);
        flow.add(pb);
		
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
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height*12/16));
		b.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void setProgress(int num){
		pb.setValue(num);
	}
	
	public void setLoadingText(String text){
		textLoading.setText(text);
	}
}