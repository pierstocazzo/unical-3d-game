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

import game.common.GameConf;
import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * Class OptionsMenu
 * A frame that contains game settings and allow to modify them
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class OptionsPanel extends JPanel {
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** MainMenu Pointer */
	MainMenu mainMenu;
	
	/** JTextPath that contains a xml file path */
	JTextArea sceneFileName;
	
	/** His Pointer used in a subClass */
	OptionsPanel optionsMenu;
	
	/** Container */
	Vector<String> itemsTrueFalse;
	
	/** Container */
	final Vector<String> itemsKeyCommands;

	/** Scene's file path */
	String sceneFilePath;

	/** Resolution's container */
	Vector<String> resolutions;
	
	/**
	 * Constructor
	 * 
	 * @param mainMenu (MainMenu)
	 */
	public OptionsPanel( final MainMenu mainMenu ){
		super();
		this.mainMenu = mainMenu;
		optionsMenu = this;

		// Get screen size informations
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// apply screen size informations
		setBounds(0,0,screenSize.width, screenSize.height);
		
		// apply layout to main panel
		setLayout(new BorderLayout());
		setOpaque(false);
		
		//Create a new sub panel that divide frame
		JPanel dividePanel = new JPanel();
		dividePanel.setLayout(new BorderLayout());
		add(dividePanel,BorderLayout.CENTER);
		
		//Create first visible panel ( Monitor Settings )
		JPanel grid = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		grid.setLayout(layout);
		grid.setOpaque(true);
		grid.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/4));
		//add a label to panel
		TitledBorder titleBorder = new TitledBorder("General Settings");
		grid.setBorder(titleBorder);
		dividePanel.add(grid, BorderLayout.NORTH);
		
		GridBagConstraints lim = new GridBagConstraints();
		
		//Create a label that show text
		JLabel resolutionLabel = new JLabel("Screen's Resolution");
		lim.gridx = 0;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(resolutionLabel, lim);
		grid.add(resolutionLabel);
		
		// Create comboBox about screen resolution settings
		resolutions = new Vector<String>();
		resolutions.add("1680x1050");
		resolutions.add("1440x900");
		resolutions.add("1280x1024");
		resolutions.add("1280x800");
		resolutions.add("1024x768");
		resolutions.add("1024x600");
		resolutions.add("800x600");
		
		final JComboBox resolutionCombo = new JComboBox(resolutions);
		resolutionCombo.setSelectedIndex(
				resolutions.indexOf((
						GameConf.getSetting( GameConf.RESOLUTION_WIDTH ) + "x" +
						GameConf.getSetting( GameConf.RESOLUTION_HEIGHT )) ));
		
		lim.gridx = 1;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(resolutionCombo, lim);
		grid.add(resolutionCombo);
		
		//Create a Label that show text
		JLabel fullscreenLabel = new JLabel("Fullscreen");
		lim.gridx = 0;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(fullscreenLabel, lim);
		grid.add(fullscreenLabel);
		
		// Create a comboBox that allow to choose fullscreen true or false
		itemsTrueFalse = new Vector<String>();
		itemsTrueFalse.add("true");
		itemsTrueFalse.add("false");
		
		final JComboBox fullscreenCombo = new JComboBox(itemsTrueFalse);
		fullscreenCombo.setSelectedIndex(itemsTrueFalse.indexOf(
				GameConf.getSetting( GameConf.IS_FULLSCREEN )
		));
		lim.gridx = 1;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(fullscreenCombo, lim);
		grid.add(fullscreenCombo);
		
		//Useful subPanel that allow to insert different components in a particular position
		JPanel xmlPanel = new JPanel();
		xmlPanel.setLayout(new FlowLayout());
		lim.gridx = 0;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(xmlPanel, lim);
		grid.add(xmlPanel);
		
		// Create a label
		JLabel xmlLabel = new JLabel("Scene's XML file: ");
		xmlPanel.add(xmlLabel);
	
		//JTextBox that contains a file path
		sceneFilePath = GameConf.getSetting( GameConf.SCENE_FILE_PATH );
        int lastDot = sceneFilePath.lastIndexOf( '/' );
        String sceneFile;
        sceneFile = sceneFilePath.substring( lastDot + 1 );
		sceneFileName = new JTextArea( sceneFile );
		xmlPanel.add(sceneFileName);
		
		//Button Browses for choose files
		JButton browsButton = new JButton("Browse");
		lim.gridx = 1;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(browsButton, lim);
		grid.add(browsButton);
		
		//Add a listener to last button for allow to choose a file
		browsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// create a object for file choosing
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(optionsMenu);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					sceneFilePath = file.getPath();
					sceneFileName.setText(file.getName());
				}
			}
		});
		
		//Create a label
		JLabel soundLabel = new JLabel("Sound");
		lim.gridx = 0;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(soundLabel, lim);
		grid.add(soundLabel);
		
		// create a JComboBox - Sound true or not
		final JComboBox soundCombo = new JComboBox(itemsTrueFalse);
		soundCombo.setSelectedIndex(itemsTrueFalse.indexOf(
				GameConf.getSetting( GameConf.SOUND_ENABLED )
		));
		lim.gridx = 1;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(soundCombo, lim);
		grid.add(soundCombo);
		
		//Create second visible panel
		JPanel grid2 = new JPanel();
		GridBagLayout layout2 = new GridBagLayout();
		grid2.setLayout(layout2);
		grid2.setOpaque(true);
		grid2.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/2));
		TitledBorder titleBorder2 = new TitledBorder("Player Command");
		grid2.setBorder(titleBorder2);
		dividePanel.add(grid2, BorderLayout.CENTER);
		
		//initialize commands list
		itemsKeyCommands = new Vector<String>();
		
		itemsKeyCommands.add("A");itemsKeyCommands.add("B");itemsKeyCommands.add("C");
		itemsKeyCommands.add("D");itemsKeyCommands.add("E");
		itemsKeyCommands.add("F");itemsKeyCommands.add("G");itemsKeyCommands.add("H");
		itemsKeyCommands.add("I");itemsKeyCommands.add("J");
		itemsKeyCommands.add("K");itemsKeyCommands.add("L");itemsKeyCommands.add("M");
		itemsKeyCommands.add("N");itemsKeyCommands.add("O");
		itemsKeyCommands.add("P");itemsKeyCommands.add("Q");itemsKeyCommands.add("R");
		itemsKeyCommands.add("S");itemsKeyCommands.add("T");
		itemsKeyCommands.add("U");itemsKeyCommands.add("V");itemsKeyCommands.add("X");
		itemsKeyCommands.add("Y");itemsKeyCommands.add("W");
		itemsKeyCommands.add("Z");itemsKeyCommands.add("LSHIFT");itemsKeyCommands.add("RSHIFT");
		itemsKeyCommands.add("SPACE");itemsKeyCommands.add("CTRL");itemsKeyCommands.add("ENTER");
		itemsKeyCommands.add("DOWN");itemsKeyCommands.add("UP");itemsKeyCommands.add("LEFT");
		itemsKeyCommands.add("RIGHT");itemsKeyCommands.add("NUMPAD0");itemsKeyCommands.add("NUMPAD1");
		itemsKeyCommands.add("NUMPAD2");itemsKeyCommands.add("NUMPAD3");itemsKeyCommands.add("NUMPAD4");
		itemsKeyCommands.add("NUMPAD5");itemsKeyCommands.add("NUMPAD6");itemsKeyCommands.add("NUMPAD7");
		itemsKeyCommands.add("NUMPAD8");itemsKeyCommands.add("NUMPAD9");
		//create a label
		JLabel combo1Label = new JLabel("Walk Forward");
		lim.gridx = 0;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo1Label, lim);
		grid2.add(combo1Label);
		
		// JComboBox - walk forward
		final JComboBox comboWalkForward = new JComboBox(itemsKeyCommands);
		comboWalkForward.setSelectedIndex(itemsKeyCommands.indexOf(
				GameConf.getSetting( GameConf.FORWARD_KEY )
		));
		lim.gridx = 1;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkForward, lim);
		grid2.add(comboWalkForward);
		
		//create a label
		JLabel combo2Label = new JLabel("Walk Backward");
		lim.gridx = 0;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo2Label, lim);
		grid2.add(combo2Label);
		
		// JComboBox - walk backward
		final JComboBox comboWalkBackward = new JComboBox(itemsKeyCommands);
		comboWalkBackward.setSelectedIndex(itemsKeyCommands.indexOf(
			GameConf.getSetting( GameConf.BACKWARD_KEY )
		));
		lim.gridx = 1;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkBackward, lim);
		grid2.add(comboWalkBackward);
		
		//create a label
		JLabel combo3Label = new JLabel("Turn Right");
		lim.gridx = 0;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo3Label, lim);
		grid2.add(combo3Label);
		
		// JComboBox - walk to right
		final JComboBox comboWalkRight = new JComboBox(itemsKeyCommands);
		comboWalkRight.setSelectedIndex(itemsKeyCommands.indexOf(
			GameConf.getSetting( GameConf.TURNRIGHT_KEY )
		));
		lim.gridx = 1;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkRight, lim);
		grid2.add(comboWalkRight);
		
		//create a label
		JLabel combo4Label = new JLabel("Trun Left");
		lim.gridx = 0;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo4Label, lim);
		grid2.add(combo4Label);
		
		// JComboBox - walk to left
		final JComboBox comboWalkLeft = new JComboBox(itemsKeyCommands);
		comboWalkLeft.setSelectedIndex(itemsKeyCommands.indexOf(
			GameConf.getSetting( GameConf.TURNLEFT_KEY )
		));
		lim.gridx = 1;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkLeft, lim);
		grid2.add(comboWalkLeft);
		
		//create a label
		JLabel combo5Label = new JLabel("Run");
		lim.gridx = 0;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo5Label, lim);
		grid2.add(combo5Label);
		
		// JComboBox - run
		final JComboBox comboRun = new JComboBox(itemsKeyCommands);
		comboRun.setSelectedIndex(itemsKeyCommands.indexOf(
				GameConf.getSetting( GameConf.RUN_KEY )
		));
		lim.gridx = 1;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboRun, lim);
		grid2.add(comboRun);
		
		//create a label
		JLabel combo6Label = new JLabel("Pause");
		lim.gridx = 0;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo6Label, lim);
		grid2.add(combo6Label);
		
		// JComboBox - pause
		final JComboBox comboPause = new JComboBox(itemsKeyCommands);
		comboPause.setSelectedIndex(itemsKeyCommands.indexOf(
			GameConf.getSetting( GameConf.PAUSE_KEY )
		));
		lim.gridx = 1;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboPause, lim);
		grid2.add(comboPause);
		
		//useful buttons in the third visible panel
		JPanel flow = new JPanel();
		flow.setLayout(new FlowLayout());
		dividePanel.add(flow,BorderLayout.SOUTH);
		
		final JButton buttonReset = new JButton("RESET");
		flow.add(buttonReset);
		JButton buttonCancel = new JButton("CANCEL");
		flow.add(buttonCancel);
		JButton buttonOk = new JButton("SAVE");
		flow.add(buttonOk);
		
		//add reset action
		buttonReset.setMnemonic('r');
		buttonReset.setMnemonic('R');
		buttonReset.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	comboWalkForward.setSelectedIndex(
			        			itemsKeyCommands.indexOf( GameConf.getDefaultSetting( GameConf.FORWARD_KEY ) ));
			        	comboWalkBackward.setSelectedIndex(
			        			itemsKeyCommands.indexOf( GameConf.getDefaultSetting( GameConf.BACKWARD_KEY ) ));
			        	comboWalkRight.setSelectedIndex(
			        			itemsKeyCommands.indexOf( GameConf.getDefaultSetting( GameConf.TURNRIGHT_KEY ) ));
			        	comboWalkLeft.setSelectedIndex(
			        			itemsKeyCommands.indexOf( GameConf.getDefaultSetting( GameConf.TURNLEFT_KEY ) ));
			        	comboRun.setSelectedIndex(
			        			itemsKeyCommands.indexOf( GameConf.getDefaultSetting( GameConf.RUN_KEY ) ));
			        	comboPause.setSelectedIndex(
			        			itemsKeyCommands.indexOf( GameConf.getDefaultSetting( GameConf.PAUSE_KEY ) ));
			        	
			        	resolutionCombo.setSelectedIndex(
			    				resolutions.indexOf(( GameConf.getDefaultSetting( GameConf.RESOLUTION_WIDTH ) + "x" +
			    						GameConf.getDefaultSetting( GameConf.RESOLUTION_HEIGHT ) ) ));
			    		
			    		fullscreenCombo.setSelectedIndex( 
			    				itemsTrueFalse.indexOf( GameConf.getDefaultSetting( GameConf.IS_FULLSCREEN ) ) );
			    		
			    		sceneFilePath = GameConf.getDefaultSetting( GameConf.SCENE_FILE_PATH );
			            int lastDot = sceneFilePath.lastIndexOf( '/' );
			            String sceneFile;
			            sceneFile = sceneFilePath.substring( lastDot + 1 );
			    		sceneFileName.setText( sceneFile );
			    		
			    		soundCombo.setSelectedIndex(
			    				itemsTrueFalse.indexOf( GameConf.getDefaultSetting( GameConf.SOUND_ENABLED ) ) );
			        }
			    }
			);
		
		// cancel return to main panel
		buttonCancel.setMnemonic('c');
		buttonCancel.setMnemonic('C');
		buttonCancel.addActionListener(
		    new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            mainMenu.switchToPanel("mainPanel");
		        }
		    }
		);
		
		// apply options
		buttonOk.setMnemonic('o');
		buttonOk.setMnemonic('O');
		buttonOk.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	GameConf.setSetting(
			        			GameConf.FORWARD_KEY, comboWalkForward.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.BACKWARD_KEY, comboWalkBackward.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.TURNRIGHT_KEY, comboWalkRight.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.TURNLEFT_KEY, comboWalkLeft.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.RUN_KEY, comboRun.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.PAUSE_KEY, comboPause.getSelectedItem().toString() );
			        	String[] resolutionSetting = resolutionCombo.getSelectedItem().toString().split("x");
			        	GameConf.setSetting(
			        			GameConf.RESOLUTION_WIDTH, resolutionSetting[0] );
			        	GameConf.setSetting(
			        			GameConf.RESOLUTION_HEIGHT, resolutionSetting[1] );
			        	GameConf.setSetting(
			        			GameConf.SOUND_ENABLED, soundCombo.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.IS_FULLSCREEN, fullscreenCombo.getSelectedItem().toString() );
			        	GameConf.setSetting(
			        			GameConf.SCENE_FILE_PATH, sceneFilePath );
			        	
			        	GameConf.save();
			        	ImagesContainer.init();
			        	mainMenu.applyChanges();
			            mainMenu.switchToPanel("mainPanel");
			        }
			    }
			);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(screenSize.width/8, 1));
		add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(screenSize.width/8, 1));
		add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, screenSize.height/8));
		add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height/8));
		add(pHorizontalEmpty2,BorderLayout.NORTH);
		
		this.setFocusable(true);
	}
}