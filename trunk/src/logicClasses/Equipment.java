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
	
	/**
	 * Costruttore di Equipment
	 */
	public Equipment(){
		this.current = 0;
		this.numberOfWeapons = 0;
	}
	
	/**
	 * Restituisce l'arma corrente
	 * 
	 * @return (Weapon) - Arma nella posizione 'current'
	 */
	public Weapon getCurrentWeapon(){
		if(numberOfWeapons > 0)
			return weaponsList.get(current);
		else
			return null;
	}
	
	/**
	 * Sposta l'indice alla successiva arma in lista.
	 * In particolare lo spostamento avviene in modo ciclico
	 * e quando supera la fine della lista riparte dall'inizio
	 */
	public void nextWeapon(){
		if((current + 1) >= numberOfWeapons)
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
		if((current - 1) < 0)
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
		if((i >= 0) && (i < numberOfWeapons))
			return weaponsList.get(i);
		else
			return null;
	}
	
	/**
	 * Aggiunge una nuova arma in lista
	 * 
	 * @param newWeapon - (Weapon) L'arma da inserire
	 */
	public void addWeapon(Weapon newWeapon){
		weaponsList.add(newWeapon);
		numberOfWeapons++;
	}
	
	/**
	 * Rimuove l'arma indicata dalla lista e la restituisce
	 * 
	 * @param i - (int) Indice dell'arma da rimuovere
	 * @return (Weapon) - Arma rimossa dalla lista
	 */
	public Weapon removeWeapon(int i){
		if((i >= 0) && (i < numberOfWeapons))
			return weaponsList.remove(i);
		else 
			return null;
	}
	
	/**
	 * Rimuove l'arma nella posizione 'current'
	 * 
	 * @return Weapon - Arma rimossa
	 */
	public Weapon removeCurrentWeapon(){
		return weaponsList.remove(current);
	}
}
