package game.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import utils.Loader;
import utils.ModelLoader;
import utils.Util;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.LightNode;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.PassNode;
import com.jme.scene.PassNodeState;
import com.jme.scene.SharedNode;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.LensFlare;
import com.jmex.effects.water.WaterRenderPass;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.RawHeightMap;

public class Environment {

	/** the animated water */
    WaterRenderPass waterEffectRenderPass;
    Quad waterQuad;
    
    /** the terrain */
    TerrainPage terrain;
    Spatial splatTerrain;
    
    /** the same terrain reflected into the water */
    Spatial reflectionTerrain;
    
    /** environment parameters */
    float farPlane = 10000.0f;
    float textureScale = 0.008f;
    float globalSplatScale = 30.0f;
	int heightMapSize = 129;
	int terrainScale = 32;
	
	/** the sun effect */
	LensFlare flare;

	/** the light */
	LightNode lightNode;
	
	/** the sky */
	Skybox skybox;

	/** the graphicalWorld */
    GraphicalWorld world;
    
    /** the physics ground of the world */
	StaticPhysicsNode ground;
	
    /** the game's bounds, a physics box that contains the scene */
    StaticPhysicsNode gameBounds;
    
    
    /** Class Environment constructor <br>
     * Create the environment for the game
     * 
     * @param world - the graphical world
     */
	public Environment( GraphicalWorld world ) {
		this.world = world;
		init();
	}
	
	private void init() {
		world.dimension = heightMapSize * terrainScale;
		
//	    DirectionalLight dr = new DirectionalLight();
//	    dr.setEnabled(true);
//	    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
//	    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
//	    dr.setDirection(new Vector3f(0.5f, -0.5f, 0));
//	
//	    lightState.detachAll();
//	    lightState.attach(dr);
	    
        ground = world.getPhysicsSpace().createStaticNode();
        gameBounds = world.getPhysicsSpace().createStaticNode();
        world.getRootNode().attachChild(ground);
        world.getRootNode().attachChild(gameBounds);
		world.loadingFrame.setProgress(25);
//	    setuplight();
		
	    CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
	    cs.setCullFace(CullState.Face.Back);
	    world.getRootNode().setRenderState(cs);

//	    lightState.detachAll();
//	    world.getRootNode().setLightCombineMode(Spatial.LightCombineMode.Off);
	    world.loadingFrame.setProgress(30);
	    FogState fogState = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
	    fogState.setDensity(1.0f);
	    fogState.setEnabled(true);
	    fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    fogState.setEnd(farPlane/2);
	    fogState.setStart(farPlane / 20.0f);
	    fogState.setDensityFunction(FogState.DensityFunction.Linear);
	    fogState.setQuality(FogState.Quality.PerVertex);
	    world.getRootNode().setRenderState(fogState);
	    world.loadingFrame.setProgress(35);
		createTerrain();
		world.loadingFrame.setProgress(40);
        createReflectionTerrain();

        buildSkyBox();
        world.loadingFrame.setProgress(45);
        world.getRootNode().attachChild( skybox );
        world.getRootNode().attachChild( splatTerrain );
        
        createWater();
        world.loadingFrame.setProgress(50);
	    RenderPass rootPass = new RenderPass();
	    rootPass.add(world.getRootNode());
	    world.getPass().add(rootPass);

	    world.getRootNode().setCullHint(Spatial.CullHint.Never);
	    world.getRootNode().setCullHint(Spatial.CullHint.Never);
		
	    createVegetation();
	    world.loadingFrame.setProgress(55);
	    createWorldBounds();
	    
	    KeyBindingManager.getKeyBindingManager().set( "take_position", KeyInput.KEY_END );
	}
	
