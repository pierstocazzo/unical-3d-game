package slashWork.testGame;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import utils.ModelLoader;

import jmetest.renderer.TestSkybox;
import jmetest.renderer.loader.TestX3DLoading;

import com.jme.animation.AnimationController;
import com.jme.animation.Bone;
import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
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

/** 
 * @author slash17
 */
public class Game extends BaseGame {
	
	// logger
    private static final Logger logger = Logger.getLogger(TestX3DLoading.class.getName());
	// parametri di visualizzazione del gioco
	int width, height, depth, freq;
	boolean fullscreen;
	// la nostra camera
	Camera cam;
    // la nostra chase camera
    ChaseCamera chaser;
    // il timer per il framerate
	Timer timer;
	// il terreno
	TerrainBlock terrain;
	// scene sara' il nostro "rootNode"
	Node scene;
	// il nostro mondo esterno
	Skybox skybox;
	// la nostra recinzione
	ForceFieldFence fence;
	// il nostro player
	Node player;
	// l'inputHandler personalizzato
	InputHandler input;
	// vettore usato per la normale del terreno
	private Vector3f normal;
	// per tenere il player a contatto col terreno
	float terrainToPlayer;

	public static void main(String[] args) {
		Game application = new Game();
//		application.setConfigShowMode(ConfigShowMode.AlwaysShow, Game2.class.getClassLoader().getResource( "images/openGame.jpg" ));
		application.start();
	}
	
	@Override
	protected void initGame() {
		// diamo un titolo alla finestra
		display.setTitle( "Assholes Game" );
		// inizializziamo il nostro rootNode
		scene = new Node("Scene graph node");
		// creiamo un depth buffer per gestire la profondit� su z
		ZBufferState zBuf = display.getRenderer().createZBufferState();
		zBuf.setEnabled(true);
		zBuf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		scene.setRenderState(zBuf);		
		// creiamo il terreno 
		buildTerrain();
		// creiamo la luce!
		buildLighting();
		// creiamo l'ambiente
		buildEnvironment();

		// creiamo il mondo esterno
		buildSkyBox();
		// creiamo il nostro player
		buildPlayer();
		// creiamo la chase camera
		buildChaseCamera();
		// e l'input
		buildInput();
		// carichiamo tutti i modelli 3d che vogliamo
		buildModels();

		
		// un CullState � in pratica un'ottimazzaione, visto che dovr� girare su una vm
		// la sua filosofia �: se per com'� la telecamera io questo oggetto lo vedo solo di fronte, 
		// perch� dovrei caricare anche le textures del retro?
		CullState cullState = display.getRenderer().createCullState();
		cullState.setCullFace( CullState.Face.Back );
		scene.setRenderState(cullState);
		
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
		
		normal = new Vector3f();
		// impostiamo il nostro dysplaySystem, inizializziamo la finestra e creiamo la camera 
		// (� in un try/catch per uscire in caso di problemi nella creazione della finestra)
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
	
		// inizializziamo la camera a 45� dalla scena, � solo un esempio!
		// ps: un frustum � in pratica il tronco di una piramide; nel nostro 
		// caso rappresenta lo spazio visibile dalla telecamera, bisogna impostargli l'angolo di
		// visualizzazione, l'aspectratio (tipo 4:3 o 16/9 ecc) e il valore minimo e massimo di 
		// distanza dalla cam per cui visualizzare le textures (la distanza visiva in pratica)
		cam.setFrustumPerspective(45.0f, (float)width / (float)height, 1, 1000);
		
		// cambiamo la posizione della telecamera in modo da vedere tutto il nostro terreno
		Vector3f loc = new Vector3f(350.0f, 100.0f, 350.0f);
		Vector3f left = new Vector3f(-0.5f, 0.0f, 0.5f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(-0.5f, 0.0f, -0.5f);
		cam.setFrame(loc, left, up, dir);
		
		// abbiamo cambiato la camera quindi aggiorniamo
		cam.update();
	
		// usiamo il timer per calcolarci gli fps, ma pu� servire per molto altro
	    timer = Timer.getTimer();
	    
        //KeyBindingManager.getKeyBindingManager().set(KeyInput.get());
	    
	    // impostiamo la camera al displaySystem
		display.getRenderer().setCamera(cam);
	
		// gestiamo ancora un solo evento, cio� la pressione del tasto "Esc" per chiudere il gioco
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
		
		// usciamo se � stato dato il comando exit (tasto esc)
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}
		// facciamo in modo che il nostro cielo si muovi assieme alla cam
		skybox.setLocalTranslation(cam.getLocation());
		
		// aggiorniamo la chase camera ogni volta per restare vicino al player
		chaser.update(interpolation);
		
        // aggiorniamo l'input
        input.update(interpolation);
 
        // questo per non far scendere la telecamera "sotto" il nostro piano
        if(cam.getLocation().y < (terrain.getHeight(cam.getLocation())+2)) {
            cam.getLocation().y = terrain.getHeight(cam.getLocation()) + 2f;
            cam.update();
        }
        
		// aggiorniamo la posizione y del player ogni volta, visto che il terreno sar� dissestato 
		// e ci saranno salite e discese
        float characterMinHeight = terrain.getHeight(player.getLocalTranslation()) + ((BoundingBox)player.getWorldBound()).yExtent + terrainToPlayer;
        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {
            player.getLocalTranslation().y = characterMinHeight;
        }

        // facciamo in modo che il nostro player segua l'andamento del terreno
        terrain.getSurfaceNormal(player.getLocalTranslation(), normal);
        if(normal != null) {
            player.rotateUpTo(normal);
        }

		scene.updateGeometricState(interpolation, true);
	}
	
