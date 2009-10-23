package slashWork.customGame;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.net.URL;

import javax.swing.ImageIcon;

import jmetest.renderer.TestSkybox;

import com.jme.animation.AnimationController;
import com.jme.animation.Bone;
import com.jme.animation.BoneAnimation;
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
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
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

import com.jme.util.export.xml.XMLImporter;;

/** Classe Game3:
 * Terzo game customizzato. 
 * Aggiungiamo all'environment creato in game2 un box che rappresenta il
 * nostro futuristico mezzo di trasporto
 * verrà gestito il movimento attraverso un inputHandler customizzato 
 * e soprattutto verrà creata la chase camera che serve a noi!
 * proprietà della camera:
 * punta sempre al nostro player e lo inseguirà (senno perchè chase!)
 * da libertà di rotazione col mouse attorno al player
 * permette di zoomare con la rotellina del mouse
 * 
 * direi che è perfetta!
 * @author slash17
 */
public class Game extends BaseGame {
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
	// scene sarà il nostro "rootNode"
	Node scene;
	// il nostro mondo esterno
	Skybox skybox;
	// la nostra recinzione
	ForceFieldFence fence;
	// il nostro player
	Player player;
	// l'inputHandler personalizzato
	InputHandler input;
	// vector used for the normal of the terrain
	private Vector3f normal;

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
		// creiamo un depth buffer per gestire la profondità su z
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

		
		// un CullState è in pratica un modo per velocizzare il tutto, visto la dovrà girare su una vm
		// la sua filosofia è: se per com'è la telecamera io questo oggetto lo vedo solo di fronte, 
		// perchè dovrei caricare anche le textures del retro?
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
		// ps: un frustum è in pratica il tronco di una piramide; nel nostro 
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
	
		// usiamo il timer per calcolarci gli fps, ma può servire per molto altro
	    timer = Timer.getTimer();
	    
        //KeyBindingManager.getKeyBindingManager().set(KeyInput.get());
	    
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
		// aggiorniamo la chase camera ogni volta per restare vicino al player
		chaser.update(interpolation);
		
        // aggiorniamo l'input
        input.update(interpolation);
        
        //ManagerCollision.checkCollision(player, chiesa);
//        ManagerCollision.checkCollision(player, fence);
 
        // questo per non far scendere la telecamera "sotto" il nostro piano
        if(cam.getLocation().y < (terrain.getHeight(cam.getLocation())+2)) {
            cam.getLocation().y = terrain.getHeight(cam.getLocation()) + 2f;
            cam.update();
        }
        
		// aggiorniamo la posizione y del player ogni volta, visto che il terreno sarà dissestato 
		// e ci saranno salite e discese
        float characterMinHeight = terrain.getHeight(player.getLocalTranslation()) + ((BoundingBox)player.getWorldBound()).yExtent + 3f;
        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {
            player.getLocalTranslation().y = characterMinHeight;
        }

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
	   pt.addTexture(new ImageIcon(Game.class.getClassLoader().getResource("texture/grassb.png")), -128, 0, 128);
	   // per le zone di media altezza usiamo terra e rocce
	   pt.addTexture(new ImageIcon(Game.class.getClassLoader().getResource("texture/dirt.jpg")), 0, 128, 255);
	   // per le zone alte usiamo la neve!
	   pt.addTexture(new ImageIcon(Game.class.getClassLoader().getResource("texture/highest.jpg")), 128, 255, 384);
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
	   Texture t2 = TextureManager.loadTexture(
               Game.class.getClassLoader().getResource(
               "texture/Detail.jpg"),
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
	}
	
	/** Funzione che crea una luce direzionale
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
//			Node model = null;
//	
//			try {
//	//			MaxToJme converter = new MaxToJme();
//	//			ObjToJme converter = new ObjToJme();
//				Md3ToJme converter = new Md3ToJme();
//	//			Md2ToJme converter = new Md2ToJme();
//	//			AseToJme converter = new AseToJme();
//				
//	//			C1.setProperty("mtllib",maxFile);
//				
//				ByteArrayOutputStream BO = new ByteArrayOutputStream();
//				
//				URL url = Game3.class.getClassLoader().getResource("xmlModels/nordhorse.md3");
//	
//				converter.convert( url.openStream(),BO);
//				model = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
//	//			model = (Node)BinaryImporter.getInstance().load( url );
//				//scale it to be MUCH smaller than it is originally
//				model.setLocalScale(0.25f);
//				model.setModelBound(new BoundingBox());
//				model.updateModelBound();
//	
//				Quaternion quaternion1 = new Quaternion();
//				quaternion1.fromAngleAxis(FastMath.PI/2, new Vector3f(0, -1, 0));
//				model.setLocalRotation(quaternion1);
//			} catch (IOException e) {
//			   e.printStackTrace();
//			}
//			
//	        Texture texture = TextureManager.loadTexture(
//	                Game3.class.getClassLoader().getResource("xmlModels/nordhorse_color.jpg"),
//	                Texture.MinificationFilter.Trilinear,
//	                Texture.MagnificationFilter.Bilinear);
//	
//	        TextureState ts = display.getRenderer().createTextureState();
//	        ts.setEnabled(true);
//	        ts.setTexture(texture);
//	        
//	        model.setRenderState( ts );
			
		Node model = null;
		
		URL url = Game.class.getClassLoader().getResource("xmlModels/prova-jme.xml");
		
		try {
			model = (Node)XMLImporter.getInstance().load( url );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.setLocalScale(2f);
		model.setModelBound(new BoundingBox());
		model.updateModelBound();
//		model.setLocalTranslation(50, terrain.getHeight(50,50) + 3, 50);
		
		//set the vehicles attributes (these numbers can be thought
		//of as Unit/Second).
		player = new Player("Player Node", model);
		player.setAcceleration(15);
		player.setBraking(15);
		player.setTurnSpeed(2.5f);
		player.setWeight(25);
		player.setMaxSpeed(25);
		player.setMinSpeed(15);

		player.setLocalTranslation(new Vector3f(100,0, 100));

		scene.attachChild(player);
		
		// attivo l'animazione
        AnimationController ac = getAnimationController();
        System.out.println("Available animations:");
        for (BoneAnimation anim : ac.getAnimations())
            System.out.println("    " + anim.getName());
        ac.setActiveAnimation("walk");
        ac.setActive(true);
        System.err.println("Executing animation '"
                + ac.getActiveAnimation().getName() + "'");
		
		scene.updateGeometricState(0, true);
		player.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

	}
	
	// funzione che ritorna il controller per il nostro player
    public AnimationController getAnimationController() {
        List<Spatial> armatures =
                player.descendantMatches(Bone.class, ".+SuperBone");
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
		Sphere s = new Sphere("Sphere", 30, 30, 15);
		s.setLocalTranslation(new Vector3f(150, 20, 150));
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		// le diamo un volto
		TextureState t;
		t = display.getRenderer().createTextureState();
		t.setEnabled(true);
		t.setTexture(TextureManager.loadTexture(
				Game1.class.getClassLoader().getResource("images/rockWall2.jpg"),
				Texture.MinificationFilter.BilinearNearestMipMap, 
				Texture.MagnificationFilter.Bilinear));

		s.setRenderState(t);
		scene.attachChild(s);
		
	}

}
