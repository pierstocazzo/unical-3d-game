package logicClasses;

/**
 * Oggetto Player
 * 
 * @author Andrea
 */
public class Player extends Character {

	/** Contenitore di armi */
	private Equipment equipment;
	
	/**
	 * Costruttore Oggetto Player
	 * 
	 * @param id - (String) Identificatore
	 * @param currentLife - (int) Vita Corrente
	 * @param maxLife - (int) Vita Massima
	 * @param maxWeapons - (int) Numero massimo di Armi
	 */
	public Player( String id, int currentLife, int maxLife, int maxWeapons) {
		super( id, currentLife, maxLife );
		equipment = new Equipment(maxWeapons);
	}

	/**
	 * Restituisce il contenitore di armi
	 * 
	 * @return equipment - (Equipment) Contenitore di armi associato
	 */
	public Equipment getEquipment(){
		return equipment;
	}
	

}