	@Override
	protected void cleanup() {
	}

	private void buildEnvironment() {
		fence = new ForceFieldFence( "Fenze" );
		//scaliamo la recinzione e la spostiamo in modo da stare un po dentro il suolo 
		fence.setLocalScale(5);
		fence.setLocalTranslation(new Vector3f(25, terrain.getHeight(25,25)+10, 25));

		scene.attachChild(fence);
		
		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();
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
	   pt.addTexture(new ImageIcon(Game.class.getClassLoader().getResource("data/texture/grassb.png")), -128, 0, 128);
	   // per le zone di media altezza usiamo terra e rocce
	   pt.addTexture(new ImageIcon(Game.class.getClassLoader().getResource("data/texture/dirt.jpg")), 0, 128, 255);
	   // per le zone alte usiamo la neve!
	   pt.addTexture(new ImageIcon(Game.class.getClassLoader().getResource("data/texture/highest.jpg")), 128, 255, 384);
	   // il numero passato alla funzione significa che creeremo textures n x n
	   pt.createTexture(32);
	   // il nostro proceduralblabla � in grado di miscelare le textures per creare un terreno un po pi� realistico
	   
	   // ora applichiamo le textures create al nostro terreno
	   TextureState ts = display.getRenderer().createTextureState();
	   Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(),
	                   Texture.MinificationFilter.BilinearNearestMipMap,
	                   Texture.MagnificationFilter.Bilinear,
	   	        true);
	   ts.setTexture(t1, 0);
	   Texture t2 = TextureManager.loadTexture(
               Game.class.getClassLoader().getResource(
               "data/texture/Detail.jpg"),
               Texture.MinificationFilter.BilinearNearestMipMap,
               Texture.MagnificationFilter.Bilinear);

	   ts.setTexture(t2, 1);
       t2.setWrap(Texture.WrapMode.Repeat);

