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

/**
 * Class Game
 * 
 * Useful class that allow to divide initialization
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public abstract class Game extends PhysicsGame {
	
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
