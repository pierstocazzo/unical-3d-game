package game.menu;

import game.common.GameSettings;

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
import javax.swing.JFrame;
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
public class OptionsMenu extends JFrame {
	
	/** Class ID */
	private static final long serialVersionUID = 1L;
	
	/** background image */
	Image background;
	
	/** MainMenu Pointer */
	MainMenu mainMenu;
	
	/** JTextPath that contains a xml file path */
	JTextArea xmlTextPath;
	
	/** His Pointer used in a subClass */
	OptionsMenu optionsMenu;
	
	Vector<String> itemsYesNo;
	
	final Vector<String> itemsKeyCommands;

	String sceneFilePath;
	
	public OptionsMenu( final MainMenu mainMenu ){
		super();
		this.mainMenu = mainMenu;
		optionsMenu = this;

		// Get screen size informations
		Dimension screenSize = 
	        Toolkit.getDefaultToolkit().getScreenSize();
		// apply screen size informations
		setBounds(0,0,screenSize.width, screenSize.height);
		// get image file
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		// scale image respect screen size 
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_SMOOTH);
		setUndecorated(true); 
		
		setTitle("Credits Game");
		
		/**
		 * Create a panel that allow to show background image
		 */
		JPanel mainPanel = new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		// apply layout to main panel
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(false);
		setContentPane(mainPanel);
		
		//Create a new sub panel that divide frame
		JPanel dividePanel = new JPanel();
		dividePanel.setLayout(new BorderLayout());
		mainPanel.add(dividePanel,BorderLayout.CENTER);
		
