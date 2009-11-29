package andreaFolder.test;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class GamePanel
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GamePanel extends JPanel {

	/** current selected item */
	public int current = 0;
	/** items of game menu */
	ArrayList<JLabel> item;
	/** images of every items */
	ArrayList<String> imageFolder;
	/** Pointer to Game Menu (owner this panel)*/
	GameMenu gm;
	/** Pointer to Main Menu */
	MainMenu mm;
	
	/**
	 * Constructor
	 * 
	 * @param gm - Game Menu
	 * @param mm - Main Menu
	 */
	public GamePanel(GameMenu gm, MainMenu mm){
		super();
		this.gm = gm;
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
	 * Refresh Game Menu
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
	}
	
	/**
	 * Initialize image path list
	 */
	public void initImageFolder(){
		imageFolder = new ArrayList<String>();
		imageFolder.add("src/andreaFolder/continue.gif");
		imageFolder.add("src/andreaFolder/continue2.gif");
		imageFolder.add("src/andreaFolder/save.gif");
		imageFolder.add("src/andreaFolder/save2.gif");
		imageFolder.add("src/andreaFolder/exit.gif");
		imageFolder.add("src/andreaFolder/exit2.gif");
	}

	/**
	 * Execute operation of selected element
	 */
	public void executeSelectedItem(){
		switch (current){
			case 0:{gm.setVisible(false);
					mm.tc.waitThread();
					gm.setVisible(true);break;}
			case 1:{gm.setVisible(false);
					SaveMenu sm = new SaveMenu(gm);
					sm.setVisible(true);break;}
			case 2:{gm.setVisible(false);
					mm.setVisible(true);
					mm.tc.notifyCloseGame();break;}
			default:break;
		}
	}
}
