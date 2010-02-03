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

import com.jme.util.Timer;

/**
 * Class Game Timer
 * Used for manage time in the game
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class GameTimer {
	
	/** pointer to game timer */
	public static Timer timer;
	
	/** Update game timer */
	public static void update() {
		timer.update();
	}
	
	/** reset game timer */
	public static void reset() {
		timer.reset();
	}
	
	/** 
	 * Get current time
	 * 
	 * @return (long)
	 */
	public static long getTime() {
		return timer.getTime();
	}
	
	/** 
	 * Get current time in seconds
	 * 
	 * @return (float)
	 */
	public static float getTimeInSeconds() {
		return timer.getTimeInSeconds();
	}
	
	/**
	 * Get current frame rate
	 * 
	 * @return (float)
	 */
	public static float getFrameRate() {
		return timer.getFrameRate();
	}
	
	/**
	 * Return time between the last call and the current
	 * 
	 * @return (float)
	 */
	public static float getTimePerFrame() {
		return timer.getTimePerFrame();
	}

	/**
	 * Initialize game timer
	 */
	public static void initTimer() {
		timer = Timer.getTimer();
		timer.reset();
	}
}