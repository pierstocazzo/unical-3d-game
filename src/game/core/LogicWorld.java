package game.core;

import game.common.Direction;
import game.common.Movement;
import game.common.State;
import game.common.WeaponType;
import game.common.MovementList.MovementType;
import game.graphics.WorldInterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import com.jme.math.Vector3f;

@SuppressWarnings("serial")
public class LogicWorld implements WorldInterface, Serializable {
	
	/** Hashmap of the characters <br>
	 * Contains all the characters (players and enemies) who interact in the game
	 */
	HashMap< String, LogicCharacter > characters;
	
	/** HashMap of the energy packages <br>
	 * Contains the energy packages placed in random position in the world
	 */
	HashMap< String, LogicEnergyPack > energyPackages;
	
	/** HashMap of the ammo packages <br>
	 * Contains the ammo packages left by enemies when they die
	 */
	HashMap< String, LogicAmmoPack > ammoPackages;
	
	/** utility counter */
	int ammoPackCounter, energyPackCounter, enemyCounter, playerCounter;
	
	/** Score Manager */
	ScoreManager scoreManager;
	
	AI enemyAi;

	
	/** <code>LogicWorld</code> Constructor<br>
	 * Initialize data structures and counters
	 */
	public LogicWorld() {
		characters = new HashMap< String, LogicCharacter >();
		ammoPackages = new HashMap< String, LogicAmmoPack >();
		energyPackages = new HashMap< String, LogicEnergyPack >();
		energyPackCounter = 0;
		ammoPackCounter = 0;
		enemyCounter = 0;
		playerCounter = 0;
		scoreManager = new ScoreManager( this );
		enemyAi = new AI( this );
	}
	
	/** Create one player with this life in this position
	 * 
	 * @param life - (int) the initial life of the player
	 * @param x - the x position of the player
	 * @param z - the z position of the player
	 */
	public void createPlayer( int life, int x, int z ) {
		playerCounter = playerCounter + 1;
		Vector3f position = new Vector3f( x, 0, z );
		LogicPlayer player = new LogicPlayer( "player" + playerCounter, life, position, this );
		characters.put( player.id, player );
	}
	
	/** Function that create characters in the number specified in this area
	 * 
	 * @param numberOfEnemies - (int) number of characters to create
	 * @param x - the x position of the area
	 * @param z - the z position of the area
	 */
	public void createEnemiesGroup( int numberOfEnemies, float x, float z ) {
		Random r = new Random();
		
		for( int i = 0; i < numberOfEnemies; i++ ) {	
			float xLocal, zLocal;
			xLocal = x + r.nextInt( numberOfEnemies * 15 );
			zLocal = z + r.nextInt( numberOfEnemies * 15 );

			createEnemy( xLocal, zLocal, State.DEFAULT, MovementType.getRandom() );
		}
	}

	/** Function that create an enemy, with this movementList, in this position
	 * 
	 * @param x - the x position of the enemy
	 * @param z - the z position of the enemy
	 * @param movementList - (MovementList) list of movements
	 */
	public void createEnemy( float x, float z, State state, MovementType movements ) {
		enemyCounter = enemyCounter + 1;
		Vector3f position = new Vector3f( x, 0, z );
		LogicEnemy enemy = new LogicEnemy( "enemy" + enemyCounter, 15, WeaponType.AR15, state, position, 
				movements, this );
		characters.put( enemy.id, enemy );
	}
	
	/**
	 *  Create a number of energy packages in random 
	 *  positions of the world...
	 */
	public void createEnergyPackages( int number, int xDimension, int zDimension ) {
		for( int i = 0; i < number; i++ ) {
			energyPackCounter = energyPackCounter + 1;
			LogicEnergyPack energyPack = new LogicEnergyPack( "energyPack" + energyPackCounter, 10 );
			energyPackages.put( energyPack.id, energyPack );
		}
	}
	
	/**
	 *  Create an ammo pack
	 */
	public void createAmmoPack( String id, WeaponType type, int quantity, Vector3f position ) {
		LogicAmmoPack ammoPack = new LogicAmmoPack( type, quantity, position );
		ammoPackages.put( id, ammoPack );
	}
	
	@Override 
	public void setPosition( String id, Vector3f position ) {
		characters.get( id ).setPosition( position );
	}
	
	@Override
	public Vector3f getPosition( String id ) {
		return characters.get(id).position;
	}

	@Override
	public Set<String> getCharactersIds() {
		return characters.keySet();
	}

	@Override
	public Set<String> getEnemiesIds() {
		return getCharactersId( "enemy" );
	}
	
	@Override
	public Set<String> getPlayersIds() {
		return getCharactersId( "player" );
	}

	/** utility function */
	private Set<String> getCharactersId( String type ) {
		Set<String> charactersId = new HashSet<String>();
		
		for( String id : characters.keySet() ) {
			// pattern matching on id: character id must be like "typeN" where N 
			// is a number and type is "player" or "enemy"
			if( Pattern.matches( "^" + type + ".+", id ) ) {
				charactersId.add( id );
			}
		}
		
		return charactersId;
	}

