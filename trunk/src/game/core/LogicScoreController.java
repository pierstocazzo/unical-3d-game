package game.core;

/**
 * Class Logic Controller (Score and Levels)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class LogicScoreController {
	
	int score;
	int level;
	
	public LogicScoreController() {
		score = 0;
		level = 1;
	}

	void setEnemyKilled(){
		// A seconda del livello incrementa lo score
		// di un valore associato
		switch (level) {
			case 1: score = score + 5;break;
			case 2: score = score + 10;break;
			case 3: score = score + 15;break;
			case 4: score = score + 20;break;
			case 5: score = score + 25;break;
		}
		update();
	}
	
	/**
	 * Update the current level using score value
	 */
	void update(){
		if( score < 100 )
			level = 1;
		else if ( score < 250 )
			level = 2;
		else if ( score < 500 )
			level = 3;
		else if ( score < 800 )
			level = 4;
		else if ( score < 1100 )
			level = 5;
	}
	
	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	/**
	 * it decreases score and level after killing player
	 */
	void playerKilled(){
		//diminuisco punteggio dell'ottanta per cento
		score = score -  (int)(score*0.8f);
		update();
	}
	
	void reset(){
		level = 1;
		score = 0;
	}
}