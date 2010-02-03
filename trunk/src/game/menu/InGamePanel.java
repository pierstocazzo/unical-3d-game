/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	/** Container */
	ArrayList<JButton> buttonsContainer;
	
	/** Pointer to Game Menu (owner this panel)*/
	MainMenu gameMenu;
	
	/** Component's container */
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
		buttonsContainer = new ArrayList<JButton>();
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
		
		setFocusable(true);
		addKeyListener( new KeyAdapter() { 
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
							case 0:goResume();break;
							case 1:goSave();break;
							case 2:exitGame();break;
						}
					}
				}
			});
		
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
		
		class MouseHandler extends MouseAdapter {
			
			JButton button;
			int index;
			public MouseHandler( JButton button, int index ){
				this.button = button;
				this.index = index;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				button.setIcon(new ImageIcon(imageContainer.get(index+1)));
				current = (int) index / 2;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setIcon(new ImageIcon(imageContainer.get(index)));
			}
		}
		
		JButton buttonResume = new JButton(new ImageIcon(imageContainer.get(0)));
		buttonResume.setBorderPainted(false);
		buttonResume.setContentAreaFilled(false);
		buttonResume.addMouseListener( new MouseHandler(buttonResume, 0){
			@Override
			public void mouseClicked(MouseEvent e) {
				goResume();
			}
		});
		buttonResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				goResume();
			}
		});
		buttonResume.setMnemonic(KeyEvent.VK_R);
		centerPanel.add(buttonResume);
		buttonsContainer.add(buttonResume);
		
		JButton buttonSave = new JButton(new ImageIcon(imageContainer.get(2)));
		buttonSave.setBorderPainted(false);
		buttonSave.setContentAreaFilled(false);
		buttonSave.addMouseListener( new MouseHandler(buttonSave, 2){
			@Override
			public void mouseClicked(MouseEvent e) {
				goSave();
			}
		});
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				goSave();
			}
		});
		buttonSave.setMnemonic(KeyEvent.VK_S);
		centerPanel.add(buttonSave);
		buttonsContainer.add(buttonSave);
		
		JButton buttonExit = new JButton(new ImageIcon(imageContainer.get(4)));
		buttonExit.setBorderPainted(false);
		buttonExit.setContentAreaFilled(false);
		buttonExit.addMouseListener( new MouseHandler(buttonExit, 4){
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
		centerPanel.add(buttonExit);
		buttonsContainer.add(buttonExit);
		
		requestFocusInWindow();
		
	}
	
	/**
	 * Resume the current game
	 */
	public void goResume(){
		// return to game
		gameMenu.setVisible(false);
		// reset timer for avoid game problems
		GameTimer.reset();
		// active a game main loop
		gameMenu.game.enabled = true;
	}
	
	/**
	 * It allows to save the game
	 */
	public void goSave(){
		gameMenu.switchToPanel("savePanel");
	}
	
	/**
	 * Exit from the application
	 */
	public void exitGame(){
		// close all (game and frames)
		gameMenu.setVisible(false);
		gameMenu.game.finish();
	}
	
	/**
	 * Move to next element
	 */
	public void next(){
		current++;
		if( current > 2 )
			current = 0;
	}
	
	/**
	 * Move to previous element
	 */
	public void prev(){
		current--;
		if( current < 0 )
			current = 2;
	}
	
	/**
	 * Refresh the menu
	 */
	public void refresh(){
		for( int i = 0; i < buttonsContainer.size(); i++ )
			if( i == current )
				buttonsContainer.get(i).setIcon(new ImageIcon(imageContainer.get(i*2+1)));
			else
				buttonsContainer.get(i).setIcon(new ImageIcon(imageContainer.get(i*2)));		
	}
}
