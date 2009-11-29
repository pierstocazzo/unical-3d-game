package andreaFolder.graphics;



import java.util.Set;

import andreaFolder.enemyAI.Movement;

import com.jme.math.Vector3f;

/**
 * Interface
 */
public interface WorldInterface {

	/** Function <code>setCharacterPosition</code><br>
	 * Update the position of the character with this id
	 *  
	 * @param id - (String) character's id
	 * @param position - (Vector3f) new position
	 */
	public abstract void setCharacterPosition( String id, Vector3f position );

	/** Function <code>getCharacterPosition</code><br>
	 * return the position of the character with this id
	 *
	 * @param id - (String) the character's id
	 * @return the position of the character with this id
	 */
	public abstract Vector3f getCharacterPosition( String id );

	/** Function getPlayerId()
	 *  returns the identificator of the logic player
	 */
	public abstract Set<String> getPlayersId();

	/** Function getPlayerId()
	 *  returns the identificators of the logic characters
	 */
	public abstract Set<String> getEnemiesId();

	/** Function characterShoot()
	 * invoke the shoot method of logic character
	 */
	public abstract void characterShoot( String id );

	/** 
	 * 
	 * @param id
	 * @return true if character isn't moving
	 */
	public abstract boolean getCharacterRest( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterRest(String id, boolean b);


	/** Function playerRest()
	 * set rest for the player
	 */
	public abstract void characterRest( String id );

	/** 
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterOnGround( String id, boolean b );

	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean getCharacterOnGround( String id );

	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean getCharacterMovingForward( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterMovingForward(String id, boolean b);

	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean getCharacterMovingBackward( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterMovingBackward(String id, boolean b);

	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean getCharacterStrafingLeft( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterStrafingLeft(String id, boolean b);


	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean getCharacterStrafingRight( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterStrafingRight(String id, boolean b);

	/** 
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean getCharacterJumping( String id );

	/**
	 * 
	 * @param id
	 * @param b
	 */
	public abstract void setCharacterJumping(String id, boolean b);

	/**
	 *
	 * @param id
	 */
	public abstract Movement getEnemyNextMovement( String id );

	/**
	 *
	 * @param id
	 * @return
	 */
	public abstract Movement getEnemyCurrentMovement( String id );

	/** Function <code>printWorld</code><br>
	 * Print the world current situation
	 */
	public abstract String printWorld();
}