       t1.setApply(Texture.ApplyMode.Combine);
       t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
       t1.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
       t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
       t1.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
       t1.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);

       t2.setApply(Texture.ApplyMode.Combine);
       t2.setCombineFuncRGB(Texture.CombinerFunctionRGB.AddSigned);
       t2.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
       t2.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
       t2.setCombineSrc1RGB(Texture.CombinerSource.Previous);
       t2.setCombineOp1RGB(Texture.CombinerOperandRGB.SourceColor);

	   terrain.setRenderState( ts );
	   terrain.setDetailTexture(1, 16);

	   // aggiungo il terreno alla coda degli oggetti opachi
	   terrain.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	   
	   scene.attachChild( terrain );  
	   logger.info("Terrain building complete\n");
	}
	
	/** Funzione che crea una luce direzionale
	 */
	private void buildLighting() {

		// creiamo la nostra luce direzionale
	    DirectionalLight light = new DirectionalLight();
	    light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    // la direzione della luce sar� in basso verso destra
	    light.setDirection(new Vector3f(1,-1,0));
	    light.setEnabled(true);
	    
	    LightState lightState = display.getRenderer().createLightState();
	    lightState.setEnabled(true);
	    lightState.attach(light);
	    scene.setRenderState(lightState);
		logger.info("Lighting building complete\n");
	    // e luce sia!
	}
	
	private void buildSkyBox() {
		// e' un box che contiene tutto e che vediamo dall'interno, quindi 
		// dobbiamo impostare le textures per le 6 facce visibili!
		
        skybox = new Skybox("skybox", 10, 10, 10);
 
        Texture north = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "data/texture/north.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "data/texture/south.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "data/texture/east.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "data/texture/west.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "data/texture/top.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "data/texture/bottom.jpg"),
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
 	   logger.info("Skybox building complete\n");
    }
	
    /**
     * settiamo i paramtri base della chase camera
     * vogliamo che sia dietro il nostro player, leggermente sopra ( fattore 1.5)
     * settiamo anche il minimo e massimo zoom
     * e la minima e massima distanza dal player
     *
     */
    private void buildChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = ((BoundingBox) player.getWorldBound()).yExtent * 1.5f;
        HashMap<String, Object> props = new HashMap<String, Object>();
        props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "10");
        props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "3");
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        props.put(ThirdPersonMouseLook.PROP_MAXASCENT, ""+90 * FastMath.DEG_TO_RAD);
        props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0, 30 * FastMath.DEG_TO_RAD));
        props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
        chaser = new ChaseCamera(cam, player, props);
        chaser.setMaxDistance(20);
        chaser.setMinDistance(1);
    }

    private void buildInput() {
	    input = new CustomInputHandler(player, settings.getRenderer());
	}

	private void buildPlayer() {
		
		player = new Node("player");
		terrainToPlayer = -2.5f;
		float scaleFactor = 0.05f;
		Quaternion rotation = new Quaternion()/*.fromAngleAxis(FastMath.PI/2, new Vector3f(-1,0,0))*/;
		

/**************** Codice per convertire il modello obj Soldato.obj in soldato.jme *******************
 * 
 *		ModelConverter.convert( "data/model/Soldato/Soldato.obj", "data/model/Soldato/soldato.jme");
 *
 */	
		
		Node model = ModelLoader.loadModel("data/model/Soldato/soldato.jme", "", scaleFactor, rotation);
	
		player.attachChild(model);
		
		// lo metto da qualche parte
		player.setLocalTranslation(new Vector3f(100,0, 100));
		player.setLocalScale(2f);
		player.setModelBound(new BoundingBox());
		player.updateModelBound();
		
		scene.attachChild(player);
//		
		// attivo l'animazione
		// creo un controller d'animazione
//        AnimationController ac = getAnimationController();
        // attivo una animazione (devo conoscere il nome)
//        ac.setActiveAnimation(0);
//        ac.setActive(true);
		
		scene.updateGeometricState(0, true);
		player.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	}
	
	/** funzione che ritorna il controller per il nostro player
	 * 
	 * @return AnimationController
	 */
    public AnimationController getAnimationController() {
        List<Bone> armatures = player.descendantMatches(Bone.class, ".+SuperBone");
        if (armatures.size() < 1)
            throw new IllegalStateException("Sorry.  Program assumes "
                    + "you have a node named with suffix 'SuperBone'");
        if (armatures.get(0).getControllerCount() != 1)
            throw new IllegalStateException(
                    "Armature should have 1 controller, but has "
                    + armatures.get(0).getControllerCount());
        Controller controller = armatures.get(0).getController(0);
        if (!(controller instanceof AnimationController))
            throw new IllegalStateException(
                    "Controller is of unexpected type: "
                    + controller.getClass().getName());
        return (AnimationController) controller;
    }

	private void buildModels() {
		
//        // create vegetation
//
//        // specifies the value of how much different types we want every game...
//        // there are currently 9 tree types and 24 plant types
//        // and we choose 5 random out of 9 tree types every game
//        // it is possible to get the same trees again in the game -> depends on
//        // the random value
//        int amountOfDifferentPlantsPerTypes = 5;
//
//        for (int i = 0; i < amountOfDifferentPlantsPerTypes; i++)
//        {
//            // 5 different treeTypes and there are 10 trees of every type
//            Vegetation.loadTrees(terrain, 50);
//            Vegetation.loadBushes(terrain, 25);
//            Vegetation.loadPlants(terrain, 70);
//        }
//        // get more variation in plants
//        // it is possible to get the same plants again --> there are 24
//        // different types, chosen
//        // per random
//        amountOfDifferentPlantsPerTypes = 5;
//        for (int i = 0; i < amountOfDifferentPlantsPerTypes; i++)
//        {
//            // makes 10 plants every plant type has 2 plants
//            Vegetation.loadPlants(terrain, 12);
//        }
//		

		Node model = ModelLoader.loadModel("data/model/Soldato/Soldato.obj", "", 0.3f, new Quaternion());
		model.setLocalTranslation(new Vector3f(150, terrain.getHeight(150,150), 150));
		model.setModelBound(new BoundingBox());
		scene.attachChild( model );
		
	}

}
