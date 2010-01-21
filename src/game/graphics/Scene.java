package game.graphics;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import utils.Loader;
import utils.Util;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jmex.terrain.util.RawHeightMap;

/** Class Scene used to read the xml file exported from FreeWorld3d<br>
 * 
 * Needed library: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author Salvatore Loria 
 */
public class Scene {
	public final Logger logger =  Logger.getLogger("global");
	
	/** put here the default scene file path, it will be loaded by default */
	public static String DEFAULT_SCENE_FILE = "src/game/data/level/island.xml";
	
	/** the xml document */
	Document document;
	
	/** the root of the xml file */
	Element xmlRoot;
	
	/** the heightmap from witch to generate the terrain */
	RawHeightMap heightmap;
	
	/** the heightmap's size */
	int heightmapSize;
	
	/** the terrain scale factor */
	Vector3f scale;
	
	/** list of all items of the scene with their position, rotation and scale */
	List<Item> items;
	
	/** path to the data directory of the game */
	String dataDirectory = "game/data";
	
	/** list containing the id and the model's path of each cashed mesh */
	List<CachedMesh> cachedMeshes;
	
	/** list containing the terrain layers to apply */
	List<TerrainLayer> terrainLayers;
	
	/** skybox's textures */
	SkyTextures skyTextures;
	
