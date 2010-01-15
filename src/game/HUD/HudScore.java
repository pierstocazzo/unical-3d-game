package game.HUD;

import utils.Loader;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Class HudScore
 * 
 * It displays score info
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
	
	/** contains score image */
	Quad scoreQuad;
	
	/**
	 * Constructor
	 * 
	 * @param userHud - (UserHud)
	 */
	public HudScore(UserHud userHud){
		//initializa score value
		this.userHud = userHud;
		value = 0;
		// get score info
		xPosition = (int) (userHud.gWorld.getResolution().x/2) ;
		yPosition = (int) userHud.gWorld.getResolution().y;
		
		// create a score quad
		scoreQuad = new Quad("scoreQuad", userHud.gWorld.getResolution().x/8, userHud.gWorld.getResolution().y/15);
		float xScoreQuad = xPosition - scoreQuad.getWidth()/4;
		scoreQuad.setLocalTranslation(xScoreQuad, userHud.gWorld.getResolution().y - scoreQuad.getHeight(), 0);
		
		/* load the texture to set to the map */
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture( TextureManager.loadTexture( Loader.load( "game/data/images/score.png" ), true ) );
        ts.setEnabled(true);
        scoreQuad.setRenderState(ts);
        
        BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);
        
        scoreQuad.setRenderState(as);
		
		userHud.gWorld.hudNode.attachChild(scoreQuad);
		
		// create score text
		score = Text.createDefaultTextLabel( "Score" );
    	score.setTextColor(ColorRGBA.red);
    	score.setLocalScale(userHud.gWorld.getResolution().x/400);
    	score.setLocalTranslation( scoreQuad.getLocalTranslation().x+scoreQuad.getWidth()/2, 
				scoreQuad.getLocalTranslation().y-scoreQuad.getHeight()/2, 0 );
    	userHud.gWorld.hudNode.attachChild( score );
	}
	
	/**
	 * Update Score informations
	 */
	public void update(){
		value = userHud.game.getScore(userHud.gWorld.player.id);
		score.print(Integer.toString(value));
		float xScoreQuad = xPosition - (scoreQuad.getWidth() + score.getWidth())/8;
		scoreQuad.setLocalTranslation(xScoreQuad, scoreQuad.getLocalTranslation().y, 0);
		
		score.setLocalTranslation( xPosition + (scoreQuad.getWidth() + score.getWidth())/8, 
								scoreQuad.getLocalTranslation().y-scoreQuad.getHeight()/2, 0 );
	}
}
