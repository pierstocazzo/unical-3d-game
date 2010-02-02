package game.core;

import game.common.GameConfiguration;
import game.common.Movement;
import game.common.State;
import game.common.WeaponType;
import game.common.GameConfiguration.EnemyInfo;
import game.common.MovementList.MovementType;
import game.graphics.WorldInterface;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

/**
 * Class LogicWorld
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
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
	int energyPackCounter, enemyCounter, playerCounter;
	
	/** Score Manager */
	ScoreManager scoreManager;
	
	/** AI manage enemies intelligence */
	AI enemyAi;
	
	/** current ammoPack value */
	int ammoPackValue;

	/** <code>LogicWorld</code> Constructor<br>
	 * Initialize data structures and counters
	 */
	public LogicWorld() {
		characters = new HashMap< String, LogicCharacter >();
		ammoPackages = new HashMap< String, LogicAmmoPack >();
		energyPackages = new HashMap< String, LogicEnergyPack >();
		energyPackCounter = 0;
		enemyCounter = 0;
		playerCounter = 0;
		scoreManager = new ScoreManager( this );
		enemyAi = new AI( this );
		
		ammoPackValue = Integer.valueOf( GameConfiguration.getParameter("initialAmmoPackValue") );
		
		//set enemy from xml file
		List<EnemyInfo> enemyList = GameConfiguration.getEmemiesInfoList();
		for( EnemyInfo e : enemyList ) {
			createEnemy( e.getPosX(), e.getPosZ(), e.getState(), e.getMovements() );
		}
		
		createPlayer( GameConfiguration.getPlayerLife(), 
				GameConfiguration.getPlayerX(), GameConfiguration.getPlayerZ());
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
	 * @param movementList - (MovementType) type of movements
	 */
	public void createEnemy( float x, float z, State state, MovementType movements ) {
		enemyCounter = enemyCounter + 1;
		Vector3f position = new Vector3f( x, 0, z );
		
		int probability = FastMath.rand.nextInt(100);
		WeaponType type;
		
		if ( probability < Integer.valueOf( GameConfiguration.getParameter("ar15_probability") ) ) {
			type = WeaponType.AR15;
		} else if ( probability < Integer.valueOf( GameConfiguration.getParameter("gatling_probability")) ) {
			type = WeaponType.GATLING;
		} else {
			type = WeaponType.BAZOOKA;
		}
		
		LogicEnemy enemy = new LogicEnemy( "enemy" + enemyCounter,
				Integer.valueOf( GameConfiguration.getParameter("initialEnemyLife") ),
				type, state, position, movements, this );
		
		characters.put( enemy.id, enemy );
	}
	
	/** Function that create an enemy, with this movementList, in this position
	 * 
	 * @param x - the x position of the enemy
	 * @param z - the z position of the enemy
	 * @param movementList - (MovementList) list of movements
	 */
	public void createEnemy( float x, float z, State state, LinkedList<Movement> movements ) {
		enemyCounter = enemyCounter + 1;
		Vector3f position = new Vector3f( x, 0, z );
		
		int rand = FastMath.rand.nextInt(100);
		WeaponType type;
		
		int ar15probability = Integer.valueOf( GameConfiguration.getParameter("ar15_probability") );
		int gatlingProbability = Integer.valueOf( GameConfiguration.getParameter("gatling_probability") );
		int bazookaProbability = Integer.valueOf( GameConfiguration.getParameter("bazooka_probability") );
		
		if ( rand < ar15probability ) {
			type = WeaponType.AR15;
		} else if ( rand < gatlingProbability  + ar15probability ) {
			type = WeaponType.GATLING;
		} else if ( rand < bazookaProbability + gatlingProbability + ar15probability ) {
			type = WeaponType.BAZOOKA;
		} else {
			type = WeaponType.AR15;
		}
		
		LogicEnemy enemy = new LogicEnemy( "enemy" + enemyCounter,
				Integer.valueOf( GameConfiguration.getParameter("initialEnemyLife") ),
				type, state, position, movements, this );
		
		characters.put( enemy.id, enemy );
	}
	
	/**
	 *  Create a number of energy packages in random 
	 *  positions of the world...
	 */
	public void createEnergyPackages( int number ) {
		for( int i = 0; i < number; i++ ) {
			energyPackCounter = energyPackCounter + 1;
			LogicEnergyPack energyPack = new LogicEnergyPack( "energyPack" + energyPackCounter, 
					Integer.valueOf( GameConfiguration.getParameter("initialEnergyPackValue") ) );
			
			energyPackages.put( energyPack.id, energyPack );
		}
	}
	
	/**
	 * Create an ammo pack
	 * 
	 * @param (string) id - ammo pack id
	 * @param (WeaponType) type - ammo pack type
	 * @param (int) quantity - ammo quantity
	 * @param (Vector3f) position - position of ammo pack
	 */
	public void createAmmoPack( String id, WeaponType type, int quantity, Vector3f position ) {
		LogicAmmoPack ammoPack = new LogicAmmoPack( type, quantity, position );
		ammoPackages.put( id, ammoPack );
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override 
	public void setPosition( String id, Vector3f position ) {
		try {
			characters.get( id ).setPosition( position );
		} catch (Exception e) {
		}
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public Vector3f getPosition( String id ) {
		try {
			return characters.get(id).position;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public Set<String> getCharactersIds() {
		return characters.keySet();
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public Set<String> getEnemiesIds() {
		return getCharactersId( "enemy" );
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public Set<String> getPlayersIds() {
		return getCharactersId( "player" );
	}

	/** 
	 * Utility function that return characters id set
	 */
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

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean shoot( String id ) {
		try {
			return characters.get(id).shoot();
		} catch (Exception e) {
			return false;
		}
    }
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public void shooted( String id, String shooterId, int bulletDamage ) {
		if( characters.get( id ) != null )
			characters.get(id).isShooted( bulletDamage, shooterId );
	}

	/**
	 * Remove indicated character from container
	 * 
	 * @param (String) id
	 */
	public void removeCharacter( String id ) {
		characters.remove( id );
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public Movement getNextMovement( String id ) {
		try {
			return characters.get(id).getNextMovement();
		} catch (Exception e) {
			return new Movement( Vector3f.ZERO, 0 );
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public Movement getCurrentMovement( String id ) {
		try { 
			return ((LogicEnemy) characters.get(id)).getCurrentMovement();
		} catch (Exception e) {
			return new Movement( Vector3f.ZERO, 0 );
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean isMoving( String id ) {
		try {
			return characters.get(id).isMoving();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void setMoving( String id, boolean moving ) {
		try {
			characters.get(id).setMoving( moving );
		} catch (Exception e) {
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void setJumping( String id, boolean jumping ) {
		try {
			characters.get(id).setJumping( jumping );
		} catch (Exception e) {
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public WeaponType getWeapon(String id) {
		try {
			return characters.get(id).getCurrentWeapon();
		} catch (Exception e) {
			return WeaponType.AR15;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean isAlive(String id) {
		if( !characters.containsKey( id ) )
			return false;
		else
			return true;
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public State getState( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).state;
		} catch (Exception e) {
			return State.DEFAULT;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void updateState(String id) {
		try {
			enemyAi.updateState(id);
		} catch (Exception e) {}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public Vector3f getShootDirection( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).shootDirection;
		} catch (Exception e) {
			return Vector3f.ZERO;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean catchAmmoPack( String playerId, String ammoPackId ) {
		try {
			if( characters.get(playerId).addAmmo( ammoPackages.get(ammoPackId).getType(), 
				ammoPackages.get(ammoPackId).getQuantity() ) ) {
				ammoPackages.remove(ammoPackId);
				return true;
			} 
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean catchEnergyPack( String playerId, String energyPackId ) {
		try {
			return characters.get(playerId).addLife( energyPackages.get(energyPackId).getValue() );
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public int getCurrentLife( String id ) {
		return characters.get(id).currentLife;
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void kill( String id ) {
		scoreManager.enemyKilled( id );
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void killed( String id ) {
		scoreManager.playerKilled( id );
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public int getScore( String id ) {
		return scoreManager.getScore(id);
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public int getLevel( String id ) {
		return scoreManager.getLevel(id);
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public Set<String> getEnergyPackagesIds() {
		return energyPackages.keySet();
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public int getAmmo( String playerId ) {
		try {
			return ((LogicPlayer) characters.get(playerId)).currentWeapon.getAmmo();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public float getAlertLevel(String id) {
		try {
			return ((LogicEnemy) characters.get(id)).getAlertTime();
		} catch (Exception e) {
			return 20;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void setState(String id, State state) {
		try {
			((LogicEnemy)characters.get(id)).setState(state);
		} catch (Exception e) {}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public int getMaxLife(String id) {
		return characters.get(id).getMaxLife();
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public int getMaxAmmo(String id) {
		try {
			return ((LogicPlayer) characters.get(id)).getMaxAmmo();
		} catch (Exception e) {
			return 100;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean firstFind( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).firstFind;
		} catch ( Exception e ) {
			return false;
		}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void setFirstFind( String id, boolean firstFind ) {
		try {
			((LogicEnemy) characters.get(id)).firstFind = firstFind;
		} catch ( Exception e ) {}
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean comeBack( String id ) {
		try {
			return ((LogicEnemy) characters.get(id)).comingBack;
		} catch ( Exception e ) {
			return false;
		}
	}
	
	/**
	 * @see WorldInterface
	 */
	@Override
	public void setComeBack( String id, boolean comeBack ) {
		try {
			((LogicEnemy) characters.get(id)).comingBack = comeBack;
		} catch ( Exception e ) {}
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean isReborn( String id ){
		if(characters.containsKey(id))
			return ((LogicPlayer)characters.get(id)).reborn;
		return false;
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void setReborn(String id, boolean value) {
		((LogicPlayer)characters.get(id)).reborn = value;
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public boolean showLevel2Message(String id) {
		return scoreManager.getShowLevel2( id );
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public void setShowLevel2Message(String id, boolean b) {
		((LogicPlayer)characters.get(id)).score.showLevel2 = b;
	}

	/**
	 * @see WorldInterface
	 */
	@Override
	public Vector3f getMoveDirection(String id) {
		return enemyAi.getMoveDirection(id);
	}

	@Override
	public void nextWeapon(String id) {
		try {
			((LogicPlayer) characters.get(id)).nextWeapon();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void cantSeePlayer( String id, boolean b ) {
		((LogicEnemy) characters.get(id)).cantSeePlayer( b );
	}
}