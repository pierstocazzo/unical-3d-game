package slashWork.game.test;

import java.nio.FloatBuffer;

import slashWork.game.base.Game;
import jmetest.effects.water.TestQuadWater;

import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.light.DirectionalLight;
import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.PassNode;
import com.jme.scene.PassNodeState;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;
import com.jmex.effects.water.WaterRenderPass;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.RawHeightMap;


public class TestNewTerrain extends Game  {


    Skybox skybox;
    
    TerrainPage terrain;
    StaticPhysicsNode ground;

    WaterRenderPass waterEffectRenderPass;
    Quad waterQuad;
    Spatial splatTerrain;
    Spatial reflectionTerrain;
    float farPlane = 10000.0f;
    float textureScale = 0.07f;
    float globalSplatScale = 90.0f;
    
    InputHandler input;

    public static void main(String[] args) {
        TestNewTerrain app = new TestNewTerrain();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }
    
    protected void update() {
    	input.update(tpf); 
        skybox.getLocalTranslation().set( cam.getLocation() );
        skybox.updateGeometricState(0.0f, true);

        Vector3f transVec = new Vector3f(cam.getLocation().x,
                waterEffectRenderPass.getWaterHeight(), cam.getLocation().z);
        setTextureCoords(0, transVec.x, -transVec.z, textureScale);
        setVertexCoords(transVec.x, transVec.y, transVec.z);
    }

    @Override
	public void setupInit() {
		display.setTitle("Test Island extended from PhysicsGame");
	}

	public void setupEnvironment() {
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
	    DirectionalLight dr = new DirectionalLight();
	    dr.setEnabled(true);
	    dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    dr.setDirection(new Vector3f(0.5f, -0.5f, 0));
	
	    lightState.detachAll();
	    lightState.attach(dr);
		
	    CullState cs = display.getRenderer().createCullState();
	    cs.setCullFace(CullState.Face.Back);
	    rootNode.setRenderState(cs);
	
	    lightState.detachAll();
	    rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);
	
	    FogState fogState = display.getRenderer().createFogState();
	    fogState.setDensity(1.0f);
	    fogState.setEnabled(true);
	    fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    fogState.setEnd(farPlane);
	    fogState.setStart(farPlane / 10.0f);
	    fogState.setDensityFunction(FogState.DensityFunction.Linear);
	    fogState.setQuality(FogState.Quality.PerVertex);
	    rootNode.setRenderState(fogState);
	    
		createTerrain();
        createReflectionTerrain();

        buildSkyBox();
        
        rootNode.attachChild(skybox);
        rootNode.attachChild(splatTerrain);
        
        createWater();
	    
	    RenderPass rootPass = new RenderPass();
	    rootPass.add(rootNode);
	    passManager.add(rootPass);
	
