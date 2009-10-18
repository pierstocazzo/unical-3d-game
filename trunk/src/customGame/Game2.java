package customGame;

import javax.swing.ImageIcon;
import jmetest.renderer.TestSkybox;
import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Skybox;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/** Classe Game1:
 * Secondo game customizzato. 
 * Con questo codice carichiamo un terreno
 * @author slash17
 */
public class Game2 extends BaseGame {
	// parametri
	private int width, height, depth, freq;
	private boolean fullscreen;
	private Camera cam;
	protected Timer timer;
	private TerrainBlock terrain;
	// ts per caricare ed applicare textures
	private TextureState ts;
	// scene sarà il nostro "rootNode"
	private Node scene;
	private Skybox skybox;
	
	
	// metodi
	public static void main(String[] args) {
		Game2 application = new Game2();
		application.setConfigShowMode(ConfigShowMode.AlwaysShow, Game2.class.getClassLoader().getResource( "images/openGame.jpg" ));
		application.start();
	}
	
	@Override
	protected void initGame() {
		// diamo un titolo alla finestra
		display.setTitle( "Assholes Game" );
		// inizializziamo il nostro rootNode
		scene = new Node("Scene graph node");
		// creiamo un depth buffer
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		scene.setRenderState(buf);		
		// creiamo il terreno 
		buildTerrain();
		// creiamo la luce!
		buildLighting();
		// creo il mio ambiente che in questo caso è una recinzione
		buildEnvironment();
		// creo il cielo
		buildSkyBox();

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
		// cambiamo la posizione della telecamera in modo da vedere tutto il nostro terreno
		Vector3f loc = new Vector3f(350.0f, 100.0f, 350.0f);
		Vector3f left = new Vector3f(-0.5f, 0.0f, 0.5f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(-0.5f, 0.0f, -0.5f);
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
		// facciamo in modo che il nostro cielo si muovi assieme alla cam
		skybox.setLocalTranslation(cam.getLocation());

		scene.updateGeometricState(interpolation, true);
	}
	
	@Override
	protected void cleanup() {
		// prima di chiudere mi assicuro di cancellare tutte le textures caricate
		ts.deleteAll();
	}

	private void buildEnvironment() {
		// un macello per creare una recinzione completamente a mano! molto meglio caricare un modello 3d
		// in breve, tutto questo crea prima i 4 piantoni ai quattro angoli della recinzione
		// poi li unisce con un puntello
		// poi mette la rete
		
		// creo il mio cilindro che sarà poi replicato 4 volte (saranno le 4 torri (o piantoni) agli angoli)
		Cylinder postGeometry = new Cylinder("post", 10, 10, 1, 10);
		// lo ruoto di 90° (Pi/2), in quanto di default è in orizzontale
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
		postGeometry.setModelBound(new BoundingBox());
		postGeometry.updateModelBound();
		// ora lo replichiamo..o meglio creiamo quattro mesh che condividono 
		// una sola geometria, appunto quella del cilindro di prima
		SharedMesh post1 = new SharedMesh("post1", postGeometry);
		post1.setLocalTranslation(new Vector3f(0,0.5f,0));
		post1.setLocalRotation(q);
		SharedMesh post2 = new SharedMesh("post2", postGeometry);
		post2.setLocalTranslation(new Vector3f(32,0.5f,0));
		post2.setLocalRotation(q);
		SharedMesh post3 = new SharedMesh("post3", postGeometry);
		post3.setLocalTranslation(new Vector3f(0,0.5f,32));
		post3.setLocalRotation(q);
		SharedMesh post4 = new SharedMesh("post4", postGeometry);
		post4.setLocalTranslation(new Vector3f(32,0.5f,32));
		post4.setLocalRotation(q);
		// i quattro cilindri fittizi vengono spostati nei quattro angoli
		// li attacco tutti ad un unico nodo
		Node towerNode = new Node("tower");
		towerNode.attachChild(post1);
		towerNode.attachChild(post2);
		towerNode.attachChild(post3);
		towerNode.attachChild(post4);
		// carico una sola texture per i cilidri
		TextureState ts2 = display.getRenderer().createTextureState();
		Texture t2 = TextureManager.loadTexture(Game2.class.getClassLoader()
		                  .getResource("texture/post.jpg"),
		                  Texture.MinificationFilter.BilinearNearestMipMap,
		                  Texture.MagnificationFilter.Bilinear); 
		ts2.setTexture(t2);
		// ecco il nodo delle torri pronto
		towerNode.setRenderState(ts2);
		
		// stesso procedimento per creare i puntelli tra le torri della mia recinzione
		Cylinder strutGeometry = new Cylinder("strut", 10,10, 0.125f, 32);
		strutGeometry.setModelBound(new BoundingBox());
		strutGeometry.updateModelBound();
		SharedMesh strut1 = new SharedMesh("strut1", strutGeometry);
		Quaternion rotate90 = new Quaternion();
		rotate90.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));
		strut1.setLocalRotation(rotate90);
		strut1.setLocalTranslation(new Vector3f(16,3f,0));
		SharedMesh strut2 = new SharedMesh("strut2", strutGeometry);
		strut2.setLocalTranslation(new Vector3f(0,3f,16));
		SharedMesh strut3 = new SharedMesh("strut3", strutGeometry);
		strut3.setLocalTranslation(new Vector3f(32,3f,16));
		SharedMesh strut4 = new SharedMesh("strut4", strutGeometry);
		strut4.setLocalRotation(rotate90);
		strut4.setLocalTranslation(new Vector3f(16,3f,32));
		//put all the struts into a single node.
		Node strutNode = new Node("strutNode");
		strutNode.attachChild(strut1);
		strutNode.attachChild(strut2);
		strutNode.attachChild(strut3);
		strutNode.attachChild(strut4);
		TextureState ts3 = display.getRenderer().createTextureState();
		Texture t3 = TextureManager.loadTexture(Game2.class.getClassLoader()
		                  .getResource("texture/rust.jpg"),
		                  Texture.MinificationFilter.BilinearNearestMipMap,
		                  Texture.MagnificationFilter.Bilinear);
		ts3.setTexture(t3);
		// ed ecco il nodo dei puntelli
		strutNode.setRenderState(ts3);
		