	public void update() {
		skybox.getLocalTranslation().set( world.getCam().getLocation() );
        skybox.updateGeometricState(0.0f, true);

        /******** Added to animate the water ********/
        Vector3f transVec = new Vector3f( world.getCam().getLocation().x,
                waterEffectRenderPass.getWaterHeight(), world.getCam().getLocation().z);
        setTextureCoords(0, transVec.x, -transVec.z, textureScale);
        setVertexCoords(transVec.x, transVec.y, transVec.z);
        
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("take_position", false ) ) {
        	System.out.println( world.getCam().getLocation() );
        }
	}
	
	/*private void setuplight() {
		world.getLight().detachAll();

        PointLight dr = new PointLight();
        dr.setEnabled(true);
        dr.setDiffuse(ColorRGBA.white.clone());
        dr.setAmbient(ColorRGBA.gray.clone());
        dr.setLocation(new Vector3f(0f, 0f, 0f));

        world.getLight().attach(dr);
        world.getLight().setTwoSidedLighting(true);

        lightNode = new LightNode("light");
        lightNode.setLight(dr);

        Vector3f min2 = new Vector3f(-0.5f, -0.5f, -0.5f);
        Vector3f max2 = new Vector3f(0.5f, 0.5f, 0.5f);
        Box lightBox = new Box("box", min2, max2);
        lightBox.setModelBound(new BoundingBox());
        lightBox.updateModelBound();
        lightNode.attachChild(lightBox);
        lightNode.setLocalTranslation(new Vector3f(-2048, 500,-1800));

        // clear the lights from this lightbox so the lightbox itself doesn't
        // get affected by light:
        lightBox.setLightCombineMode(LightCombineMode.Off);

        // Setup the lensflare textures.
        TextureState[] tex = new TextureState[4];
        tex[0] = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        tex[0].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare1.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, Image.Format.RGBA8,
                0.0f, true));
        tex[0].setEnabled(true);

        tex[1] = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        tex[1].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare2.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[1].setEnabled(true);

        tex[2] = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        tex[2].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare3.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[2].setEnabled(true);

        tex[3] = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        tex[3].setTexture(TextureManager.loadTexture(LensFlare.class
                .getClassLoader()
                .getResource("jmetest/data/texture/flare4.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        tex[3].setEnabled(true);

        flare = LensFlareFactory.createBasicLensFlare("flare", tex);
        flare.setRootNode(world.getRootNode());
        world.getRootNode().attachChild(lightNode);

        // notice that it comes at the end
        lightNode.attachChild(flare);
	}*/
	
	private void createVegetation() {
		
		float x, z;

		for( int k = 1; k < 6; k++ ) {
			Node tree = ModelLoader.loadModel( "game/data/models/vegetation/palm" + k + ".3ds", 
					"game/data/models/vegetation/palm" + k + ".png", 0.08f );
			
			for (int i = 0; i < 200; i++) {
				SharedNode sharedTree = new SharedNode( "tree" + i, tree );
				x = (float) Math.random() * world.dimension - world.dimension/2;
				z = (float) Math.random() * world.dimension - world.dimension/2;
				while( terrain.getHeight(x, z) <= 25 || terrain.getHeight(x, z) >= 50 ) {
					x = (float) Math.random() * world.dimension - world.dimension/2;
					z = (float) Math.random() * world.dimension - world.dimension/2;
				}
				sharedTree.setModelBound( new BoundingBox() );
				sharedTree.updateModelBound();
				world.getCollisionNode().attachChild( sharedTree );
				sharedTree.setLocalTranslation(new Vector3f( x, terrain.getHeight(x, z) - 20, z ));
				sharedTree.lock();
				world.trees.put( world.treeCounter++, sharedTree );
			}
		}
		
		for( int k = 1; k < 3; k++ ) {
			Node tree = ModelLoader.loadModel( "game/data/models/vegetation/tree" + k + ".3ds", 
					"game/data/models/vegetation/tree" + k + ".png", 0.6f );
			
			for (int i = 0; i < 400; i++) {
				SharedNode sharedTree = new SharedNode( "tree" + i, tree );
				x = (float) Math.random() * world.dimension - world.dimension/2;
				z = (float) Math.random() * world.dimension - world.dimension/2;
				while( terrain.getHeight(x, z) <= 60 || terrain.getHeight(x, z) >= 200 ) {
					x = (float) Math.random() * world.dimension - world.dimension/2;
					z = (float) Math.random() * world.dimension - world.dimension/2;
				}
				sharedTree.setModelBound( new BoundingBox() );
				sharedTree.updateModelBound();
				world.getCollisionNode().attachChild( sharedTree );
				sharedTree.setLocalTranslation(new Vector3f( x, terrain.getHeight(x, z) - 20, z ));
				sharedTree.lock();
				world.trees.put( world.treeCounter++, sharedTree );
			}
		}
	}
	
	 /** Function that creates the game bounds with a physics box that contains all the world
     */
	public void createWorldBounds() {

		float x = world.dimension;
		float z = x;
		float y = 800;
		
		gameBounds.setLocalTranslation( -x/2, 0, -z/2 );
		
	    PhysicsBox downBox = gameBounds.createBox("downBox");
	    downBox.setLocalTranslation( x/2, -20, z/2 );
	    downBox.setLocalScale( new Vector3f( x, 5, z) );

	    PhysicsBox upperBox = gameBounds.createBox("upperBox");
	    upperBox.setLocalTranslation( x/2, y-20, z/2 );
	    upperBox.setLocalScale( new Vector3f( x, 5, z) );
	    
	    PhysicsBox eastBox = gameBounds.createBox("eastBox");
	    eastBox.setLocalTranslation( x, y/2-20, z/2 );
	    eastBox.setLocalScale( new Vector3f( 5, y, z ) );
	    
	    PhysicsBox westBox = gameBounds.createBox("westBox");
	    westBox.setLocalTranslation( 0, y/2-20, z/2 );
	    westBox.setLocalScale( new Vector3f( 5, y, z ) );
	    
	    PhysicsBox southBox = gameBounds.createBox("southBox");
	    southBox.setLocalTranslation( x/2, y/2-20, 0 );
	    southBox.setLocalScale( new Vector3f( x, y, 5 ) );

	    PhysicsBox northBox = gameBounds.createBox("northBox");
	    northBox.setLocalTranslation( x/2, y/2-20, z );
	    northBox.setLocalScale( new Vector3f( x, y, 5 ) );
	}
	
	private void createWater() {
		waterEffectRenderPass = new WaterRenderPass( world.getCam(), 6, true, true);
	    waterEffectRenderPass.setWaterPlane( new Plane( Vector3f.UNIT_Y, 0 ) );
	    waterEffectRenderPass.setClipBias(-1.0f);
	    waterEffectRenderPass.setReflectionThrottle(0.0f);
	    waterEffectRenderPass.setRefractionThrottle(0.0f);
	
	    waterQuad = new Quad("waterQuad", 10, 10);
	    FloatBuffer normBuf = waterQuad.getNormalBuffer();
	    normBuf.clear();
	    normBuf.put(0).put(1).put(0);
	    normBuf.put(0).put(1).put(0);
	    normBuf.put(0).put(1).put(0);
	    normBuf.put(0).put(1).put(0);
	
	    waterEffectRenderPass.setWaterEffectOnSpatial(waterQuad);
	    world.getRootNode().attachChild(waterQuad);
	
	    waterEffectRenderPass.setReflectedScene(skybox);
	    waterEffectRenderPass.addReflectedScene(reflectionTerrain);
	    waterEffectRenderPass.setSkybox(skybox);
	    world.getPass().add(waterEffectRenderPass);
	}

	private void createTerrain() {
		RawHeightMap heightMap = new RawHeightMap( getClass()
                .getClassLoader().getResource(
                        "game/data/texture/terrain2/height.raw"),
                heightMapSize, RawHeightMap.FORMAT_16BITLE, false);
		
        Vector3f terrainScale = new Vector3f( this.terrainScale, 0.008f, this.terrainScale );
//        heightMap.setHeightScale(0.001f);
        
        terrain = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        terrain.getLocalTranslation().set( 0, -20f, 0);
        terrain.setDetailTexture(1, 1);

     // create some interesting texturestates for splatting
        TextureState ts1 = createSplatTextureState(
                "game/data/texture/terrain2/base.jpg", 
                null);

        TextureState ts2 = createSplatTextureState(
                "game/data/texture/terrain2/grass.jpg",
                "game/data/texture/terrain2/grass.png");

        TextureState ts3 = createSplatTextureState(
                "game/data/texture/terrain2/brightgrass.jpg",
                "game/data/texture/terrain2/brightgrass.png");
        
        TextureState ts4 = createSplatTextureState(
                "game/data/texture/terrain2/rock.jpg",
                "game/data/texture/terrain2/rock.png");

        TextureState ts5 = createSplatTextureState(
        		"game/data/texture/terrain2/road.jpg",
        		"game/data/texture/terrain2/road.png");
        
        // alpha used for blending the passnodestates together
        BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);

        // alpha used for blending the lightmap
        BlendState as2 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as2.setBlendEnabled(true);
        as2.setSourceFunction(BlendState.SourceFunction.DestinationColor);
        as2.setDestinationFunction(BlendState.DestinationFunction.SourceColor);
        as2.setTestEnabled(true);
        as2.setTestFunction(BlendState.TestFunction.GreaterThan);
        as2.setEnabled(true);

        // //////////////////// PASS STUFF START
        // try out a passnode to use for splatting
        PassNode splattingPassNode = new PassNode("SplatPassNode");
        splattingPassNode.attachChild(terrain);
        
        PassNodeState passNodeState = new PassNodeState();
        
        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts1);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts2);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);
        
        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts3);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts4);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts5);
        passNodeState.setPassState(as);
        splattingPassNode.addPass(passNodeState);

        // //////////////////// PASS STUFF END

        // lock some things to increase the performance
        splattingPassNode.lockBounds();
        splattingPassNode.lockTransforms();
        splattingPassNode.lockShadows();

        splatTerrain = splattingPassNode;
        splatTerrain.setCullHint(Spatial.CullHint.Dynamic);
        
	    ground.attachChild( splatTerrain );
    	
	    ground.generatePhysicsGeometry();
    }

    private void createReflectionTerrain() {
    	RawHeightMap heightMap = new RawHeightMap( Loader.load(
                        "game/data/texture/terrain2/height.raw"),
                heightMapSize, RawHeightMap.FORMAT_16BITLE, false);

        Vector3f terrainScale = new Vector3f( this.terrainScale, 0.008f, this.terrainScale );
        TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        page.getLocalTranslation().set(  0, -20, 0 );
        page.setDetailTexture(1, 1);

        TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t0 = TextureManager.loadTexture( Loader.load(
                        "game/data/texture/terrain2/terrainLod.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        ts1.setTexture(t0, 0);

        page.setRenderState( ts1 );
        
        reflectionTerrain = page;
        reflectionTerrain.lock();
        
        initSpatial(reflectionTerrain);
    }

    private void addAlphaSplat(TextureState ts, String alpha) {
    	
    	URL alphaURL = Loader.load( alpha );
    	
    	BufferedImage tex = null;
		try {
			tex = Util.grayScaleToAlpha( ImageIO.read( alphaURL ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        Texture t1 = TextureManager.loadTexture( tex,
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear, false );
        
        t1.setWrap(Texture.WrapMode.Repeat);
        t1.setApply(Texture.ApplyMode.Combine);
        t1.setCombineFuncRGB(Texture.CombinerFunctionRGB.Replace);
        t1.setCombineSrc0RGB(Texture.CombinerSource.Previous);
        t1.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
        t1.setCombineFuncAlpha(Texture.CombinerFunctionAlpha.Replace);
        ts.setTexture(t1, ts.getNumberOfSetTextures());
    }

    private TextureState createSplatTextureState(String texture, String alpha) {
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture( Loader.load( texture ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        t0.setApply(Texture.ApplyMode.Modulate);
        t0.setScale(new Vector3f(globalSplatScale, globalSplatScale, 1.0f));
        ts.setTexture(t0, 0);

        if (alpha != null) {
            addAlphaSplat(ts, alpha);
        }

        return ts;
    }

//    private TextureState createLightmapTextureState(String texture) {
//        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
//
//        Texture t0 = TextureManager.loadTexture( Loader.load( texture ),
//                Texture.MinificationFilter.Trilinear,
//                Texture.MagnificationFilter.Bilinear);
//        t0.setWrap(Texture.WrapMode.Repeat);
//        ts.setTexture(t0, 0);
//
//        return ts;
//    }

    private void buildSkyBox() {
    	skybox = new Skybox("skybox", 10, 10, 10);

        String dir = "game/data/images/skybox/";
        Texture north = TextureManager.loadTexture( Loader.load(dir + "north.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture( Loader.load(dir + "south.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture( Loader.load(dir + "east.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture( Loader.load(dir + "west.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture( Loader.load(dir + "up.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture( Loader.load(dir + "down.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);

        skybox.setTexture(Skybox.Face.North, north);
        skybox.setTexture(Skybox.Face.West, west);
        skybox.setTexture(Skybox.Face.South, south);
        skybox.setTexture(Skybox.Face.East, east);
        skybox.setTexture(Skybox.Face.Up, up);
        skybox.setTexture(Skybox.Face.Down, down);
        skybox.preloadTextures();

        CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cullState.setCullFace(CullState.Face.None);
        cullState.setEnabled(true);
        skybox.setRenderState(cullState);

        FogState fs = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
        fs.setEnabled(false);
        skybox.setRenderState(fs);

        skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
        skybox.setCullHint(Spatial.CullHint.Never);
        skybox.setTextureCombineMode(TextureCombineMode.Replace);
        skybox.updateRenderState();

        skybox.lockBounds();
        skybox.lockMeshes();
    }

    private void setVertexCoords(float x, float y, float z) {
        FloatBuffer vertBuf = waterQuad.getVertexBuffer();
        vertBuf.clear();

        vertBuf.put(x - farPlane).put(y).put(z - farPlane);
        vertBuf.put(x - farPlane).put(y).put(z + farPlane);
        vertBuf.put(x + farPlane).put(y).put(z + farPlane);
        vertBuf.put(x + farPlane).put(y).put(z - farPlane);
    }

    private void setTextureCoords(int buffer, float x, float y,
            float textureScale) {
        x *= textureScale * 0.5f;
        y *= textureScale * 0.5f;
        textureScale = farPlane * textureScale;
        FloatBuffer texBuf;
        texBuf = waterQuad.getTextureCoords(buffer).coords;
        texBuf.clear();
        texBuf.put(x).put(textureScale + y);
        texBuf.put(x).put(y);
        texBuf.put(textureScale + x).put(y);
        texBuf.put(textureScale + x).put(textureScale + y);
    }

    private void initSpatial(Spatial spatial) {
        ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        spatial.setRenderState(buf);

        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        spatial.setRenderState(cs);

        spatial.setCullHint(Spatial.CullHint.Dynamic);

        spatial.updateGeometricState(0.0f, true);
        spatial.updateRenderState();
    }
    
    public TerrainPage getTerrain() {
    	return terrain;
    }
}
