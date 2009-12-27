package game.graphics;

import game.HUD.WorldMap2D;
import game.base.Game;
import game.common.GameTimer;
import game.input.ThirdPersonHandler;
import game.menu.LoadingFrame;
import game.sound.SoundManager;

import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import jmetest.TutorialGuide.ExplosionFactory;
import utils.Loader;
import utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseLookHandler;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.PassNode;
import com.jme.scene.PassNodeState;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;
import com.jmex.effects.water.WaterRenderPass;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.RawHeightMap;

/** Class GraphicalWorld <br>
 * The main graphics class which contains all the graphical objects and characters
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 */
public class GraphicalWorld extends Game {
	
	public Node getHudNode() {
		return hudNode;
	}

	Node collisionNode;
	
	/** environment */
    WaterRenderPass waterEffectRenderPass;
    Quad waterQuad;
    
    TerrainPage terrain;
    Spatial splatTerrain;
    Spatial reflectionTerrain;
    
    float farPlane = 10000.0f;
    float textureScale = 0.07f;
    float globalSplatScale = 90.0f;
	
	/** an interface to communicate with the application core */
	WorldInterface core;
	
	/** a custom freeCamInput handler to control the player */
	ThirdPersonHandler inputHandler;
    
    /** the world's ground */
    StaticPhysicsNode ground;
    
    /** the game's bounds, a physics box that contains the scene */
    StaticPhysicsNode gameBounds;
    
    /** hashmap of the characters */
    HashMap< String, Character > characters; 
    
    /** hashmap of the bullets */
	HashMap< String, Bullet > bullets;
    int bulletsCounter = 0;
    
    /** hashmap of the ammo packages */
	HashMap< String, AmmoPackage > ammoPackages;
    int ammoPackagesCounter = 0;
    
    /** hashmap of the energy packages */
	HashMap< String, EnergyPackage > energyPackages;
    int energyPackagesCounter = 0;
    
    /** the player, in single player mode */
    Player player;

    /** the sky */
	Skybox skybox;

	/** set to false when you don't want to do the world update */
	boolean enabled = true;
	
	/** HUD node */
	public Node hudNode;
	/** very very basic hud */
	Text life;
	Quad crosshair;
	Text gameOver;
	Text fps;
	
	/** audio controller */
	SoundManager audio;
	boolean audioEnabled;
	
	boolean freeCam;

	MouseLookHandler mouseLookHandler;

	LensFlare flare;

	LightNode lightNode;

	WorldMap2D hudMap;
	
	float dimension;

	Vector2f resolution;

	int heightMapSize;

	private int terrainScale;
	
	
	
