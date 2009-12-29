package slashWork.game.graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import jmetest.TutorialGuide.ExplosionFactory;
import slashWork.game.base.Game;
import slashWork.game.input.PhysicsInputHandler;
import slashWork.game.main.ThreadController;
import slashWork.game.sound.AudioManager;
import utils.Loader;
import utils.ModelLoader;

import com.jme.image.Texture;
import com.jme.light.DirectionalLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;

/** Class GraphicalWorld <br>
 * The main graphics class which contains all the graphical objects and characters
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 */
public class GraphicalWorld extends Game {
	
	/** an interface to communicate with the application core */
	WorldInterface core;
	
	/** a custom freeCamInput handler to control the player */
    PhysicsInputHandler physicsInputHandler;
    
    /** the world's ground */
    StaticPhysicsNode ground;
    
    /** the game's bounds, a physics box that contains the scene */
    StaticPhysicsNode gameBounds;
    
    /** hashmap of the characters */
    HashMap< String, PhysicsCharacter > characters; 
    
    /** hashmap of the bullets */
	HashMap< String, PhysicsBullet > bullets;
    int bulletsCounter = 0;
    
    /** hashmap of the ammo packages */
	HashMap< String, PhysicsAmmoPackage > ammoPackages;
    int ammoPackagesCounter = 0;
    
    /** hashmap of the energy packages */
	HashMap< String, PhysicsEnergyPackage > energyPackages;
    int energyPackagesCounter = 0;
    
    /** the player, in single player mode */
    PhysicsCharacter player;

    /** the sky */
	Skybox skybox;

	/** set to false when you don't want to do the world update */
	boolean enabled = true;
	
	/** HUD node */
	Node hudNode;
	/** very very basic hud */
	Text life;
	Quad crosshair;
	Text fps;
	
	/** audio controller */
	AudioManager audio;

	/** audio controller status */
	boolean audioEnabled;
	