		// questa sarebbe la rete..
		Box forceFieldX = new Box("forceFieldX", new Vector3f(-16, -3f, -0.1f), new Vector3f(16f, 3f, 0.1f));
		forceFieldX.setModelBound(new BoundingBox());
		forceFieldX.updateModelBound();
		SharedMesh forceFieldX1 = new SharedMesh("forceFieldX1",forceFieldX);
		forceFieldX1.setLocalTranslation(new Vector3f(16,0,0));
		SharedMesh forceFieldX2 = new SharedMesh("forceFieldX2",forceFieldX);
		forceFieldX2.setLocalTranslation(new Vector3f(16,0,32));
		 
		Box forceFieldZ = new Box("forceFieldZ", new Vector3f(-0.1f, -3f, -16), new Vector3f(0.1f, 3f, 16));
		forceFieldZ.setModelBound(new BoundingBox());
		forceFieldZ.updateModelBound();
		SharedMesh forceFieldZ1 = new SharedMesh("forceFieldZ1",forceFieldZ);
		forceFieldZ1.setLocalTranslation(new Vector3f(0,0,16));
		SharedMesh forceFieldZ2 = new SharedMesh("forceFieldZ2",forceFieldZ);
		forceFieldZ2.setLocalTranslation(new Vector3f(32,0,16));
		 
		Node forceFieldNode = new Node("forceFieldNode");
		forceFieldNode.attachChild(forceFieldX1);
		forceFieldNode.attachChild(forceFieldX2);
		forceFieldNode.attachChild(forceFieldZ1);
		forceFieldNode.attachChild(forceFieldZ2);
		
		TextureState ts = display.getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(Game2.class.getClassLoader()
		                  .getResource("texture/reflector.jpg"),
		                  Texture.MinificationFilter.BilinearNearestMipMap,
		                  Texture.MagnificationFilter.Bilinear);
		 
		t.setWrap(Texture.WrapMode.Repeat);
		t.setTranslation(new Vector3f());
		ts.setTexture(t);
		// ecco il nodo della rete pronto
		forceFieldNode.setRenderState(ts);
		
