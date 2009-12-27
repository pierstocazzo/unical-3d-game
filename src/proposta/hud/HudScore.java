package proposta.hud;

import java.util.Random;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;

/**
 * Class HudScore
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudScore {
	
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
		value = 0;
		xPosition = (int) (userHud.gWorld.settings.getWidth()/2) ;
		yPosition = userHud.gWorld.settings.getHeight();
		
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
		Random r = new Random();
		score.print("SCORE: "+Integer.toString(r.nextInt()));
		score.setLocalTranslation( xPosition-(score.getWidth()/2), yPosition-100, 0 );
		//aggiorna score
	}
}
