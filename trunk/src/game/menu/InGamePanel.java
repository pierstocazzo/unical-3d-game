package game.menu;

import game.common.GameConfiguration;
import game.common.GameTimer;
import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class GamePanel
 * It create a panel used in InGameMenu class
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class InGamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/** current selected item */
	public int current = 0;
	/** images of every items */
	ArrayList<String> imageFolder;
	/** Preloaded images */
	ArrayList<Image> imageContainer;
	/** Pointer to Game Menu (owner this panel)*/
	InGameMenu gameMenu;
	
	JPanel localPanel;
	
	/**
	 * Constructor of InGamePanel Class
	 * 
	 * @param gameMenu - Game Menu
	 */
	public InGamePanel(InGameMenu gameMenu){
		super();
		this.gameMenu = gameMenu;
		setCursor(Cursor.getDefaultCursor());
		imageFolder = ImagesContainer.getMenuImagesFolders();
		if(GameConfiguration.isFullscreen().equals("true"))
			imageContainer = ImagesContainer.getInGameMenuImagesContainer_with_fullscreen();
		else
			imageContainer = ImagesContainer.getInGameMenuImagesContainer_no_fullscreen();
		
		initItem();
		
		this.setLayout(new GridLayout(5,1));
		this.setOpaque(false);
	}
	
	/**
	 * It initializes images used in panel components
	 */
	public void initItem(){
		
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
				button.setIcon(new ImageIcon(imageContainer.get(index+1)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setIcon(new ImageIcon(imageContainer.get(index)));
			}

			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		}
		JButton buttonResume = new JButton(new ImageIcon(imageContainer.get(0)));
		buttonResume.setBorderPainted(false);
		buttonResume.setContentAreaFilled(false);
		buttonResume.addMouseListener( new MouseHandler(buttonResume, 0){
			@Override
			public void mouseClicked(MouseEvent e) {
				// return to game
				gameMenu.setVisible(false);
				// reset timer for avoid game problems
				GameTimer.reset();
				// active a game main loop
				gameMenu.game.enabled = true;
			}
		});
		add(buttonResume);
		
		JButton buttonSave = new JButton(new ImageIcon(imageContainer.get(2)));
		buttonSave.setBorderPainted(false);
		buttonSave.setContentAreaFilled(false);
		buttonSave.addMouseListener( new MouseHandler(buttonSave, 2){
			@Override
			public void mouseClicked(MouseEvent e) {
				gameMenu.setVisible(false);
				// create a new frame
				SaveMenu sm = new SaveMenu(gameMenu);
				sm.setVisible(true);
			}
		});
		add(buttonSave);
		
		JButton buttonExit = new JButton(new ImageIcon(imageContainer.get(4)));
		buttonExit.setBorderPainted(false);
		buttonExit.setContentAreaFilled(false);
		buttonExit.addMouseListener( new MouseHandler(buttonExit, 4){
			@Override
			public void mouseClicked(MouseEvent e) {
				// close all (game and frames)
				gameMenu.setVisible(false);
				gameMenu.game.finish();
			}
		});
		add(buttonExit);
	}
}
