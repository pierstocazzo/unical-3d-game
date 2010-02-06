/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.graphics;

import game.common.GameConf;
import game.graphics.Scene.CachedMesh;
import game.graphics.Scene.Item;
import game.graphics.Scene.TerrainLayer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import utils.Loader;
import utils.ModelLoader;
import utils.Util;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
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
import com.jmex.effects.water.WaterRenderPass;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.RawHeightMap;

/**
 * Enviroment class - manage items and objects
 * 
 * @author Andrea Martire, Salvatore Loria, Giuseppe Leone
 */
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
//    float globalSplatScale = 30.0f;
	int heightMapSize;
	
	/** the sky */
	Skybox skybox;

	/** the graphicalWorld */
    GraphicalWorld world;
    
    /** the physics ground of the world */
	StaticPhysicsNode ground;
	
    /** the game's bounds, a physics box that contains the scene */
    StaticPhysicsNode gameBounds;
    
    /** the scene */
    Scene scene;
	
    /** terrain scale factor */
    Vector3f terrainScale;
	
    /** height of the water */
    float waterHeight;
    
    
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
		scene = new Scene( GameConf.getSetting( GameConf.SCENE_FILE_PATH ) );
		
		heightMapSize = scene.getHeightmapSize();
		terrainScale = scene.getScale();
		
		world.dimension = (heightMapSize-1) * terrainScale.x;
	    
		waterHeight = scene.getWaterHeight();
		
        ground = world.getPhysicsSpace().createStaticNode();
        gameBounds = world.getPhysicsSpace().createStaticNode();
        world.getRootNode().attachChild(ground);
        world.getRootNode().attachChild(gameBounds);
        
	    world.getMenu().setProgress(30);
	    
	    FogState fogState = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
	    fogState.setDensity(1.0f);
	    fogState.setEnabled(true);
	    fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    fogState.setEnd(farPlane/2);
	    fogState.setStart(farPlane / 20.0f);
	    fogState.setDensityFunction(FogState.DensityFunction.Linear);
	    fogState.setQuality(FogState.Quality.PerVertex);
	    world.getRootNode().setRenderState(fogState);
	    
	    world.getMenu().setProgress(35);
		createTerrain();
		
		world.getMenu().setProgress(40);
        createReflectionTerrain();

        buildSkyBox();
        world.getRootNode().attachChild( skybox );
        world.getRootNode().attachChild( splatTerrain );
        
        world.getMenu().setProgress(45);
        createWater();
        
	    RenderPass rootPass = new RenderPass();
	    rootPass.add(world.getRootNode());
	    world.getPass().add(rootPass);

	    world.getRootNode().setCullHint(Spatial.CullHint.Never);
	    world.getRootNode().setCullHint(Spatial.CullHint.Never);
	    
        world.getMenu().setProgress(50);
        
	    loadItems();
	    
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
	
	public void loadItems() {
		/* load all 3d models needed in the scene */
		HashMap< String, Node > cachedModels = new HashMap<String, Node>();
		
		for( CachedMesh cachedMesh : scene.getCachedMeshes() ) {
			String modelPath = cachedMesh.modelPath;
			String texturePath = cachedMesh.texturePath;
			Node model = ModelLoader.loadModel( modelPath, texturePath, 1 );
			cachedModels.put( cachedMesh.id, model );
		}
		
		/* create a sharedNode for each item of the scene, using the models previously loaded */
		for( Item item : scene.getItems() ) {
			String meshId = item.meshId;

			SharedNode sharedModel = new SharedNode( cachedModels.get(meshId) );
			world.collisionNode.attachChild( sharedModel );
			
			Vector3f position = item.position.subtract( 
					world.dimension/2, scene.getWaterHeight(), world.dimension/2 );
			sharedModel.setLocalTranslation( position );
			
			sharedModel.setLocalRotation( item.rotation );
			
			sharedModel.setLocalScale( item.scale );
			
			sharedModel.setModelBound( new BoundingBox() );
			sharedModel.updateModelBound();
			sharedModel.lock();
			
			world.items.add( sharedModel );
		}
	}
	
	 /** Function that creates the game bounds with a physics box that contains all the world
     */
	public void createWorldBounds() {

		float x = world.dimension;
		float z = world.dimension;
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
		RawHeightMap heightMap = scene.getHeightmap();
		
        terrain = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        terrain.getLocalTranslation().set( 0, -waterHeight, 0 );
        terrain.setDetailTexture(1, 1);
        
        // alpha used for blending the passnodestates together
        BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);

        PassNode splattingPassNode = new PassNode("SplatPassNode");
        splattingPassNode.attachChild(terrain);
        
        PassNodeState passNodeState = new PassNodeState();
        
        String dir = "game/data";
        Iterator<TerrainLayer> it = scene.getTerrainLayers().iterator();
        while( it.hasNext() ) {
        	TerrainLayer layer = it.next();
        	TextureState ts;
        	if( layer.alpha != null ) {
	            ts = createSplatTextureState(
	            		dir + layer.texture,
	            		dir + layer.alpha,
	            		layer.scale );
        	} else {
                ts = createSplatTextureState(
                		dir + layer.texture,
                		null, layer.scale );
        	}
            
            passNodeState = new PassNodeState();
            passNodeState.setPassState(ts);
            passNodeState.setPassState(as);
            splattingPassNode.addPass(passNodeState);
        }

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
    	RawHeightMap heightMap = scene.getHeightmap();

        TerrainPage page = new TerrainPage("Terrain", 33, heightMap.getSize(),
                terrainScale, heightMap.getHeightMap());
        page.getLocalTranslation().set(  0, -waterHeight, 0 );
        page.setDetailTexture(1, 1);

        TextureState ts1 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        Texture t0 = TextureManager.loadTexture( Loader.load(
                        "game/data/terrain/terrainLod.jpg"),
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

    private TextureState createSplatTextureState( String texture, String alpha, float scale ) {
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

        Texture t0 = TextureManager.loadTexture( Loader.load( texture ),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        t0.setWrap(Texture.WrapMode.Repeat);
        t0.setApply(Texture.ApplyMode.Modulate);
        t0.setScale(new Vector3f( scale, scale, 1.0f));
        ts.setTexture(t0, 0);

        if (alpha != null) {
            addAlphaSplat(ts, alpha);
        }

        return ts;
    }

    private void buildSkyBox() {
    	skybox = new Skybox("skybox", 10, 10, 10);

        Texture north = TextureManager.loadTexture( Loader.load( scene.getSkyTextures().north ),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture south = TextureManager.loadTexture( Loader.load( scene.getSkyTextures().south ),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture east = TextureManager.loadTexture( Loader.load( scene.getSkyTextures().east ),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture west = TextureManager.loadTexture( Loader.load( scene.getSkyTextures().west ),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture up = TextureManager.loadTexture( Loader.load( scene.getSkyTextures().up ),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear);
        Texture down = TextureManager.loadTexture( Loader.load( scene.getSkyTextures().down ),
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
    
    public float getHeight( float x, float z ) {
    	return terrain.getHeight( x, z ) - waterHeight;
    }

	public float getHeight( Vector3f pos ) {
		return terrain.getHeight( pos.x, pos.z ) - waterHeight;
	}
}
