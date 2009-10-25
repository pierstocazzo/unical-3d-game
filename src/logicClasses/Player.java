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
	 */
	public Player( String id, int currentLife, int maxLife ) {
		super( id, currentLife, maxLife );
		// TODO Auto-generated constructor stub
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
