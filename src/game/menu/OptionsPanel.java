package game.menu;

import game.common.GameConfiguration;
import game.common.ImagesContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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
	
	/** background image */
	Image background;
	
	/** MainMenu Pointer */
	MainMenu mainMenu;
	
	/** JTextPath that contains a xml file path */
	JTextArea sceneFileName;
	
	/** His Pointer used in a subClass */
	OptionsPanel optionsMenu;
	
	Vector<String> itemsTrueFalse;
	
	final Vector<String> itemsKeyCommands;

	String sceneFilePath;

	Vector<String> resolutions;
	
	public OptionsPanel( final MainMenu mainMenu ){
		super();
		this.mainMenu = mainMenu;
		optionsMenu = this;

		// Get screen size informations
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// apply screen size informations
		setBounds(0,0,screenSize.width, screenSize.height);
		// get image file
		background = ImagesContainer.getBackgroundMainMenu();
		
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
				resolutions.indexOf((GameConfiguration.getResolutionWidth() + "x" +
									GameConfiguration.getResolutionHeight()) ));
		
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
		fullscreenCombo.setSelectedIndex(itemsTrueFalse.indexOf(GameConfiguration.isFullscreen()));
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
		sceneFilePath = GameConfiguration.getSceneFilePath();
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
		soundCombo.setSelectedIndex(itemsTrueFalse.indexOf(GameConfiguration.isSoundEnabled()));
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
		comboWalkForward.setSelectedIndex(itemsKeyCommands.indexOf(GameConfiguration.getForwardKey()));
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
		comboWalkBackward.setSelectedIndex(itemsKeyCommands.indexOf(GameConfiguration.getBackwardKey()));
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
		comboWalkRight.setSelectedIndex(itemsKeyCommands.indexOf(GameConfiguration.getTurnRightKey()));
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
		comboWalkLeft.setSelectedIndex(itemsKeyCommands.indexOf(GameConfiguration.getTurnLeftKey()));
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
		comboRun.setSelectedIndex(itemsKeyCommands.indexOf(GameConfiguration.getRunKey()));
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
		comboPause.setSelectedIndex(itemsKeyCommands.indexOf(GameConfiguration.getPauseKey()));
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
		
		JButton buttonReset = new JButton("RESET");
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
			        			itemsKeyCommands.indexOf(GameConfiguration.getDefaultForwardKey()));
			        	comboWalkBackward.setSelectedIndex(
			        			itemsKeyCommands.indexOf(GameConfiguration.getDefaultBackwardKey()));
			        	comboWalkRight.setSelectedIndex(
			        			itemsKeyCommands.indexOf(GameConfiguration.getDefaultTurnRightKey()));
			        	comboWalkLeft.setSelectedIndex(
			        			itemsKeyCommands.indexOf(GameConfiguration.getDefaultTurnLeftKey()));
			        	comboRun.setSelectedIndex(
			        			itemsKeyCommands.indexOf(GameConfiguration.getDefaultRunKey()));
			        	comboPause.setSelectedIndex(
			        			itemsKeyCommands.indexOf(GameConfiguration.getDefaultPauseKey()));
			        	
			        	resolutionCombo.setSelectedIndex(
			    				resolutions.indexOf((GameConfiguration.getDefaultResolutionWidth() + "x" +
			    									GameConfiguration.getDefaultResolutionHeight()) ));
			    		
			    		fullscreenCombo.setSelectedIndex( 
			    				itemsTrueFalse.indexOf(GameConfiguration.getDefaultIsFullscreen() ) );
			    		
			    		sceneFilePath = GameConfiguration.getDefaultSceneFilePath();
			            int lastDot = sceneFilePath.lastIndexOf( '/' );
			            String sceneFile;
			            sceneFile = sceneFilePath.substring( lastDot + 1 );
			    		sceneFileName.setText( sceneFile );
			    		
			    		soundCombo.setSelectedIndex(
			    				itemsTrueFalse.indexOf(GameConfiguration.getDefaultSoundEnabled() ) );
			        }
			    }
			);
		
		// cancel return to main panel
		buttonCancel.setMnemonic('c');
		buttonCancel.setMnemonic('C');
		buttonCancel.addActionListener(
		    new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            mainMenu.switchToMainPanel();
		        }
		    }
		);
		
		// apply options
		buttonOk.setMnemonic('o');
		buttonOk.setMnemonic('O');
		buttonOk.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	GameConfiguration.setForwardKey(comboWalkForward.getSelectedItem().toString());
			        	GameConfiguration.setBackwardKey(comboWalkBackward.getSelectedItem().toString());
			        	GameConfiguration.setTurnRightKey(comboWalkRight.getSelectedItem().toString());
			        	GameConfiguration.setTurnLeftKey(comboWalkLeft.getSelectedItem().toString());
			        	GameConfiguration.setRunKey(comboRun.getSelectedItem().toString());
			        	GameConfiguration.setPauseKey(comboPause.getSelectedItem().toString());
			        	
			        	String[] resolutionSetting = resolutionCombo.getSelectedItem().toString().split("x");
			        	GameConfiguration.setResolutionWidth(resolutionSetting[0]);
			        	GameConfiguration.setResolutionHeight(resolutionSetting[1]);
			        	GameConfiguration.setSoundEnabled(soundCombo.getSelectedItem().toString());
			        	GameConfiguration.setFullscreen(fullscreenCombo.getSelectedItem().toString());
			        	
			        	GameConfiguration.setSceneFilePath( sceneFilePath );
			        	
			        	GameConfiguration.save();
			        	ImagesContainer.init();
			            mainMenu.switchToMainPanel();
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
	
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(background, 0, 0, this);
		super.paintComponent(g);
	}
}