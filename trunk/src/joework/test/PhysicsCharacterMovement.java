/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.test;

import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Box;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;
import javax.swing.ImageIcon;
import jmetest.input.TestThirdPersonController;
import jmetest.terrain.TestTerrain;
import joework.app.AssHoleBaseGame;

/**
 *
 * @author joseph
 */
public class PhysicsCharacterMovement extends AssHoleBaseGame {

    StaticPhysicsNode staticNode;
    DynamicPhysicsNode dynamicNode, body, feet;

    //TerrainPage terrain;
    TerrainBlock terrain;

    public static void main( String[] args ) {
        PhysicsCharacterMovement m = new PhysicsCharacterMovement();
        m.setConfigShowMode(ConfigShowMode.AlwaysShow);
        m.start();
    }

    @Override
    protected void setupInit() {
        // Creo lo spazio fisico statico
        staticNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( staticNode );

        // Creo lo spazio fisico dinamico
        dynamicNode = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild(dynamicNode);

        //showPhysics = true;
        pause = true;
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


        //FaultFractalHeightMap heightMap = new FaultFractalHeightMap(65, 1, 1, 1, 0.2f);
        MidPointHeightMap heightMap = new MidPointHeightMap(32, 1);
        Vector3f terrainScale = new Vector3f(10, .04f, 10);
        //heightMap.setHeightScale(0.25f);
        //terrain = new TerrainPage("Terrain", 33, heightMap.getSize(), terrainScale, heightMap.getHeightMap());
        terrain = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale, heightMap.getHeightMap(), Vector3f.ZERO);
        
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

    @Override
    protected void setupCharacters() {
        // todo
    }

    @Override
    protected void setupPlayer() {
        /**
         * Creo il nuovo materiale per il personaggio:
         * Informazioni sul contatto e sulle proprietà principali
         * del materiale
         */
        MutableContactInfo contactMaterial = new MutableContactInfo();
        contactMaterial.setBounce(0);
        contactMaterial.setMu(1);
        contactMaterial.setDampingCoefficient(10);

        Material characterMaterial = new Material("materiale_personaggio");
        characterMaterial.setDensity(1f);
        characterMaterial.putContactHandlingDetails(characterMaterial, contactMaterial);

        /**
         * Creo la geometria modello del personaggio
         */
        Box player_geometry = new Box("player geometry", new Vector3f(-1,-2,-1), new Vector3f(1,2,1));
        player_geometry.setRandomColors();
        dynamicNode.attachChild(player_geometry);
        dynamicNode.generatePhysicsGeometry();
        dynamicNode.setLocalTranslation(160, 100, 160);


        /**
         * Recupero le informazioni per costruire
         * la geometria del cilindro che conterrà il modello
         */
        float cRadius = player_geometry.xExtent >= player_geometry.zExtent ? player_geometry.xExtent : player_geometry.zExtent ;
        float cHeight = player_geometry.yExtent * 2f;

        /**
         * Creo la sfera fisica che starà sul terreno
         */
        feet = getPhysicsSpace().createDynamicNode();

        PhysicsSphere feetGeom = feet.createSphere("feet");
        feetGeom.setLocalScale(cRadius);

        feet.setMaterial(characterMaterial);
        feet.computeMass();

        //player_geometry.setLocalTranslation(0, -cRadius / player_geometry.getLocalScale().y, 0);

    }

    @Override
    protected void setupCamera() {
        cam.setLocation(new Vector3f(160,50,320));
        cam.update();
    }

    @Override
    protected void setupInput() {
        input = new FirstPersonHandler(cam, 100f, 1f);
    }

    @Override
    protected void simpleUpdate() {
        input.update(tpf);
    }

    @Override
    protected void setupChaseCamera() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
