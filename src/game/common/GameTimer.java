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