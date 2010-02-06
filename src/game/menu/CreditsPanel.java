/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

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
        setOpaque(false);
		JLabel photo = new JLabel();
		Image img = ImagesContainer.getBackgroundCreditsFrame();
		photo.setIcon(new ImageIcon(img));
        
        add(photo,BorderLayout.CENTER);
		
		addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainMenu.switchToPanel("mainPanel");
			}
		});
	}
}