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

package game.main;

import game.core.LogicWorld;
import game.graphics.GraphicalWorld;
import game.menu.MainMenu;

/**
 * Class GameThread
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameThread extends Thread {

	LogicWorld logicGame;
	GraphicalWorld game;
	MainMenu menu;
	boolean isLoaded;
	
	
	public GameThread( MainMenu menu ) {
		this.menu = menu;
		logicGame = new LogicWorld();
    	isLoaded = false;
	}
	
	public GameThread( LogicWorld logicGame, MainMenu menu ){
		this.menu = menu;
		this.logicGame = logicGame;
		isLoaded = true;
	}

	public void run() {
		game = new GraphicalWorld( logicGame, menu, isLoaded );
		menu.setGame( game );
        game.start();
    }
	
	public void quit() {
		game.finish();
	}
}
