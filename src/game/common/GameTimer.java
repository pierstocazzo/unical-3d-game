package game.common;

import com.jme.util.Timer;

public class GameTimer {
	public static Timer timer;
	
	public static void update() {
		timer.update();
	}
	
	public static void reset() {
		timer.reset();
	}
	
	public static long getTime() {
		return timer.getTime();
	}
	
	public static float getTimeInSeconds() {
		return timer.getTimeInSeconds();
	}
	
	public static float getFrameRate() {
		return timer.getFrameRate();
	}
	
	public static float getTimePerFrame() {
		return timer.getTimePerFrame();
	}

	public static void initTimer() {
		timer = Timer.getTimer();
		timer.reset();
	}
}

