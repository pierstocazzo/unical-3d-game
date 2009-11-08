package game.graphics;

import com.jme.math.Vector3f;

/**
 * Interface
 */
public interface WorldInterface {
	
	/** Function <code>move</code><br>
	 * Update the position of the character with this id
	 *  
	 * @param id - (String) character id
	 * @param position - (Vector3f) new character position
	 */
    public abstract void move( String id, Vector3f position );

	/** Function <code>printWorld</code><br>
	 * Print the world current situation
	 */
	public abstract String printWorld();
}
