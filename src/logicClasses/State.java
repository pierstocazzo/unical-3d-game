package logicClasses;

/**
 * State's Container
 * 
 * @author Andrea
 */
public class State {
	/** Resting state */
	final static int DEFAULT = 0;
	/** Sentry state with fixed path */
	final static int SENTINEL = 1;
	/** Guard state with fixed position */
	final static int GUARD = 2;
	/** Alert state */
	final static int ALERT = 3;
	/** Attack state */
	final static int ATTACK = 4;
	/** Death state */
	final static int DEATH = 5;
}