	/** water height */
	float waterHeight;
	
	
	/** Class <code>Scene</code> constructor <p>
	 * Create a scene from witch you can get the heightmap for the terrain,
	 * the terrain layers and a list of all items of the scene with their position,
	 * rotation and scale.
	 * @param sceneFilePath - (String) the path of the scene's xml file
	 */
	public Scene( String sceneFilePath ) {
		SAXBuilder builder = new SAXBuilder();
		
		/* it tries to load the gived scene's file, if it doesen't exist or it's not correct, 
		 * it will load the default scene file */
		try {
			document = builder.build( new File( sceneFilePath ) );
		} catch (Exception e) {
			logger.log( Level.SEVERE, 
					"Error loading the scene file, make sure the path is correct." +
					"\nDefault scene will be loaded" );
			try {
				document = builder.build( new File( DEFAULT_SCENE_FILE ) );
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
		
		xmlRoot = document.getRootElement();
		
		buildScene();
	}
	
	/** It Loads the heightmap scecified in the xml file and set the size and scale parameters.
	 * It also creates a list of all items of the scene.
	 */
	private void buildScene() {
		try {
			Element terrain = xmlRoot.getChild( "Terrain" );
			
			heightmapSize = Integer.valueOf( terrain.getAttributeValue( "Size" ) );
			scale = new Vector3f();
			scale.x = Float.valueOf( terrain.getAttributeValue( "Step" ) );
			scale.y = Float.valueOf( terrain.getChild( "Heightmap" ).getAttributeValue( "Scale" ) );
			scale.z = scale.x;
			URL heightmapURL = Loader.load( dataDirectory + 
					terrain.getChild( "Heightmap" ).getAttributeValue( "File" ) );
			heightmap = new RawHeightMap( heightmapURL, heightmapSize, RawHeightMap.FORMAT_16BITLE, false );
		} catch (Exception e) {
			logger.severe( 
					"The scene file you are trying to load doesn't contain the necessary heightmap informations" +
					"\nDefault scene will be loaded");
			try {
				document = new SAXBuilder().build( new File( DEFAULT_SCENE_FILE ) );
				xmlRoot = document.getRootElement();
				buildScene();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			
		}
		
		try {
			waterHeight = Float.valueOf( xmlRoot.getChild("Water").getAttributeValue("Height") );
		} catch (Exception e) {
			waterHeight = 0;
		}
		
		loadTerrainLayers();
		loadCashedMeshes();
		loadItems();
		loadSkyTextures();
	}

	/** 
	 * Create a list of the terrain layers to apply
	 */
	@SuppressWarnings("unchecked")
	private void loadTerrainLayers() {
		terrainLayers = new LinkedList<TerrainLayer>();
		
		try {
			Element terrainLayer = xmlRoot.getChild("TerrainLayers");
			Iterator<Element> it = terrainLayer.getChildren().iterator();
			while( it.hasNext() ) {
				Element layer = it.next();
				String texture = layer.getChild("Texture").getAttributeValue("File");
				if( layer.getAttributeValue("Type").equals( "Base Layer" ) ) {
					terrainLayers.add( new TerrainLayer( texture, null ) );
				} else {
					String alpha = layer.getChild("AlphaMap").getAttributeValue("File");
					terrainLayers.add( new TerrainLayer( texture, alpha ) );
				}
			}
		} catch (Exception e) {
			logger.info( "No terrain layer found in the scene file\n" +
					"No texture will be loaded for the terrian" );
		}
	}
	
	/**
	 *  Create a list of all items of the scene with their position, scale and rotation 
	 */
	@SuppressWarnings("unchecked")
	private void loadItems() {
		items = new LinkedList<Item>();
		Element sceneLayers;
		
		try {
			sceneLayers = xmlRoot.getChild("SceneLayers");
		} catch (Exception e) {
			return;
		}
		
		/* catch all children of the sceneLayer (they are the models to load */
		List<Element> layers = sceneLayers.getChildren();
		for( Element layer : layers ) {
			List<Element> meshes = layer.getChildren();
			for( Element curr : meshes ) {
				try {
					Item item = new Item(); 
					
					/* extract the layer name */
					item.layer = layer.getAttributeValue("Name");
					
					/* extract the position of the object */
					
					String positionString = curr.getAttributeValue("Position");
					String[] positionArray = positionString.split(",");
					
					Vector3f position = new Vector3f();
					position.x = Float.valueOf( positionArray[0] );
					position.y = Float.valueOf( positionArray[1] );
					position.z = Float.valueOf( positionArray[2] );
					
					item.position = position;
					
					/* extract its rotation */
					
					String rotationString = curr.getAttributeValue("Quat");
					String[] rotationArray = rotationString.split(",");
					
					Quaternion rotation = new Quaternion();
					rotation.x = Float.valueOf( rotationArray[0] );
					rotation.y = Float.valueOf( rotationArray[1] );
					rotation.z = Float.valueOf( rotationArray[2] );
					rotation.w = Float.valueOf( rotationArray[3] );
					
					item.rotation = rotation;
					
					/* extract its scale */
					
					String scaleString = curr.getAttributeValue("Scale");
					String[] scaleArray = scaleString.split(",");
					
					Vector3f scale = new Vector3f();
					scale.x = Float.valueOf( scaleArray[0] );
					scale.y = Float.valueOf( scaleArray[1] );
					scale.z = Float.valueOf( scaleArray[2] );
					
					item.scale = scale;
					
					/* extract the mesh id */
					item.meshId = curr.getAttributeValue("CachedMeshId");
					
					items.add( item );
				} catch (Exception e) {
					// do nothing if there is an exception loading some item
				}
			}
		}
	}
	
	/** Function loadCashedMeshes() <p>
	 */
	@SuppressWarnings("unchecked")
	private void loadCashedMeshes() {
		cachedMeshes = new LinkedList<CachedMesh>();
		List<Element> meshGroups;
		
		try {
			meshGroups = xmlRoot.getChild("CachedMeshGroups").getChildren();
		} catch (Exception e) {
			return;
		}
		
		for( Element meshGroup : meshGroups ) {
			List<Element> meshes = meshGroup.getChildren();
			for( Element mesh : meshes ) {
				String id = mesh.getAttributeValue("Id");
				String filePath = dataDirectory + mesh.getAttributeValue("Filename");
				String texturePath;
				if( meshGroup.getAttributeValue("Name").equals("vegetation") ) 
					texturePath = filePath.replaceFirst( Util.extensionOf( filePath ), "png" );
				else 
					texturePath = filePath.replaceFirst( Util.extensionOf( filePath ), "jpg" );
				
				cachedMeshes.add( new CachedMesh( id, filePath, texturePath ) );
			}
		}
	}
	
	private void loadSkyTextures() {
		skyTextures = new SkyTextures();
		Element sky;
		
		try{ 
			sky = xmlRoot.getChild("Skymesh");
			skyTextures.up = dataDirectory + sky.getAttributeValue("TexUp");
			skyTextures.down = dataDirectory + sky.getAttributeValue("TexDown");
			skyTextures.south = dataDirectory + sky.getAttributeValue("TexFront");
			skyTextures.north = dataDirectory + sky.getAttributeValue("TexBack");
			skyTextures.east = dataDirectory + sky.getAttributeValue("TexLeft");
			skyTextures.west = dataDirectory + sky.getAttributeValue("TexRight");
		} catch (Exception e) {
			return;
		}
	}
	
	public List<CachedMesh> getCachedMeshes() {
		return cachedMeshes;
	}
	
	public RawHeightMap getHeightmap() {
		return heightmap;
	}

	public int getHeightmapSize() {
		return heightmapSize;
	}

	public Vector3f getScale() {
		return scale;
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public List<TerrainLayer> getTerrainLayers() {
		return terrainLayers;
	}
	
	public SkyTextures getSkyTextures() {
		return skyTextures;
	}
	
	public float getWaterHeight() {
		return waterHeight;
	}
	
	public class TerrainLayer {
		String texture;
		String alpha;
		
		TerrainLayer( String texture, String alpha ) {
			this.texture = texture;
			this.alpha = alpha;
		}
	}
	
	public class CachedMesh {
		String id;
		String modelPath;
		String texturePath;
		
		CachedMesh( String id, String modelPath, String texturePath ) {
			this.id = id;
			this.modelPath = modelPath;
			this.texturePath = texturePath;
		}
	}
	
	public class Item {
		Vector3f position;
		Quaternion rotation;
		Vector3f scale;
		String meshId;
		String layer;
		
		public Item() {
			this.position = new Vector3f();
			this.rotation = new Quaternion();
			this.scale = new Vector3f();
		}
	}
	
	public class SkyTextures {
		String up, down, north, south, west, east;
	}
}