	public float getDimension() {
		return dimension;
	}

	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) 
	 */
	public GraphicalWorld( WorldInterface core ) {
		this.core = core;
		
//		audioEnabled = true;
	}
	
	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) 
	 */
    public GraphicalWorld( WorldInterface core, LoadingFrame loadingFrame ) {
        this.core = core;
        super.loadingFrame = loadingFrame;
        
        audioEnabled = true;
    }


	public void setCrosshair() {
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setTestEnabled(false);
        as.setSourceFunction( SourceFunction.SourceAlpha );
        as.setDestinationFunction( DestinationFunction.OneMinusSourceAlpha );
        as.setEnabled(true);
		TextureState cross = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		cross.setTexture( TextureManager.loadTexture( Loader.load(
		                "game/data/images/crosshair.png" ), false ) );
		cross.setEnabled(true);
		crosshair = new Quad( "quad", 30, 30 );
		crosshair.setLocalTranslation( DisplaySystem.getDisplaySystem().getWidth() / 2,
				DisplaySystem.getDisplaySystem().getHeight() / 2, 0 );
		crosshair.setRenderState(as);
		crosshair.setRenderState(cross);
		
		hudNode.attachChild(crosshair);
		hudNode.updateGeometricState( 0.0f, true );
		hudNode.updateRenderState();
	}
	
    public void setupInit() {
   	
		hudNode = new Node( "HUD" );
		rootNode.attachChild( hudNode );
		hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		collisionNode = new Node( "CollisionNode" );
		
		characters = new HashMap<String, Character>();
		bullets = new HashMap<String, Bullet>();
		ammoPackages = new HashMap<String, AmmoPackage>();
		energyPackages = new HashMap<String, EnergyPackage>();
		
        ground = getPhysicsSpace().createStaticNode();
        gameBounds = getPhysicsSpace().createStaticNode();
        
        rootNode.attachChild(ground);
        rootNode.attachChild(gameBounds);
        
    	life = Text.createDefaultTextLabel( "life" );
    	life.setLocalTranslation( 20, 20, 0 );
    	rootNode.attachChild( life );
    	
    	setCrosshair();
    	
		gameOver = Text.createDefaultTextLabel( "gameOver" );
		rootNode.attachChild(gameOver);
		
		fps = Text.createDefaultTextLabel( "life" );
    	fps.setLocalTranslation( 20, 40, 0 );
    	rootNode.attachChild( fps );
		
    	ExplosionFactory.warmup();
    	
		if( audioEnabled ) {
			audio = new SoundManager( cam );
		}
		

		resolution = new Vector2f( settings.getWidth(), settings.getHeight() );
		
		heightMapSize = 129;
		terrainScale = 20;
		
		dimension = heightMapSize * terrainScale;
		
		hudMap = new WorldMap2D( this );
    }
    
    
    public Vector2f getResolution() {
		return resolution;
	}

	/** Create graphic characters
     *  and place them in positions setted in the logic game
     */
    public void setupEnemies() { 	    	
        for( String id : core.getEnemiesIds() ) {
        	Node model = ModelLoader.loadModel("game/data/models/soldier/enemy.jme", 
        			"game/data/models/soldier/soldier.jpg", 1f, new Quaternion());
            model.setLocalTranslation(0, -2f, 0);  
            
			Texture texture = TextureManager.loadTexture( Loader.load( "game/data/models/soldier/lr300map.jpg" ),
	                Texture.MinificationFilter.Trilinear,
	                Texture.MagnificationFilter.Bilinear);
	        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	        ts.setEnabled(true);
	        ts.setTexture(texture);
			model.getChild( "weapon" ).setRenderState( ts );

            Enemy enemy = new Enemy( id, this, 20, 100,  model );
            
            Vector3f position = core.getPosition(id);
            position.setY( terrain.getHeight( position.x, position.z ) + 1 );
        	enemy.getCharacterNode().getLocalTranslation().set( position );
        	characters.put( id, enemy );
        	rootNode.attachChild( characters.get(id).getCharacterNode() );
        }
    }
    
    /** Create graphic players
     *  and place them in positions setted in the logic game
     */
    public void setupPlayer() {
    	
    	Node model = ModelLoader.loadModel("game/data/models/soldier/player.jme", 
    			"game/data/models/soldier/soldato.jpg", 1 );
        model.setLocalTranslation(0, -2f, 0);   
        
		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/models/soldier/lr300map.jpg" ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(texture);
		model.getChild( "weapon" ).setRenderState( ts );
        
        for( String id : core.getPlayersIds() ) {
        	player = new Player( id, this, 20, 100, model );
            player.getCharacterNode().getLocalTranslation().set( core.getPosition(id) );
            rootNode.attachChild( player.getCharacterNode() );
            characters.put( player.id, player );
        }
    }
    
    public void setupCamera() {
        cam.setLocation(new Vector3f(160,30,160));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, farPlane);
        cam.update();
    }

    public void setupInput() {
//        physicsInputHandler = new PhysicsInputHandler( player, cam );
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    	
        KeyBindingManager.getKeyBindingManager().set( "freeCamAction", KeyInput.KEY_C );
        KeyBindingManager.getKeyBindingManager().set( "shoot", KeyInput.KEY_F );
        KeyBindingManager.getKeyBindingManager().set( "firstPerson", KeyInput.KEY_R );
    	
        HashMap<String, Object> handlerProps = new HashMap<String, Object>();
        handlerProps.put(ThirdPersonHandler.PROP_DOGRADUAL, "true");
        handlerProps.put(ThirdPersonHandler.PROP_TURNSPEED, ""+(1.0f * FastMath.PI));
        handlerProps.put(ThirdPersonHandler.PROP_LOCKBACKWARDS, "false");
        handlerProps.put(ThirdPersonHandler.PROP_CAMERAALIGNEDMOVE, "true");
//        handlerProps.put(ThirdPersonHandler.PROP_PERMITTER, player );
        inputHandler = new ThirdPersonHandler( player, cam, handlerProps);
    }

	@Override
    protected void update() {
		if( !enabled )
			return;
		
		if( audioEnabled )
			audio.update();
    	
		if( core.isAlive( player.id ) == false ) {
    		life.print( "Life: 0" );
    		gameOver();
    	} else {
    		life.print( "Life: " + core.getCurrentLife( player.id ) );
    		
	        inputHandler.update(tpf);
	        freeCamInput.update(tpf);
	        float camMinHeight = terrain.getHeight(cam.getLocation()) + 2f;
	        if (!Float.isInfinite(camMinHeight) && !Float.isNaN(camMinHeight)
	                && cam.getLocation().y <= camMinHeight) {
	            cam.getLocation().y = camMinHeight;
	            cam.update();
	        }
	        
	        float characterMinHeight = terrain.getHeight(player.getCharacterNode()
	                .getLocalTranslation()) - 7.5f ;
	        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {
	            player.getCharacterNode().getLocalTranslation().y = characterMinHeight;
	        }
	        
	        skybox.getLocalTranslation().set( cam.getLocation() );
	        skybox.updateGeometricState(0.0f, true);
	        
//	        lightNode.getLocalTranslation().set( cam.getLocation().add( 200, 200, 200 ) );
//	        rootNode.updateGeometricState(0, true);

	        /******** Added to animate the water ********/
	        Vector3f transVec = new Vector3f(cam.getLocation().x,
	                waterEffectRenderPass.getWaterHeight(), cam.getLocation().z);
	        setTextureCoords(0, transVec.x, -transVec.z, textureScale);
	        setVertexCoords(transVec.x, transVec.y, transVec.z);
	        /****************/
	        
	        updateCharacters(tpf);
	        
	        /** print the crosshair only in first person view */
	        if( inputHandler.isFirstPerson() ) {
	        	if( !hudNode.hasChild( crosshair ) )
	        		hudNode.attachChild( crosshair );
	        } else {
	        	if( hudNode.hasChild( crosshair ) )
	        		hudNode.detachChild( crosshair );
	        }
	        
		    if( freeCam ) {
		    	inputHandler.setEnabled( false );
		    	freeCamInput.setEnabled( true );
		    } else {
		    	freeCamInput.setEnabled( false );
		    	inputHandler.setEnabled( true );
		    }
	        
	        updateBullets(tpf);
	        updateAmmoPackages(tpf);
	        updateEnergyPackages(tpf);
	        
    	}
    	
		updateInput();
		
		hudMap.update();
		
    	fps.print( "Frame Rate: " + (int) GameTimer.getFrameRate() + "fps" );
    }
    
	private void updateInput() {
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("freeCamAction", false ) ) {
            freeCam = !freeCam;
        }
	}

	private void gameOver() {
		gameOver.print( "Game Over" );
        gameOver.setLocalScale( 3 );
        gameOver.setTextColor( ColorRGBA.red );
		gameOver.setLocalTranslation(new Vector3f(display.getWidth() / 2f - gameOver.getWidth() / 2,
				display.getHeight() / 2f - gameOver.getHeight() / 2, 0));
		
		playDeathSound();
		
		enabled = false;
	}

	/** Function updateCharacters<br>
	 * Call the update method of each character contained in the characters hashMap
	 */
	private void updateCharacters( float time ) {
		Collection<Character> c = new LinkedList<Character>( characters.values() );
		Iterator<Character> it = c.iterator();
		while( it.hasNext() ) {
			it.next().update(time);
		}
		c.clear();
		c = null;
	}
	
	/** Function updateBullets <br>
	 * Call the update method of each bullet contained in the bullets hashMap
	 */
	private void updateBullets( float time ) {
		Collection<Bullet> c = new LinkedList<Bullet>( bullets.values() );
		Iterator<Bullet> it = c.iterator();
		while( it.hasNext() ) {
			it.next().update(time);
		}
		c.clear();
		c = null;
	}
	
	/** Function updateAmmoPackages <br>
	 * Call the update method of each ammo pack contained in the ammoPackages hashMap
	 */
    public void updateAmmoPackages( float time ) {
		Collection<AmmoPackage> c = new LinkedList<AmmoPackage>( ammoPackages.values() );
		Iterator<AmmoPackage> it = c.iterator();
    	while( it.hasNext() ) {
    		it.next().update(time);
    	}
		c.clear();
		c = null;
    }
    
	/** Function updateEnergyPackages <br>
	 * Call the update method of each energy pack contained in the energyPackages hashMap
	 */
    public void updateEnergyPackages( float time ) {
		Collection<EnergyPackage> c = new LinkedList<EnergyPackage>( energyPackages.values() );
		Iterator<EnergyPackage> it = c.iterator();
    	while( it.hasNext() ) {
    		it.next().update(time);
    	}
		c.clear();
		c = null;
    }

	public void setupEnvironment() {
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
//	    DirectionalLight dr = new DirectionalLight();
//	    dr.setEnabled(true);
//	    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
//	    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
//	    dr.setDirection(new Vector3f(0.5f, -0.5f, 0));
//	
//	    lightState.detachAll();
//	    lightState.attach(dr);
	    
	    setuplight();
		
	    CullState cs = display.getRenderer().createCullState();
	    cs.setCullFace(CullState.Face.Back);
	    rootNode.setRenderState(cs);
//	
//	    lightState.detachAll();
//	    rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);
	
	    FogState fogState = display.getRenderer().createFogState();
	    fogState.setDensity(1.0f);
	    fogState.setEnabled(true);
	    fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    fogState.setEnd(farPlane);
	    fogState.setStart(farPlane / 10.0f);
	    fogState.setDensityFunction(FogState.DensityFunction.Linear);
	    fogState.setQuality(FogState.Quality.PerVertex);
	    rootNode.setRenderState(fogState);
	    
		createTerrain();
        createReflectionTerrain();

        buildSkyBox();
        
        rootNode.attachChild(skybox);
        rootNode.attachChild(splatTerrain);
        
        createWater();
	    
	    RenderPass rootPass = new RenderPass();
	    rootPass.add(rootNode);
	    passManager.add(rootPass);

	    rootNode.setCullHint(Spatial.CullHint.Never);
	    rootNode.setCullHint(Spatial.CullHint.Never);
		
	    createVegetation();
	    createWorldBounds();
	    setupEnergyPackages();
	}
	