	/** Position Vector for Rendering Optimization */
	Vector3f playerPosition = new Vector3f();
	Vector3f characterPosition = new Vector3f();
	
	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) 
	 * @param x - (int) the x dimension of the world
	 * @param z - (int) the z dimension of the world
	 */
	public GraphicalWorld( WorldInterface core, ThreadController tc ) {
		this.core = core;
		
		if( tc != null )
			super.threadController = tc;
		
		/** set to true if you want to enable the audio system */
		audioEnabled = false;
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
		
		characters = new HashMap<String, PhysicsCharacter>();
		bullets = new HashMap<String, PhysicsBullet>();
		ammoPackages = new HashMap<String, PhysicsAmmoPackage>();
		energyPackages = new HashMap<String, PhysicsEnergyPackage>();
		
        ground = getPhysicsSpace().createStaticNode();
        gameBounds = getPhysicsSpace().createStaticNode();
        
        rootNode.attachChild(ground);
        rootNode.attachChild(gameBounds);
        
    	life = Text.createDefaultTextLabel( "life" );
    	life.setLocalTranslation( 20, 20, 0 );
    	rootNode.attachChild( life );
    	
    	setCrosshair();
    	
		fps = Text.createDefaultTextLabel( "life" );
    	fps.setLocalTranslation( 20, 40, 0 );
    	rootNode.attachChild( fps );
		
    	ExplosionFactory.warmup();
    	
    	if( audioEnabled ) 
    		audio = new AudioManager( cam );
    	
//        pause = true;
    }
    
    /** Create graphic characters
     *  and place them in positions setted in the logic game
     */
    public void setupEnemies() { 	    	
        for( String id : core.getEnemiesId() ) {
        	Node model = ModelLoader.loadModel("game/data/models/soldier/sold2.ms3d", 
        			"game/data/models/soldier/soldier.jpg", 1f, new Quaternion());
            model.setLocalTranslation(0, -2f, 0);   
            
    		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/models/soldier/lr300map.jpg" ),
                    Texture.MinificationFilter.Trilinear,
                    Texture.MagnificationFilter.Bilinear);
            TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
            ts.setEnabled(true);
            ts.setTexture(texture);
    		model.getChild( "weapon" ).setRenderState( ts );

            PhysicsEnemy enemy = new PhysicsEnemy( id, this, 20, 100,  model );
        	enemy.getCharacterNode().getLocalTranslation().set( core.getCharacterPosition(id) );
        	characters.put( id, enemy );
        	rootNode.attachChild( characters.get(id).getCharacterNode() );
        }
    }
    
    /** Create graphic players
     *  and place them in positions setted in the logic game
     */
    public void setupPlayer() {
    	
		Node model = ModelLoader.loadModel("game/data/models/soldier/prova.ms3d", 
				"game/data/models/soldier/soldier.jpg", 1 );
	    model.setLocalTranslation(0, -2f, 0);   
	    
		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/models/soldier/AR15.jpg" ),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear);
	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    ts.setEnabled(true);
	    ts.setTexture(texture);
		model.getChild( "weapon" ).setRenderState( ts );
        
        for( String id : core.getPlayersId() ) {
        	player = new PhysicsCharacter( id, this, 20, 100, model );
            player.getCharacterNode().getLocalTranslation().set( core.getCharacterPosition(id) );
            rootNode.attachChild( player.getCharacterNode() );
            characters.put( player.id, player );
        }
    }
    
    public void setupCamera() {
//        cam.setLocation(new Vector3f(160,30,160));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, 1000);
        cam.update();
    }

    public void setupInput() {
        physicsInputHandler = new PhysicsInputHandler( player, cam );
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

			updateRenderOptimizer();

    		life.print( "Life: " + core.getCharacterLife( player.id ) );
    		
	        physicsInputHandler.update(tpf);
	        freeCamInput.update(tpf);
	        
	        skybox.getLocalTranslation().set( cam.getLocation() );
	        skybox.updateGeometricState(0.0f, true);
	        
	        updateCharacters(tpf);
	        
	        /** print the crosshair only in first person view */
	        if( player.isFirstPerson() ) {
	        	if( !hudNode.hasChild( crosshair ) )
	        		hudNode.attachChild( crosshair );
	        } else {
	        	if( hudNode.hasChild( crosshair ) )
	        		hudNode.detachChild( crosshair );
	        }
	        
	        updateBullets(tpf);
	        updateAmmoPackages(tpf);
	        updateEnergyPackages(tpf);
    	}
    	
    	fps.print( "Frame Rate: " + (int) timer.getFrameRate() + "fps" );
    }
    
	private void gameOver() {
		Text gameOver = Text.createDefaultTextLabel( "gameOver" );
		rootNode.attachChild( gameOver );
		gameOver.print( "Game Over" );
        gameOver.setLocalScale( 3 );
        gameOver.setTextColor( ColorRGBA.red );
		gameOver.setLocalTranslation(new Vector3f(display.getWidth() / 2f - gameOver.getWidth() / 2,
				display.getHeight() / 2f - gameOver.getHeight() / 2, 0));
		
		if( audioEnabled ) {
			AudioManager.death.setWorldPosition( cam.getLocation() );
			AudioManager.death.play();
		}
		
		enabled = false;
		pause = true;
	}

	/** Function updateCharacters<br>
	 * Call the update method of each character contained in the characters hashMap
	 */
	private void updateCharacters( float time ) {
		Collection<PhysicsCharacter> c = new LinkedList<PhysicsCharacter>( characters.values() );
		Iterator<PhysicsCharacter> it = c.iterator();
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
		Collection<PhysicsBullet> c = new LinkedList<PhysicsBullet>( bullets.values() );
		Iterator<PhysicsBullet> it = c.iterator();
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
		Collection<PhysicsAmmoPackage> c = new LinkedList<PhysicsAmmoPackage>( ammoPackages.values() );
		Iterator<PhysicsAmmoPackage> it = c.iterator();
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
		Collection<PhysicsEnergyPackage> c = new LinkedList<PhysicsEnergyPackage>( energyPackages.values() );
		Iterator<PhysicsEnergyPackage> it = c.iterator();
    	while( it.hasNext() ) {
    		it.next().update(time);
    	}
		c.clear();
		c = null;
    }

	public void setupEnvironment() {
		
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
	    DirectionalLight dr = new DirectionalLight();
	    dr.setEnabled(true);
	    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    dr.setDirection(new Vector3f(0.5f, -0.5f, 0));
	
	    lightState.detachAll();
	    lightState.attach(dr);
		
//	    CullState cs = display.getRenderer().createCullState();
//	    cs.setCullFace(CullState.Face.Back);
//	    rootNode.setRenderState(cs);
//	
//	    lightState.detachAll();
//	    rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);
//	    
//	    FogState fogState = display.getRenderer().createFogState();
//	    fogState.setDensity(1.0f);
//	    fogState.setEnabled(true);
//	    fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
//	    fogState.setEnd(farPlane);
//	    fogState.setStart(farPlane / 10.0f);
//	    fogState.setDensityFunction(FogState.DensityFunction.Linear);
//	    fogState.setQuality(FogState.Quality.PerVertex);
//	    rootNode.setRenderState(fogState);
	    
	    
		ground = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( ground );
        
        final Box floor = new Box("floor", new Vector3f( 500, 0, 500 ), 1000, 0.25f, 1000 );
        floor.setDefaultColor( ColorRGBA.gray );
        ground.attachChild( floor );
        ground.generatePhysicsGeometry();
	    

        buildSkyBox();
      
        rootNode.attachChild(skybox);

		
	    createVegetation();
	    createWorldBounds();
	    setupEnergyPackages();
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
		HashMap< String, Vector3f > hash = core.getEnergyPackagesPosition();
		for( String id : hash.keySet() ) {
			PhysicsEnergyPackage e = new PhysicsEnergyPackage( id, this, hash.get(id) );
			energyPackages.put( e.id, e );
		}
	}
	
    /** Function that creates the game bounds with a physics box that contains all the world
     */
	public void createWorldBounds() {

		float x = 1000;
		float z = 1000;
		float y = 120;
		
	    PhysicsBox downBox = gameBounds.createBox("downBox");
	    downBox.setLocalTranslation( x/2, -20, z/2 );
	    downBox.setLocalScale( new Vector3f( x, 5, z) );

	    PhysicsBox upperBox = gameBounds.createBox("upperBox");
	    upperBox.setLocalTranslation( x/2, 100, z/2 );
	    upperBox.setLocalScale( new Vector3f( x, 5, z) );
	    
	    PhysicsBox eastBox = gameBounds.createBox("eastBox");
	    eastBox.setLocalTranslation( x, 40, z/2 );
	    eastBox.setLocalScale( new Vector3f( 5, y, z ) );
	    
	    PhysicsBox westBox = gameBounds.createBox("westBox");
	    westBox.setLocalTranslation( 0, 40, z/2 );
	    westBox.setLocalScale( new Vector3f( 5, y, z ) );
	    
	    PhysicsBox southBox = gameBounds.createBox("southBox");
	    southBox.setLocalTranslation( x/2, 40, 0 );
	    southBox.setLocalScale( new Vector3f( x, y, 5 ) );

	    PhysicsBox northBox = gameBounds.createBox("northBox");
	    northBox.setLocalTranslation( x/2, 40, z );
	    northBox.setLocalScale( new Vector3f( x, y, 5 ) );
	}
	
	public WorldInterface getCore() {
		return core;
	}
	
	public StaticPhysicsNode getGround() {
		return ground;
	}
	
	@Override
	protected void cleanup() {
		super.cleanup();
		if( audioEnabled )
			audio.cleanup();
	}
	
    private void buildSkyBox() {
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

        CullState cullState = display.getRenderer().createCullState();
        cullState.setCullFace(CullState.Face.None);
        cullState.setEnabled(true);
        skybox.setRenderState(cullState);
        
//		DA PROBLEMI
//        ZBufferState zState = display.getRenderer().createZBufferState();
//        zState.setEnabled(true);
//        skybox.setRenderState(zState);
//
//        skybox.setRenderState(fs);
//
//        skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
//        skybox.setCullHint(Spatial.CullHint.Never);
//        skybox.setTextureCombineMode(TextureCombineMode.Replace);
        skybox.updateRenderState();

        skybox.lockBounds();
        skybox.lockMeshes();
    }

    
	public void shoot( Vector3f position ) {
		if( audioEnabled ) {
	    	AudioManager.explosion.setWorldPosition( position.clone() );
	    	AudioManager.explosion.setVolume( 0.7f );
			AudioManager.shoot.play();
		}
	}
	
	public void explode( Vector3f position ) {
		if( audioEnabled ) {
	    	AudioManager.explosion.setWorldPosition( position.clone() );
	    	AudioManager.explosion.setVolume( 5 );
			AudioManager.explosion.play();
		}
	}

	private void updateRenderOptimizer() {
		float distance;
		
		playerPosition.set( core.getCharacterPosition( player.id ) );
		playerPosition.setY(0); //Set the Y of the player position to zero

		for( String id : core.getEnemiesId() ) {
			characterPosition.set( core.getCharacterPosition( id ) );
			characterPosition.setY(0); //Set the Y of the character position to zero

			distance = playerPosition.distance( characterPosition );
			
			if ( distance > 150 ) { // Puramente indicativa a scopo di test!!!
				if ( characters.get(id).isEnabled() ) {
					characters.get(id).hideModel();
					characters.get(id).clearDynamics();
					characters.get(id).setEnabled(false);
				}
			} else {
				if ( !characters.get(id).isEnabled() ) {
					characters.get(id).showModel();
					characters.get(id).setEnabled(true);
				}
			}
		}
	}
}
