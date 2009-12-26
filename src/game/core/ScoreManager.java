package game.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class Logic Controller (Score and Levels)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class ScoreManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	LogicWorld world;
	HashMap<String, Score> players;
	
	public ScoreManager( LogicWorld world ) {
		this.world = world;
		players = new HashMap<String, Score>();
	}

	public void initScoreManager() {
		for( String id : world.getPlayersId() ) {
			players.put( id, new Score() );
		}
	}
	
	/**
	 * Update the current level using score value
	 */
	void update(){
		for( String id : players.keySet() ) {
			players.get(id).update();
		}
	}

	/**
	 * it decreases score and level after the player was killed
	 */
	void playerKilled( String playerId ) {
		players.get(playerId).decreaseScore();
	}
	
	void enemyKilled( String playerId ) {
		players.get(playerId).increaseScore();
	}
	
	void reset() {
		for( String id : players.keySet() ) {
			players.get(id).reset();
		}
	}
	
	public int getScore( String id ) {
		return players.get(id).score;
	}
	
	public int getLevel( String id ) {
		return players.get(id).level;
	}
	
	
	
	public class Score {
		int score = 0;
		int level = 1;
		
		public void reset() {
			level = 1;
			score = 0;
		}

		public void update() {
			if( score < 10 )
				level = 1;
			else if ( score < 25 ) // passo al livello 2 dopo aver ucciso 5 nemici
				level = 2;
			else if ( score < 125 ) // passo al livello 3 dopo aver ucciso altri 10 nemici
				level = 3;
			else if ( score < 350 ) // passo al livello 4 dopo aver ucciso altri 15 nemici
				level = 4;
			else if ( score < 750 ) // passo al livello 5 dopo aver ucciso altri 20 nemici
				level = 5;
		}
		
		
		public void increaseScore() {
			switch ( level ) {
			case 1:
				score = score + 5;
				break;
			case 2: 
				score = score + 10;
				break;
			case 3:
				score = score + 15;
				break;
			case 4:
				score = score + 20;
				break;
			case 5: 
				score = score + 25;
			}
		}
		
		public void decreaseScore() {
			score = score -  (int)(score*0.8f);
		}
	}
}