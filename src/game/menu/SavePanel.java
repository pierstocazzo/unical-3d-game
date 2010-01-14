package game.menu;

import game.core.LogicWorld;
import game.graphics.GraphicalWorld;

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

/**
 * Class SavePanel
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class SavePanel extends JPanel {
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** Value of selected item */
	public int current = 0;
	
	/** Labels container */
	ArrayList<JLabel> itemLabel;
	
	/** Images container */
	ArrayList<String> imageFolder;
	
	/** InGameMenu Pointer */
	InGameMenu gameMenu;
	
	/** SaveMenu Pointer */
	SaveMenu saveMenu;
	
	/** It contains file name */
	JTextField text;
	
	public SavePanel(SaveMenu saveMenu, InGameMenu gameMenu){
		super();
		this.gameMenu = gameMenu;
		this.saveMenu = saveMenu;
		
		JLabel message = new JLabel("Please insert file name and press ENTER");
		add(message);
		
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
	
	/**
	 * Execute code associated to selected item
	 */
	public void executeSelectedItem(){
		saveMenu.setVisible(false);
		
		FileOutputStream fin = null;
		try {
				fin = new FileOutputStream("gameSave/"+text.getText());
			} 
		catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		ObjectOutputStream ois = null;
		try {
				ois = new ObjectOutputStream(fin);
			} 
		catch (IOException e) {
				e.printStackTrace();
			}
		
		try {
				ois.writeObject((LogicWorld)((GraphicalWorld)gameMenu.game).getCore());
			} 
		catch (IOException e) {
				e.printStackTrace();
			}
		
		gameMenu.setVisible(true);
	}
}
