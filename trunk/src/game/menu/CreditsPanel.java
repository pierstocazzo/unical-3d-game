package game.menu;

import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class CreditsFrame
 * Used for displaying game credits 
 * (Development Team, reference libraries, copied code, projects reference)
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class CreditsPanel extends JPanel {
	/** Class ID */
	private static final long serialVersionUID = 1L;

	/** Useful pointer to MainMenu frame */
	MainMenu mainMenu;
	
	/** 
	 * Constructor Class CreditsFrame
	 * 
	 * @param mainMenu (MainMenu)
	 */
	public CreditsPanel( final MainMenu mainMenu ){
		super();
		this.mainMenu = mainMenu;
		
		// Applied a borderLayout
		setLayout(new BorderLayout());
		setOpaque(false);
        
		JLabel photo = new JLabel();
		Image img = ImagesContainer.getBackgroundCreditsFrame();
		photo.setIcon(new ImageIcon(img));
        
        add(photo,BorderLayout.CENTER);
		
		addKeyListener( new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				mainMenu.switchToMainPanel();
			}
		});
		setFocusable(true);
	}
}