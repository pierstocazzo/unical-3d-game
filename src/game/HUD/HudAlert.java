package game.HUD;

import game.common.GameTimer;
import game.common.State;
import game.core.LogicEnemy;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;

/**
 * Class HudAlert
 * 
 * @author Andrea Martire, Giuseppe Leone, Salvatore Loria
 */
public class HudAlert {

	/** Pointer to UserHud (main node) */
	UserHud userHud;
	
	/** Little text on the alert bar */
	Text alertNum;
	
	float alertValue;
	
	/** Background Quad of life Bar */
	Quad backQuad;
	
	/** Front Quad of life Bar */
	Quad frontQuad;
	
	/** Screen width */
	float screenWidth;
	
	/** Screen height */
	float screenHeight;
	
	/** Border bar parameter */
	int borderWeight = 3;
	
	/** Bar standard Length in pixel */
	int initialLenght;
	
	State stateColor;
	
	/** Constructor
	 * 
	 * @param userHud
	 */
	public HudAlert(UserHud userHud){
		this.userHud = userHud;
		screenWidth = userHud.gWorld.getResolution().x;
    	screenHeight = userHud.gWorld.getResolution().y;
    	
		createBar();
		
    	alertNum = Text.createDefaultTextLabel( "lifeNum" );
    	alertNum.setTextColor(ColorRGBA.black);
    	alertNum.setLocalScale(screenWidth/1200);
    	alertNum.setLocalTranslation( backQuad.getLocalTranslation().x, 
    			backQuad.getLocalTranslation().y, 0 );
    	userHud.gWorld.hudNode.attachChild( alertNum );
    	//for correct first visualization
    	alertValue = 0;
    	frontQuad.resize(initialLenght*alertValue/100, 
				frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										frontQuad.getLocalTranslation().y, 0);
		alertNum.print(Float.toString(alertValue));
		alertNum.setLocalTranslation(backQuad.getLocalTranslation().x-backQuad.getWidth()/2,
				backQuad.getLocalTranslation().y+backQuad.getHeight()/2, 0);
		alertNum.print("Alert Level");
   	}
	
	/**
	 * It updates Life bar informations
	 */
	public void update(){
		//initialize
		stateColor = State.DEFAULT;
		alertValue = getAlertLevel();
		//if an enemy is in alert state
		if(stateColor == State.ALERT)
			frontQuad.setDefaultColor(ColorRGBA.yellow);
		//if an enemy is in attack state
		if(stateColor == State.ATTACK)
			frontQuad.setDefaultColor(ColorRGBA.red);
		//if all enemy are in default state
		if(stateColor == State.DEFAULT)
			checkColor();
		frontQuad.resize(initialLenght*alertValue/100, 
						frontQuad.getHeight());
		frontQuad.setLocalTranslation(screenWidth/40+frontQuad.getWidth()/2+borderWeight,
										frontQuad.getLocalTranslation().y, 0);
	}
	
	public float getAlertLevel(){
		final int ALERT_RANGE = LogicEnemy.ALERT_RANGE;
		stateColor = State.DEFAULT;
		//calculate max alert level
		float max = -99999;
		for( String id : userHud.game.getEnemiesIds() ){
//			System.out.println("ALERT="+userHud.game.getAlertLevel(id));
			State currState = userHud.game.getState(id);
			if(currState == State.ATTACK || currState == State.FINDATTACK ||
					currState == State.ALERT || currState == State.FIND || currState == State.GUARDATTACK)
				if(userHud.game.getAlertLevel(id) > max )
					max = userHud.game.getAlertLevel(id);
			//check if current enemy is in attack
			if(currState == State.ATTACK || currState == State.FINDATTACK || currState == State.GUARDATTACK)
				stateColor = State.ATTACK;
			//check if current enemy is in alert e nobody is in attack
			if((currState == State.ALERT || currState == State.FIND)
					&& stateColor != State.ATTACK)
				stateColor = State.ALERT;
		}
		//If max < 0
		if(max < 0)
			return 0;//return minimal value
		//calculate time difference
		float diff = GameTimer.getTimeInSeconds() - max;
		//check difference
		if(diff < 0)
			diff = 0;
		//calculate percentage
		if(((ALERT_RANGE - diff)*100/ALERT_RANGE)<0)
			return 0;
		return (ALERT_RANGE - diff)*100/ALERT_RANGE;
	}
	
	/** 
	 * It creates Life Bar
	 */
	public void createBar(){
		backQuad = new Quad("backQuad", screenWidth/6 , screenHeight/30);
    	backQuad.setDefaultColor(ColorRGBA.blue);
    	backQuad.setLocalTranslation(screenWidth/40+backQuad.getWidth()/2,
    			screenHeight-screenHeight/20-backQuad.getHeight()/2, 0);
    	backQuad.lock();
    	userHud.gWorld.hudNode.attachChild( backQuad );
    	
    	frontQuad = new Quad("frontQuad", backQuad.getWidth() - borderWeight*2 , 
    										backQuad.getHeight() - borderWeight*2);
    	initialLenght = (int) frontQuad.getWidth();
    	frontQuad.setDefaultColor(ColorRGBA.green);
    	frontQuad.setLocalTranslation(backQuad.getLocalTranslation().x,
    			backQuad.getLocalTranslation().y, 0);
    	userHud.gWorld.hudNode.attachChild( frontQuad );
    	
	}
	
	/**
	 * It checks value life and change quad color
	 */
	public void checkColor(){
		if(alertValue > 80)
			frontQuad.setDefaultColor(ColorRGBA.red);
		else if(alertValue > 20)
			frontQuad.setDefaultColor(ColorRGBA.yellow);
		else 
			frontQuad.setDefaultColor(ColorRGBA.green);
	}
}
