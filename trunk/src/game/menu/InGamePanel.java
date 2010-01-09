package game.menu;

import game.common.GameTimer;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class GamePanel
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class InGamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/** current selected item */
	public int current = 0;
	/** items of game menu */
	ArrayList<JLabel> item;
	/** images of every items */
	ArrayList<String> imageFolder;
	/** Preloaded images */
	ArrayList<Image> imageContainer;
	/** Pointer to Game Menu (owner this panel)*/
	InGameMenu gm;
	
	/**
	 * Constructor
	 * 
	 * @param gm - Game Menu
	 * @param mm - Main Menu
	 */
	public InGamePanel(InGameMenu gm){
		super();
		this.gm = gm;
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
		return new ImageIcon(imageContainer.get(i*2));
	}
	
	/**
	 * Get Path String of selected image of element i
	 * 
	 * @param i
	 * @return (String) path
	 */
	public Icon SelectedImage(int i){
		return new ImageIcon(imageContainer.get(i*2+1));
	}
	
	/**
	 * Refresh Game Menu
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
	}
	
	/**
	 * Initialize image path list
	 */
	public void initImageFolder(){
		imageFolder = new ArrayList<String>();
		imageFolder.add("src/game/data/images/menu/resume.png");
		imageFolder.add("src/game/data/images/menu/resume2.png");
		imageFolder.add("src/game/data/images/menu/save.png");
		imageFolder.add("src/game/data/images/menu/save2.png");
		imageFolder.add("src/game/data/images/menu/exit.png");
		imageFolder.add("src/game/data/images/menu/exit2.png");
		imageContainer = new ArrayList<Image>();
		for(int i=0; i<imageFolder.size(); i++){
			Image img = Toolkit.getDefaultToolkit().getImage( imageFolder.get(i) );
			img = img.getScaledInstance(gm.screenSize.width/3, gm.screenSize.height/10, Image.SCALE_DEFAULT);
			imageContainer.add( img );
		}
	}

	/**
	 * Execute operation of selected element
	 */
	public void executeSelectedItem(){
		switch (current){
			case 0:
				gm.setVisible(false);
				GameTimer.reset();
				gm.game.enabled = true;
				break;
			case 1:
				gm.setVisible(false);
				SaveMenu sm = new SaveMenu(gm);
				sm.setVisible(true);
				break;
			case 2:
				gm.setVisible(false);
				System.exit(0);
				System.out.println("exit game menu");
				break;
		}
	}
}
