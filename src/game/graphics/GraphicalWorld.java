package game.graphics;

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
    Node enemies;

    PhysicsCharacter player;
    PhysicsEnemy enemy;

    ChaseCamera chaser;

    TerrainBlock terrain;

	public GraphicalWorld( WorldInterface core ) {
		setCore( core );
	}
	
	@Override
    protected void setupInit() {
        staticNode = getPhysicsSpace().createStaticNode();
        enemies = new Node("enemies");
        
        rootNode.attachChild(staticNode);
        rootNode.attachChild(enemies);
        
        pause = true;
//        showPhysics = true;
    }

    @Override
    protected void setupEnemies() { 	    	
    	Set<String> ids = core.getEnemiesId();
    	
//    	HashMap< String, Vector3f > positions = core.getEnemiesPosition();
    	
    	Node model = ModelLoader.loadModel("data/model/Soldato/Soldato.obj", "", 0.2f, new Quaternion());
    	model.setLocalTranslation(0, -1.5f, 0);
    	
        for( String id : ids ) {
        	enemy = new PhysicsEnemy( id, this, Vector3f.UNIT_X, 40, 100,  model );
                enemy.getCharacterNode().setLocalTranslation( core.getCharacterInitialPosition(id) );
                rootNode.attachChild( enemy.getCharacterNode() );
//    		Thread t = new Thread( enemy );
//    		t.start();
        }
    }

    @Override
    protected void setupPlayer() {
    	/** create graphic player */
        Node model = ModelLoader.loadModel("data/model/Soldato/Soldato.obj", "", 0.2f, new Quaternion());
         
        model.setLocalTranslation(0, -1.5f, 0);   
        
    	//Set<String> ids = core.getPlayersId();
    	
        //for( String id : ids ) {
        	player = new PhysicsCharacter( "player1", this, Vector3f.UNIT_X, 1000, 100, model );
//            rootNode.attachChild( player.model );
        //}

        player.getCharacterNode().setLocalTranslation(160, 30, 160);

        rootNode.attachChild( player.getCharacterNode() );
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
    protected void simpleUpdate() {
        
    	
    	player.update( tpf );
        myInput.update(tpf);
        chaser.update(tpf);
        
        enemy.update(tpf);
        
//        debug print
//        System.out.println( core.printWorld() );
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
