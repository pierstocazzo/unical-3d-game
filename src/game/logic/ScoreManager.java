package game.logic;

import java.util.Set;

public class ScoreManager {
	LogicWorld world;
	
	public ScoreManager( LogicWorld world ) {
		this.world = world;
	}
	
	public void update(){
		int playerScore = world.player.getScore();
		if( playerScore > 100 && playerScore < 200 ){
			//Level 1
			world.player.currentLevel = 1;
		}
		else {
			//TODO
		}
		
		if( world.player.currentLevel != world.player.previousLevel ){
			changeDifficult();
			world.player.previousLevel = world.player.currentLevel;
		}
			
		
	}
	
	public void changeDifficult(){
		Set<String> keySet = world.enemies.keySet();
		for( String id : keySet ){
			world.enemies.get(id).addLife(10);
		}
	}
}
