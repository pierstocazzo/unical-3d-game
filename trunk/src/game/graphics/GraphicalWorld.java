package game.graphics;

import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;

import utils.ModelLoader;

import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.light.DirectionalLight;
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
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

import game.base.CustomGame;
import game.input.PhysicsInputHandler;
import game.test.testGame;

public class GraphicalWorld extends CustomGame {

	WorldInterface core;
	
    PhysicsInputHandler myInput;
    StaticPhysicsNode staticNode;
    
    HashMap< String, PhysicsEnemy > characters; 
    
    PhysicsCharacter player;

    ChaseCamera chaser;

    TerrainBlock terrain;

	public GraphicalWorld( WorldInterface core ) {
		setCore( core );
		characters = new HashMap<String, PhysicsEnemy>();
	}
	
	@Override
    protected void setupInit() {
        staticNode = getPhysicsSpace().createStaticNode();
        
        rootNode.attachChild(staticNode);
        
        pause = true;
//        showPhysics = true;
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
        cam.setLocation(new Vector3f(100,30,200));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, 1000);
        cam.update();
    }

    @Override
    protected void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = player.getCharacterBody().getLocalTranslation().y + 5;
        chaser = new ChaseCamera(cam, player.getCharacterBody());
        chaser.setTargetOffset(targetOffset);
    }

    @Override
    protected void setupInput() {
        myInput = new PhysicsInputHandler( player, cam );
    }

    @Override
    protected void update() {
    	player.update( tpf );
        myInput.update(tpf);
        chaser.update(tpf);
        
        updateEnemies(tpf);
    }
	
	private void updateEnemies( float tpf ) {
		for( String id : characters.keySet() ) {
			characters.get(id).update(tpf);
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
	    staticNode.attachChild(terrain);
	
	    staticNode.generatePhysicsGeometry(true);
	
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
	}

	public WorldInterface getCore() {
		return core;
	}

	public void setCore( WorldInterface core ) {
		this.core = core;
	}
	
	public StaticPhysicsNode getGround() {
		return staticNode;
	}
	
}
