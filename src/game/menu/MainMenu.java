package game.menu;

import game.common.GameConfiguration;
import game.common.ImagesContainer;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

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
	
	/** Reference to the current Panel */
	JPanel currentPanel;
//	HashMap<String, JPanel> panelsContainer;
	
	/** background image */
	Image background;
	
	/** Screen size informations */
	Dimension screenSize;
	
	/** Panels */
	MainPanel mainPanel;
	CreditsPanel creditsPanel;
	
	/**
	 * Constructor of MainMenu Class
	 */
	public MainMenu(){
		super();
		
		// get screen size informations
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// apply screen size value to current frame
	    setBounds( 0, 0, screenSize.width, screenSize.height );
		
		GameConfiguration.init();
		ImagesContainer.init();
		
		// hide frame border
		setUndecorated(true); 
		setAlwaysOnTop(true);
		requestFocus();
		
		setTitle("Main Menu");
		
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
   		
//		panelsContainer = new HashMap<String, JPanel>();
//		panelsContainer.put("mainPanel", new MainPanel(this));
//		panelsContainer.put("creditsPanel", new CreditsPanel(this));
		
	    mainPanel = new MainPanel(this);
	    creditsPanel = new CreditsPanel(this);
	    
		// set a layout to main panel
//		switchToMainPanel();
	    currentPanel = mainPanel;
	    switchTo( mainPanel );
	}
	
	/** 
	 * Switch di pannello: <br>
	 * rimuovo il pannello corrente dal ContentPane e aggiungo il pannello
	 * passato come parametro della funzione, aggiornando currentPanel e chiamando 
	 * la funzione "validate()" per riorganizzare i componenti
	 * 
	 * ps: la funzione "removeAll()" mi ha dato problemi, per questo utilizzo currentPanel
	 * 
	 * @param panel - (JPanel) il pannello da visualizzare
	 */
	public void switchTo( JPanel panel ) {
		remove( currentPanel );
		currentPanel = panel;
		add( panel );
		repaint();
		validate();
	}
	
//	public void switchToMainPanel(){
//		remove( panelsContainer.get("creditsPanel") );
//		add( panelsContainer.get("mainPanel") );
//		repaint();
//		validate();
//	}
//
//	public void switchToCreditsPanel() {
//		remove( panelsContainer.get("mainPanel") );
//		add( panelsContainer.get("creditsPanel") );
//		repaint();
//		validate();
//	}
}