package slashWork.customGame;

import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;

/** Classe Game1:
 * Primo game customizzato. Si estende BaseGame e non SimpleGame.
 * Tutto quello che fa questa classe è visualizzare solo una sfera!
 * @author slash17
 */
public class Game1 extends BaseGame {
	// parametri
	private int width, height, depth, freq;
	private boolean fullscreen;
	private Camera cam;
	protected Timer timer;
	// ts per caricare ed applicare textures
	private TextureState ts;
	// scene sarà il nostro "rootNode"
	private Node scene;
	
	// metodi
	public static void main(String[] args) {
		Game1 application = new Game1();
		application.setConfigShowMode(ConfigShowMode.AlwaysShow, Game1.class.getClassLoader().getResource( "images/openGame.jpg" ));
		application.start();
	}
	
	@Override
	protected void cleanup() {
		// prima di chiudere mi assicuro di cancellare tutte le textures caricate
		ts.deleteAll();
	}

	@Override
	protected void initGame() {
		// inizializziamo il nostro rootNode
		scene = new Node("Scene graph node");
	 
		// creiamo una sfera
		Sphere s = new Sphere("Sphere", 30, 30, 25);
		s.setLocalTranslation(new Vector3f(0, 0, -40));
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		// le diamo un volto
		ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(TextureManager.loadTexture(
				Game1.class.getClassLoader().getResource("images/rockWall2.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap, 
				Texture.MagnificationFilter.Bilinear));

		s.setRenderState(ts);

		scene.attachChild(s);

		// aggiorniamo il rootNode visto che abbiamo aggiunto oggetti e textures
		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();
	}

	@Override
	protected void initSystem() {
		// impostiamo i parametri di visualizzazione
		width = this.settings.getWidth();
		height = this.settings.getHeight();
		depth = this.settings.getDepth();
		freq = this.settings.getFrequency();
		fullscreen = this.settings.isFullscreen();
 
		// impostiamo il nostro dysplaySystem, inizializziamo la finestra e creo la camera 
		// (è in un try/catch per uscire in caso di problemi nella creazione della finestra)
		try {
			display = DisplaySystem.getDisplaySystem(this.getNewSettings().getRenderer());
			display.createWindow(width, height, depth, freq, fullscreen);
 
			cam = display.getRenderer().createCamera(width, height);
		} catch (JmeException e) {
			e.printStackTrace();
			System.exit(1);
		}
 
		// impostiamo lo sfondo nero classico
		display.getRenderer().setBackgroundColor(ColorRGBA.black);
 
		// inizializziamo la camera a 45° dalla scena, è solo un esempio!
		cam.setFrustumPerspective(45.0f, (float)width / (float)height, 1, 1000);
		Vector3f loc = new Vector3f(0.0f, 0.0f, 30.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
		// la piazzo ed oriento dove voglio
		cam.setFrame(loc, left, up, dir);
		
		// abbiamo cambiato la camera quindi aggiorniamo
		cam.update();
 
		// usiamo il timer per calcolarci gli fps, ma può servire per molto altro
        timer = Timer.getTimer();
        
        // impostiamo la camera al displaySystem
		display.getRenderer().setCamera(cam);
 
		// gestiamo ancora un solo evento, cioè la pressione del tasto "Esc" per chiudere il gioco
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
	}

	@Override
	protected void reinit() {
		// ricreo la mia finestra
		display.recreateWindow(width, height, depth, freq, fullscreen);
	}

	@Override
	protected void render(float interpolation) {
		// cancello tutto
		display.getRenderer().clearBuffers();
		// ridisegno la scena
		display.getRenderer().draw(scene);

	}

	@Override
	protected void update( float interpolation ) {
		// aggiorniamo il timer e il framerate
		timer.update();
		interpolation = timer.getTimePerFrame();
		// usciamo se è stato dato il comando exit (tasto esc)
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}
	}
}
