package slashWork.game.graphics;

import slashWork.game.base.Game;
import slashWork.game.input.PhysicsInputHandler;
import slashWork.game.test.testGame;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;

import jmetest.TutorialGuide.TestPongCool;

import utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.DirectionalLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Pyramid;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

/** Class GraphicalWorld <br>
 * The main graphics class which contains all the graphical objects and characters
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 */
public class GraphicalWorld extends Game {
	
	/** an interface to communicate with the application core */
	WorldInterface core;
	
	/** a custom input handler to control the player */
    PhysicsInputHandler physicsInputHandler;
    
    /** the world's ground */
    StaticPhysicsNode ground;
    
    /** the world's terrain attached to the ground node */
    TerrainBlock terrain;
    
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
	
	/** x and z dimension of the world */
	Vector2f worldDimension;

	/** set to false when you don't want to do the world update */
	boolean enabled = true;
	
	/** basic basic hud */
	Text life;
	Text crosshair;
	Text gameOver;
	Text fps;
	
	/** audio controller */
	AudioSystem audio;
	
	public AudioTrack shoot;
	public AudioTrack explosion;
	public AudioTrack death;
	
	
	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) 
	 * @param x - (int) the x dimension of the world
	 * @param z - (int) the z dimension of the world
	 */
	public GraphicalWorld( WorldInterface core, int x, int z ) {
		setCore( core );
		worldDimension = new Vector2f( x, z );
	}

    public void setupInit() {
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
    	
		crosshair = Text.createDefaultTextLabel( "crosshair" );
		crosshair.setLocalTranslation(new Vector3f(display.getWidth() / 2f - 8f,
				display.getHeight() / 2f - 8f, 0));
		rootNode.attachChild(crosshair);
        
		gameOver = Text.createDefaultTextLabel( "gameOver" );
		rootNode.attachChild(gameOver);
		
		fps = Text.createDefaultTextLabel( "life" );
    	fps.setLocalTranslation( 20, 40, 0 );
    	rootNode.attachChild( fps );
		
        pause = true;
    }
    
    private void initSound() {
		// grab a handle to our audio system.
		audio = AudioSystem.getSystem();

		// setup our ear tracker to track the camera's position and orientation.
		audio.getEar().trackOrientation(cam);
		audio.getEar().trackPosition(cam);

		// setup a music score for our demo
		AudioTrack music1 = getMusic(TestPongCool.class.getResource("/slashWork/game/data/audio/game.ogg"));
		audio.getMusicQueue().setRepeatType(RepeatType.ALL);
		audio.getMusicQueue().setCrossfadeinTime(2.5f);
		audio.getMusicQueue().setCrossfadeoutTime(2.5f);
		audio.getMusicQueue().addTrack(music1);
		audio.getMusicQueue().play();

		shoot = audio.createAudioTrack("/slashWork/game/data/audio/mp5.ogg", false);
		shoot.setRelative(true);
		shoot.setMaxAudibleDistance(100000);
		shoot.setVolume(.7f);
		
		explosion = audio.createAudioTrack("/slashWork/game/data/audio/explosion.ogg", false);
		explosion.setRelative(true);
		explosion.setMaxAudibleDistance(100000);
		explosion.setVolume(4.0f);
		
		death = audio.createAudioTrack("/slashWork/game/data/audio/death.ogg", false);
		death.setRelative(true);
		death.setMaxAudibleDistance(100000);
		death.setVolume(4.0f);
    }
    
	private AudioTrack getMusic(URL resource) {
		// Create a non-streaming, non-looping, relative sound clip.
		AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, true);
		sound.setType(TrackType.MUSIC);
		sound.setRelative(true);
		sound.setTargetVolume(0.7f);
		sound.setLooping(false);
		return sound;
	}
    
    /** Create graphic characters
     *  and place them in positions setted in the logic game
     */
    public void setupEnemies() { 	    	
        for( String id : core.getEnemiesId() ) {
            Node model = ModelLoader.loadModel("data/soldato/soldato.ms3d", "data/soldato/soldato.jpg", 1f, new Quaternion());
            model.setLocalTranslation(0, -2f, 0);   

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
    	
    	Node model = ModelLoader.loadModel("data/soldato/soldato.ms3d", "data/soldato/soldato.jpg", 1f, new Quaternion());
        model.setLocalTranslation(0, -2f, 0);   
        
        for( String id : core.getPlayersId() ) {
        	player = new PhysicsCharacter( id, this, 20, 100, model );
            player.getCharacterNode().getLocalTranslation().set( core.getCharacterPosition(id) );
            rootNode.attachChild( player.getCharacterNode() );
            characters.put( player.id, player );
        }
    }
    
    public void setupCamera() {
        cam.setLocation(new Vector3f(160,30,160));
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
		audio.update();
    	if( core.isAlive( player.id ) == false ) {
    		life.print( "Life: 0" );
    		gameOver();
    	} else {
    		life.print( "Life: " + core.getCharacterLife( player.id ) );
    		
	        physicsInputHandler.update(tpf);
	        updateCharacters(tpf);
	        
	        if( player.isFirstPerson() ) {
	        	crosshair.print( "+" );
	        } else {
	        	crosshair.print( "" );
	        }
	        
	        updateBullets(tpf);
	        updateAmmoPackages(tpf);
	        updateEnergyPackages(tpf);
	        
	        skybox.setLocalTranslation(cam.getLocation());
	        skybox.updateGeometricState( 0, false );
    	}
    	
    	fps.print( "Frame Rate: " + (int) timer.getFrameRate() + "fps" );
    }
    
	private void gameOver() {
		gameOver.print( "Game Over" );
        gameOver.setLocalScale( 3 );
        gameOver.setTextColor( ColorRGBA.red );
		gameOver.setLocalTranslation(new Vector3f(display.getWidth() / 2f - gameOver.getWidth() / 2,
				display.getHeight() / 2f - gameOver.getHeight() / 2, 0));
		
		death.setWorldPosition( cam.getLocation() );
		death.play();
		
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
		// TODO ambientazione
		initSound();
		
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	
	    DirectionalLight dr = new DirectionalLight();
	    dr.setEnabled(true);
	    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    dr.setDirection(new Vector3f(0.5f, -0.5f, 0));
	
	    lightState.detachAll();
	    lightState.attach(dr);
	
	    MidPointHeightMap heightMap = new MidPointHeightMap( 32, 1 );
	    Vector3f terrainScale = new Vector3f( worldDimension.x/32, .04f, worldDimension.y/32 );
	    terrain = new TerrainBlock( "Terrain", heightMap.getSize(), terrainScale, 
	    		heightMap.getHeightMap(), new Vector3f( 0, 0, 0 ) );
	
	    terrain.setDetailTexture(1, 16);
	    ground.attachChild(terrain);
	
	    ground.generatePhysicsGeometry(true);
	
	    ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
	    pt.addTexture(new ImageIcon(testGame.class.getClassLoader().getResource(
	    		"game/data/texture/grassb.png" ) ), -128, 0, 200 );
	    pt.addTexture(new ImageIcon(testGame.class.getClassLoader().getResource(
	    		"game/data/texture/dirt.jpg" ) ), 50, 200, 384 );
//	    pt.addTexture(new ImageIcon(testGame.class.getClassLoader().getResource(
//	    		"game/data/texture/highest.jpg" ) ), 128, 255, 384 );
	
	    pt.createTexture(512);
	
	    TextureState ts = display.getRenderer().createTextureState();
	    ts.setEnabled(true);
	    Texture t1 = TextureManager.loadTexture( pt.getImageIcon().getImage(), 
	    		Texture.MinificationFilter.Trilinear,
	    		Texture.MagnificationFilter.Bilinear, true);
	    ts.setTexture(t1, 0);
	
	    Texture t2 = TextureManager.loadTexture(testGame.class
	            .getClassLoader()
	            .getResource("game/data/texture/Detail.jpg"),
	            Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
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
	    ground.setRenderState(ts);
	
        TextureState treeTex = display.getRenderer().createTextureState();
        treeTex.setEnabled(true);
        Texture tr = TextureManager.loadTexture(
                GraphicalWorld.class.getClassLoader().getResource(
                        "jmetest/data/texture/grass.jpg"), Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        treeTex.setTexture(tr);

//        Node p = ModelLoader.loadModel( "game/data/Tree1.3ds", "", 0.2f, 
//        		new Quaternion().fromAngleAxis( FastMath.PI * 3/2, Vector3f.UNIT_X));
        Pyramid p = new Pyramid("p", 10, 20);
        p.setModelBound(new BoundingBox());
        p.updateModelBound();
        p.setRenderState(treeTex);
        p.setTextureCombineMode(TextureCombineMode.Replace);
        
        for (int i = 0; i < 100; i++) {
        	Spatial s1 = new SharedMesh("tree"+i, p);
            float x = (float) Math.random() * 128 * 5;
            float z = (float) Math.random() * 128 * 5;
            s1.setLocalTranslation(new Vector3f(x, terrain.getHeight(x, z), z));
            rootNode.attachChild(s1);
        }
	    
	    
	    FogState fs = display.getRenderer().createFogState();
	    fs.setDensity(0.5f);
	    fs.setEnabled(true);
	    fs.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
	    fs.setEnd(1000);
	    fs.setStart(500);
	    fs.setDensityFunction(FogState.DensityFunction.Linear);
	    fs.setQuality(FogState.Quality.PerVertex);
	    rootNode.setRenderState(fs);
	    
	    createVegetation();
	    createWorldBounds();
	    buildSkyBox();
	    setupEnergyPackages();
	}
	
	private void createVegetation() {
//		appesantisce un casino...anche un solo albero...bah!
//		Node tree = ModelLoader.loadModel( "slashWork/game/data/Tree1.jme", "", .2f, 
//				new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Z ) );
//		
//		tree.setLocalTranslation( 130, 10, 130 );
//		
//		rootNode.attachChild( tree );
	}

	private void setupEnergyPackages() {
		HashMap< String, Vector3f > hash = core.getEnergyPackagesPosition();
		for( String id : hash.keySet() ) {
			PhysicsEnergyPackage e = new PhysicsEnergyPackage( id, this, hash.get(id) );
			energyPackages.put( e.id, e );
		}
	}

	private void buildSkyBox() {
        skybox = new Skybox("skybox", 10, 10, 10);
 
        Texture north = TextureManager.loadTexture(
            GraphicalWorld.class.getClassLoader().getResource(
            "game/data/texture/north.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(
        	GraphicalWorld.class.getClassLoader().getResource(
            "game/data/texture/south.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(
        	GraphicalWorld.class.getClassLoader().getResource(
            "game/data/texture/east.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(
        	GraphicalWorld.class.getClassLoader().getResource(
            "game/data/texture/west.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(
        	GraphicalWorld.class.getClassLoader().getResource(
            "game/data/texture/top.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(
        	GraphicalWorld.class.getClassLoader().getResource(
            "game/data/texture/bottom.jpg"),
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
        
        rootNode.attachChild(skybox);
    }
	
    /** Function that creates the game bounds with a physics box that contains all the world
     */
	public void createWorldBounds() {

		float x = worldDimension.x;
		float z = worldDimension.y;
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

	public void setCore( WorldInterface core ) {
		this.core = core;
	}
	
	public StaticPhysicsNode getGround() {
		return ground;
	}
	
//	private void initSound() {
//		// grab a handle to our audio system.
//		audio = AudioSystem.getSystem();
//
//		// setup our ear tracker to track the camera's position and orientation.
//		audio.getEar().trackOrientation(cam);
//		audio.getEar().trackPosition(cam);
//
//		// setup a music score for our demo
//		AudioTrack music1 = getMusic(TestPongCool.class.getResource("/jmetest/data/sound/test.ogg"));
//		audio.getMusicQueue().setRepeatType(RepeatType.ALL);
//		audio.getMusicQueue().setCrossfadeinTime(2.5f);
//		audio.getMusicQueue().setCrossfadeoutTime(2.5f);
//		audio.getMusicQueue().addTrack(music1);
//		audio.getMusicQueue().play();
//
//		shootSound = audio.createAudioTrack("/jmetest/data/sound/explosion.ogg", false);
//		shootSound.setRelative(true);
//		shootSound.setMaxAudibleDistance(100000);
//		shootSound.setVolume(1.0f);
//	}
//
//	private AudioTrack getMusic(URL resource) {
//		// Create a non-streaming, non-looping, relative sound clip.
//		AudioTrack sound = AudioSystem.getSystem().createAudioTrack(resource, true);
//		sound.setType(TrackType.MUSIC);
//		sound.setRelative(true);
//		sound.setTargetVolume(0.7f);
//		sound.setLooping(false);
//		return sound;
//	}
}