	@Override
	public boolean shoot( String id ) {
        return characters.get(id).shoot();
    }
	
	@Override
	public void shooted( String id, String shooterId, int bulletDamage ) {
		if( characters.get( id ) != null )
			characters.get(id).isShooted( bulletDamage, shooterId );
	}

	public void removeCharacter( String id ) {
		characters.remove( id );
	}
	
	@Override
	public Movement getNextMovement( String id ) {
		return characters.get(id).getNextMovement();
	}

	@Override
	public Movement getCurrentMovement( String id ) {
		try { 
			return ((LogicEnemy) characters.get(id)).getCurrentMovement();
		} catch (Exception e) {
			return new Movement( Vector3f.ZERO, 0 );
		}
	}

	@Override
	public boolean isMoving( String id ) {
		return characters.get(id).isMoving();
	}

	@Override
	public void setMoving( String id, boolean b ) {
		characters.get(id).setMoving(b);
	}

	@Override
	public void setJumping( String id, boolean b ) {
		characters.get(id).setJumping(b);
	}

	@Override
	public WeaponType getWeapon(String id) {
		return characters.get(id).getCurrentWeapon();
	}

	@Override
	public boolean isAlive(String id) {
		if( !characters.containsKey( id ) )
			return false;
		else
			return true;
	}

	@Override
	public State getState( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).state;
		} catch (Exception e) {
			return State.DEFAULT;
		}
	}

	@Override
	public void updateState(String id) {
		try {
//			((LogicEnemy) characters.get(id)).updateState();
			enemyAi.updateState(id);
		} catch (Exception e) {
		}
	}

	@Override
	public Vector3f getShootDirection( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).shootDirection;
		} catch (Exception e) {
			return Vector3f.ZERO;
		}
	}

	@Override
	public boolean catchAmmoPack( String playerId, String ammoPackId ) {
		try {
			if( characters.get(playerId).addAmmo( ammoPackages.get(ammoPackId).getType(), ammoPackages.get(ammoPackId).getQuantity() ) ) {
				ammoPackages.remove(ammoPackId);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean catchEnergyPack( String playerId, String energyPackId ) {
		return characters.get(playerId).addLife( energyPackages.get(energyPackId).getValue() );
	}

	@Override
	public int getCurrentLife( String id ) {
		return characters.get(id).currentLife;
	}

	@Override
	public void kill( String id ) {
		scoreManager.enemyKilled( id );
	}

	@Override
	public void killed( String id ) {
		scoreManager.playerKilled( id );
	}
	
	public void initScoreManager() {
		scoreManager.initScoreManager();
	}
	
	@Override
	public int getScore( String id ) {
		return scoreManager.getScore(id);
	}
	
	@Override
	public int getLevel( String id ) {
		return scoreManager.getLevel(id);
	}

	@Override
	public Set<String> getEnergyPackagesIds() {
		return energyPackages.keySet();
	}
	
	@Override
	public int getAmmo( String playerId ) {
		try {
			return ((LogicPlayer) characters.get(playerId)).currentWeapon.getAmmo();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public float getAlertLevel(String id) {
		try {
			return ((LogicEnemy) characters.get(id)).getAlertTime();
		} catch (Exception e) {
			return 20;
		}
	}

	@Override
	public void setState(String id, State state) {
		try {
			((LogicEnemy)characters.get(id)).setState(state);
		} catch (Exception e) {
			// do nothing 
		}
	}

	@Override
	public int getMaxLife(String id) {
		return characters.get(id).getMaxLife();
	}

	@Override
	public int getMaxAmmo(String id) {
		try {
			return ((LogicPlayer) characters.get(id)).getMaxAmmo();
		} catch (Exception e) {
			return 100;
		}
	}

	@Override
	public boolean firstFind( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).firstFind;
		} catch ( Exception e ) {
			return false;
		}
	}

	@Override
	public void setFirstFind( String id, boolean firstFind ) {
		try {
			((LogicEnemy) characters.get(id)).firstFind = firstFind;
		} catch ( Exception e ) {
		}
	}
	
	@Override
	public boolean comeBack( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).comingBack;
		} catch ( Exception e ) {
			return false;
		}
	}
	
	@Override
	public void setComeBack( String id, boolean comeBack ) {
		try {
			((LogicEnemy) characters.get(id)).comingBack = comeBack;
		} catch ( Exception e ) {
		}
	}

	@Override
	public boolean isReborn( String id ){
		if(characters.containsKey(id))
			return ((LogicPlayer)characters.get(id)).reborn;
		return false;
	}

	@Override
	public void setReborn(String id, boolean value) {
		((LogicPlayer)characters.get(id)).reborn = value;
	}

	@Override
	public boolean showLevel2Message(String id) {
		return scoreManager.getShowLevel2( id );
	}

	@Override
	public void setShowLevel2Message(String id, boolean b) {
		scoreManager.players.get(id).showLevel2 = b;
	}

	@Override
	public Vector3f getMoveDirection(String id) {
		return enemyAi.getMoveDirection(id);
	}
}
