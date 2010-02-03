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

package game.base;

import game.common.GameConfiguration;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

/**
 * Class Game
 * 
 * Useful class that allow to divide initialization
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public abstract class Game extends PhysicsGame {
	
	/** Used for debug mode */
	public InputHandler freeCamInput;
	
    @Override
    protected void setupGame() {
    	getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_init" ) );
    	getMenu().setProgress(5);
        setupInit();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_environment" ) );
        getMenu().setProgress(20);
        setupEnvironment();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_players" ) );
        getMenu().setProgress(60);
        setupPlayer();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_enemies" ) );
        getMenu().setProgress(80);
        setupEnemies();
        getMenu().setLoadingText( GameConfiguration.getPhrase( "loading_input" ) );
        setupCamera();
        setupInput();
        getMenu().setProgress(100);
        freeCamInput = new FirstPersonHandler( cam, 200, 1 );
        freeCamInput.setEnabled( false );
        try{
        	Thread.sleep(2000);
        } catch (Exception e) {
			// TODO: handle exception
		}
        
        getMenu().hideMenu();
    }
    
    /** initialize system */
    public abstract void setupInit();
    
    /** initialize environment */
    public abstract void setupEnvironment();
    
    /** initialize player */
    public abstract void setupPlayer();
    
    /** initialize enemies */
    public abstract void setupEnemies();
    
    /** initialize camera */
    public abstract void setupCamera();
    
    /** initialize input handler */
    public abstract void setupInput();
}
