package game.core;

import game.enemyAI.Movement;
import game.enemyAI.MovementList.MovementType;

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
	
	/** Score Controller */
	LogicScoreController scoreController;
	
	/** <code>LogicWorld</code> Constructor<br>
	 * Initialize data structures and counters
	 */
	public LogicWorld() {
		characters = new HashMap< String, LogicCharacter >();
		ammoPackages = new HashMap< String, LogicAmmoPack >();
		energyPackages = new HashMap< String, LogicEnergyPack >();
		ammoPackCounter = 0;
		energyPackCounter = 0;
		enemyCounter = 0;
		playerCounter = 0;
		scoreController = new LogicScoreController();
	}
	
	/** Create one player with this life in this position
	 * 
	 * @param life - (int) the initial life of the player
	 * @param position - (Vector3f) the initial position of the player
	 */
	public void createPlayer( int life, Vector3f position ) {
		playerCounter = playerCounter + 1;
		LogicPlayer player = new LogicPlayer( "player" + playerCounter, life, position, this );
		characters.put( player.id, player );
	}
	
	/** Function that create characters in the number specified in this area
	 * 
	 * @param numberOfEnemies - (int) number of characters to create
	 * @param area - (Vector3f) the vector that identify the area in whitch characters will be created
	 */
	public void createEnemiesGroup( int numberOfEnemies, Vector3f area ) {
		float x = area.getX();
		float z = area.getZ();

		Random r = new Random();

		for( int i = 0; i < numberOfEnemies; i++ ) {		
			float xRelative = x + r.nextInt( numberOfEnemies * 5 );
			float zRelative = z + r.nextInt( numberOfEnemies * 5 );

			Vector3f position = new Vector3f( xRelative, area.getY(), zRelative );
			
			createEnemy( position, MovementType.VERTICAL_SENTINEL );
		}
	}

	/** Function that create an enemy, with this movementList, in this position
	 * 
	 * @param position - (Vector3f) the position of the enemy
	 * @param movementList - (MovementList) list of movements
	 */
	public void createEnemy( Vector3f position, MovementType movements ) {
		enemyCounter = enemyCounter + 1;
		LogicEnemy enemy = new LogicEnemy( "enemy" + enemyCounter, 15, WeaponType.MP5, State.DEFAULT, position, movements, this );
		characters.put( enemy.id, enemy );
	}
	
	/**
	 *  Create a number of energy packages in random 
	 *  positions of the world...
	 */
	public void createEnergyPackages( int number, int xDimension, int zDimension ) {
		for( int i = 0; i < number; i++ ) {
			energyPackCounter = energyPackCounter + 1;
			Vector3f position = new Vector3f();
			Random r = new Random();
			position.setX( r.nextInt( xDimension ) );
			position.setY( r.nextInt( 20 ) );
			position.setZ( r.nextInt( zDimension ) );
			LogicEnergyPack energyPack = new LogicEnergyPack( "energyPack" + energyPackCounter, position, 10 );
			energyPackages.put( energyPack.id, energyPack );
		}
	}
	
	/**
	 *  Create an ammo pack
	 */
	public void createAmmoPack( String id, WeaponType type, int quantity, Vector3f position ) {
		LogicAmmoPack ammoPack = new LogicAmmoPack( type, quantity, position );
		ammoPackCounter++;
		ammoPackages.put( id, ammoPack );
	}
	
	@Override 
	public void setCharacterPosition( String id, Vector3f position ) {
		characters.get( id ).setPosition( position );
	}
	
	@Override
	public Vector3f getCharacterPosition( String id ) {
		return characters.get(id).position;
	}

	@Override
	public Set<String> getEnemiesId() {
		return getCharactersId( "enemy" );
	}
	
	@Override
	public Set<String> getPlayersId() {
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

	public void characterShoot( String id ) {
        characters.get(id).shoot();
    }
	
	public void removeCharacter( String id ) {
		characters.remove( id );
	}
	
	@Override
	public Movement getEnemyNextMovement( String id ) {
		return characters.get(id).getNextMovement();
	}

	@Override
	public Movement getEnemyCurrentMovement( String id ) {
		return ((LogicEnemy) characters.get(id)).getCurrentMovement();
	}

	@Override
	/** 
	 * stop all character's movements
	 */
	public void characterRest( String id ) {
		characters.get(id).rest();
	}

	@Override
	public void setCharacterOnGround( String id, boolean b ) {
		characters.get(id).setOnGround( b );
	}

	@Override
	public boolean getCharacterOnGround( String id ) {
		return characters.get(id).getOnGround();
	}

	@Override
	public boolean getCharacterRest( String id ) {
		return characters.get(id).getRest();
	}

	@Override
	public void setCharacterRest( String id, boolean b ) {
		characters.get(id).setRest(b);
	}

	@Override
	public void setCharacterMovingBackward( String id, boolean b ) {
		characters.get(id).setMovingBackward(b);
	}

	@Override
	public boolean getCharacterJumping( String id ) {
		return characters.get(id).getJumping();
	}

	@Override
	public boolean getCharacterMovingBackward( String id ) {
		return characters.get(id).getMovingBackward();
	}

	@Override
	public boolean getCharacterMovingForward( String id ) {
		return characters.get(id).getMovingForward();
	}

	@Override
	public boolean getCharacterStrafingLeft( String id ) {
		return characters.get(id).getStrafingLeft();
	}

	@Override
	public boolean getCharacterStrafingRight( String id ) {
		return characters.get(id).getStrafingRight();
	}

	@Override
	public void setCharacterJumping( String id, boolean b ) {
		characters.get(id).setJumping(b);
	}

	@Override
	public void setCharacterMovingForward( String id, boolean b ) {
		characters.get(id).setMovingForward(b);
	}

	@Override
	public void setCharacterStrafingLeft( String id, boolean b ) {
		characters.get(id).setStrafingLeft(b);
	}

	@Override
	public void setCharacterStrafingRight( String id, boolean b ) {
		characters.get(id).setStrafingRight(b);
	}

	/** 
	 * Print the position of all characters
	 * @return String to print
	 */
	public String printWorld() {
		String s = "World status: ";
				
		//		s = s + "\n Player position: " + players.gposition;
				
		for( String id : characters.keySet() ){
			s = s + "\n" + characters.get(id).id + " energ = " + characters.get(id).currentLife;
		}

		return s;
	}

	@Override
	public WeaponType getCharacterWeapon(String id) {
		return characters.get(id).getCurrentWeapon();
	}

	@Override
	public void characterShoted( String id, int bulletDamage ) {
		if( characters.containsKey( id ))
			characters.get(id).isShooted( bulletDamage );
	}

	@Override
	public boolean isAlive(String id) {
		if( !characters.containsKey( id ) )
			return false;
		else
			return true;
	}

	@Override
	public void updateEnemyState(String id) {
		((LogicEnemy) characters.get(id)).updateState();
	}

	@Override
	public Vector3f getEnemyShootDirection( String id ) {
		return ((LogicEnemy) characters.get(id)).shootDirection;
	}

	@Override
	public State getEnemyState( String id ) {
		return ((LogicEnemy) characters.get(id)).state;
	}

	@Override
	public boolean catchAmmoPack( String playerId, String ammoPackId ) {
		for( LogicWeapon weapon : ((LogicPlayer) characters.get(playerId)).equipment.values() ) {
			if( weapon.type == ammoPackages.get(ammoPackId).getType() ) {
				weapon.addAmmo( ammoPackages.get(ammoPackId).getQuantity() );
				ammoPackages.remove(ammoPackId);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void catchEnergyPack( String playerId, String energyPackId ) {
		characters.get(playerId).addLife( energyPackages.get(energyPackId).getValue() );
	}

	@Override
	public LogicAmmoPack getAmmoPack( String ammoPackId ) {
		return ammoPackages.get(ammoPackId);
	}

	@Override
	public int getCharacterLife( String id ) {
		return characters.get(id).currentLife;
	}

	@Override
	public HashMap< String, Vector3f > getEnergyPackagesPosition() {
		HashMap< String, Vector3f > hash = new HashMap<String, Vector3f>();
		for( LogicEnergyPack energyPack : energyPackages.values() ) {
			hash.put( energyPack.id, energyPack.position );
		}
		return hash;
	}

	@Override
	public void setEnemyKilled() {
		scoreController.setEnemyKilled();
	}

	@Override
	public void setPlayerKilled() {
		scoreController.playerKilled();
	}
}
