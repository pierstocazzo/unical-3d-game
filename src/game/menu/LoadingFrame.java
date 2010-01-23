package game.menu;

import game.common.GameConfiguration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Class LoadingFrame
 * Used for displaying current progress of loading game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class LoadingFrame extends JFrame {
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** Backgroung Image */
	Image background;
	
	/** Progress Bar */
	JProgressBar progressBar;
	
	/** Label used for communicate during loading */
	JLabel textLoading;
	
	/**
	 * Constructor Class LoadingFrame
	 */
	public LoadingFrame(){
		super();

		// Get screen size
		Dimension screenSize = new Dimension (
				Integer.valueOf(GameConfiguration.getResolutionWidth()).intValue(),
				Integer.valueOf(GameConfiguration.getResolutionHeight()).intValue());
		// apply screen size to current frame
		setBounds(0,0,screenSize.width, screenSize.height);
		// get image file
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		// scaled background image respect screen size
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_SMOOTH);
		//hide frame border
		setUndecorated(true); 
	    
		//hide cursor
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
		
		setTitle("Loading Game");
		
		/** create a panel that contains label and progress bar */
		JPanel loadingFranePanel = new JPanel(){
			private static final long serialVersionUID = 1L;

			// painted background before panel components
			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		// applied a border layout
		loadingFranePanel.setLayout(new BorderLayout());
		loadingFranePanel.setOpaque(false);
		setContentPane(loadingFranePanel);
		
		// create a sub panel for inserts new components on particular screen position
		JPanel flowPanel = new JPanel();
		flowPanel.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/8));
		flowPanel.setOpaque(false);
		
		// delete spacing between components of the flow layout
		flowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		loadingFranePanel.add(flowPanel,BorderLayout.CENTER);
		
		// create a label that express loading informations
		textLoading = new JLabel("Loading...");
		textLoading.setFont(new Font("Times New Roman", Font.BOLD, 32));
		textLoading.setForeground(Color.red);
		textLoading.setHorizontalAlignment(JLabel.CENTER);
		textLoading.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/16));
		flowPanel.add(textLoading);
		
		// create a progress bar
		progressBar = new JProgressBar(0, 100);
		progressBar.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/16));
        progressBar.setValue(0);
        progressBar.setOpaque(false);
        progressBar.setForeground(Color.red);
        flowPanel.add(progressBar);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(screenSize.width/8, 1));
		loadingFranePanel.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(screenSize.width/8, 1));
		loadingFranePanel.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, screenSize.height/8));
		loadingFranePanel.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height*12/16));
		loadingFranePanel.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    
	    setVisible(true);
		setAlwaysOnTop(true);
	}
	
	/**
	 * It sets current progress
	 *  
	 * @param num - (int) Progress Value
	 */
	public void setProgress( int num ){
		progressBar.setValue(num);
	}
	
	/**
	 * It sets current displayed informations
	 * 
	 * @param text - (String) Text informations
	 */
	public void setLoadingText(String text){
		textLoading.setText(text);
	}
}