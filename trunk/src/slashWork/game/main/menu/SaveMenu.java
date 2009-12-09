package slashWork.game.main.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

/**
 * Class SaveMenu
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
public class SaveMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	SavePanel p;
	InGameMenu gm;
	Image sfondo;
	
	public SaveMenu(InGameMenu gm){
		super();
		this.gm = gm;
		sfondo = Toolkit.getDefaultToolkit().getImage("src/game/data/images/menu/background.jpg");
		
		this.setUndecorated(true); 
	    
		//hide cursor
		setCursor(getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(), "null"));
		
		this.setTitle("Save Game");
		createMenu();
		
		Dimension screenSize = 
	        Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width, screenSize.height);
	    setResizable(false);
	    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	public void createMenu(){
		//create main panel
		JPanel b = new JPanel(){
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g){
				g.drawImage(sfondo, 0, 0, this);
				super.paintComponent(g);
			}
		};
		
		b.setLayout(new BorderLayout());
		b.setOpaque(false);
		this.setContentPane(b);
		p = new SavePanel(this,gm);
		b.add(p, BorderLayout.CENTER);
		
		//add left vertical empty panel
		JPanel pVerticalEmpty1 = new JPanel();
		pVerticalEmpty1.setOpaque(false);
		pVerticalEmpty1.setPreferredSize(new Dimension(350, 1));
		b.add(pVerticalEmpty1,BorderLayout.WEST);
		
		//add right vertical empty panel
		JPanel pVerticalEmpty2 = new JPanel();
		pVerticalEmpty2.setOpaque(false);
		pVerticalEmpty2.setPreferredSize(new Dimension(350, 1));
		b.add(pVerticalEmpty2,BorderLayout.EAST);
		
		//add lower horizontal empty panel
		JPanel pHorizontalEmpty1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pHorizontalEmpty1.setOpaque(false);
		pHorizontalEmpty1.setPreferredSize(new Dimension(1, 100));
		b.add(pHorizontalEmpty1,BorderLayout.SOUTH);
		
		//add upper horizontal empty panel
		JPanel pHorizontalEmpty2 = new JPanel();
		pHorizontalEmpty2.setOpaque(false);
		pHorizontalEmpty2.setPreferredSize(new Dimension(1, 250));
		b.add(pHorizontalEmpty2,BorderLayout.NORTH);
		
		this.setVisible(true);
		
		class KeyHandler implements KeyListener{
			SavePanel panel;
			public KeyHandler( SavePanel p){
				this.panel = p;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					panel.executeSelectedItem();
				else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
					setVisible(false);
					gm.setVisible(true);
				}
				else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
					try {
						panel.text.setText(panel.text.getText(0,panel.text.getText().length()-1));
					} catch (BadLocationException e1) {
						panel.text.setText("");
					}
				}
				//to improve TODO
				else if(Pattern.matches( "^[a-zA-Z_0-9]$", KeyEvent.getKeyText(e.getKeyCode())) ||
						e.getKeyChar()=='-' || e.getKeyChar()=='_' || e.getKeyChar()==' '){
							panel.text.setText(panel.text.getText()+e.getKeyChar());
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		}
		
		this.addKeyListener( new KeyHandler(p));
		this.setFocusable(true);
	}
}