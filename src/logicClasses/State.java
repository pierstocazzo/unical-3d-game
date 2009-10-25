package logicClasses;

/**
 * Contenitore di Stati
 * 
 * @author Andrea
 */
public class State {
	/** Stato di riposo */
	final static int DEFAULT = 0;
	/** Stato di sentinella con percorso prefissato*/
	final static int SENTINEL = 1;
	/** Stato di guardia con posizione fissa */
	final static int GUARD = 2;
	/** Stato di allerta */
	final static int ALERT = 3;
	/** Stato di attacco */
	final static int ATTACK = 4;
	/** Stato di morte*/
	final static int DEATH = 5;
}
