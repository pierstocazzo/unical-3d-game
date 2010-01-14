package game.menu;

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
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_DEFAULT);
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
		JComboBox resolutionCombo = new JComboBox(resolutions);
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
		itemsYesNo.add("SI");itemsYesNo.add("NO");
		JComboBox fullscreenCombo = new JComboBox(itemsYesNo);
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
		xmlTextPath = new JTextArea("world.xml");
		xmlPanel.add(xmlTextPath);
		
		//Button Browses for choose files
		JButton browsButton = new JButton("Sfoglia");
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
		JComboBox soundCombo = new JComboBox(itemsYesNo);
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
		final String avanti_default = "W";
		final String indietro_default = "S";
		final String destra_default = "D";
		final String sinistra_default = "A";
		final String corri_default = "SHIFT";
		final String pausa_default = "P";
		
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
		itemsKeyCommands.add("Z");itemsKeyCommands.add("SHIFT");itemsKeyCommands.add("SPACE");
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
		final JComboBox combo1 = new JComboBox(itemsKeyCommands);
		combo1.setSelectedIndex(itemsKeyCommands.indexOf(avanti_default));
		lim.gridx = 1;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo1, lim);
		grid2.add(combo1);
		
		//create a label
		JLabel combo2Label = new JLabel("Cammina Indietro");
		lim.gridx = 0;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo2Label, lim);
		grid2.add(combo2Label);
		
		// JComboBox - walk backward
		final JComboBox combo2 = new JComboBox(itemsKeyCommands);
		combo2.setSelectedIndex(itemsKeyCommands.indexOf(indietro_default));
		lim.gridx = 1;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo2, lim);
		grid2.add(combo2);
		
		//create a label
		JLabel combo3Label = new JLabel("Cammina a Destra");
		lim.gridx = 0;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo3Label, lim);
		grid2.add(combo3Label);
		
		// JComboBox - walk to right
		final JComboBox combo3 = new JComboBox(itemsKeyCommands);
		combo3.setSelectedIndex(itemsKeyCommands.indexOf(destra_default));
		lim.gridx = 1;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo3, lim);
		grid2.add(combo3);
		
		//create a label
		JLabel combo4Label = new JLabel("Cammina a Sinistra");
		lim.gridx = 0;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo4Label, lim);
		grid2.add(combo4Label);
		
		// JComboBox - walk to left
		final JComboBox combo4 = new JComboBox(itemsKeyCommands);
		combo4.setSelectedIndex(itemsKeyCommands.indexOf(sinistra_default));
		lim.gridx = 1;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo4, lim);
		grid2.add(combo4);
		
		//create a label
		JLabel combo5Label = new JLabel("Corri");
		lim.gridx = 0;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo5Label, lim);
		grid2.add(combo5Label);
		
		// JComboBox - run
		final JComboBox combo5 = new JComboBox(itemsKeyCommands);
		combo5.setSelectedIndex(itemsKeyCommands.indexOf(corri_default));
		lim.gridx = 1;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo5, lim);
		grid2.add(combo5);
		
		//create a label
		JLabel combo6Label = new JLabel("Pausa");
		lim.gridx = 0;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo6Label, lim);
		grid2.add(combo6Label);
		
		// JComboBox - pause
		final JComboBox combo6 = new JComboBox(itemsKeyCommands);
		combo6.setSelectedIndex(itemsKeyCommands.indexOf(pausa_default));
		lim.gridx = 1;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo6, lim);
		grid2.add(combo6);
		
		//useful buttons in the third visible panel
		JPanel flow = new JPanel();
		flow.setLayout(new FlowLayout());
		dividePanel.add(flow,BorderLayout.SOUTH);
		
		JButton buttonReset = new JButton("RESET");
		flow.add(buttonReset);
		JButton buttonCancel = new JButton("ANNULLA");
		flow.add(buttonCancel);
		JButton buttonOk = new JButton("OK");
		flow.add(buttonOk);
		
		//add reset action
		buttonReset.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	combo1.setSelectedIndex(itemsKeyCommands.indexOf(avanti_default));
			        	combo2.setSelectedIndex(itemsKeyCommands.indexOf(indietro_default));
			        	combo3.setSelectedIndex(itemsKeyCommands.indexOf(destra_default));
			        	combo4.setSelectedIndex(itemsKeyCommands.indexOf(sinistra_default));
			        	combo5.setSelectedIndex(itemsKeyCommands.indexOf(corri_default));
			        	combo6.setSelectedIndex(itemsKeyCommands.indexOf(pausa_default));
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