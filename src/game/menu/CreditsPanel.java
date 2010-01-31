package game.menu;

import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        
		JLabel photo = new JLabel();
		Image img = ImagesContainer.getBackgroundCreditsFrame();
		photo.setIcon(new ImageIcon(img));
        
        add(photo,BorderLayout.CENTER);
		
		addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainMenu.switchToMainPanel();
			}
		});
	}
}