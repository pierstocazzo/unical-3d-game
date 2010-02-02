package game.menu;

import game.common.GameConfiguration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Class LoadingFrame
 * Used for displaying current progress of loading game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class LoadingPanel extends JPanel {
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** Progress Bar */
	JProgressBar progressBar;
	
	/** Label used for communicate during loading */
	JLabel textLoading;
	
	/** Pointer to frame */
	MainMenu mainMenu;
	
	/**
	 * Constructor Class LoadingFrame
	 * @param mainMenu 
	 */
	public LoadingPanel( MainMenu mainMenu ) {
		super();
		this.mainMenu = mainMenu;
		
		/** Get screen size */
		Dimension screenSize;
		if(GameConfiguration.isFullscreen().equals("true")){
			screenSize = new Dimension( 
					Integer.valueOf(GameConfiguration.getResolutionWidth()).intValue(), 
					Integer.valueOf(GameConfiguration.getResolutionHeight()).intValue() );
		}
		else{
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		}
	
		// applied a border layout
		setLayout(new BorderLayout());
		setOpaque(false);
		
		// create a sub panel for inserts new components on particular screen position
		JPanel flowPanel = new JPanel();
		flowPanel.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/8));
		flowPanel.setOpaque(false);
	
		// delete spacing between components of the flow layout
		flowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		add(flowPanel,BorderLayout.CENTER);
		
		// create a label that express loading informations
		textLoading = new JLabel("");
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
		add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(screenSize.width/8, 1));
		add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, screenSize.height/8));
		add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height*12/16));
		add(pHorizontalEmpty2,BorderLayout.NORTH);
		
	    setVisible(true);
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