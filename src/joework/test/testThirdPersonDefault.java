/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.test;


import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.input.ThirdPersonHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;
import java.util.HashMap;
import javax.swing.ImageIcon;

import utils.ModelLoader;
import jmetest.input.TestThirdPersonController;
import jmetest.terrain.TestTerrain;
import joework.app.PhysicsGame;

/**
 *
 * @author joseph
 */
public class testThirdPersonDefault extends PhysicsGame {

    Box floor;
    StaticPhysicsNode staticNode;
    DynamicPhysicsNode player;
//    Node player;
    ChaseCamera chaser;
    TerrainPage terrain;
    InputHandler input;
	public static void main( String[] args ) {
        testThirdPersonDefault obj = new testThirdPersonDefault();
        obj.setConfigShowMode(ConfigShowMode.AlwaysShow);
        obj.start();
    }

    @Override
    protected void simpleInitGame() {
        // ZBufferState
        ZBufferState zbuf = display.getRenderer().createZBufferState();
        zbuf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        zbuf.setEnabled(true);
        rootNode.setRenderState(zbuf);

        // CullState
        CullState cs = display.getRenderer().createCullState();
        cs.setEnabled(true);
        cs.setCullFace(CullState.Face.Back);
        rootNode.setRenderState(cs);

        // Creazione del gioco
        
        setupTerrain();
        setupWall();
        setupPlayer();
        setupChaseCamera();
        setupInput();

        pause = true;
    }

    private void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = ((BoundingBox) player.getWorldBound()).yExtent * 1.5f;
        chaser = new ChaseCamera(cam, player);
        chaser.setTargetOffset(targetOffset);
    }

    private void setupInput() {
        HashMap<String, Object> handlerProps = new HashMap<String, Object>();
        handlerProps.put(ThirdPersonHandler.PROP_DOGRADUAL, "true");
        handlerProps.put(ThirdPersonHandler.PROP_TURNSPEED, ""+(1.0f * FastMath.PI));
        handlerProps.put(ThirdPersonHandler.PROP_LOCKBACKWARDS, "false");
        handlerProps.put(ThirdPersonHandler.PROP_CAMERAALIGNEDMOVE, "true");
        input = new ThirdPersonHandler(player, cam, handlerProps);
        input.setActionSpeed(100f);
    }

    public void setupWall() {
        final Box wall = new Box("wall", new Vector3f(), 1000, 100, 1f);
        StaticPhysicsNode wallNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild(wallNode);
        wallNode.attachChild(wall);
        wall.getLocalTranslation().set(new Vector3f(50, terrain.getHeight(50, 50) , 50));
        wallNode.generatePhysicsGeometry();
    }
    
    @Override
    protected void simpleUpdate() {

        if ( cam.getLocation().y < terrain.getHeight(cam.getLocation()) + 10 ) {
            cam.getLocation().y = terrain.getHeight(cam.getLocation()) + 10;
            cam.update();
        }

        float playerMinHeight = terrain.getHeight(player.getLocalTranslation())+((BoundingBox)player.getWorldBound()).yExtent;
        if (!Float.isInfinite(playerMinHeight) && !Float.isNaN(playerMinHeight)) {
            player.getLocalTranslation().y = playerMinHeight;
        }

        chaser.update(tpf);
        input.update(tpf);
    }

    private void setupPlayer() {
        Box b = new Box("box", Vector3f.ZERO, 1,2,1);
        b.setModelBound(new BoundingBox());
        b.updateModelBound();
        

/* 		se il player collide anche leggermente con il terreno o con 
 * 		il muro che ho creato, inizia a rimbalzare di qua e di la
 * 		dobbiamo discuterne...
 */
    	Node model = ModelLoader.loadModel("data/model/drfreak.md2", "data/model/drfreak.jpg", 1f, 
    				new Quaternion().fromAngleAxis(FastMath.PI/2, new Vector3f(0,-1,0)));

        player = getPhysicsSpace().createDynamicNode(); 

//		disabilitiamo la gravità sul player altrimenti non si trova con l'update
        player.setAffectedByGravity(false);      
        
        player.setLocalTranslation(20, 200, 20);
        rootNode.attachChild(player);
//        player.attachChild(b);
        player.attachChild(model);
        player.updateWorldBound(); // We do this to allow the camera setup access to the world bound in our setup code.

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(
            TextureManager.loadTexture(
            TestThirdPersonController.class.getClassLoader().getResource(
            "data/model/drfreak.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear));
        player.setRenderState(ts);
        player.generatePhysicsGeometry();
    }

    private void setupTerrain() {
        rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
        display.getRenderer().setBackgroundColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));

        staticNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( staticNode );

        DirectionalLight dr = new DirectionalLight();
        dr.setEnabled(true);
        dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        dr.setDirection(new Vector3f(0.5f, -0.5f, 0));

        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        rootNode.setRenderState(cs);

        lightState.detachAll();
        lightState.attach(dr);

        FaultFractalHeightMap heightMap = new FaultFractalHeightMap(513, 10, 0, 127, 0.75f);
        Vector3f terrainScale = new Vector3f(10, 1, 10);
        heightMap.setHeightScale(0.001f);
        terrain = new TerrainPage("Terrain", 33, heightMap.getSize(), terrainScale, heightMap.getHeightMap());

        terrain.setDetailTexture(1, 16);
        
/* *************************** ecco la parte modificata ***************************/
//        staticNode.attachChild(terrain);
        rootNode.attachChild(terrain);

        staticNode.generatePhysicsGeometry(true);

        ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
        pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader().getResource("data/texture/grassb.png")), -128, 0, 128);
        pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader().getResource("data/texture/dirt.jpg")), 0, 128, 255);
        pt.addTexture(new ImageIcon(TestTerrain.class.getClassLoader().getResource("data/texture/highest.jpg")), 128, 255, 384);

        pt.createTexture(512);

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
        ts.setTexture(t1, 0);

        Texture t2 = TextureManager.loadTexture(TestThirdPersonController.class
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
        terrain.setRenderState(ts);

        FogState fs = display.getRenderer().createFogState();
        fs.setDensity(0.5f);
        fs.setEnabled(true);
        fs.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
        fs.setEnd(1000);
        fs.setStart(500);
        fs.setDensityFunction(FogState.DensityFunction.Linear);
        fs.setQuality(FogState.Quality.PerVertex);
        terrain.setRenderState(fs);
    }
}
