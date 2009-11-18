package game.graphics;

import game.base.CustomGame;
import game.input.PhysicsInputHandler;
import game.test.testGame;

import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;

import utils.ModelLoader;

import com.jme.image.Texture;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
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

	WorldInterface core;
	
    PhysicsInputHandler physicsInputHandler;
    
    StaticPhysicsNode ground;
    StaticPhysicsNode gameBounds;
    
    HashMap< String, PhysicsEnemy > characters; 
    
    int bulletsCounter;
	HashMap< String, PhysicsBullet > bullets;
    
    PhysicsCharacter player;

    TerrainBlock terrain;

	public GraphicalWorld( WorldInterface core ) {
		setCore( core );
		bulletsCounter = 0;
		characters = new HashMap<String, PhysicsEnemy>();
		bullets = new HashMap<String, PhysicsBullet>();
	}
	
	@Override
    protected void setupInit() {
        ground = getPhysicsSpace().createStaticNode();
        
        rootNode.attachChild(ground);
        
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
    	
    	player.setFirstPerson(false);
    	
    	player.update(tpf);
        physicsInputHandler.update(tpf);
        
        updateEnemies(tpf);
        updateBullets(tpf);
    }
    
	private void updateEnemies( float tpf ) {
		for( String id : characters.keySet() ) {
			characters.get(id).update(tpf);
		}
	}
	
	private void updateBullets( float tpf ) {
		Set<String> ids = bullets.keySet();
		for( String id : ids ) {
			bullets.get(id).update(tpf);
		}
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
	
	    MidPointHeightMap heightMap = new MidPointHeightMap(32, 1);
	    Vector3f terrainScale = new Vector3f(10, .04f, 10);
	    terrain = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale, heightMap.getHeightMap(), Vector3f.ZERO);
	
	    terrain.setDetailTexture(1, 16);
	    ground.attachChild(terrain);
	
	    ground.generatePhysicsGeometry(true);
	
	    ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
	    pt.addTexture(new ImageIcon(testGame.class.getClassLoader().getResource("data/texture/grassb.png")), -128, 0, 128);
	    pt.addTexture(new ImageIcon(testGame.class.getClassLoader().getResource("data/texture/dirt.jpg")), 0, 128, 255);
	    pt.addTexture(new ImageIcon(testGame.class.getClassLoader().getResource("data/texture/highest.jpg")), 128, 255, 384);
	
	    pt.createTexture(512);
	
	    TextureState ts = display.getRenderer().createTextureState();
	    ts.setEnabled(true);
	    Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
	    ts.setTexture(t1, 0);
	
	    Texture t2 = TextureManager.loadTexture(testGame.class
	            .getClassLoader()
	            .getResource("data/texture/Detail.jpg"),
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
	    rootNode.setRenderState(ts);
	
	    FogState fs = display.getRenderer().createFogState();
	    fs.setDensity(0.5f);
	    fs.setEnabled(true);
	    fs.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
	    fs.setEnd(1000);
	    fs.setStart(500);
	    fs.setDensityFunction(FogState.DensityFunction.Linear);
	    fs.setQuality(FogState.Quality.PerVertex);
	    rootNode.setRenderState(fs);
	    
	    createLimits();
	}
	
    /** Function that creates the game limits with a physics box that contains all the world
     */
	public void createLimits() {

	    gameBounds = getPhysicsSpace().createStaticNode();

	    PhysicsBox downBox = gameBounds.createBox("downBox");
	    downBox.setLocalTranslation( 160, 0, 160 );
	    downBox.setLocalScale( new Vector3f( 320, 0.5f, 320) );

	    PhysicsBox upperBox = gameBounds.createBox("upperBox");
	    upperBox.setLocalTranslation( 160, 100, 160 );
	    upperBox.setLocalScale( new Vector3f( 320, 0.5f, 320) );
	    
	    PhysicsBox eastBox = gameBounds.createBox("eastBox");
	    eastBox.setLocalTranslation( 320, 50, 160 );
	    eastBox.setLocalScale( new Vector3f( 100, 0.5f, 320) );
	    eastBox.setLocalRotation( new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Z ));
	    
	    PhysicsBox westBox = gameBounds.createBox("westBox");
	    westBox.setLocalTranslation( 0, 50, 160 );
	    westBox.setLocalScale( new Vector3f( 100, 0.5f, 320) );
	    westBox.setLocalRotation( new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Z ));
	    
	    PhysicsBox southBox = gameBounds.createBox("southBox");
	    southBox.setLocalTranslation( 160, 50, 0 );
	    southBox.setLocalScale( new Vector3f( 100, 0.5f, 320) );
	    southBox.setLocalRotation( new Quaternion(new Quaternion(new float[] {
                0, (float) Math.toRadians(90), (float) Math.toRadians(90) })));

	    PhysicsBox northBox = gameBounds.createBox("northBox");
	    northBox.setLocalTranslation( 160, 50, 320 );
	    northBox.setLocalScale( new Vector3f( 100, 0.5f, 320) );
	    northBox.setLocalRotation( new Quaternion(new Quaternion(new float[] {
                0, (float) Math.toRadians(90), (float) Math.toRadians(90) })));
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
