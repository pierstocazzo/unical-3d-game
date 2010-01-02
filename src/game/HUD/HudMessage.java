package game.HUD;

import game.common.GameTimer;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;

public class HudMessage {
	Text message;
	String messageString;
	float time;
	public boolean enabled = true;
	int seconds;
	UserHud userHud;
	int x = 0;
	int y = 0;
	
	public HudMessage(String messageString, int seconds, ColorRGBA color, UserHud userHud){
		message = Text.createDefaultTextLabel(messageString);
		message.setTextColor(color);
		message.lock();
		
		time = GameTimer.getTimeInSeconds();
		this.messageString = messageString;
		this.seconds = seconds;
		this.userHud = userHud;
		
		userHud.gWorld.hudNode.attachChild(message);
	}
	
	public void update(int x, int y){
		if(GameTimer.getTimeInSeconds() > time + seconds){
			enabled = false;
			messageString = "";
			message.print(messageString);
		}
		this.x = x;
		this.y = (int) (y - message.getHeight()/2);
	}
	
	public void show(){
		if(enabled){
			message.setLocalTranslation(x,y,0);
			message.print(messageString);
		}
	}
	
	public float getHeight(){
		return message.getHeight();
	}
}
