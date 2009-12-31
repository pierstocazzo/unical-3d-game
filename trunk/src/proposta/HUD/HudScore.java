package proposta.HUD;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;

/**
 * Class HudScore
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudScore {
	
	/** User Hud */
	UserHud userHud;
	/** Score value */
	int value;
	/** useful variable */
	int xPosition;
	/** useful variable */
	int yPosition;
	/** X position of text */
	int xScore;
	/** Y position of text */
	int yScore;
	/** Score Text */
	Text score;
	
	/**
	 * Constructor
	 * 
	 * @param userHud - (UserHud)
	 */
	public HudScore(UserHud userHud){
		//inizializza valore score
		this.userHud = userHud;
		value = 0;
		xPosition = (int) (userHud.gWorld.getResolution().x/2) ;
		yPosition = (int) userHud.gWorld.getResolution().y;
		
		score = Text.createDefaultTextLabel( "Score" );
    	score.setTextColor(ColorRGBA.red);
    	score.setLocalScale(3);
    	score.setLocalTranslation( xPosition-(score.getWidth()/2), yPosition-100, 0 );
    	userHud.gWorld.hudNode.attachChild( score );
	}
	
	/**
	 * Update Score informations
	 */
	public void update(){
		value = userHud.game.getScore(userHud.gWorld.player.id);
		score.print("SCORE: "+Integer.toString(value));
		score.setLocalTranslation( xPosition-(score.getWidth()/2), yPosition-100, 0 );
		//aggiorna score
	}
}
