package game.core;

import game.common.Movement;
import game.common.WeaponType;
import game.common.GameConfiguration;
import game.core.ScoreManager.Score;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.jme.math.Vector3f;

/**
 * Class LogicPlayer
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
@SuppressWarnings("serial")
public class LogicPlayer extends LogicCharacter implements Serializable {

	/** Player's equipment */
	List<LogicWeapon> equipment;
	
	/** number of weapons in the player's equipment */
	int weaponCounter;
	
	/** Weapon in use */
	LogicWeapon currentWeapon;
	int currentWeaponIndex = 0;
	
	int maxAmmo;
	
	boolean reborn = false;
	
	Score score;
	
	/**
	 * LogicPlayer Constructor <br>
	 * Create a new Player
	 * @param id - (String) Identifier
	 * @param currentLife - (int) Current Life
	 * @param maxLife - (int) Max Life
	 */
	public LogicPlayer( String id, int maxLife , Vector3f position, LogicWorld world ) {
		super( id, maxLife, position, world );
		equipment = new LinkedList<LogicWeapon>();
		this.weaponCounter = 0;
		maxAmmo = Integer.valueOf( GameConfiguration.getParameter("initialPlayerAmmo") );
		/** Initially the player has just the AR15 */
		LogicWeapon ar15 = new LogicWeapon( super.id + "ar15", maxAmmo, WeaponType.AR15 );
		LogicWeapon gatling = new LogicWeapon( super.id + "gatling", maxAmmo, WeaponType.GATLING );
		LogicWeapon bazooka = new LogicWeapon( super.id + "bazooka", maxAmmo, WeaponType.BAZOOKA );
		addWeapon( ar15 );
		addWeapon( gatling );
		addWeapon( bazooka );
		
		/** assign the mp5 as current weapon */
		currentWeapon = ar15;
		
		this.score = new Score();
	}
	
	/** Function <code>addWeapon</code><br>
	 * 
	 * Add a new weapon to the player equipment.
	 * @param weapon - (LogicWeapon) the weapon to add
	 */
	public void addWeapon( LogicWeapon weapon ) {
		// the player can't carry more than three weapons
		if( weaponCounter < 3 ) {
			weaponCounter = weaponCounter + 1;
			weapon.id = weapon.id + weaponCounter;
			equipment.add( weapon );
		} 
		// advise the player he can't carry more than three weapons
	}
	
	public boolean addAmmo( WeaponType weaponType, int quantity ) {
		for( LogicWeapon weapon : equipment ) {
			if( weapon.type == weaponType ) {
				if( weapon.ammo >= maxAmmo ) {
					return false;
				} else {
					weapon.addAmmo( quantity );
					if( weapon.ammo > maxAmmo )
						weapon.ammo = maxAmmo;
					return true;
				}
			}
		}
		return false;
	}
	
	/** 
	 * Just decrease ammunitions
	 */
	public boolean shoot() {
		if( currentWeapon.ammo > 0 ) { 
			currentWeapon.decreaseAmmo();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Movement getNextMovement() {
		return null;
	}

	@Override
	public WeaponType getCurrentWeapon() {
		return currentWeapon.type;
	}
	
	@Override
	public void die( String shooterId ) {
		world.scoreManager.playerKilled(id);
		if(world.scoreManager.getScore(id)<=10){
			world.killed(id);
			super.die( shooterId );
		}
		else{
			reborn = true;
			reborn();
		}
	}
	
	public void reborn(){
		currentLife = maxLife;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}

	public void nextWeapon() {
		currentWeaponIndex++;
		if( currentWeaponIndex < equipment.size() ) {
			currentWeapon = equipment.get(currentWeaponIndex);
		} else {
			currentWeaponIndex = 0;
			currentWeapon = equipment.get(currentWeaponIndex);
		}
	}
}
