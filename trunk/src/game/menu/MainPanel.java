package game.menu;

import game.common.ImagesContainer;
import game.core.LogicWorld;
import game.main.GameThread;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * Class MainPanel
 * Center Panel displayed in main frame
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class MainPanel extends JPanel {
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** Number of current element */
	public int current = 0;
	
	/** Preloaded images */
	ArrayList<Image> menuImageContainer;
	/** Preloaded images */
	ArrayList<JButton> buttonContainer;
	
	/** MainMenu */
	MainMenu mainMenu;
	
	JPanel centerPanel;
	
	/** Pointer to current thread game */
	GameThread gameThread;
	
	/**
	 * Constructor of MainPanel
	 * 
	 * @param mainMenu - MainMenu
	 */
	public MainPanel(MainMenu mainMenu){
		super();
		this.mainMenu = mainMenu;
		menuImageContainer = ImagesContainer.getMenuImagesContainer();
		buttonContainer = new ArrayList<JButton>();
		
		setLayout(new BorderLayout());
		setOpaque(false);
		setBorder( BorderFactory.createEmptyBorder() );
		
		createMenu();
		initItem();
		
	}
	
	/**
	 * Create a panel. This panel is displayed with its components at frame center
	 */
	public void createMenu(){
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(5,1));
		centerPanel.setOpaque(false);
		add( centerPanel, BorderLayout.CENTER );
		
		//add left vertical empty panel for spacing
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension( mainMenu.screenSize.width/4, 1));
		add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel for spacing
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension( mainMenu.screenSize.width/4, 1));
		add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel for spacing
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, mainMenu.screenSize.height/4));
		add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel for spacing
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, mainMenu.screenSize.height/4));
		add(pHorizontalEmpty2,BorderLayout.NORTH);
		
		setFocusable(true);
		setVisible(true);
	}
	
	/**
	 * Initialize item images
	 */
	public void initItem(){
		
		setFocusable(true);
		addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent arg0) {}
				
				@Override
				public void keyReleased(KeyEvent arg0) {}
				
				@Override
				public void keyPressed(KeyEvent event) {
					if(event.getKeyCode() == KeyEvent.VK_DOWN){
						next();
						refresh();
					}
					if(event.getKeyCode() == KeyEvent.VK_UP){
						prev();
						refresh();
					}
					if(event.getKeyCode() == KeyEvent.VK_ENTER){
						switch (current) {
							case 0:newGame();break;
							case 1:loadGame();break;
							case 2:goOptions();break;
							case 3:goCredits();break;
							case 4:exitGame();break;
						}
					}
				}
			});
		
		class MouseHandler implements MouseListener{
			
			JButton button;
			int index;
			public MouseHandler( JButton button, int index ){
				this.button = button;
				this.index = index;
			}

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setIcon(new ImageIcon(menuImageContainer.get(index+1)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setIcon(new ImageIcon(menuImageContainer.get(index)));
			}

			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		}
		
		JButton buttonNewgame = new JButton(new ImageIcon(menuImageContainer.get(0)));
		buttonNewgame.setBorderPainted(false);
		buttonNewgame.setContentAreaFilled(false);
		buttonNewgame.addMouseListener( new MouseHandler(buttonNewgame, 0) {
			@Override
			public void mouseClicked(MouseEvent e) {
				newGame();
			}
		});
		buttonNewgame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		buttonNewgame.setMnemonic(KeyEvent.VK_N);
		centerPanel.add(buttonNewgame);
		buttonContainer.add(buttonNewgame);
		
		JButton buttonLoad = new JButton(new ImageIcon(menuImageContainer.get(2)));
		buttonLoad.setBorderPainted(false);
		buttonLoad.setContentAreaFilled(false);
		buttonLoad.addMouseListener(new MouseHandler(buttonLoad, 2){
			@Override
			public void mouseClicked(MouseEvent e) { 
				loadGame();
			}
		});
		buttonLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadGame();
			}
		});
		buttonLoad.setMnemonic(KeyEvent.VK_L);
		centerPanel.add( buttonLoad );
		buttonContainer.add(buttonLoad);
		
		JButton buttonOptions = new JButton(new ImageIcon(menuImageContainer.get(4)));
		buttonOptions.setBorderPainted(false);
		buttonOptions.setContentAreaFilled(false);
		buttonOptions.setSelected(false);
		buttonOptions.addMouseListener( new MouseHandler(buttonOptions,4) {

			@Override
			public void mouseClicked(MouseEvent e) {
				goOptions();
			}
		});
		buttonOptions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				goOptions();
			}
		});
		buttonOptions.setMnemonic(KeyEvent.VK_O);
		centerPanel.add( buttonOptions );
		buttonContainer.add(buttonOptions);
			
		JButton buttonCredits = new JButton(new ImageIcon(menuImageContainer.get(6)));
		buttonCredits.setBorderPainted(false);
		buttonCredits.setContentAreaFilled(false);
		buttonCredits.addMouseListener( new MouseHandler(buttonCredits,6) {

			@Override
			public void mouseClicked(MouseEvent e) {
				// switch to credits panel
				goCredits();
			}
		});
		buttonCredits.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				goCredits();
			}
		});
		buttonCredits.setMnemonic(KeyEvent.VK_C);
		centerPanel.add( buttonCredits );
		buttonContainer.add(buttonCredits);
		
		
		JButton buttonExit = new JButton(new ImageIcon(menuImageContainer.get(8)));
		buttonExit.setBorderPainted(false);
		buttonExit.setContentAreaFilled(false);
		buttonExit.addMouseListener( new MouseHandler(buttonExit,8) {
			@Override
			public void mouseClicked(MouseEvent e) {
				exitGame();
			}
		});
		buttonExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exitGame();
			}
		});
		buttonExit.setMnemonic(KeyEvent.VK_X);
		centerPanel.add( buttonExit );
		buttonContainer.add(buttonExit);
	}
	
	public void newGame(){
		mainMenu.switchToLoadingPanel();
		mainMenu.setAlwaysOnTop(true);
		mainMenu.requestFocus();
		
		// Create a new game thread and launch it
		gameThread = new GameThread( mainMenu );
		gameThread.start();
	}
	
	public void loadGame(){
		//Load game with fileChooser
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./gameSave"));
		int returnVal = fc.showOpenDialog(mainMenu);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			System.out.println("Opening: " + file.getName() + ".");

			FileInputStream fin = null;
			try {
				fin = new FileInputStream("gameSave/"+file.getName());
			} catch (FileNotFoundException e0) {
				e0.printStackTrace();
			}

			ObjectInputStream ois = null;
			try { 
				ois = new ObjectInputStream(fin); 
			} catch (IOException e1) { 
				e1.printStackTrace(); 
			}

			LogicWorld gameLoaded = null;
			try {
				try { 
					gameLoaded = (LogicWorld) ois.readObject();
				} 
				catch (IOException e2) {
					e2.printStackTrace();
				}
			} catch (ClassNotFoundException e3) {
				e3.printStackTrace();
			} 
			
			mainMenu.switchToLoadingPanel();
			mainMenu.setAlwaysOnTop(true);
			mainMenu.requestFocus();
			
			// create a new game thread and launch it
			gameThread = new GameThread( gameLoaded, mainMenu );
			gameThread.start();
		} 
	}
	
	public void goOptions(){
		mainMenu.switchToOptionsPanel();
	}
	
	public void goCredits(){
		mainMenu.switchToCreditsPanel();
	}
	
	public void exitGame(){
		System.exit(0);
	}
	
	public void next(){
		current++;
		if( current > 4 )
			current = 0;
	}
	
	public void prev(){
		current--;
		if( current < 0 )
			current = 4;
	}
	
	public void refresh(){
		for( int i = 0; i < buttonContainer.size(); i++ )
			if( i == current )
				buttonContainer.get(i).setIcon(new ImageIcon(menuImageContainer.get(i*2+1)));
			else
				buttonContainer.get(i).setIcon(new ImageIcon(menuImageContainer.get(i*2)));		
	}
}

//quando ritorno nel frame il pannello ha perso il focus e non vanno più i tasti