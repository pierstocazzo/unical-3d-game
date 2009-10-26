package logicClasses;
/**
 * Classe Astratta Personaggio
 * 
 * @author Andrea
 */
public abstract class Character {

	/** Idenfiticatore */
	String id;
	
	/** Valore Vita Corrente */
	private int currentLife;
	
	/** Valore Vita Massima */
	private int maxLife;
	
	/** Punteggio */
	private int score;
	
	/** Stato attuale */
	private int currentState;
	
	/**
	 * Costruttore dell'oggetto Character
	 * che verra' invocato solo dalle classi derivate
	 * in quanto Character e' una classe astratta
	 * 
	 * @param id - (String) Idenficatore
	 * @param currentLife - (int) Valore vita corrente
	 * @param maxLife - (int) Valore massimo che la vita puo' assumere
	 */
	public Character( String id, int currentLife, int maxLife ) {
		this.id = id;
		this.currentLife = currentLife;
		this.maxLife = maxLife;
		this.currentState = State.DEFAULT;
		this.score = 0;
	}
	
	/**
	 * Metodo che incrementa il valore currentLife
	 * rispettando il limite posto da maxLife
	 * 
	 * @param newLife - (int) Valore vita da aggiungere
	 */
	void addLife( int newLife ) {
		if( currentLife + newLife >= maxLife) {
			currentLife = maxLife;
		}
		else
			currentLife = currentLife + newLife;
	}
	
	// DA RIVEDERE
	/**
	 * Decrementa la vita corrente lasciando un valore coerente
	 * 
	 * @param removedLife - (int) Valore vita da rimuovere
	 */
	void removeLife( int removedLife ){
		currentLife = currentLife - removedLife;
		if( currentLife <= 0 ) {
			currentLife = 0;
			currentState = State.DEATH;
		}
	}
	
	// DA RIVEDERE
	/**
	 * Cambia lo stato corrente.
	 * Per coerenza si inseriscano solo stati definiti 
	 * nella classe State
	 * 
	 * @param newState - (int) Nuovo stato 
	 */
	void setState( int newState ){
		currentState = newState;
	}
	
	// DA RIVEDERE 
	/**
	 * Inserisce il nuovo valore di score
	 * assicurandosi che sia un valore coerente
	 * 
	 * @param newScore - (int) nuovo valore di score
	 */
	void setScore( int newScore ){
		if( newScore > 0 )
			score = newScore;
	}

	/**
	 * Ritorna il valore di vita corrente
	 * 
	 * @return currentLife 
	 */
	public int getCurrentLife() {
		return currentLife;
	}

	/**
	 * Ritorna la massima vita 
	 * 
	 * @return maxLife
	 */
	public int getMaxLife() {
		return maxLife;
	}

	/**
	 * Imposta il valore di vita massima,
	 * assicurandosi di lasciare valori coerenti
	 * 
	 * @param maxLife - (int) nuovo valore di massima vita
	 */
	public void setMaxLife( int newMaxLife ) {
		if( newMaxLife > 0) {
			this.maxLife = newMaxLife;
			if( currentLife > maxLife )
				this.currentLife = maxLife;
		}
	}

	/**
	 * Restituisce lo stato corrente
	 * 
	 * @return currentState
	 */
	public int getCurrState() {
		return currentState;
	}

	/**
	 * Restituisce score attuale
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}
}
