package game.menu;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.core.LogicWorld;
import game.main.GameThread;

/**
 * Class MainPanel
 * Center Panel displayed in main frame
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class MainPanel extends JPanel {
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** List of menu items */
	ArrayList<JLabel> item;
	
	/** Number of current element */
	public int current = 0;
	
	/** List of image path */
	ArrayList<String> imageFolder;
	
	/** Preloaded images */
	ArrayList<Image> imageContainer;
	
	/** MainMenu */
	MainMenu mainMenu;
	
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
		initImageFolder();
		initItem();
		
		this.setLayout(new GridLayout(5,1));
		this.setOpaque(false);
	}
	
	/**
	 * Get previous element number
	 * 
	 * @return (int) current
	 */
	public int prev(){
		this.current = this.current + 1;
		if(this.current >= item.size())
			this.current = 0;
		refreshMenu();
		return current;
	}
	
	/**
	 * Get next element number
	 */
	public int next(){
		this.current = this.current - 1;
		if(this.current < 0)
			this.current = item.size() - 1;
		refreshMenu();
		return current;
	}
	
	/**
	 * Get Path String of unselected image of element i
	 * 
	 * @param (int) i
	 * @return (String) path
	 */
	public Icon StandardImage(int i){
		return new ImageIcon(imageContainer.get(i*2));
	}
	
	/**
	 * Get Path String of selected image of element i
	 * 
	 * @param (int) i
	 * @return (String) path
	 */
	public Icon SelectedImage(int i){
		return new ImageIcon(imageContainer.get(i*2+1));
	}
	
	/**
	 * Refresh Main Menu
	 */
	public void refreshMenu(){
		for( int i=0; i < item.size(); i++ ){
			if(current == i)
				item.get(i).setIcon(SelectedImage(i));
			else
				item.get(i).setIcon(StandardImage(i));
		}
	}
	
	/**
	 * Initialize item images
	 */
	public void initItem(){
		item = new ArrayList<JLabel>();
		item.add( new JLabel(new ImageIcon(imageContainer.get(1))));
		this.add(item.get(0));
		item.add( new JLabel(new ImageIcon(imageContainer.get(2))));
		this.add(item.get(1));
		item.add( new JLabel(new ImageIcon(imageContainer.get(4))));
		this.add(item.get(2));
		item.add( new JLabel(new ImageIcon(imageContainer.get(6))));
		this.add(item.get(3));
		item.add( new JLabel(new ImageIcon(imageContainer.get(8))));
		this.add(item.get(4));
	}
	
	/**
	 * Initialize image path list
	 */
	public void initImageFolder(){
		imageFolder = new ArrayList<String>();
		imageFolder.add("src/game/data/images/menu/newgame.png");
		imageFolder.add("src/game/data/images/menu/newgame2.png");
		imageFolder.add("src/game/data/images/menu/load.png");
		imageFolder.add("src/game/data/images/menu/load2.png");
		imageFolder.add("src/game/data/images/menu/options.png");
		imageFolder.add("src/game/data/images/menu/options2.png");
		imageFolder.add("src/game/data/images/menu/credits.png");
		imageFolder.add("src/game/data/images/menu/credits2.png");
		imageFolder.add("src/game/data/images/menu/exit.png");
		imageFolder.add("src/game/data/images/menu/exit2.png");
		imageContainer = new ArrayList<Image>();
		// scale all image respect screen size
		for(int i=0; i<imageFolder.size(); i++){
			Image img = Toolkit.getDefaultToolkit().getImage( imageFolder.get(i) );
			img = img.getScaledInstance(mainMenu.screenSize.width/3, mainMenu.screenSize.height/10, Image.SCALE_SMOOTH);
			imageContainer.add(img);
		}
	}

	/**
	 * Execute operation of selected element
	 * @throws InterruptedException 
	 */
	public void executeSelectedItem(){
		switch (current){
			case 0:
				mainMenu.setAlwaysOnTop(false);
				
				// Create a Loading Bar
				LoadingFrame load = new LoadingFrame();
		
				// Create a new game thread and launch it
				gameThread = new GameThread(load);
				Thread gameThreadNew = new Thread( gameThread );
				gameThreadNew.start();
				
				mainMenu.setVisible(false);break;
			case 1:
				//Load game with fileChooser
				mainMenu.showCursor();
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println("Opening: " + file.getName() + ".");
					
					FileInputStream fin = null;
					try {
							fin = new FileInputStream("gameSave/"+file.getName());
						}
					catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					
					ObjectInputStream ois = null;
					try { 
							ois = new ObjectInputStream(fin); 
						} 
					catch (IOException e) { 
							e.printStackTrace(); 
						}
					
					LogicWorld gameLoaded = null;
					try {
							try { 
									gameLoaded = (LogicWorld) ois.readObject();
								} 
							catch (IOException e) {
									e.printStackTrace();
								}
						}
					catch (ClassNotFoundException e) {
							e.printStackTrace();
						} 
					
					//Create a Loading Bar
					LoadingFrame loadingFrame = new LoadingFrame();
					
					// create a new game thread and launch it
					gameThread = new GameThread(gameLoaded,loadingFrame);
					Thread gameThreadLoaded = new Thread( gameThread );
					gameThreadLoaded.start();
					
					mainMenu.setVisible(false);
				} 
				mainMenu.hideCursor();
				break;
			case 2:
				// create a optionsMenu
				OptionsMenu om = new OptionsMenu(mainMenu);
				om.setVisible(true);
				mainMenu.setVisible(false);
				break;
			case 3:
				// create a CreditsFrame
				CreditsFrame cm = new CreditsFrame(mainMenu);
				cm.setVisible(true);
				mainMenu.setVisible(false);
				break;
			case 4:
				// close all
				System.exit(0);
				break;
		}
	}
}