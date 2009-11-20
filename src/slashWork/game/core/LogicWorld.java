package slashWork.game.core;

import slashWork.game.enemyAI.Movement;
import slashWork.game.enemyAI.MovementList.MovementType;
import slashWork.game.graphics.WorldInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import com.jme.math.Vector3f;

public class LogicWorld implements WorldInterface {
	
	/**
	 *  Hashmap of the characters
	 */
	HashMap< String, LogicCharacter > characters;
	
	/**
	 *  HashMap of the energy packages
	 */
	HashMap< Integer, LogicEnergyPack > energyPackages;
	
	/**
	 *  HashMap of the ammo packages
	 */
	HashMap< Integer, LogicAmmo > ammoPackages;
	
	/** utility counters */
	int ammoPackCounter, energyPackCounter, enemyCounter, playerCounter;
	
	/** 
	 *  <code>LogicWorld</code> Constructor<br>
	 */
	public LogicWorld() {
		characters = new HashMap< String, LogicCharacter >();
		ammoPackages = new HashMap<Integer, LogicAmmo>();
		energyPackages = new HashMap<Integer, LogicEnergyPack>();
		ammoPackCounter = 0;
		energyPackCounter = 0;
		enemyCounter = 0;
		playerCounter = 0;
	}
	
	/** create one player */
	public void createPlayer( int maxLife, Vector3f position ) {
		playerCounter = playerCounter + 1;
		LogicPlayer player = new LogicPlayer( "player" + playerCounter, maxLife, position, this );
		characters.put( player.id, player );
	}
	
	/** Function that create characters in the number specified in this area
	 * 
	 * @param numberOfEnemies - (int) number of characters to create
	 * @param area - (Vector3f) the vector that identify the area in whitch characters will be created
	 */
	public void createEnemiesGroup( int numberOfEnemies, Vector3f area ) {
		float x = area.getX();
		float z = area.getY();

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
		LogicEnemy enemy = new LogicEnemy( "enemy" + enemyCounter, 50, EnumWeaponType.MP5, position, movements, this );
		characters.put( enemy.id, enemy );
	}
	
	/**
	 *  Create a number of energy packages in random 
	 *  positions of the world...
	 */
	public void createEnergyPackages( int number ) {
		//TODO 
	}
	
	/**
	 *  Create an ammo pack
	 */
	public void createAmmoPack( EnumWeaponType type, int quantity, Vector3f position ) {
		LogicAmmo ammoPack = new LogicAmmo( type, quantity, position );
		ammoPackCounter++;
		ammoPackages.put( ammoPackCounter, ammoPack );
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
			if( Pattern.matches( "^" + type + ".", id ) ) {
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
	public EnumWeaponType getCharacterWeapon(String id) {
		return characters.get(id).getCurrentWeapon();
	}

	@Override
	public void characterShoted( String id, int bulletDamage ) {
		characters.get(id).isShooted( bulletDamage );
	}

	@Override
	public boolean isAlive(String id) {
		if( !characters.containsKey( id ) )
			return false;
		else
			return true;
	}

}
