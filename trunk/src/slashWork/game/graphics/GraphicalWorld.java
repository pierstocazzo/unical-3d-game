package slashWork.game.graphics;

import slashWork.game.base.CustomGame;
import slashWork.game.input.PhysicsInputHandler;
import slashWork.game.test.testGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import jmetest.renderer.TestSkybox;
import jmetest.terrain.TestTerrainTrees;

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
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class GraphicalWorld extends CustomGame {

	static final Logger logger = Logger.getLogger(ModelLoader.class.getName());
	
	WorldInterface core;
	
    PhysicsInputHandler physicsInputHandler;
    
    StaticPhysicsNode ground;
    StaticPhysicsNode gameBounds;
    
    HashMap< String, PhysicsCharacter > characters; 
    
    int bulletsCounter = 0;
	HashMap< String, PhysicsBullet > bullets;
    
    PhysicsCharacter player;

    TerrainBlock terrain;

	private Skybox skybox;
	
	Vector2f worldDimension;
	

	public GraphicalWorld( WorldInterface core, int x, int y ) {
		setCore( core );
		characters = new HashMap<String, PhysicsCharacter>();
		bullets = new HashMap<String, PhysicsBullet>();
		worldDimension = new Vector2f( x, y );
	}
	
	@Override
    protected void setupInit() {
        ground = getPhysicsSpace().createStaticNode();
        gameBounds = getPhysicsSpace().createStaticNode();
        
        rootNode.attachChild(ground);
        rootNode.attachChild(gameBounds);
        
        pause = true;
    }

    /** Create graphic characters
     *  and place them in positions setted in the logic game
     */
    @Override
    protected void setupEnemies() { 	    	
    	Set<String> ids = core.getEnemiesId();
        
        for( String id : ids ) {
        	
            Node model = ModelLoader.loadModel("game/data/models/dwarf/dwarf1.ms3d", "", 0.1f, new Quaternion());
            model.setLocalTranslation(0, -1.7f, 0);   
        	
    		Texture texture = TextureManager.loadTexture(
                    ModelLoader.class.getClassLoader().getResource( "game/data/models/dwarf/dwarf.jpg" ),
                    Texture.MinificationFilter.Trilinear,
                    Texture.MagnificationFilter.Bilinear);
            TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
            ts.setEnabled(true);
            ts.setTexture(texture);
    		model.setRenderState( ts );
            
    		texture = TextureManager.loadTexture(
                    ModelLoader.class.getClassLoader().getResource( "game/data/models/dwarf/axe.jpg" ),
                    Texture.MinificationFilter.Trilinear,
                    Texture.MagnificationFilter.Bilinear);
            TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
            ts1.setEnabled(true);
            ts1.setTexture(texture);
    		model.getChild("axe").setRenderState( ts1 );

            PhysicsEnemy enemy = new PhysicsEnemy( id, this, 40, 100,  model );
        	enemy.getCharacterNode().getLocalTranslation().set( core.getCharacterPosition(id) );
        	characters.put( id, enemy );
        	rootNode.attachChild( characters.get(id).getCharacterNode() );
        }
    }
    
    /** Create graphic players
     *  and place them in positions setted in the logic game
     */
    @Override
    protected void setupPlayer() {
    	
        Node model = ModelLoader.loadModel("game/data/models/dwarf/dwarf2.ms3d", "", 0.1f, new Quaternion());
        model.setLocalTranslation(0, -1.7f, 0);   
        
		Texture texture = TextureManager.loadTexture(
                ModelLoader.class.getClassLoader().getResource( "game/data/models/dwarf/dwarf2.jpg" ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(texture);
		model.setRenderState( ts );
        
		texture = TextureManager.loadTexture(
                ModelLoader.class.getClassLoader().getResource( "game/data/models/dwarf/axe.jpg" ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts1.setEnabled(true);
        ts1.setTexture(texture);
		model.getChild("axe").setRenderState( ts1 );
        
    	Set<String> ids = core.getPlayersId();
    	
        for( String id : ids ) {
        	player = new PhysicsCharacter( id, this, 1000, 100, model );
            player.getCharacterNode().getLocalTranslation().set( core.getCharacterPosition(id) );
            rootNode.attachChild( player.getCharacterNode() );
            characters.put( player.id, player );
        }
    }

    @Override
    protected void setupCamera() {
        cam.setLocation(new Vector3f(160,30,160));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, 1000);
        cam.update();
    }

    @Override
    protected void setupInput() {
        physicsInputHandler = new PhysicsInputHandler( player, cam );
    }

    @Override
    protected void update() {
    	updateCharacters(tpf);
        physicsInputHandler.update(tpf);
        
        updateBullets(tpf);
        
        skybox.setLocalTranslation(cam.getLocation());
        skybox.updateGeometricState(0, false);
    }
    
	/** Function updateCharacters<br>
	 * Call the update method of each character contained in the characters hashMap
	 */
	private void updateCharacters( float time ) {
		for( String id : characters.keySet() ) {
			characters.get(id).update( time );
		}
		
		updateCharactersHashMap();
	}
	
	/** Function updateBullets <br>
	 * Call the update method of each bullet contained in the bullets hashMap
	 */
	private void updateBullets( float time ) {
		Set<String> ids = bullets.keySet();
		for( String id : ids ) {
			bullets.get(id).update( time );
		}
		
		updateBulletsHashMap();
	}

	/** Function updateBulletsHashMap
	 * Update the bullets hashMap removing the detached bullets
	 */
	private void updateBulletsHashMap() {
		Collection<PhysicsBullet> bulletsCollection = new LinkedList<PhysicsBullet>();
		bulletsCollection.addAll( bullets.values() );
		for( PhysicsBullet bullet : bulletsCollection ) {
			if( !bullet.isActive() ) {
				bullets.remove( bullet.id );
				logger.info( bullet.id + " removed");
			}
		}
	}
	
	/** Function updateCharactersHashMap
	 * Update the characters hashMap removing the dead characters
	 */
	private void updateCharactersHashMap() {
		Collection<PhysicsCharacter> charactersCollection = new LinkedList<PhysicsCharacter>();
		charactersCollection.addAll( characters.values() );
		for( PhysicsCharacter character : charactersCollection ) {
			if( !character.isActive() ) {
				characters.remove( character.id );
				logger.info( character.id + " removed");
			}
		}
	}
	
	/**
	 * paints a crosshair
	 */
	private void paintCrossHair()
	{
		/** Create a + for the middle of the screen */
		Text cross = new Text("Crosshairs", "+");
		cross.setTextColor(ColorRGBA.white);
		// 8 is half the width of a font char
		/** Move the + to the middle */
		cross.setLocalTranslation(new Vector3f(display.getWidth() / 2f - 8f,
				display.getHeight() / 2f - 8f, 0));
		rootNode.attachChild(cross);
	}
	
	@Override
	protected void setupEnvironment() {
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
	
	    DirectionalLight dr = new DirectionalLight();
	    dr.setEnabled(true);
	    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    dr.setDirection(new Vector3f(0.5f, -0.5f, 0));
	
	    lightState.detachAll();
	    lightState.attach(dr);
	
	    MidPointHeightMap heightMap = new MidPointHeightMap( 32, 1 );
	    Vector3f terrainScale = new Vector3f( worldDimension.x/32, .004f, worldDimension.y/32 );
	    terrain = new TerrainBlock( "Terrain", heightMap.getSize(), terrainScale, 
	    		heightMap.getHeightMap(), Vector3f.ZERO );
	
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
                TestTerrainTrees.class.getClassLoader().getResource(
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
        
        for (int i = 0; i < 10; i++) {
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
	    
	    createWorldBounds();
	    paintCrossHair();
	    buildSkyBox();
	}
	
	private void buildSkyBox() {
        skybox = new Skybox("skybox", 10, 10, 10);
 
        Texture north = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "game/data/texture/north.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "game/data/texture/south.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "game/data/texture/east.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "game/data/texture/west.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
            "game/data/texture/top.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(
            TestSkybox.class.getClassLoader().getResource(
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
	
}
