package game.core;

import game.enemyAI.MovementList;
import game.enemyAI.Movement;
import game.graphics.WorldInterface;

import java.util.HashMap;
import java.util.HashSet;
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
	int ammoPackCounter, energyPackCounter;
	
	/** 
	 *  <code>LogicWorld</code> Constructor<br>
	 */
	public LogicWorld() {
		characters = new HashMap< String, LogicCharacter >();
	}
	
	/** create one player */
	public void createPlayer( String id, int maxLife, Vector3f position ) {
		characters.put( id , new LogicPlayer( id, maxLife, position, this ) );
	}
	
	/**
	 * Function that create enemies in the number specified
	 * @param numberOfEnemies - (int) number of enemies to create
	 */
	public void createEnemies( int numberOfEnemies, Vector3f area, MovementList movementList ) {
//		float x = area.getX();
//		float z = area.getY();
//
//		Random r = new Random();

		for( int i = 0; i < numberOfEnemies; i++ ) {		
//			float xRelative = x + r.nextInt(numberOfEnemies * 5);
//			float zRelative = z + r.nextInt(numberOfEnemies * 5);
//
//			Vector3f position = new Vector3f( xRelative, 50, zRelative );
//
//			LogicEnemy enemy = new LogicEnemy( "enemy" + i, 50, EnumWeaponType.MP5, 
//					position, new MovementList( MovementType.LARGE_PERIMETER ) );
//			characters.put( enemy.id, enemy );
			LogicEnemy enemy = new LogicEnemy( "enemy" + i, 50, EnumWeaponType.MP5, area, movementList, this );
			characters.put( enemy.id, enemy );
		}
	}

	/**
	 *  Create a number of energy packages in random 
	 *  positions of the world...
	 */
	public void createEnergyPackages(  ) {
		//TODO 
	}
	
	/**
	 *  Create an ammo pack
	 */
	public void createAmmoPack( EnumWeaponType type, int quantity, Vector3f position ) {
		LogicAmmo ammoPack = new LogicAmmo( type, quantity, position );
		ammoPackages.put( ammoPackCounter, ammoPack );
	}
	
	@Override 
	public void setCharacterPosition( String id, Vector3f position ) {
		characters.get( id ).setPosition( position );
	}
	
	@Override
	public Vector3f getCharacterPosition(String id) {
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

	public void characterShoot(String id) {
        characters.get(id).shoot();
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
	public boolean getCharacterOnGround(String id) {
		return characters.get(id).getOnGround();
	}

	@Override
	public boolean getCharacterRest(String id) {
		return characters.get(id).getRest();
	}

	@Override
	public void setCharacterRest(String id, boolean b) {
		characters.get(id).setRest(b);
	}

	@Override
	public void setCharacterMovingBackward(String id, boolean b) {
		characters.get(id).setMovingBackward(b);
	}

	@Override
	public boolean getCharacterJumping(String id) {
		return characters.get(id).getJumping();
	}

	@Override
	public boolean getCharacterMovingBackward(String id) {
		return characters.get(id).getMovingBackward();
	}

	@Override
	public boolean getCharacterMovingForward(String id) {
		return characters.get(id).getMovingForward();
	}

	@Override
	public boolean getCharacterStrafingLeft(String id) {
		return characters.get(id).getStrafingLeft();
	}

	@Override
	public boolean getCharacterStrafingRight(String id) {
		return characters.get(id).getStrafingRight();
	}

	@Override
	public void setCharacterJumping(String id, boolean b) {
		characters.get(id).setJumping(b);
	}

	@Override
	public void setCharacterMovingForward(String id, boolean b) {
		characters.get(id).setMovingForward(b);
	}

	@Override
	public void setCharacterStrafingLeft(String id, boolean b) {
		characters.get(id).setStrafingLeft(b);
	}

	@Override
	public void setCharacterStrafingRight(String id, boolean b) {
		characters.get(id).setStrafingRight(b);
	}

	/** 
	 * Print the position of all characters
	 * @return String to print
	 */
	public String printWorld() {
		String s = "World status: ";
		//		
		//		Set<String> keySet = enemies.keySet();
		//		
		////		s = s + "\n Player position: " + players.gposition;
		//		
		//		for( String key : keySet ){
		//			s = s + "\n" + enemies.get(key).id + " in position: " + enemies.get(key).position;
		//		}

		return s;
	}
}