		//Create first visible panel ( Monitor Settings )
		JPanel grid = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		grid.setLayout(layout);
		grid.setOpaque(true);
		grid.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/4));
		//add a label to panel
		TitledBorder titleBorder = new TitledBorder("Impostazioni Generali");
		grid.setBorder(titleBorder);
		dividePanel.add(grid, BorderLayout.NORTH);
		
		GridBagConstraints lim = new GridBagConstraints();
		
		//Create a label that show text
		JLabel resolutionLabel = new JLabel("Risoluzione dello schermo");
		lim.gridx = 0;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(resolutionLabel, lim);
		grid.add(resolutionLabel);
		
		// Create comboBox about screen resolution settings
		Vector<String> resolutions = new Vector<String>();
		resolutions.add("1280x800");resolutions.add("1024x768");resolutions.add("800x600");
		final JComboBox resolutionCombo = new JComboBox(resolutions);
		resolutionCombo.setSelectedIndex(
				resolutions.indexOf((GameSettings.getResolutionWidth() + "x" +
									GameSettings.getResolutionHeight()) ));
		
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
		
		// Create a comboBox that allow to choose fullscreen yes or no
		itemsYesNo = new Vector<String>();
		itemsYesNo.add("YES");itemsYesNo.add("NO");
		final JComboBox fullscreenCombo = new JComboBox(itemsYesNo);
		fullscreenCombo.setSelectedIndex(itemsYesNo.indexOf(GameSettings.isFullscreen()));
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
		JLabel xmlLabel = new JLabel("XML file per l'ambientazione: ");
		xmlPanel.add(xmlLabel);
	
		//JTextBox that contains a file path
		sceneFilePath = GameSettings.getSceneFilePath();
        int lastDot = sceneFilePath.lastIndexOf( '/' );
        String sceneFile;
        sceneFile = sceneFilePath.substring( lastDot + 1 );
		xmlTextPath = new JTextArea( sceneFile );
		xmlPanel.add(xmlTextPath);
		
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
					xmlTextPath.setText(file.getName());
				}
			}
		});
		
		//Create a label
		JLabel soundLabel = new JLabel("Suono");
		lim.gridx = 0;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(soundLabel, lim);
		grid.add(soundLabel);
		
		// create a JComboBox - Sound SI or NO
		final JComboBox soundCombo = new JComboBox(itemsYesNo);
		soundCombo.setSelectedIndex(itemsYesNo.indexOf(GameSettings.isSoundEnabled()));
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
		TitledBorder titleBorder2 = new TitledBorder("Comandi Giocatore");
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
		itemsKeyCommands.add("Z");itemsKeyCommands.add("LSHIFT");itemsKeyCommands.add("SPACE");
		itemsKeyCommands.add("CTRL");itemsKeyCommands.add("ENTER");
		
		//create a label
		JLabel combo1Label = new JLabel("Cammina Avanti");
		lim.gridx = 0;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo1Label, lim);
		grid2.add(combo1Label);
		
		// JComboBox - walk forward
		final JComboBox comboWalkForward = new JComboBox(itemsKeyCommands);
		comboWalkForward.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getForwardKey()));
		lim.gridx = 1;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkForward, lim);
		grid2.add(comboWalkForward);
		
		//create a label
		JLabel combo2Label = new JLabel("Cammina Indietro");
		lim.gridx = 0;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo2Label, lim);
		grid2.add(combo2Label);
		
		// JComboBox - walk backward
		final JComboBox comboWalkBackward = new JComboBox(itemsKeyCommands);
		comboWalkBackward.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getBackwardKey()));
		lim.gridx = 1;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkBackward, lim);
		grid2.add(comboWalkBackward);
		
		//create a label
		JLabel combo3Label = new JLabel("Cammina a Destra");
		lim.gridx = 0;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo3Label, lim);
		grid2.add(combo3Label);
		
		// JComboBox - walk to right
		final JComboBox comboWalkRight = new JComboBox(itemsKeyCommands);
		comboWalkRight.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getStrafeRightKey()));
		lim.gridx = 1;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkRight, lim);
		grid2.add(comboWalkRight);
		
		//create a label
		JLabel combo4Label = new JLabel("Cammina a Sinistra");
		lim.gridx = 0;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo4Label, lim);
		grid2.add(combo4Label);
		
		// JComboBox - walk to left
		final JComboBox comboWalkLeft = new JComboBox(itemsKeyCommands);
		comboWalkLeft.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getStrafeLeftKey()));
		lim.gridx = 1;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboWalkLeft, lim);
		grid2.add(comboWalkLeft);
		
		//create a label
		JLabel combo5Label = new JLabel("Corri");
		lim.gridx = 0;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo5Label, lim);
		grid2.add(combo5Label);
		
		// JComboBox - run
		final JComboBox comboRun = new JComboBox(itemsKeyCommands);
		comboRun.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getRunKey()));
		lim.gridx = 1;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(comboRun, lim);
		grid2.add(comboRun);
		
		//create a label
		JLabel combo6Label = new JLabel("Pausa");
		lim.gridx = 0;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo6Label, lim);
		grid2.add(combo6Label);
		
		// JComboBox - pause
		final JComboBox comboPause = new JComboBox(itemsKeyCommands);
		comboPause.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getPauseKey()));
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
		buttonReset.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	comboWalkForward.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getDefaultForwardKey()));
			        	comboWalkBackward.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getDefaultBackwardKey()));
			        	comboWalkRight.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getDefaultStrafeRightKey()));
			        	comboWalkLeft.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getDefaultStrafeLeftKey()));
			        	comboRun.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getDefaultRunKey()));
			        	comboPause.setSelectedIndex(itemsKeyCommands.indexOf(GameSettings.getDefaultPauseKey()));
			        }
			    }
			);
		
		// cancel return to main panel
		buttonCancel.addActionListener(
		    new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            setVisible(false);
		            mainMenu.setVisible(true);
		        }
		    }
		);
		
		// apply options
		buttonOk.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	GameSettings.setForwardKey(comboWalkForward.getSelectedItem().toString());
			        	GameSettings.setBackwardKey(comboWalkBackward.getSelectedItem().toString());
			        	GameSettings.setStrafeRightKey(comboWalkRight.getSelectedItem().toString());
			        	GameSettings.setStrafeLeftKey(comboWalkLeft.getSelectedItem().toString());
			        	GameSettings.setRunKey(comboRun.getSelectedItem().toString());
			        	GameSettings.setPauseKey(comboPause.getSelectedItem().toString());
			        	
			        	String[] resolutionSetting = resolutionCombo.getSelectedItem().toString().split("x");
			        	GameSettings.setResolutionWidth(resolutionSetting[0]);
			        	GameSettings.setResolutionHeight(resolutionSetting[1]);
			        	GameSettings.setSoundEnabled(soundCombo.getSelectedItem().toString());
			        	GameSettings.setFullscreen(fullscreenCombo.getSelectedItem().toString());
			        	
			        	GameSettings.setSceneFilePath( sceneFilePath );
			        	
			        	GameSettings.save();
			            setVisible(false);
			            mainMenu.setVisible(true);
			        }
			    }
			);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(screenSize.width/8, 1));
		mainPanel.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(screenSize.width/8, 1));
		mainPanel.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, screenSize.height/8));
		mainPanel.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, screenSize.height/8));
		mainPanel.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.setFocusable(true);
	}
}