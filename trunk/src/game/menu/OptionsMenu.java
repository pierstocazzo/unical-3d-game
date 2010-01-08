package game.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * Class SaveMenu
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class OptionsMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	Image background;
	MainMenu mm;
	
	public OptionsMenu( final MainMenu mm ){
		super();
		this.mm = mm;

		Dimension screenSize = 
	        Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width, screenSize.height);
		background = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		background = background.getScaledInstance(screenSize.width,screenSize.height,Image.SCALE_DEFAULT);
		setUndecorated(true); 
		
		setTitle("Credits Game");
		
		JPanel mainPanel = new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g){
				g.drawImage(background, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(false);
		setContentPane(mainPanel);
		
		//Pannello di suddivisione in 3 parti
		JPanel dividePanel = new JPanel();
		dividePanel.setLayout(new BorderLayout());
		mainPanel.add(dividePanel,BorderLayout.CENTER);
		
		//Pannello per la gestione del monitor
		JPanel grid = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		grid.setLayout(layout);
		grid.setOpaque(true);
		grid.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/4));
		TitledBorder titleBorder = new TitledBorder("Impostazioni del Monitor");
		grid.setBorder(titleBorder);
		dividePanel.add(grid, BorderLayout.NORTH);
		
		GridBagConstraints lim = new GridBagConstraints();
		
		JLabel resolutionLabel = new JLabel("Risoluzione dello schermo");
		lim.gridx = 0;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(resolutionLabel, lim);
		grid.add(resolutionLabel);
		
		Vector<String> resolutions = new Vector<String>();
		resolutions.add("1280x800");resolutions.add("1024x768");resolutions.add("800x600");
		JComboBox resolutionCombo = new JComboBox(resolutions);
		lim.gridx = 1;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(resolutionCombo, lim);
		grid.add(resolutionCombo);
		
		JLabel fullscreenLabel = new JLabel("Fullscreen");
		lim.gridx = 0;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(fullscreenLabel, lim);
		grid.add(fullscreenLabel);
		
		// JComboBox - Fullscreen SI o NO
		Vector<String> list = new Vector<String>();
		list.add("SI");list.add("NO");
		JComboBox fullscreenCombo = new JComboBox(list);
		lim.gridx = 1;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout.setConstraints(fullscreenCombo, lim);
		grid.add(fullscreenCombo);
		
		//Pannello per la gestione dei comandi utente
		JPanel grid2 = new JPanel();
		GridBagLayout layout2 = new GridBagLayout();
		grid2.setLayout(layout2);
		grid2.setOpaque(true);
		grid2.setPreferredSize(new Dimension(screenSize.width*6/8, screenSize.height/2));
		TitledBorder titleBorder2 = new TitledBorder("Comandi Giocatore");
		grid2.setBorder(titleBorder2);
		dividePanel.add(grid2, BorderLayout.CENTER);
		
		//Inizializzo lista possibili comandi
		final Vector<String> items = new Vector<String>();
		final String avanti_default = "W";
		final String indietro_default = "S";
		final String destra_default = "D";
		final String sinistra_default = "A";
		final String corri_default = "SHIFT";
		final String pausa_default = "P";
		
		items.add(sinistra_default);items.add("B");items.add("C");items.add(destra_default);items.add("E");
		items.add("F");items.add("G");items.add("H");items.add("I");items.add("J");
		items.add("K");items.add("L");items.add("M");items.add("N");items.add("O");
		items.add(pausa_default);items.add("Q");items.add("R");items.add(indietro_default);items.add("T");
		items.add("U");items.add("V");items.add("X");items.add("Y");items.add(avanti_default);
		items.add("Z");items.add(corri_default);items.add("SPACE");items.add("CTRL");items.add("ENTER");
		
		JLabel combo1Label = new JLabel("Cammina Avanti");
		lim.gridx = 0;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo1Label, lim);
		grid2.add(combo1Label);
		
		// JComboBox - Cammina Avanti
		final JComboBox combo1 = new JComboBox(items);
		combo1.setSelectedIndex(items.indexOf(avanti_default));
		lim.gridx = 1;
		lim.gridy = 0;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo1, lim);
		grid2.add(combo1);
		
		JLabel combo2Label = new JLabel("Cammina Indietro");
		lim.gridx = 0;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo2Label, lim);
		grid2.add(combo2Label);
		
		// JComboBox - Cammina indietro
		final JComboBox combo2 = new JComboBox(items);
		combo2.setSelectedIndex(items.indexOf(indietro_default));
		lim.gridx = 1;
		lim.gridy = 1;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo2, lim);
		grid2.add(combo2);
		
		JLabel combo3Label = new JLabel("Cammina a Destra");
		lim.gridx = 0;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo3Label, lim);
		grid2.add(combo3Label);
		
		// JComboBox - Cammina a Destra
		final JComboBox combo3 = new JComboBox(items);
		combo3.setSelectedIndex(items.indexOf(destra_default));
		lim.gridx = 1;
		lim.gridy = 2;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo3, lim);
		grid2.add(combo3);
		
		JLabel combo4Label = new JLabel("Cammina a Sinistra");
		lim.gridx = 0;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo4Label, lim);
		grid2.add(combo4Label);
		
		// JComboBox - Cammina a Sinistra
		final JComboBox combo4 = new JComboBox(items);
		combo4.setSelectedIndex(items.indexOf(sinistra_default));
		lim.gridx = 1;
		lim.gridy = 3;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo4, lim);
		grid2.add(combo4);
		
		JLabel combo5Label = new JLabel("Corri");
		lim.gridx = 0;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo5Label, lim);
		grid2.add(combo5Label);
		
		// JComboBox - Corri
		final JComboBox combo5 = new JComboBox(items);
		combo5.setSelectedIndex(items.indexOf(corri_default));
		lim.gridx = 1;
		lim.gridy = 4;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo5, lim);
		grid2.add(combo5);
		
		JLabel combo6Label = new JLabel("Pausa");
		lim.gridx = 0;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo6Label, lim);
		grid2.add(combo6Label);
		
		// JComboBox - Pausa
		final JComboBox combo6 = new JComboBox(items);
		combo6.setSelectedIndex(items.indexOf(pausa_default));
		lim.gridx = 1;
		lim.gridy = 5;
		lim.weightx = 0.5;
		lim.weighty = 0.5;
		layout2.setConstraints(combo6, lim);
		grid2.add(combo6);
		
		//Bottoni di utilità
		JPanel flow = new JPanel();
		flow.setLayout(new FlowLayout());
		dividePanel.add(flow,BorderLayout.SOUTH);
		
		JButton buttonReset = new JButton("RESET");
		flow.add(buttonReset);
		JButton buttonCancel = new JButton("ANNULLA");
		flow.add(buttonCancel);
		JButton buttonOk = new JButton("OK");
		flow.add(buttonOk);
		
		buttonReset.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	combo1.setSelectedIndex(items.indexOf(avanti_default));
			        	combo2.setSelectedIndex(items.indexOf(indietro_default));
			        	combo3.setSelectedIndex(items.indexOf(destra_default));
			        	combo4.setSelectedIndex(items.indexOf(sinistra_default));
			        	combo5.setSelectedIndex(items.indexOf(corri_default));
			        	combo6.setSelectedIndex(items.indexOf(pausa_default));
			        }
			    }
			);
		
		buttonCancel.addActionListener(
		    new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            setVisible(false);
		            mm.setVisible(true);
		        }
		    }
		);
		
		buttonOk.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			        	//applica modifiche
			            setVisible(false);
			            mm.setVisible(true);
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
//	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.setFocusable(true);
	}
}