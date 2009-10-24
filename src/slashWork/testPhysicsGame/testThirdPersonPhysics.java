package slashWork.testPhysicsGame;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.ChaseCamera;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;
import javax.swing.ImageIcon;

import jmetest.input.TestThirdPersonController;
import jmetest.terrain.TestTerrain;
import joework.app.PhysicsGame;

/**
 * Esempio di terreno - giocatore - ostacolo con fisica
 * 
 * @author slash17
 *
 */
public class testThirdPersonPhysics extends PhysicsGame {
	boolean playerOnFloor = false;
    Box floor;
    StaticPhysicsNode staticNode;
    DynamicPhysicsNode player;
    ChaseCamera chaser;
    TerrainPage terrain;
    InputHandler input;
    
	public static void main( String[] args ) {
        testThirdPersonPhysics obj = new testThirdPersonPhysics();
        obj.setConfigShowMode(ConfigShowMode.AlwaysShow);
        obj.start();
    }

    @Override
	protected void simpleUpdate() {
	
	    if ( cam.getLocation().y < terrain.getHeight(cam.getLocation()) + 10 ) {
	        cam.getLocation().y = terrain.getHeight(cam.getLocation()) + 10;
	        cam.update();
	    }
//	
//	    float playerMinHeight = terrain.getHeight(player.getLocalTranslation())+((BoundingBox)player.getWorldBound()).yExtent;
//	    if (!Float.isInfinite(playerMinHeight) && !Float.isNaN(playerMinHeight)) {
//	        player.getLocalTranslation().y = playerMinHeight;
//	    }
	
	    chaser.update(tpf);
	    input.update(tpf);
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
        setupInput();
        setupTerrain();
        setupWall();
        setupPlayer();
        setupChaseCamera();

        pause = true;
    }

    private void setupChaseCamera() {
        Vector3f targetOffset = new Vector3f();
        targetOffset.y = ((BoundingBox) player.getWorldBound()).yExtent * 1.5f;
        chaser = new ChaseCamera(cam, player);
        chaser.setTargetOffset(targetOffset);
    }

    private void setupInput() {
//        HashMap<String, Object> handlerProps = new HashMap<String, Object>();
//        handlerProps.put(ThirdPersonHandler.PROP_DOGRADUAL, "true");
//        handlerProps.put(ThirdPersonHandler.PROP_TURNSPEED, ""+(1.0f * FastMath.PI));
//        handlerProps.put(ThirdPersonHandler.PROP_LOCKBACKWARDS, "false");
//        handlerProps.put(ThirdPersonHandler.PROP_CAMERAALIGNEDMOVE, "true");
//        input = new ThirdPersonHandler(player, cam, handlerProps);
//        input.setActionSpeed(100f);
        /** Create a basic input controller. */
        FirstPersonHandler firstPersonHandler = new FirstPersonHandler( cam, 500,
                100 );
        input = firstPersonHandler;
    }

    public void setupWall() {
        final Box wall = new Box("wall", new Vector3f(), 1000, 100, 1f);
        StaticPhysicsNode wallNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild(wallNode);
        wallNode.attachChild(wall);
        wall.getLocalTranslation().set(new Vector3f(50, terrain.getHeight(50, 50) , 50));
        
        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(
            TextureManager.loadTexture(
            TestThirdPersonController.class.getClassLoader().getResource(
            "data/images/rockwall2.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear));
        wall.setRenderState(ts);
        
        wallNode.generatePhysicsGeometry();
    }
    
    private void setupPlayer() {
    	player = createBox();
        player.setName( "player" );
        player.setModelBound(new BoundingBox());
        player.updateModelBound();
        // per il movimento del box, gli impostiamo un materiale custum, a cui setteremo il surfacemotion
        final Material playerMaterial = new Material( "player material" );
        player.setMaterial( playerMaterial );
        player.setLocalTranslation(20, 200, 20);
        player.updateWorldBound(); // We do this to allow the camera setup access to the world bound in our setup code.

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(
            TextureManager.loadTexture(
            TestThirdPersonController.class.getClassLoader().getResource(
            "data/images/openGame.jpg"),
            Texture.MinificationFilter.BilinearNearestMipMap,
            Texture.MagnificationFilter.Bilinear));
        player.setRenderState(ts);
        player.generatePhysicsGeometry();
        

        // i quattro movimenti Y - G - H - J
        input.addAction( new MoveAction( new Vector3f( -2, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_G, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 2, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_J, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 0, 0, -2 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_Y, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 0, 0, 2 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_H, InputHandler.AXIS_NONE, false );

        // per far saltare il nostro box applichiamo una forza verso l'alto (per il salto premere SPAZIO)
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( playerOnFloor && evt.getTriggerPressed() ) {
                    player.addForce( new Vector3f( 0, 500, 0 ) );
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false );

        // per capire quando il box e' poggiato sul piano, usiamo una variabile booleana playerOnFloor
        // se piano e box hanno colliso il nostro box si trova sul piano, quindi impostiamo playerOnFloor a true

        // evento di collisione
        SyntheticButton playerCollisionEventHandler = player.getCollisionEventHandler();
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == staticNode || contactInfo.getNode2() == staticNode ) {
                    playerOnFloor = true;
                }
            }
        }, playerCollisionEventHandler, false );

        // ad ogni step fisico reimpostiamo a false playerOnFloor
        // in modo da non permettere salti quando si � gi� in aria
        getPhysicsSpace().addToUpdateCallbacks( new PhysicsUpdateCallback() {
            public void beforeStep( PhysicsSpace space, float time ) {
                playerOnFloor = false;
            }
            public void afterStep( PhysicsSpace space, float time ) {
            }
        } );

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

        lightState.detachAll();
        lightState.attach(dr);

        FaultFractalHeightMap heightMap = new FaultFractalHeightMap(513, 10, 0, 127, 0.75f);
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
    
    private DynamicPhysicsNode createBox() {
        DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( dynamicNode );
        final Box visualFallingBox = new Box( "falling box", new Vector3f(), 0.5f, 0.5f, 0.5f );
        dynamicNode.attachChild( visualFallingBox );
        dynamicNode.generatePhysicsGeometry();
        return dynamicNode;
    }

    /**
     * Classe per gestire i movimenti
     */
    private class MoveAction extends InputAction {
        private Vector3f direction;

        public MoveAction( Vector3f direction ) {
            this.direction = direction;
        }
        public void performAction( InputActionEvent evt ) {
            if ( evt.getTriggerPressed() ) {
                // quando viene premuto un testo, impostiamo il movimento nella direzione passata al costruttore
                player.getMaterial().setSurfaceMotion( direction );
            } else {
                // appena rilasciamo il tasto il movimento si azzera...
                player.getMaterial().setSurfaceMotion( Vector3f.ZERO );
            }
        }
    }
}
