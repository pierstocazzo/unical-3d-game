package game.graphics;


import game.enemyAI.Movement;

import java.util.HashMap;
import java.util.Set;

import com.jme.math.Vector3f;

/**
 * Interface
 */
public interface WorldInterface {
	
	/** Function <code>moveEnemy</code><br>
	 * Update the position of the enemy with this id
	 *  
	 * @param id - (String) enemy id
	 * @param position - (Vector3f) new enemy position
	 */
    public abstract void moveCharacter( String id, Vector3f position );
    
	/** Function <code>printWorld</code><br>
	 * Print the world current situation
	 */
	public abstract String printWorld();
	
	/** Function getPlayerId()
	 *  returns the identificator of the logic player
	 */
	public abstract Set<String> getPlayersId();
	
	/** Function getPlayerId()
	 *  returns the identificators of the logic characters
	 */
	public abstract Set<String> getEnemiesId();
	
	/** Function getEnemiesPosition()
	 * return an hashMap with the position of each logic enemy
	 * @return HashMap
	 */
	public abstract HashMap<String, Vector3f> getEnemiesPosition();

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
	
	public abstract Movement getEnemyCurrentMovement( String id );
	
	public abstract Vector3f getEnemyInitialPosition( String id );
	
	public abstract void setEnemyInitialPosition( String id, Vector3f position );

	public abstract Vector3f getCharacterPosition( String id );


	
}
