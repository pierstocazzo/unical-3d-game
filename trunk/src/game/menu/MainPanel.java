package game.menu;

import java.awt.GridLayout;
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
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/** List of menu items */
	ArrayList<JLabel> item;
	/** Number of current element */
	public int current = 0;
	/** List of image path */
	ArrayList<String> imageFolder;
	/** Preloaded images */
	ArrayList<ImageIcon> imageContainer;
	/** MainMenu */
	MainMenu mm;
	
	GameThread gameThread;
	
	/**
	 * Constructor
	 * 
	 * @param mm - MainMenu
	 */
	public MainPanel(MainMenu mm){
		super();
		this.mm = mm;
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
	 * 
	 * @return (int) current
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
	 * @param i
	 * @return (String) path
	 */
	public Icon StandardImage(int i){
		return imageContainer.get(i*2);
	}
	
	/**
	 * Get Path String of selected image of element i
	 * 
	 * @param i
	 * @return (String) path
	 */
	public Icon SelectedImage(int i){
		return imageContainer.get(i*2+1);
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
		item.add( new JLabel(imageContainer.get(1)));
		this.add(item.get(0));
		item.add( new JLabel(imageContainer.get(2)));
		this.add(item.get(1));
		item.add( new JLabel(imageContainer.get(4)));
		this.add(item.get(2));
		item.add( new JLabel(imageContainer.get(6)));
		this.add(item.get(3));
		item.add( new JLabel(imageContainer.get(8)));
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
		imageContainer = new ArrayList<ImageIcon>();
		for(int i=0; i<imageFolder.size(); i++)
			imageContainer.add( new ImageIcon(imageFolder.get(i)) );
	}

	/**
	 * Execute operation of selected element
	 * @throws InterruptedException 
	 */
	public void executeSelectedItem(){
		switch (current){
			case 0:
				mm.setAlwaysOnTop(false);
				
				//Loading Bar
				LoadingFrame load = new LoadingFrame();
				load.setVisible(true);
				load.setAlwaysOnTop(true);
		
				gameThread = new GameThread(load);
				
				System.out.println("New Game");
				Thread gameThreadNew = new Thread( gameThread );
				gameThreadNew.start();
				
				mm.setVisible(false);
				System.out.println("Exit from Main Menu");break;
			case 1:
				//Load game with fileChooser
				mm.showCursor();
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					System.out.println("Opening: " + file.getName() + ".");
					
					FileInputStream fin = null;
					try { fin = new FileInputStream("gameSave/"+file.getName());} 
					catch (FileNotFoundException e) {e.printStackTrace();}
					ObjectInputStream ois = null;
					try { ois = new ObjectInputStream(fin); } 
					catch (IOException e) { e.printStackTrace(); }
					LogicWorld gameLoaded = null;
					try {try { gameLoaded = (LogicWorld) ois.readObject();
					} catch (IOException e) {e.printStackTrace();}}
					catch (ClassNotFoundException e) {e.printStackTrace();} 
					
					//Loading Bar
					LoadingFrame loadingFrame = new LoadingFrame();
					loadingFrame.setVisible(true);
					loadingFrame.setAlwaysOnTop(true);
					
					gameThread = new GameThread(gameLoaded,loadingFrame);
					
					Thread gameThreadLoaded = new Thread( gameThread );
					gameThreadLoaded.start();
					mm.setVisible(false);
					System.out.println("Exit from Main Menu");
				} 
				mm.hideCursor();
				break;
			case 2:break;//todo Options
			case 3:break;//todo Credits
			case 4:
				System.out.println("exit main menu");
				System.exit(0);
				break;
		}
	}
}
