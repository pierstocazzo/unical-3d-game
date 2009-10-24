/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package andreaFolder;

import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;


import javax.swing.ImageIcon;

import utils.ModelLoader;
import jmetest.input.TestThirdPersonController;
import jmetest.terrain.TestTerrain;
import joework.app.AssHoleBaseGame;


public class ExamplePhysicMovement extends AssHoleBaseGame {

    StaticPhysicsNode staticNode;
    DynamicPhysicsNode dynamicNode, dynamicMoto;
    boolean motoOnFloor=false;

    //TerrainPage terrain;
    TerrainBlock terrain;

    public static void main( String[] args ) {
        ExamplePhysicMovement m = new ExamplePhysicMovement();
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
         * Informazioni sul contatto e sulle proprietÃ  principali
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
//        Box player_geometry = new Box("player geometry", new Vector3f(-1,-2,-1), new Vector3f(1,2,1));
//        player_geometry.setRandomColors();
//        dynamicNode.attachChild(player_geometry);
//        dynamicNode.generatePhysicsGeometry();
//        dynamicNode.setLocalTranslation(160, 100, 160);


        /**
         * Recupero le informazioni per costruire
         * la geometria del cilindro che conterrÃ  il modello
         */
//        float cRadius = player_geometry.xExtent >= player_geometry.zExtent ? player_geometry.xExtent : player_geometry.zExtent ;
//        float cHeight = player_geometry.yExtent * 2f;

        /**
         * Creo la sfera fisica che starÃ  sul terreno
         */
//        feet = getPhysicsSpace().createDynamicNode();
//
//        PhysicsSphere feetGeom = feet.createSphere("feet");
//        feetGeom.setLocalScale(cRadius);
//
//        feet.setMaterial(characterMaterial);
//        feet.computeMass();

        //player_geometry.setLocalTranslation(0, -cRadius / player_geometry.getLocalScale().y, 0);

        /*URL modelURL= PhysicsCharacterMovement.class.getClassLoader().getResource("data/model/tree1.jme");
		//URL textureURL = Game.class.getClassLoader().getResource("data/texture/tree1.tga");
        
		Node model = null;
        try {
			model = (Node) BinaryImporter.getInstance().load( modelURL );
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//model.setName("plant");
        //model.updateRenderState();
        player.attachChild(model);
        rootNode.attachChild(player);*/
        
        Node model = ModelLoader.loadModel("data/model/bike.3ds", "", 0.025f, new Quaternion());
       
        DynamicPhysicsNode dynamicMoto = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild(dynamicMoto);
        dynamicMoto.attachChild(model);
        dynamicMoto.setLocalTranslation(100, 50, 100);
        dynamicMoto.setMaterial(Material.IRON);
        
        dynamicMoto.generatePhysicsGeometry();
        /** Nota bene: 
         * 
         * 	La computeMass() si può applicare solo se presenta la geometria
         *  altrimenti non può fare calcoli di densità, centro massa etc...
         */
        dynamicMoto.computeMass();
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
        /** 
         * Mi assicuto che la mia moto stia alzata correttamente
         * NB. Da rivedere perche' la moto cade ugualmente
         */
        Vector3f normal = null;
        if(dynamicNode.hasCollision(terrain, true)){
        	motoOnFloor=true;
        }
        else
        	motoOnFloor=false;
        if(motoOnFloor){
//        		System.out.println("OnFloor");
		        terrain.getSurfaceNormal(dynamicMoto.getLocalTranslation().x,
		        						dynamicMoto.getLocalTranslation().z,
		        							normal);
		        if(normal != null) {
		            dynamicMoto.rotateUpTo(normal);
		        }
//        	Vector forza = new Vector(0,30,0);
//        	Quaternion quaternion1 = new Quaternion();
//			quaternion1.fromAngleAxis(FastMath.PI/2, new Vector3f(0, 1, 0));
//			dynamicMoto.setLocalRotation(quaternion1);
			dynamicMoto.addForce(new Vector3f(0,100,0));
        }
//        dynamicMoto.rotateUpTo(new Vector3f(0,1,0));
    }

}
