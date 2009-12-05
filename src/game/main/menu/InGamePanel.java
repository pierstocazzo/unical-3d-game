package game.main.menu;

import java.awt.GridLayout;
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
	ArrayList<ImageIcon> imageContainer;
	/** Pointer to Game Menu (owner this panel)*/
	InGameMenu gm;
	/** Pointer to Main Menu */
	MainMenu mm;
	
	/**
	 * Constructor
	 * 
	 * @param gm - Game Menu
	 * @param mm - Main Menu
	 */
	public InGamePanel(InGameMenu gm, MainMenu mm){
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
		item.add( new JLabel(imageContainer.get(1)));
		this.add(item.get(0));
		item.add( new JLabel(imageContainer.get(2)));
		this.add(item.get(1));
		item.add( new JLabel(imageContainer.get(4)));
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
		imageContainer = new ArrayList<ImageIcon>();
		for(int i=0; i<imageFolder.size(); i++)
			imageContainer.add( new ImageIcon(imageFolder.get(i)) );
	}

	/**
	 * Execute operation of selected element
	 */
	public void executeSelectedItem(){
		switch (current){
			case 0:{mm.tc.waitThread();
					//force repaint on top of this frame
					gm.setVisible(false);
					gm.setVisible(true);
					break;}
			case 1:{gm.setVisible(false);
					SaveMenu sm = new SaveMenu(gm);
					sm.setVisible(true);break;}
			case 2:{gm.setVisible(false);
					mm.tc.notifyCloseGame();
					System.out.println("exit game menu");
//					System.exit(0);
					break;}
			default:break;
		}
	}
}
