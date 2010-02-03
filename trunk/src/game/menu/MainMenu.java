package game.menu;

import game.base.PhysicsGame;
import game.common.GameConfiguration;
import game.common.ImagesContainer;

import java.awt.*;
import java.util.HashMap;

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
	HashMap<String, JPanel> panelsContainer;

	/** Screen size informations */
	Dimension screenSize;

	PhysicsGame game;
	
	JPanel paneContainer;
	Image background;
	
	/**
	 * Constructor of MainMenu Class
	 */
	@SuppressWarnings("serial")
	public MainMenu(){
		super();
		
		// get screen size informations
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// apply screen size value to current frame
	    setBounds( 0, 0, screenSize.width, screenSize.height );
		
		ImagesContainer.init();
		
		if(GameConfiguration.isFullscreen().equals("true")){
			background = ImagesContainer.getBackground_with_FullScreen();
		}
		else{
			background = ImagesContainer.getBackground_no_FullScreen();
		}
		
		paneContainer = new JPanel() {
			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
//				super.paintComponent(g);
			}
		};

		paneContainer.setLayout( new BorderLayout() );
		setContentPane(paneContainer);
		
		// hide frame border
		setUndecorated(true); 
		setAlwaysOnTop(true);
		requestFocus();
		
		setTitle("Main Menu");
		
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
   		
		panelsContainer = new HashMap<String, JPanel>();
		panelsContainer.put("mainPanel", new MainPanel(this));
		panelsContainer.put("creditsPanel", new CreditsPanel(this));
		panelsContainer.put("optionsPanel", new OptionsPanel(this));
		panelsContainer.put("loadingPanel", new LoadingPanel(this) );
		panelsContainer.put("inGamePanel", new InGamePanel(this));
	    panelsContainer.put("savePanel", new SavePanel(this));
	    
		// set a layout to main panel
		switchToPanel("mainPanel");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void switchToPanel( String panel ){
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get( panel );
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	public void setLoadingText( String text ) {
		((LoadingPanel) panelsContainer.get("loadingPanel")).setLoadingText( text );
	}
	
	public void setProgress( int progress ) {
		((LoadingPanel) panelsContainer.get("loadingPanel")).setProgress( progress );
	}

	public void setGame( PhysicsGame game) {
		this.game = game;
	}

	public void hideMenu() {
		setVisible(false);
	}
}