		// creo un unico nodo "recinzione" a cui attaccare le sue parti
		Node forceFieldFence = new Node("fence");
		forceFieldFence.attachChild(forceFieldNode);
		forceFieldFence.attachChild(towerNode);
		forceFieldFence.attachChild(strutNode);
		
		scene.attachChild(forceFieldFence);
		
		// scaliamo tutti di un fattore 5
		forceFieldFence.setLocalScale(5);
		// muoviamo la recinzione in modo da sistemarla  bene sul nostro terreno
		forceFieldFence.setLocalTranslation(new Vector3f(25, terrain.getHeight(25,25) + 15, 25));
		// rendiamo la rete della recinzione trasparente
		BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		as1.setBlendEnabled(true);
		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as1.setDestinationFunction(BlendState.DestinationFunction.One);
		as1.setTestEnabled(true);
		as1.setTestFunction(BlendState.TestFunction.GreaterThan);
		as1.setEnabled(true);
		forceFieldNode.setRenderState(as1);
		// ottimizziamo l'ordine di renderizzazione 
		forceFieldNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		towerNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		strutNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		terrain.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	}

	private void buildTerrain() {
	   // generiamo i dati di un terreno casuale
	   MidPointHeightMap heightMap = new MidPointHeightMap( 64, 1f );
	   // creiamo un vettore di ridimensionamento del terreno
	   Vector3f terrainScale = new Vector3f(4, 0.0575f, 4);
	   // creiamo il nostro blocco di terreno, attraverso i dati generati prima
	   terrain = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale,
			   						heightMap.getHeightMap(), new Vector3f(0, 0, 0));

	   terrain.setModelBound(new BoundingBox());
	   terrain.updateModelBound();
	   
	   // generiamo le textures...
	   ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
	   // per le zone basse usiamo l'erba
	   pt.addTexture(new ImageIcon(Game2.class.getClassLoader().getResource("texture/grassb.png")), -128, 0, 128);
	   // per le zone di media altezza usiamo terra e rocce
	   pt.addTexture(new ImageIcon(Game2.class.getClassLoader().getResource("texture/dirt.jpg")), 0, 128, 255);
	   // per le zone alte usiamo la neve!
	   pt.addTexture(new ImageIcon(Game2.class.getClassLoader().getResource("texture/highest.jpg")), 128, 255, 384);
	   // il numero passato alla funzione significa che creeremo textures n x n
	   pt.createTexture(32);
	   // il nostro proceduralblabla è in grado di miscelare le textures per creare un terreno un po più realistico
	   
	   // ora applichiamo le textures create al nostro terreno
	   TextureState ts = display.getRenderer().createTextureState();
	   Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(),
	                   Texture.MinificationFilter.BilinearNearestMipMap,
	                   Texture.MagnificationFilter.Bilinear,
	   	        true);
	   ts.setTexture(t1, 0);
	   terrain.setRenderState( ts );
	   
	   scene.attachChild( terrain );
	}
	
	/** Funzione che crea una luce direzionale
	 * 
	 */
	private void buildLighting() {
		// creiamo la nostra luce direzionale
	    DirectionalLight light = new DirectionalLight();
	    light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    // la direzione della luce sarà in basso verso destra
	    light.setDirection(new Vector3f(1,-1,0));
	    light.setEnabled(true);
	    
	    LightState lightState = display.getRenderer().createLightState();
	    lightState.setEnabled(true);
	    lightState.attach(light);
	    scene.setRenderState(lightState);
	    // e luce sia!
	}
	
	private void buildSkyBox() {
		// è un box che contiene tutto e che vediamo dall'interno, quindi 
		// dobbiamo impostare le textures per le 6 facce visibili!
		
        skybox = new Skybox("skybox", 10, 10, 10);
 
        Texture north = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "texture/north.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "texture/south.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "texture/east.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "texture/west.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "texture/top.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "texture/bottom.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
 
        skybox.setTexture(Skybox.Face.North, north);
        skybox.setTexture(Skybox.Face.West, west);
        skybox.setTexture(Skybox.Face.South, south);
        skybox.setTexture(Skybox.Face.East, east);
        skybox.setTexture(Skybox.Face.Up, up);
        skybox.setTexture(Skybox.Face.Down, down);
        skybox.preloadTextures();
        skybox.updateRenderState();
        scene.attachChild(skybox);
    }
}
