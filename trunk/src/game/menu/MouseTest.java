package game.menu;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


public class MouseTest extends JFrame{

	public MouseTest(){
		setSize(400,400);
		JButton button = 
			new JButton( new ImageIcon("src/game/data/images/menu/newgame.png"));
		getContentPane().add(button);
	}
	
	public static void main( String[] argv ){
		JFrame f = new MouseTest();
		f.setVisible(true);
	}
}
