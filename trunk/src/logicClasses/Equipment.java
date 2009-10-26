package logicClasses;

import java.util.List;

/**
 * Classe Equipment - Weapon's Container
 * 
 * @author Andrea
 */
public class Equipment {
	
	/** List of weapons */
	private List<Weapon> weaponsList;
	
	/** Current Weapon */
	private int current;
	
	/** Number of weapons */
	private int numberOfWeapons;
	
	/** Max number of weapons */
	private int maxWeapons;
	
	/**
	 * Constructor Equipment
	 */
	public Equipment( int maxWeapons ){
		this.current = 0;
		this.numberOfWeapons = 0;
		
		/* Ci assicuriamo che contenga almeno un'arma */
		if( maxWeapons > 0)
			this.setMaxWeapons( maxWeapons );
		else
			this.setMaxWeapons( 1 );
	}
	
	/**
	 * It gets current weapon
	 * 
	 * @return (Weapon) - Weapon in 'current' position
	 */
	public Weapon getCurrentWeapon(){
		if( numberOfWeapons > 0 )
			return weaponsList.get( current );
		else
			return null;
	}
	
	/**
	 * It moves index at next weapon
	 */
	public void nextWeapon(){
		if( (current + 1) >= numberOfWeapons )
			current = 0;
		else
			current = current + 1;
	}
	
	/**
	 * It moves index at previous weapon
	 */
	public void backWeapon(){
		if( (current - 1) < 0 )
			current = numberOfWeapons - 1;
		else
			current = current - 1;
	}
	
	/**
	 * It gets weapon in 'i' position
	 * 
	 * @param i - (int) Index of requested weapon
	 * @return (Weapon) Requested weapon
	 */
	public Weapon getWeapon(int i){
		if( (i >= 0) && (i < numberOfWeapons) )
			return weaponsList.get(i);
		else
			return null;
	}
	
	/**
	 * It add a new weapon
	 * 
	 * @param newWeapon - (Weapon) The weapon to be included
	 * @return (boolean) - true if weapon has been included, false else
	 */
	public boolean addWeapon(Weapon newWeapon){
		if( ( numberOfWeapons + 1 ) <= maxWeapons ){
			weaponsList.add( newWeapon );
			numberOfWeapons = numberOfWeapons + 1;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Removes the weapon shown in the list and returns
	 * 
	 * @param i - (int) Index of the weapon to be removed
	 * @return (Weapon) - Removed weapon
	 */
	public Weapon removeWeapon(int i){
		if( ( i >= 0 ) && ( i < numberOfWeapons ) )
			return weaponsList.remove( i );
		else 
			return null;
	}
	
	/**
	 * Removes weapon in 'current' position
	 * 
	 * @return Weapon - Removed weapon
	 */
	public Weapon removeCurrentWeapon(){
		return weaponsList.remove( current );
	}

	/**
	 * @param maxWeapons the maxWeapons to set
	 */
	public void setMaxWeapons( int maxWeapons ) {
		this.maxWeapons = maxWeapons;
	}

	/**
	 * @return the maxWeapons
	 */
	public int getMaxWeapons() {
		return maxWeapons;
	}
}
