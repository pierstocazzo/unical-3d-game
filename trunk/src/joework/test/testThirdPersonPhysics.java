/**
 * Some test of thirdPersonInputHandler
 */

package joework.test;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import jmetest.terrain.TestTerrain;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.InputHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;
import jmetest.input.TestThirdPersonController;
import joework.app.PhysicsGame;
import joework.input.thirdpersonphysics.ThirdPersonPhysicsHandler;

/**
 * <code>testThirdPersonPhysics</code>
 *
 * @author Joshua Slack
 * @version $Revision: 4130 $
 */
public class testThirdPersonPhysics extends PhysicsGame {
    private static final Logger logger = Logger.getLogger(testThirdPersonPhysics.class.getName());

    private StaticPhysicsNode staticNode;
    private DynamicPhysicsNode m_character;

    private ChaseCamera chaser;
    private TerrainPage terrain;
    private InputHandler input;

    /**
     * Entry point for the test,
     *
     * @param args
     */
    public static void main(String[] args) {
        testThirdPersonPhysics app = new testThirdPersonPhysics();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    /**
     * builds the scene.
     *
     * @see com.jme.app.SimpleGame#initGame()
     */
    protected void simpleInitGame() {
        display.setTitle("jME - 3rd person controller test");

        setupCharacter();
        setupTerrain();
        setupChaseCamera();
        setupInput();
    }

    @Override
    protected void simpleUpdate() {
        input.update(tpf);
        chaser.update(tpf);
        float camMinHeight = terrain.getHeight(cam.getLocation()) + 2f;
        if (!Float.isInfinite(camMinHeight) && !Float.isNaN(camMinHeight)
                && cam.getLocation().y <= camMinHeight) {
            cam.getLocation().y = camMinHeight;
            cam.update();
        }
    }

    private void setupCharacter() {
        Box b = new Box("box", new Vector3f(), 5,5,5);
        b.setModelBound(new BoundingBox());
        b.updateModelBound();
        m_character = getPhysicsSpace().createDynamicNode();
        m_character.attachChild(b);
        m_character.generatePhysicsGeometry();
        m_character.setMaterial(Material.IRON);
        m_character.setMass(100);
        m_character.computeMass();
        m_character.updateWorldBound();
        m_character.setLocalTranslation(5, 180, 5);

        rootNode.attachChild(m_character);

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(
            TextureManager.loadTexture(
            testThirdPersonPhysics.class.getClassLoader().getResource(
            "jmetest/data/images/Monkey.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear));
        m_character.setRenderState(ts);
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

        FaultFractalHeightMap heightMap = new FaultFractalHeightMap(257, 32, 0, 255, 0.75f);
        Vector3f terrainScale = new Vector3f(10, 1, 10);
        heightMap.setHeightScale(0.001f);
        terrain = new TerrainPage("Terrain", 33, heightMap.getSize(), terrainScale, heightMap.getHeightMap());

        terrain.setDetailTexture(1, 16);
        staticNode.attachChild(terrain);

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

    private void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = ((BoundingBox) m_character.getWorldBound()).yExtent * 1.5f;
        chaser = new ChaseCamera(cam, m_character);
        chaser.setTargetOffset(targetOffset);
    }

    private void setupInput() {
        HashMap<String, Object> handlerProps = new HashMap<String, Object>();
        handlerProps.put(ThirdPersonPhysicsHandler.PROP_DOGRADUAL, "true");
        handlerProps.put(ThirdPersonPhysicsHandler.PROP_TURNSPEED, ""+(1.0f * FastMath.PI));
        input = new ThirdPersonPhysicsHandler(m_character, cam, handlerProps);
        input.setActionSpeed(100f);
    }
}