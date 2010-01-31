package game.menu;

import game.common.GameConfiguration;
import game.common.GameTimer;
import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	/** Preloaded images */
	ArrayList<Image> imageContainer;
	/** Pointer to Game Menu (owner this panel)*/
	MainMenu gameMenu;
	
	JPanel localPanel;
	
	/**
	 * Constructor of InGamePanel Class
	 * 
	 * @param gameMenu - Game Menu
	 */
	public InGamePanel(MainMenu gameMenu){
		super();
		this.gameMenu = gameMenu;
		setCursor(Cursor.getDefaultCursor());
		
		if( GameConfiguration.isFullscreen().equals("true") ) {
			imageContainer = ImagesContainer.getInGameMenuImagesContainer_with_fullscreen();
		} else {
			imageContainer = ImagesContainer.getInGameMenuImagesContainer_no_fullscreen();
		}
		
		setLayout(new BorderLayout());
		setOpaque(false);
		initItem();
	}
	
	/**
	 * It initializes images used in panel components
	 */
	public void initItem(){
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new GridLayout(3,1) );
		centerPanel.setOpaque(false);
		add(centerPanel, BorderLayout.CENTER);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize( new Dimension(gameMenu.screenSize.width/4, 1 ) );
		add( pVerticalEmpty1, BorderLayout.WEST );
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize( new Dimension(gameMenu.screenSize.width/4, 1 ) );
		add( pVerticalEmpty2, BorderLayout.EAST );
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize( new Dimension( 1, gameMenu.screenSize.height/3) );
		add( pHorizontalEmpty1, BorderLayout.SOUTH );
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize( new Dimension( 1, gameMenu.screenSize.height/4 ) );
		add( pHorizontalEmpty2, BorderLayout.NORTH );
		
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
		buttonResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// return to game
				gameMenu.setVisible(false);
				// reset timer for avoid game problems
				GameTimer.reset();
				// active a game main loop
				gameMenu.game.enabled = true;
			}
		});
		buttonResume.setMnemonic(KeyEvent.VK_R);
		centerPanel.add(buttonResume);
		
		JButton buttonSave = new JButton(new ImageIcon(imageContainer.get(2)));
		buttonSave.setBorderPainted(false);
		buttonSave.setContentAreaFilled(false);
		buttonSave.addMouseListener( new MouseHandler(buttonSave, 2){
			@Override
			public void mouseClicked(MouseEvent e) {
				gameMenu.switchToSavePanel();
			}
		});
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameMenu.switchToSavePanel();
			}
		});
		buttonSave.setMnemonic(KeyEvent.VK_S);
		centerPanel.add(buttonSave);
		
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
		buttonExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// close all (game and frames)
				gameMenu.setVisible(false);
				gameMenu.game.finish();
			}
		});
		buttonExit.setMnemonic(KeyEvent.VK_X);
		centerPanel.add(buttonExit);
		
		requestFocusInWindow();
		
	}
	
	public void next(){
		current++;
		if( current > 2 )
			current = 0;
	}
	
	public void prev(){
		current--;
		if( current < 0 )
			current = 2;
	}
}
