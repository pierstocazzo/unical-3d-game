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

package game.common;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * A class that contains and handles all images
 * <br>
 * used in the main menu of the Game
 * 
 * @author Salvatore Loria, Andrea Martire, Giuseppe Leone
 *
 */
public class ImagesContainer {

	static Dimension screenSize;
	
	static Image backgroundMainMenu;
	
	/** List of image path */
	static ArrayList<String> menuImagesFolders;
	
	/** Preloaded images */
	static ArrayList<Image> menuImagesContainer;
	
	static Image backgroundCreditsFrame;
	
	static Image background_no_FullScreen;
	static Image background_with_FullScreen;
	
	static ArrayList<String> inGameMenuImagesFolder;
	static ArrayList<Image> inGameMenuImagesContainer_with_fullscreen;

	static ArrayList<Image> inGameMenuImagesContainer_no_fullscreen;

	public static void init(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// get background image
		backgroundMainMenu = Toolkit.getDefaultToolkit().getImage( "src/game/data/images/menu/background.jpg" );
		
		// scale background image respect screen size
		backgroundMainMenu = backgroundMainMenu.getScaledInstance(
				screenSize.width, screenSize.height, Image.SCALE_FAST);
		
		initMenuImagesComponentFolders();
		
		backgroundCreditsFrame = Toolkit.getDefaultToolkit().getImage( 
				"src/game/data/images/menu/credits16_9.jpg" );
		backgroundCreditsFrame = backgroundCreditsFrame.getScaledInstance(
				screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
		
		// get background image
		background_no_FullScreen = Toolkit.getDefaultToolkit().getImage( 
				"src/game/data/images/menu/background.jpg" );
		background_no_FullScreen = background_no_FullScreen.getScaledInstance(
				screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
		
		// get background image
		background_with_FullScreen = Toolkit.getDefaultToolkit().getImage( 
			"src/game/data/images/menu/background.jpg" );
		background_with_FullScreen = background_with_FullScreen.getScaledInstance(
			GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ), 
			GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT ), 
			Image.SCALE_FAST
		);
		
		initInGameMenuImage_no_Fullscreen();
		initInGameMenuImage_with_Fullscreen();
	}
	
	/**
	 * Initialize image path list
	 */
	public static void initMenuImagesComponentFolders(){
		menuImagesFolders = new ArrayList<String>();
		menuImagesFolders.add("src/game/data/images/menu/newgame.png");
		menuImagesFolders.add("src/game/data/images/menu/newgame2.png");
		menuImagesFolders.add("src/game/data/images/menu/load.png");
		menuImagesFolders.add("src/game/data/images/menu/load2.png");
		menuImagesFolders.add("src/game/data/images/menu/options.png");
		menuImagesFolders.add("src/game/data/images/menu/options2.png");
		menuImagesFolders.add("src/game/data/images/menu/credits.png");
		menuImagesFolders.add("src/game/data/images/menu/credits2.png");
		menuImagesFolders.add("src/game/data/images/menu/exit.png");
		menuImagesFolders.add("src/game/data/images/menu/exit2.png");
		menuImagesContainer = new ArrayList<Image>();
		// scale all image respect screen size
		for(int i=0; i<menuImagesFolders.size(); i++){
			Image img = Toolkit.getDefaultToolkit().getImage( menuImagesFolders.get(i) );
			img = img.getScaledInstance(screenSize.width/3, screenSize.height/10, Image.SCALE_SMOOTH);
			menuImagesContainer.add(img);
		}
	}
	
	/**
	 * Initialize image path list
	 */
	public static void initInGameMenuImage_no_Fullscreen(){
		inGameMenuImagesFolder = new ArrayList<String>();
		inGameMenuImagesFolder.add("src/game/data/images/menu/resume.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/resume2.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/save.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/save2.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/exit.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/exit2.png");
		inGameMenuImagesContainer_no_fullscreen = new ArrayList<Image>();
		// Scale every image respect screen size
		for(int i=0; i<inGameMenuImagesFolder.size(); i++){
			Image img = Toolkit.getDefaultToolkit().getImage( inGameMenuImagesFolder.get(i) );
			img = img.getScaledInstance(screenSize.width/3, screenSize.height/10, Image.SCALE_SMOOTH);
			inGameMenuImagesContainer_no_fullscreen.add( img );
		}
	}
	
	/**
	 * Initialize image path list
	 */
	public static void initInGameMenuImage_with_Fullscreen(){
		inGameMenuImagesFolder = new ArrayList<String>();
		inGameMenuImagesFolder.add("src/game/data/images/menu/resume.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/resume2.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/save.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/save2.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/exit.png");
		inGameMenuImagesFolder.add("src/game/data/images/menu/exit2.png");
		Dimension screenSizeFullscreen = new Dimension(
			GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ), 
			GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT )
		);
		inGameMenuImagesContainer_with_fullscreen = new ArrayList<Image>();
		// Scale every image respect screen size
		for(int i=0; i<inGameMenuImagesFolder.size(); i++){
			Image img = Toolkit.getDefaultToolkit().getImage( inGameMenuImagesFolder.get(i) );
			img = img.getScaledInstance(screenSizeFullscreen.width/3, 
					screenSizeFullscreen.height/10, Image.SCALE_FAST);
			inGameMenuImagesContainer_with_fullscreen.add( img );
		}
	}
	
	public static Image getBackgroundMainMenu() {
		return backgroundMainMenu;
	}
	
	public static ArrayList<String> getMenuImagesFolders() {
		return menuImagesFolders;
	}

	public static ArrayList<Image> getMenuImagesContainer() {
		return menuImagesContainer;
	}

	public static Image getBackgroundCreditsFrame() {
		return backgroundCreditsFrame;
	}
	
	public static Image getBackground_no_FullScreen() {
		return background_no_FullScreen;
	}

	public static Image getBackground_with_FullScreen() {
		return background_with_FullScreen;
	}
	
	public static ArrayList<Image> getInGameMenuImagesContainer_with_fullscreen() {
		return inGameMenuImagesContainer_with_fullscreen;
	}

	public static ArrayList<Image> getInGameMenuImagesContainer_no_fullscreen() {
		return inGameMenuImagesContainer_no_fullscreen;
	}
}
