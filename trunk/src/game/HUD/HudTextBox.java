package game.HUD;

import java.util.ArrayList;

import com.jme.scene.Text;

/**
 * Class HudTextBox (DEPRECATED)
 * 
 * Used for print intrusive message on screen
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class HudTextBox {

	/** Text printed */
	String text;
	
	/** Useful pointer */
	UserHud userHud;
	
	/** screen weight */
	float weight;
	
	/** screen height */
	float height;
	
	/** max char number for every text line */
	int maxCharLine = 35;
	
	/** text label container */
	ArrayList<Text> textList;
	
	/** substring container */
	ArrayList<String> stringList;
	
	/**
	 * Constructor
	 * 
	 * @param userHud (UserHud)
	 */
	public HudTextBox(UserHud userHud){
		this.userHud = userHud;
		weight = userHud.world.getResolution().x;
		height = userHud.world.getResolution().y;
		textList = new ArrayList<Text>();
		stringList = new ArrayList<String>();
		
		setText("TEST. "+"Anche oggi le strade di Roma, Milano e altre tre citta' saranno " +
				"congestionate dai cittadini a caccia di affari. Se per la Confcommercio " +
				"7 italiani su 10 sono interessati a fare grandi affari con i saldi, " +
				"per il Codacons, associazione dei consumatori, i commercianti in questo " +
				"anno di crisi hanno poco da sperare. I negozi intanto gia' presentano " +
				"sconti fortissimi, fino alla meta' del prezzo.");
		
	}
	
	/** 
	 * Set new text
	 * 
	 * @param (string) - text
	 */
	public void setText(String text){
		this.text = text;
		checkText();
	}
	
	/**
	 * Get current text
	 * 
	 * @return (String) text
	 */
	public String getText(){
		return new String(text);
	}
	
	/**
	 * It splits text in subString for obtain a nice text printing
	 */
	public void checkText(){
		int counter = 0;
		int min = 0;
		int numberLine = 1;
		int lastSpace = 0;
		/** Loop on the text */
		for(int i = 0; i < text.length(); i++){
			/** Check if there is a space and save it */
			if (text.charAt(i)==' ')
				lastSpace = i;
			/** Check if has been exceeded char limit */
			if ( counter >= maxCharLine){
				Text tmp = Text.createDefaultTextLabel("line"+numberLine);
				String subString = text.substring(min, lastSpace);
				
				/** create subString */
				stringList.add(subString);
				tmp.print(subString);
				tmp.setLocalScale(1.5f);
				tmp.setLocalTranslation(weight/2-tmp.getWidth()/2,height/2-tmp.getHeight()*numberLine,0);
				tmp.lock();
				textList.add(tmp);
				userHud.hudNode.attachChild(tmp);
				
				numberLine++;
				counter = 0;
				min = lastSpace + 1;
				i = lastSpace + 1;
			}
			counter++;
			/** It checks if there are other chars non used */
			if( i + 1 == text.length()){
				Text tmp = Text.createDefaultTextLabel("line"+numberLine);
				String subString = text.substring(min, i + 1);
				stringList.add(subString);
				tmp.print(subString);
				tmp.setLocalScale(1.5f);
				tmp.setLocalTranslation(weight/2-tmp.getWidth()/2,height/2-tmp.getHeight()*numberLine,0);
				tmp.lock();
				textList.add(tmp);
				userHud.hudNode.attachChild(tmp);
			}
		}
		
		/** it moves the text group on screen center */
		for(int i = 0; i < textList.size(); i++){
			Text tmp =  textList.get(i);
			tmp.setLocalTranslation(tmp.getLocalTranslation().x, 
					tmp.getLocalTranslation().y + (textList.size()/2)*tmp.getHeight(), 0);
		}
	}
}