//	private void createFogState() {
//        FogState fs = display.getRenderer().createFogState();
//        fs.setDensity(0.5f);
//        fs.setEnabled(true);
//        fs.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
//        fs.setEnd(1000);
//        fs.setStart(500);
//        fs.setDensityFunction(FogState.DensityFunction.Linear);
//        fs.setQuality(FogState.Quality.PerVertex);
//        rootNode.setRenderState(fs);
//    }
	
	private void setuplight() {
		lightState.detachAll();

        PointLight dr = new PointLight();
        dr.setEnabled(true);
        dr.setDiffuse(ColorRGBA.white.clone());
        dr.setAmbient(ColorRGBA.gray.clone());
        dr.setLocation(new Vector3f(0f, 0f, 0f));

        lightState.attach(dr);
        lightState.setTwoSidedLighting(true);

        lightNode = new LightNode("light");
        lightNode.setLight(dr);

        Vector3f min2 = new Vector3f(-0.5f, -0.5f, -0.5f);
        Vector3f max2 = new Vector3f(0.5f, 0.5f, 0.5f);
        Box lightBox = new Box("box", min2, max2);
        lightBox.setModelBound(new BoundingBox());
        lightBox.updateModelBound();
//        lightNode.attachChild(lightBox);
        lightNode.setLocalTranslation(new Vector3f(-1200, 200,-1200));

        // clear the lights from this lightbox so the lightbox itself doesn't
        // get affected by light:
        lightBox.setLightCombineMode(LightCombineMode.Off);

        // Setup the lensflare textures.
        TextureState[] tex = new TextureState[4];
        tex[0] = display.getRenderer().createTextureState();
        tex[0].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare1.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.RGBA8,
                0.0f, true));
        tex[0].setEnabled(true);

        tex[1] = display.getRenderer().createTextureState();
        tex[1].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare2.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[1].setEnabled(true);

        tex[2] = display.getRenderer().createTextureState();
        tex[2].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare3.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[2].setEnabled(true);

        tex[3] = display.getRenderer().createTextureState();
        tex[3].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare4.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[3].setEnabled(true);

        flare = LensFlareFactory.createBasicLensFlare("flare", tex);
        flare.setRootNode(rootNode);
        rootNode.attachChild(lightNode);

        // notice that it comes at the end
        lightNode.attachChild(flare);
	}

	// TODO creare alberi e cazzate varie
	private void createVegetation() {
//		appesantisce un casino...anche un solo albero...bah!
//		Node tree = ModelLoader.loadModel( "game/data/models/vegetation/tree1.3ds", 
//				"", 0.02f, Util.X270 );
//		
//		tree.setLocalTranslation( 30, terrain.getHeight( 30, 30 ), 30 );
//		
//		rootNode.attachChild( tree );
		
//     TextureState treeTex = display.getRenderer().createTextureState();
//      treeTex.setEnabled(true);
//      Texture tr = TextureManager.loadTexture(
//              GraphicalWorld.class.getClassLoader().getResource(
//                      "game/data/texture/grass.jpg"), Texture.MinificationFilter.Trilinear,
//              Texture.MagnificationFilter.Bilinear);
//      treeTex.setTexture(tr);
//
////      Node p = ModelLoader.loadModel( "game/data/Tree1.3ds", "", 0.2f, 
////      		new Quaternion().fromAngleAxis( FastMath.PI * 3/2, Vector3f.UNIT_X));
//      Pyramid p = new Pyramid("p", 10, 20);
//      p.setModelBound(new BoundingBox());
//      p.updateModelBound();
//      p.setRenderState(treeTex);
//      p.setTextureCombineMode(TextureCombineMode.Replace);
//      
//      for (int i = 0; i < 100; i++) {
//      	Spatial s1 = new SharedMesh("tree"+i, p);
//          float x = (float) Math.random() * 128 * 5;
//          float z = (float) Math.random() * 128 * 5;
//          s1.setLocalTranslation(new Vector3f(x, ( (TerrainPage) splatTerrain).getHeight(x, z), z));
//          rootNode.attachChild(s1);
//      }
	}

	private void setupEnergyPackages() {
		for( String id : core.getEnergyPackagesIds() ) {
			Vector3f position = new Vector3f();
			Random r = new Random();
			position.setX( r.nextInt( (int) dimension ) );
			position.setY( r.nextInt( 20 ) );
			position.setZ( r.nextInt( (int) dimension ) );
			EnergyPackage e = new EnergyPackage( id, this, position );
			energyPackages.put( e.id, e );
		}
	}
	
    /** Function that creates the game bounds with a physics box that contains all the world
     */
	public void createWorldBounds() {

		float x = dimension;
		float z = dimension;
		float y = 800;
		
		gameBounds.setLocalTranslation( -x/2, 0, -z/2 );
		
	    PhysicsBox downBox = gameBounds.createBox("downBox");
	    downBox.setLocalTranslation( x/2, -20, z/2 );
	    downBox.setLocalScale( new Vector3f( x, 5, z) );

	    PhysicsBox upperBox = gameBounds.createBox("upperBox");
	    upperBox.setLocalTranslation( x/2, y-20, z/2 );
	    upperBox.setLocalScale( new Vector3f( x, 5, z) );
	    
	    PhysicsBox eastBox = gameBounds.createBox("eastBox");
	    eastBox.setLocalTranslation( x, y/2-20, z/2 );
	    eastBox.setLocalScale( new Vector3f( 5, y, z ) );
	    
	    PhysicsBox westBox = gameBounds.createBox("westBox");
	    westBox.setLocalTranslation( 0, y/2-20, z/2 );
	    westBox.setLocalScale( new Vector3f( 5, y, z ) );
	    
	    PhysicsBox southBox = gameBounds.createBox("southBox");
	    southBox.setLocalTranslation( x/2, y/2-20, 0 );
	    southBox.setLocalScale( new Vector3f( x, y, 5 ) );

	    PhysicsBox northBox = gameBounds.createBox("northBox");
	    northBox.setLocalTranslation( x/2, y/2-20, z );
	    northBox.setLocalScale( new Vector3f( x, y, 5 ) );
	    
	}
	
	public WorldInterface getCore() {
		return core;
	}
	
	public StaticPhysicsNode getGround() {
		return ground;
	}
	
	protected void cleanup() {
		super.cleanup();
		if( audioEnabled )
			audio.cleanup();
	}
	
	/************* METODI AGGIUNTI PER L'AMBIENTAZIONE *****************/
	
	private void createWater() {
		waterEffectRenderPass = new WaterRenderPass(cam, 6, false, true);
	    waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
	            0.0f), 0.0f));
	    waterEffectRenderPass.setClipBias(-1.0f);
	    waterEffectRenderPass.setReflectionThrottle(0.0f);
	    waterEffectRenderPass.setRefractionThrottle(0.0f);
	
	    waterQuad = new Quad("waterQuad", 1, 1);
	    FloatBuffer normBuf = waterQuad.getNormalBuffer();
	    normBuf.clear();
	    normBuf.put(0).put(1).put(0);
	    normBuf.put(0).put(1).put(0);
	    normBuf.put(0).put(1).put(0);
	    normBuf.put(0).put(1).put(0);
	
	    waterEffectRenderPass.setWaterEffectOnSpatial(waterQuad);
	    rootNode.attachChild(waterQuad);
	
	    waterEffectRenderPass.setReflectedScene(skybox);
	    waterEffectRenderPass.addReflectedScene(reflectionTerrain);
	    waterEffectRenderPass.setSkybox(skybox);
	    passManager.add(waterEffectRenderPass);
	}

	private void createTerrain() {
		RawHeightMap heightMap = new RawHeightMap( getClass()
                .getClassLoader().getResource(
                        "jmetest/data/texture/terrain/heights.raw"),
                heightMapSize, RawHeightMap.FORMAT_16BITLE, false);
		
        Vector3f terrainScale = new Vector3f( 20, 0.005f, 20 );
        heightMap.setHeightScale(0.001f);
        
        dimension = heightMapSize * terrainScale.x;
        
        terrain = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        terrain.getLocalTranslation().set( 0, -9.5f, 0);
        terrain.setDetailTexture(1, 1);

        // create some interesting texturestates for splatting
        TextureState ts1 = createSplatTextureState(
                "jmetest/data/texture/terrain/baserock.jpg", null);

        TextureState ts2 = createSplatTextureState(
                "jmetest/data/texture/terrain/darkrock.jpg",
                "jmetest/data/texture/terrain/darkrockalpha.png");

        TextureState ts3 = createSplatTextureState(
                "jmetest/data/texture/terrain/deadgrass.jpg",
                "jmetest/data/texture/terrain/deadalpha.png");

        TextureState ts4 = createSplatTextureState(
                "jmetest/data/texture/terrain/nicegrass.jpg",
                "jmetest/data/texture/terrain/grassalpha.png");

        TextureState ts5 = createSplatTextureState(
                "jmetest/data/texture/terrain/road.jpg",
                "jmetest/data/texture/terrain/roadalpha.png");

        TextureState ts6 = createLightmapTextureState(
        		"jmetest/data/texture/terrain/lightmap.jpg");

        // alpha used for blending the passnodestates together
        BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);

        // alpha used for blending the lightmap
        BlendState as2 = display.getRenderer().createBlendState();
        as2.setBlendEnabled(true);
        as2.setSourceFunction(BlendState.SourceFunction.DestinationColor);
        as2.setDestinationFunction(BlendState.DestinationFunction.SourceColor);
        as2.setTestEnabled(true);
        as2.setTestFunction(BlendState.TestFunction.GreaterThan);
        as2.setEnabled(true);

        // //////////////////// PASS STUFF START
        // try out a passnode to use for splatting
        PassNode splattingPassNode = new PassNode("SplatPassNode");
        splattingPassNode.attachChild(terrain);

        PassNodeState passNodeState = new PassNodeState();
        passNodeState.setPassState(ts1);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts2);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts3);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts4);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts5);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts6);
        passNodeState.setPassState(as2);
        splattingPassNode.addPass(passNodeState);
        // //////////////////// PASS STUFF END

        // lock some things to increase the performance
        splattingPassNode.lockBounds();
        splattingPassNode.lockTransforms();
        splattingPassNode.lockShadows();

        splatTerrain = splattingPassNode;
        splatTerrain.setCullHint(Spatial.CullHint.Dynamic);
        
	    ground.attachChild( splatTerrain );
    	
	    ground.generatePhysicsGeometry(true);
    }

    private void createReflectionTerrain() {
    	RawHeightMap heightMap = new RawHeightMap( Loader.load(
                        "jmetest/data/texture/terrain/heights.raw"),
                heightMapSize, RawHeightMap.FORMAT_16BITLE, false);

        Vector3f terrainScale = new Vector3f( 20, 0.007f, 20 );
        heightMap.setHeightScale(0.001f);
        TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        page.getLocalTranslation().set(  0, -9.5f, 0 );
        page.setDetailTexture(1, 1);

        TextureState ts1 = display.getRenderer().createTextureState();
        Texture t0 = TextureManager.loadTexture( Loader.load(
                        "jmetest/data/texture/terrain/terrainlod.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
//        t0.setWrap(Texture.WrapMode.Repeat);
//        t0.setApply(Texture.ApplyMode.Modulate);
//        t0.setScale(new Vector3f(1.0f, 1.0f, 1.0f));
        ts1.setTexture(t0, 0);

//        // //////////////////// PASS STUFF START
//        // try out a passnode to use for splatting
//        PassNode splattingPassNode = new PassNode("SplatPassNode");
//        splattingPassNode.attachChild(page);
//
//        PassNodeState passNodeState = new PassNodeState();
//        passNodeState.setPassState(ts1);
//        splattingPassNode.addPass(passNodeState);
//        // //////////////////// PASS STUFF END
//
//        // lock some things to increase the performance
//        splattingPassNode.lockBounds();
//        splattingPassNode.lockTransforms();
//        splattingPassNode.lockShadows();
//
//        reflectionTerrain = splattingPassNode;

        page.setRenderState( ts1 );
        
        reflectionTerrain = page;
        
        initSpatial(reflectionTerrain);
    }

    private void addAlphaSplat(TextureState ts, String alpha) {
        Texture t1 = TextureManager.loadTexture( Loader.load( alpha ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t1.setWrap(Texture.WrapMode.Repeat);
        t1.setApply(Texture.ApplyMode.Combine);
        t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Replace);
        t1.setCombineSrc0RGB(Texture.CombinerSource.Previous);
        t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t1.setCombineFuncAlpha(Texture.CombinerFunctionAlpha.Replace);
        ts.setTexture(t1, ts.getNumberOfSetTextures());
    }

    private TextureState createSplatTextureState(String texture, String alpha) {
        TextureState ts = display.getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture( Loader.load( texture ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        t0.setApply(Texture.ApplyMode.Modulate);
        t0.setScale(new Vector3f(globalSplatScale, globalSplatScale, 1.0f));
        ts.setTexture(t0, 0);

        if (alpha != null) {
            addAlphaSplat(ts, alpha);
        }

        return ts;
    }

    private TextureState createLightmapTextureState(String texture) {
        TextureState ts = display.getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture( Loader.load( texture ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        ts.setTexture(t0, 0);

        return ts;
    }

    private void buildSkyBox() {
//        skybox = new Skybox("skybox", 10, 10, 10);
//
//        String dir = "game/data/images/skybox/";
//        Texture north = TextureManager.loadTexture( Loader.load(dir + "nord.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture south = TextureManager.loadTexture( Loader.load(dir + "sud.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture east = TextureManager.loadTexture( Loader.load(dir + "est.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture west = TextureManager.loadTexture( Loader.load(dir + "ovest.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture up = TextureManager.loadTexture( Loader.load(dir + "up.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture down = TextureManager.loadTexture( Loader.load(dir + "down.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//
//        skybox.setTexture(Skybox.Face.North, north);
//        skybox.setTexture(Skybox.Face.West, west);
//        skybox.setTexture(Skybox.Face.South, south);
//        skybox.setTexture(Skybox.Face.East, east);
//        skybox.setTexture(Skybox.Face.Up, up);
//        skybox.setTexture(Skybox.Face.Down, down);
//        skybox.preloadTextures();
//
//        CullState cullState = display.getRenderer().createCullState();
//        cullState.setCullFace(CullState.Face.None);
//        cullState.setEnabled(true);
//        skybox.setRenderState(cullState);
//
//        FogState fs = display.getRenderer().createFogState();
//        fs.setEnabled(false);
//        skybox.setRenderState(fs);
//
////        skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
//        skybox.setCullHint(Spatial.CullHint.Never);
//        skybox.setTextureCombineMode(TextureCombineMode.Replace);
//        skybox.updateRenderState();
//
//        skybox.lockBounds();
//        skybox.lockMeshes();
    
    	
    	skybox = new Skybox("skybox", 10, 10, 10);

        String dir = "jmetest/data/skybox1/";
        Texture north = TextureManager.loadTexture( Loader.load(dir + "1.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture( Loader.load(dir + "3.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture( Loader.load(dir + "2.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture( Loader.load(dir + "4.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture( Loader.load(dir + "6.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture( Loader.load(dir + "5.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);

        skybox.setTexture(Skybox.Face.North, north);
        skybox.setTexture(Skybox.Face.West, west);
        skybox.setTexture(Skybox.Face.South, south);
        skybox.setTexture(Skybox.Face.East, east);
        skybox.setTexture(Skybox.Face.Up, up);
        skybox.setTexture(Skybox.Face.Down, down);
        skybox.preloadTextures();

//        CullState cullState = display.getRenderer().createCullState();
//        cullState.setCullFace(CullState.Face.None);
//        cullState.setEnabled(true);
//        skybox.setRenderState(cullState);

//        ZBufferState zState = display.getRenderer().createZBufferState();
//        zState.setEnabled(false);
//        skybox.setRenderState(zState);

//        FogState fs = display.getRenderer().createFogState();
//        fs.setEnabled(false);
//        skybox.setRenderState(fs);

//        skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
//        skybox.setCullHint(Spatial.CullHint.Never);
//        skybox.setTextureCombineMode(TextureCombineMode.Replace);
//        skybox.updateRenderState();

        skybox.lockBounds();
        skybox.lockMeshes();
    }

    private void setVertexCoords(float x, float y, float z) {
        FloatBuffer vertBuf = waterQuad.getVertexBuffer();
        vertBuf.clear();

        vertBuf.put(x - farPlane).put(y).put(z - farPlane);
        vertBuf.put(x - farPlane).put(y).put(z + farPlane);
        vertBuf.put(x + farPlane).put(y).put(z + farPlane);
        vertBuf.put(x + farPlane).put(y).put(z - farPlane);
    }

    private void setTextureCoords(int buffer, float x, float y,
            float textureScale) {
        x *= textureScale * 0.5f;
        y *= textureScale * 0.5f;
        textureScale = farPlane * textureScale;
        FloatBuffer texBuf;
        texBuf = waterQuad.getTextureCoords(buffer).coords;
        texBuf.clear();
        texBuf.put(x).put(textureScale + y);
        texBuf.put(x).put(y);
        texBuf.put(textureScale + x).put(y);
        texBuf.put(textureScale + x).put(textureScale + y);
    }

    private void initSpatial(Spatial spatial) {
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        spatial.setRenderState(buf);

        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        spatial.setRenderState(cs);

        spatial.setCullHint(Spatial.CullHint.Never);

        spatial.updateGeometricState(0.0f, true);
        spatial.updateRenderState();
    }

	public void shoot( Vector3f position ) {
		if( audioEnabled ) {
	    	SoundManager.explosion.setWorldPosition( position.clone() );
	    	SoundManager.explosion.setVolume( 0.7f );
			SoundManager.shoot.play();
		}
	}
	
	public void explode( Vector3f position ) {
		if( audioEnabled ) {
	    	SoundManager.explosion.setWorldPosition( position.clone() );
	    	SoundManager.explosion.setVolume( 5 );
			SoundManager.explosion.play();
		}
	}
    
	private void playDeathSound() {
		if( audioEnabled ) {
	    	SoundManager.death.setWorldPosition( cam.getLocation().clone() );
	    	SoundManager.death.setVolume( 5 );
			SoundManager.death.play();
		}
	}
}
