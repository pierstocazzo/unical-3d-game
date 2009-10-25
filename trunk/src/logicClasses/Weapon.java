package logicClasses;

/**
 * Classe Arma
 * 
 * @author Andrea
 *
 */
public class Weapon {
	
	/** Identificatore */
	String id;
	
	/** Tipo di arma */
	private String type;
	
	/** Potenza dell'arma */
	private int power;
	
	/** Danni arrecati da ogni colpo dell'arma */
	private int damage;
	
	/** Proiettili o razzi correnti */
	private int currBullets;
	
	/** True se l'arma e' pesante, False altrimenti */
	boolean isHeavy;
	
	/**
	 * Costruttore
	 * 
	 * @param i - Identificatore
	 * @param pw - Potenza di Fuoco per singolo colpo
	 * @param currBull - Colpi disponibili
	 * @param isH - Se Pesante o meno
	 */
	public Weapon(String i,int pw, int dmg, int currBull, boolean isH) {
		id=i;
		power=pw;
		damage=dmg;
		currBullets=currBull;
		isHeavy=isH;
	}

	/**
	 * Restituisce il tipo dell'arma
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Permette di cambiare il tipo
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Restituisce la potenza dell'arma
	 * 
	 * @return power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * Imposta la potenza dell'arma
	 * 
	 * @param power
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * Restituisce i danni causabili da ogni colpo
	 * 
	 * @return damage
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Imposta i danni causabili da ogni colpo
	 * 
	 * @param damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Restituisce i colpi rimanenti
	 * 
	 * @return currBullets
	 */
	public int getCurrBullets() {
		return currBullets;
	}

	/**
	 * Aggiunge nuovi colpi
	 * 
	 * @param newBullets
	 */
	public void addBullets(int newBullets) {
		if(currBullets>0)
			this.currBullets += newBullets;
	}

}
