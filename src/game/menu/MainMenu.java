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
		
		GameConfiguration.init();
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
//		add(paneContainer);
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
		panelsContainer.put("loadingPanel", new LoadingFrame(this) );
		panelsContainer.put("inGamePanel", new InGamePanel(this));
	    panelsContainer.put("savePanel", new SavePanel(this));
	    
	    
	    
		// set a layout to main panel
		switchToPanel("mainPanel");
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

	public void switchToCreditsPanel() {
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("creditsPanel");
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	public void switchToOptionsPanel() {
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("optionsPanel");
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	public void switchToLoadingPanel() {
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("loadingPanel");
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	public void switchToInGamePanel() {
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("inGamePanel");
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	public void switchToSavePanel() {
		if ( currentPanel != null )
			remove( currentPanel );
		currentPanel = panelsContainer.get("savePanel");
		add( currentPanel );
		repaint();
		validate();
		currentPanel.requestFocus();
	}
	
	public void setLoadingText( String text ) {
		((LoadingFrame) panelsContainer.get("loadingPanel")).setLoadingText( text );
	}
	
	public void setProgress( int progress ) {
		((LoadingFrame) panelsContainer.get("loadingPanel")).setProgress( progress );
	}

	public void setGame( PhysicsGame game) {
		this.game = game;
	}

	public void hideMenu() {
		setVisible(false);
	}
}