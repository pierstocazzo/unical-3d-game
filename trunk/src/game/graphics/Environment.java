package game.graphics;

import java.nio.FloatBuffer;

import utils.Loader;
import utils.ModelLoader;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
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
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;
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
    float textureScale = 0.07f;
    float globalSplatScale = 90.0f;
	int heightMapSize;
	int terrainScale;
	
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
		
		heightMapSize = 129;
		terrainScale = 20;
		
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
		
	    setuplight();
		
	    CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
	    cs.setCullFace(CullState.Face.Back);
	    world.getRootNode().setRenderState(cs);

//	    lightState.detachAll();
//	    world.getRootNode().setLightCombineMode(Spatial.LightCombineMode.Off);
	
	    FogState fogState = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
	    fogState.setDensity(1.0f);
	    fogState.setEnabled(true);
	    fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    fogState.setEnd(farPlane);
	    fogState.setStart(farPlane / 10.0f);
	    fogState.setDensityFunction(FogState.DensityFunction.Linear);
	    fogState.setQuality(FogState.Quality.PerVertex);
	    world.getRootNode().setRenderState(fogState);
	    
		createTerrain();
        createReflectionTerrain();

        buildSkyBox();
        
        world.getRootNode().attachChild( skybox );
        world.getRootNode().attachChild( splatTerrain );
        
        createWater();
	    
	    RenderPass rootPass = new RenderPass();
	    rootPass.add(world.getRootNode());
	    world.getPass().add(rootPass);

	    world.getRootNode().setCullHint(Spatial.CullHint.Never);
	    world.getRootNode().setCullHint(Spatial.CullHint.Never);
		
	    createVegetation();
	    createWorldBounds();
	}
	
	public void update() {
		skybox.getLocalTranslation().set( world.getCam().getLocation() );
        skybox.updateGeometricState(0.0f, true);
        
//        lightNode.getLocalTranslation().set( cam.getLocation().add( 200, 200, 200 ) );
//        world.getRootNode().updateGeometricState(0, true);

        /******** Added to animate the water ********/
        Vector3f transVec = new Vector3f( world.getCam().getLocation().x,
                waterEffectRenderPass.getWaterHeight(), world.getCam().getLocation().z);
        setTextureCoords(0, transVec.x, -transVec.z, 0.07f);
        setVertexCoords(transVec.x, transVec.y, transVec.z);
	}
	
	private void setuplight() {
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
//        lightNode.attachChild(lightBox);
        lightNode.setLocalTranslation(new Vector3f(-1200, 200,-1200));

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
	}
	
	private void createVegetation() {
		
		float x, z;

		for( int k = 1; k < 6; k++ ) {
			Node tree = ModelLoader.loadModel( "game/data/models/vegetation/palm" + k + ".3ds", 
					"game/data/models/vegetation/palm" + k + ".png", 0.06f );
			
			for (int i = 0; i < 300; i++) {
				SharedNode sharedTree = new SharedNode( "tree" + i, tree );
				x = (float) Math.random() * world.dimension - world.dimension/2;
				z = (float) Math.random() * world.dimension - world.dimension/2;
				while( terrain.getHeight(x, z) <= 10 || terrain.getHeight(x, z) >= 150 ) {
					x = (float) Math.random() * world.dimension - world.dimension/2;
					z = (float) Math.random() * world.dimension - world.dimension/2;
				}
				sharedTree.setLocalTranslation(new Vector3f( x, terrain.getHeight(x, z) - 10, z ));
				world.getRootNode().attachChild( sharedTree );
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
		waterEffectRenderPass = new WaterRenderPass( world.getCam(), 6, false, true);
	    waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
	            0.0f), 0.0f));
	    waterEffectRenderPass.setClipBias(-1.0f);
	    waterEffectRenderPass.setReflectionThrottle(0.0f);
	    waterEffectRenderPass.setRefractionThrottle(0.0f);
	
	    waterQuad = new Quad("waterQuad", 1, 1);
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
                        "game/data/texture/terrain/heights.raw"),
                heightMapSize, RawHeightMap.FORMAT_16BITLE, false);
		
        Vector3f terrainScale = new Vector3f( 20, 0.005f, 20 );
        heightMap.setHeightScale(0.001f);
        
        terrain = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        terrain.getLocalTranslation().set( 0, -9.5f, 0);
        terrain.setDetailTexture(1, 1);

        // create some interesting texturestates for splatting
        TextureState ts1 = createSplatTextureState(
                "game/data/texture/terrain/sand.jpg", null);

        TextureState ts2 = createSplatTextureState(
                "game/data/texture/terrain/sand.jpg",
                "game/data/texture/terrain/darkrockalpha.png");

        TextureState ts3 = createSplatTextureState(
                "game/data/texture/terrain/nicegrass.jpg",
                "game/data/texture/terrain/deadalpha.png");

        TextureState ts4 = createSplatTextureState(
                "game/data/texture/terrain/nicegrass.jpg",
                "game/data/texture/terrain/grassalpha.png");

        TextureState ts5 = createSplatTextureState(
                "game/data/texture/terrain/road.jpg",
                "game/data/texture/terrain/roadalpha.png");

        TextureState ts6 = createLightmapTextureState(
        		"game/data/texture/terrain/lightmap.jpg");

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
        passNodeState.setPassState(ts1);
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
//
//        passNodeState = new PassNodeState();
////        passNodeState.setPassState(ts6);
//        passNodeState.setPassState(as2);
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
                        "game/data/texture/terrain/heights.raw"),
                heightMapSize, RawHeightMap.FORMAT_16BITLE, false);

        Vector3f terrainScale = new Vector3f( 20, 0.007f, 20 );
        heightMap.setHeightScale(0.001f);
        TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        page.getLocalTranslation().set(  0, -9.5f, 0 );
        page.setDetailTexture(1, 1);

        TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t0 = TextureManager.loadTexture( Loader.load(
                        "game/data/texture/terrain/terrainlod.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
//        t0.setWrap(Texture.WrapMode.Repeat);
//        t0.setApply(Texture.ApplyMode.Modulate);
//        t0.setScale(new Vector3f(1.0f, 1.0f, 1.0f));
        ts1.setTexture(t0, 0);

//        // //////////////////// PASS STUFF START
//        // try out a passnode to use for splatting
//        PassNode splattingPassNode = new PassNode("SplatPassNode");
//        splattingPassNode.attachChild(page);
//
//        PassNodeState passNodeState = new PassNodeState();
//        passNodeState.setPassState(ts1);
//        splattingPassNode.addPass(passNodeState);
//        // //////////////////// PASS STUFF END
//
//        // lock some things to increase the performance
//        splattingPassNode.lockBounds();
//        splattingPassNode.lockTransforms();
//        splattingPassNode.lockShadows();
//
//        reflectionTerrain = splattingPassNode;

        page.setRenderState( ts1 );
        
        reflectionTerrain = page;
        
        initSpatial(reflectionTerrain);
    }

    private void addAlphaSplat(TextureState ts, String alpha) {
        Texture t1 = TextureManager.loadTexture( Loader.load( alpha ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
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

    private TextureState createLightmapTextureState(String texture) {
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture( Loader.load( texture ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        ts.setTexture(t0, 0);

        return ts;
    }

    private void buildSkyBox() {
//        skybox = new Skybox("skybox", 10, 10, 10);
//
//        String dir = "game/data/images/skybox/";
//        Texture north = TextureManager.loadTexture( Loader.load(dir + "nord.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture south = TextureManager.loadTexture( Loader.load(dir + "sud.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture east = TextureManager.loadTexture( Loader.load(dir + "est.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture west = TextureManager.loadTexture( Loader.load(dir + "ovest.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture up = TextureManager.loadTexture( Loader.load(dir + "up.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//        Texture down = TextureManager.loadTexture( Loader.load(dir + "down.jpg"),
//                Texture.MinificationFilter.BilinearNearestMipMap,
//                Texture.MagnificationFilter.Bilinear);
//
//        skybox.setTexture(Skybox.Face.North, north);
//        skybox.setTexture(Skybox.Face.West, west);
//        skybox.setTexture(Skybox.Face.South, south);
//        skybox.setTexture(Skybox.Face.East, east);
//        skybox.setTexture(Skybox.Face.Up, up);
//        skybox.setTexture(Skybox.Face.Down, down);
//        skybox.preloadTextures();
//
//        CullState cullState = display.getRenderer().createCullState();
//        cullState.setCullFace(CullState.Face.None);
//        cullState.setEnabled(true);
//        skybox.setRenderState(cullState);
//
//        FogState fs = display.getRenderer().createFogState();
//        fs.setEnabled(false);
//        skybox.setRenderState(fs);
//
////        skybox.setLightCombineMode(Spatial.LightCombineMode.Off);
//        skybox.setCullHint(Spatial.CullHint.Never);
//        skybox.setTextureCombineMode(TextureCombineMode.Replace);
//        skybox.updateRenderState();
//
//        skybox.lockBounds();
//        skybox.lockMeshes();
    
    	
    	skybox = new Skybox("skybox", 10, 10, 10);

        String dir = "jmetest/data/skybox1/";
        Texture north = TextureManager.loadTexture( Loader.load(dir + "1.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture( Loader.load(dir + "3.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture( Loader.load(dir + "2.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture( Loader.load(dir + "4.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture( Loader.load(dir + "6.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture( Loader.load(dir + "5.jpg"),
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

//        ZBufferState zState = display.getRenderer().createZBufferState();
//        zState.setEnabled(false);
//        skybox.setRenderState(zState);

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
