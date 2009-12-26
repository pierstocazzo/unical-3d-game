package andreaFolder.test;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import andreaFolder.TestSwing.FileChooserDemo;
import andreaFolder.core.LogicWorld;

/**
 * Class MainPanel
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class MainPanel extends JPanel {

	/** List of menu items */
	ArrayList<JLabel> item;
	/** Number of current element */
	public int current = 0;
	/** List of image path */
	ArrayList<String> imageFolder;
	/** Thread Monitor */
	ThreadController tc;
	/** MainMenu */
	MainMenu mm;
	
	/**
	 * Constructor
	 * 
	 * @param tc - ThreadController
	 * @param mm - MainMenu
	 */
	public MainPanel(ThreadController tc, MainMenu mm){
		super();
		this.tc = tc;
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
	public String StandardImage(int i){
		return imageFolder.get(i*2);
	}
	
	/**
	 * Get Path String of selected image of element i
	 * 
	 * @param i
	 * @return (String) path
	 */
	public String SelectedImage(int i){
		return imageFolder.get(i*2+1);
	}
	
	/**
	 * Refresh Main Menu
	 */
	public void refreshMenu(){
		for( int i=0; i < item.size(); i++ ){
			if(current == i)
				item.get(i).setIcon(new ImageIcon(SelectedImage(i)));
			else
				item.get(i).setIcon(new ImageIcon(StandardImage(i)));
		}
	}
	
	/**
	 * Initialize item images
	 */
	public void initItem(){
		item = new ArrayList<JLabel>();
		item.add( new JLabel(new ImageIcon(imageFolder.get(1))));
		this.add(item.get(0));
		item.add( new JLabel(new ImageIcon(imageFolder.get(2))));
		this.add(item.get(1));
		item.add( new JLabel(new ImageIcon(imageFolder.get(4))));
		this.add(item.get(2));
		item.add( new JLabel(new ImageIcon(imageFolder.get(6))));
		this.add(item.get(3));
		item.add( new JLabel(new ImageIcon(imageFolder.get(8))));
		this.add(item.get(4));
	}
	
	/**
	 * Initialize image path list
	 */
	public void initImageFolder(){
		imageFolder = new ArrayList<String>();
		imageFolder.add("src/andreaFolder/new.gif");
		imageFolder.add("src/andreaFolder/new2.gif");
		imageFolder.add("src/andreaFolder/load.gif");
		imageFolder.add("src/andreaFolder/load2.gif");
		imageFolder.add("src/andreaFolder/options.gif");
		imageFolder.add("src/andreaFolder/options2.gif");
		imageFolder.add("src/andreaFolder/credits.gif");
		imageFolder.add("src/andreaFolder/credits2.gif");
		imageFolder.add("src/andreaFolder/exit.gif");
		imageFolder.add("src/andreaFolder/exit2.gif");
	}

	/**
	 * Execute operation of selected element
	 */
	public void executeSelectedItem(){
		switch (current){
			case 0:{mm.setAlwaysOnTop(false);
					System.out.println("New Game");
					mm.game = new testGame(tc);
					Thread gameThread = new Thread(mm.game);
					gameThread.start();
					tc.waitThread();
					mm.setVisible(false);
					GameMenu gameMenu = new GameMenu(tc,mm);
					gameMenu.setVisible(true);
					System.out.println("GameMenu");break;}
			case 1:{//Load game with fileChooser
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
						
						mm.tc.close=false;
						mm.game = new testGame(mm.tc,gameLoaded);
						Thread gameThread = new Thread(mm.game);
						gameThread.start();
						tc.waitThread();
						System.out.println("gioco caricato in pausa, svegliato swing");
						mm.setVisible(false);
						GameMenu gameMenu = new GameMenu(tc,mm);
						gameMenu.setVisible(true);
						System.out.println("GameMenu");
					} 
					else
						System.out.println("Open command cancelled by user.");
					mm.hideCursor();
					break;}
			case 2:break;//todo Options
			case 3:break;//todo Credits
			case 4:{tc.notifyCloseGame();
					System.exit(0);break;}
			default:break;
		}
	}
}