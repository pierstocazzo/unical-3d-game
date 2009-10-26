package logicClasses;

import java.util.List;

/**
 * Classe Equipment - Contenitore di armi
 * 
 * @author Andrea
 */
public class Equipment {
	
	/** Lista di armi */
	private List<Weapon> weaponsList;
	
	/** Arma corrente */
	private int current;
	
	/** Numero di armi nella lista */
	private int numberOfWeapons;
	
	/** Numero massimo di armi che il contenitore puo' avere */
	private int maxWeapons;
	
	/**
	 * Costruttore di Equipment
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
	 * Restituisce l'arma corrente
	 * 
	 * @return (Weapon) - Arma nella posizione 'current'
	 */
	public Weapon getCurrentWeapon(){
		if( numberOfWeapons > 0 )
			return weaponsList.get( current );
		else
			return null;
	}
	
	/**
	 * Sposta l'indice alla successiva arma in lista.
	 * In particolare lo spostamento avviene in modo ciclico
	 * e quando supera la fine della lista riparte dall'inizio
	 */
	public void nextWeapon(){
		if( (current + 1) >= numberOfWeapons )
			current = 0;
		else
			current = current + 1;
	}
	
	/**
	 * Sposta l'indice alla precedente arma in lista.
	 * In particolare lo spostamento avviene in modo ciclico
	 * e quando supera l'inizio della lista riparte dalla fine
	 */
	public void backWeapon(){
		if( (current - 1) < 0 )
			current = numberOfWeapons - 1;
		else
			current = current - 1;
	}
	
	/**
	 * Restituisce l'arma in posizione i in lista.
	 * Fa un controllo di coerenza dell'indice
	 * 
	 * @param i - (int) Indice dell'arma richiesta
	 * @return (Weapon) Arma richiesta
	 */
	public Weapon getWeapon(int i){
		if( (i >= 0) && (i < numberOfWeapons) )
			return weaponsList.get(i);
		else
			return null;
	}
	
	/**
	 * Aggiunge una nuova arma in lista
	 * 
	 * @param newWeapon - (Weapon) L'arma da inserire
	 * @return (boolean) - true se l'arma e' stata inserita, false altrimenti
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
	 * Rimuove l'arma indicata dalla lista e la restituisce
	 * 
	 * @param i - (int) Indice dell'arma da rimuovere
	 * @return (Weapon) - Arma rimossa dalla lista
	 */
	public Weapon removeWeapon(int i){
		if( ( i >= 0 ) && ( i < numberOfWeapons ) )
			return weaponsList.remove( i );
		else 
			return null;
	}
	
	/**
	 * Rimuove l'arma nella posizione 'current'
	 * 
	 * @return Weapon - Arma rimossa
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
