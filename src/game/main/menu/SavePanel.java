package game.main.menu;


import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SavePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public int current = 0;
	ArrayList<JLabel> item;
	ArrayList<String> imageFolder;
	InGameMenu gm;
	SaveMenu sm;
	JTextField text;
	
	public SavePanel(SaveMenu sm, InGameMenu gm){
		super();
		this.gm = gm;
		this.sm = sm;
		
		GregorianCalendar gc = new GregorianCalendar();

		text = new JTextField("Game_"+
								gc.get(Calendar.DAY_OF_MONTH)+"-"+
								gc.get(Calendar.MONTH)+"-"+
								gc.get(Calendar.YEAR)+"_"+
								gc.get(Calendar.HOUR_OF_DAY)+"-"+
								gc.get(Calendar.MINUTE)+"-"+
								gc.get(Calendar.SECOND), 40);
		
		text.setEditable(true);
		add(text);
		this.setLayout(new GridLayout(5,1));
		this.setOpaque(false);
	}
	
	public void executeSelectedItem(){
		sm.setVisible(false);
		
		FileOutputStream fin = null;
		try {fin = new FileOutputStream("gameSave/"+text.getText());} 
		catch (FileNotFoundException e) {e.printStackTrace();}
		ObjectOutputStream ois = null;
		try {ois = new ObjectOutputStream(fin);} 
		catch (IOException e) {e.printStackTrace();}
		try {ois.writeObject(gm.mainMenu.game.logicGame);} 
//		try {ois.writeObject(new Movement( Direction.FORWARD, 40 ));} 
		catch (IOException e) {e.printStackTrace();System.exit(0);}
		
		gm.setVisible(true);
	}
}
