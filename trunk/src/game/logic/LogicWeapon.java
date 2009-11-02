package game.logic;

/**
 * Classe LogicWeapon
 * 
 * @author Andrea
 *
 */
public class LogicWeapon {
	
	/** Identifier */
	String id;
	
	/** Tipo di arma */
	private String type;
	
	/** LogicWeapon's power */
	private int power;
	
	/** Damage of every bullet */
	private int damage;
	
	/** Remaining bullets */
	private int currBullets;
	
	/** True = Heavy weapon, False else */
	boolean isHeavy;
	
	/**
	 * Constructor
	 * 
	 * @param i - Identifier
	 * @param pw - Power of every bullet
	 * @param currBull - Aviable bullets
	 * @param isH - If it is heavy
	 */
	public LogicWeapon( String id, int power, int damage, int currentBull, boolean isHeavy) {
		this.id = id;
		this.power = power;
		this.damage = damage;
		this.currBullets = currentBull;
		this.isHeavy = isHeavy;
	}

	/**
	 * It gets weapon's type
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * It allows to set weapon's type
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * It get weapon's power
	 * 
	 * @return power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * It sets weapon's power
	 * 
	 * @param power
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * It gets damage of every bullet
	 * 
	 * @return damage
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * It sets damage of every bullet
	 * 
	 * @param damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * It returns remaining bullets
	 * 
	 * @return currBullets
	 */
	public int getCurrBullets() {
		return currBullets;
	}

	/**
	 * Add new bullets
	 * 
	 * @param newBullets
	 */
	public void addBullets(int newBullets) {
		if(currBullets>0)
			this.currBullets += newBullets;
	}

}
