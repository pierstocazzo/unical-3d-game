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
	 * Costruttore dell'oggetto Charapter
	 * che verra' invocato solo dalle classi derivate
	 * in quanto Charapter e' una classe astratta
	 * 
	 * @param i - Idenficatore
	 * @param cLife - Valore vita corrente
	 * @param mLife - Valore massimo che la vita puo' assumere
	 */
	public Character(String i, int cLife, int mLife) {
		id=i;
		currentLife=cLife;
		maxLife=mLife;
		currentState=State.DEFAULT;
		score=0;
	}
	
	/**
	 * Metodo che incrementa il valore currentLife
	 * rispettando il limite posto da maxLife
	 * 
	 * @param newLife - Valore vita da aggiungere
	 */
	void addLife(int newLife){
		if(currentLife+newLife>=maxLife){
			currentLife=maxLife;
		}
		else
			currentLife+=newLife;
	}
	
	/**
	 * Decrementa la vita corrente lasciando un valore coerente
	 * 
	 * @param rmLife - Valore vita da rimuovere
	 */
	void removeLife(int rmLife){
		currentLife-=rmLife;
		if(currentLife<0){
			currentLife=0;
			currentState=State.DEATH;
		}
	}
	
	/**
	 * Cambia il valore dello stato corrente.
	 * Per coerenza si inseriscano solo stati definiti 
	 * nella classe State
	 * 
	 * @param newState - Nuovo stato
	 */
	void setState(int newState){
		currentState=newState;
	}
	
	/**
	 * Inserisce il nuovo valore di score
	 * assicurandosi che sia un valore coerente
	 * 
	 * @param newScore
	 */
	void setScore(int newScore){
		if(newScore>0)
			score=newScore;
	}

	/**
	 * Restituisce la vita corrente
	 * 
	 * @return currentLife
	 */
	public int getCurrentLife() {
		return currentLife;
	}

	/**
	 * Restituisce la massima vita corrente
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
	 * @param maxLife
	 */
	public void setMaxLife(int maxLife) {
		if(maxLife>0){
			this.maxLife = maxLife;
			if(currentLife>maxLife)
				this.currentLife=maxLife;
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