	    rootNode.setCullHint(Spatial.CullHint.Never);
	}
	
	@Override
	public void setupPlayer() {
	}

	@Override
	public void setupEnemies() {
	}

	@Override
	public void setupCamera() {
	    cam.setFrustumPerspective(45.0f, (float) display.getWidth()
	            / (float) display.getHeight(), 1f, farPlane);
	    cam.setLocation(new Vector3f(-320, 80, -270));
	    cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
	    cam.update();
	}

	@Override
	public void setupInput() {
		input = new FirstPersonHandler( cam, 50, 1 );
	}

	private void createWater() {
		waterEffectRenderPass = new WaterRenderPass(cam, 6, false, true);
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
	    rootNode.attachChild(waterQuad);
	
	    waterEffectRenderPass.setReflectedScene(skybox);
	    waterEffectRenderPass.addReflectedScene(reflectionTerrain);
	    waterEffectRenderPass.setSkybox(skybox);
	    passManager.add(waterEffectRenderPass);
	}

	private void createTerrain() {
		RawHeightMap heightMap = new RawHeightMap( getClass()
                .getClassLoader().getResource(
                        "jmetest/data/texture/terrain/heights.raw"),
                129, RawHeightMap.FORMAT_16BITLE, false);

        Vector3f terrainScale = new Vector3f(20, 0.003f, 20);
        heightMap.setHeightScale(0.001f);
        
        TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        page.getLocalTranslation().set(0, -9.5f, 0);
        page.setDetailTexture(1, 1);

        // create some interesting texturestates for splatting
        TextureState ts1 = createSplatTextureState(
                "jmetest/data/texture/terrain/baserock.jpg", null);

        TextureState ts2 = createSplatTextureState(
                "jmetest/data/texture/terrain/darkrock.jpg",
                "jmetest/data/texture/terrain/darkrockalpha.png");

        TextureState ts3 = createSplatTextureState(
                "jmetest/data/texture/terrain/deadgrass.jpg",
                "jmetest/data/texture/terrain/deadalpha.png");

        TextureState ts4 = createSplatTextureState(
                "jmetest/data/texture/terrain/nicegrass.jpg",
                "jmetest/data/texture/terrain/grassalpha.png");

        TextureState ts5 = createSplatTextureState(
                "jmetest/data/texture/terrain/road.jpg",
                "jmetest/data/texture/terrain/roadalpha.png");

        TextureState ts6 = createLightmapTextureState(
        		"jmetest/data/texture/terrain/lightmap.jpg");

        // alpha used for blending the passnodestates together
        BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);

        // alpha used for blending the lightmap
        BlendState as2 = display.getRenderer().createBlendState();
        as2.setBlendEnabled(true);
        as2.setSourceFunction(BlendState.SourceFunction.DestinationColor);
        as2.setDestinationFunction(BlendState.DestinationFunction.SourceColor);
        as2.setTestEnabled(true);
        as2.setTestFunction(BlendState.TestFunction.GreaterThan);
        as2.setEnabled(true);

        // //////////////////// PASS STUFF START
        // try out a passnode to use for splatting
        PassNode splattingPassNode = new PassNode("SplatPassNode");
        splattingPassNode.attachChild(page);

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

        passNodeState = new PassNodeState();
        passNodeState.setPassState(ts6);
        passNodeState.setPassState(as2);
        splattingPassNode.addPass(passNodeState);
        // //////////////////// PASS STUFF END

        // lock some things to increase the performance
        splattingPassNode.lockBounds();
        splattingPassNode.lockTransforms();
        splattingPassNode.lockShadows();

        splatTerrain = splattingPassNode;
        splatTerrain.setCullHint(Spatial.CullHint.Dynamic);
    }

    private void createReflectionTerrain() {
    	RawHeightMap heightMap = new RawHeightMap(TestNewTerrain.class
                .getClassLoader().getResource(
                        "jmetest/data/texture/terrain/heights.raw"),
                129, RawHeightMap.FORMAT_16BITLE, false);

        Vector3f terrainScale = new Vector3f( 20, 0.003f, 20 );
        heightMap.setHeightScale(0.001f);
        TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        page.getLocalTranslation().set( 0, -9.5f, 0 );
        page.setDetailTexture(1, 1);

        // create some interesting texturestates for splatting
        TextureState ts1 = display.getRenderer().createTextureState();
        Texture t0 = TextureManager.loadTexture(TestNewTerrain.class
                .getClassLoader().getResource(
                        "jmetest/data/texture/terrain/terrainlod.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        t0.setApply(Texture.ApplyMode.Modulate);
        t0.setScale(new Vector3f(1.0f, 1.0f, 1.0f));
        ts1.setTexture(t0, 0);

        // //////////////////// PASS STUFF START
        // try out a passnode to use for splatting
        PassNode splattingPassNode = new PassNode("SplatPassNode");
        splattingPassNode.attachChild(page);

        PassNodeState passNodeState = new PassNodeState();
        passNodeState.setPassState(ts1);
        splattingPassNode.addPass(passNodeState);
        // //////////////////// PASS STUFF END

        // lock some things to increase the performance
        splattingPassNode.lockBounds();
        splattingPassNode.lockTransforms();
        splattingPassNode.lockShadows();

        reflectionTerrain = splattingPassNode;

        initSpatial(reflectionTerrain);
    }

    private void addAlphaSplat(TextureState ts, String alpha) {
        Texture t1 = TextureManager.loadTexture(TestNewTerrain.class
                .getClassLoader().getResource(alpha),
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
        TextureState ts = display.getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture(TestNewTerrain.class
                .getClassLoader().getResource(texture),
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
        TextureState ts = display.getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture(TestNewTerrain.class
                .getClassLoader().getResource(texture),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        ts.setTexture(t0, 0);

        return ts;
    }

    private void buildSkyBox() {
        skybox = new Skybox("skybox", 10, 10, 10);

        String dir = "jmetest/data/skybox1/";
        Texture north = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "1.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "3.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "2.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "4.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "6.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "5.jpg"),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);

        skybox.setTexture(Skybox.Face.North, north);
        skybox.setTexture(Skybox.Face.West, west);
        skybox.setTexture(Skybox.Face.South, south);
        skybox.setTexture(Skybox.Face.East, east);
        skybox.setTexture(Skybox.Face.Up, up);
        skybox.setTexture(Skybox.Face.Down, down);
        skybox.preloadTextures();

        CullState cullState = display.getRenderer().createCullState();
        cullState.setCullFace(CullState.Face.None);
        cullState.setEnabled(true);
        skybox.setRenderState(cullState);
        
//		DA PROBLEMI
//        ZBufferState zState = display.getRenderer().createZBufferState();
//        zState.setEnabled(true);
//        skybox.setRenderState(zState);

        FogState fs = display.getRenderer().createFogState();
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
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        spatial.setRenderState(buf);

        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        spatial.setRenderState(cs);

        spatial.setCullHint(Spatial.CullHint.Never);

        spatial.updateGeometricState(0.0f, true);
        spatial.updateRenderState();
    }
}
