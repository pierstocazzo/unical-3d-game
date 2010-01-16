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
			Logger.getAnonymousLogger().log( Level.SEVERE, 
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
		Element terrain = xmlRoot.getChild( "Terrain" );
		heightmapSize = Integer.valueOf( terrain.getAttributeValue( "Size" ) );
		scale = new Vector3f();
		scale.x = Float.valueOf( terrain.getAttributeValue( "Step" ) );
		scale.y = Float.valueOf( terrain.getChild( "Heightmap" ).getAttributeValue( "Scale" ) );
		scale.z = scale.x;
		URL heightmapURL = Loader.load( dataDirectory + 
				terrain.getChild( "Heightmap" ).getAttributeValue( "File" ) );
		heightmap = new RawHeightMap( heightmapURL, heightmapSize, RawHeightMap.FORMAT_16BITLE, false );
		
		loadTerrainLayers();
		loadCashedMeshes();
		loadItems();
	}

	/** 
	 * Create a list of the terrain layers to apply
	 */
	@SuppressWarnings("unchecked")
	private void loadTerrainLayers() {
		terrainLayers = new LinkedList<TerrainLayer>();
		
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
	}
	
	/**
	 *  Create a list of all items of the scene with their position, scale and rotation 
	 */
	@SuppressWarnings("unchecked")
	private void loadItems() {
		items = new LinkedList<Item>();
		
		/* The xml file exported from FW3D contains lot of things.
		 * here we just want to list all the items in the scene, with their
		 * position, scale and rotation
		 * so we need to look at the SceneLayers
		 */
		Element sceneLayer = xmlRoot.getChild("SceneLayers").getChild("SceneLayer");
		
		/* catch all children of the sceneLayer (they are the models to load */
		List<Element> children = sceneLayer.getChildren();
		
		for( Element curr : children ) {
			Item item = new Item();
			
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
		}
	}
	
	/** Function loadCashedMeshes() <p>
	 */
	@SuppressWarnings("unchecked")
	private void loadCashedMeshes() {
		cachedMeshes = new LinkedList<CachedMesh>();
		
		Element meshGroup = xmlRoot.getChild("CachedMeshGroups").getChild("CachedMeshGroup");
		Iterator<Element> it = meshGroup.getChildren().iterator();
		
		while( it.hasNext() ) {
			Element mesh = it.next();
			String id = mesh.getAttributeValue("Id");
			String filePath = dataDirectory + "/models/environment/" + mesh.getAttributeValue("Name");
			cachedMeshes.add( new CachedMesh( id, filePath ) );
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
		
		CachedMesh( String id, String modelPath ) {
			this.id = id;
			this.modelPath = modelPath;
		}
	}
	
	public class Item {
		Vector3f position;
		Quaternion rotation;
		Vector3f scale;
		String meshId;
		
		public Item() {
			this.position = new Vector3f();
			this.rotation = new Quaternion();
			this.scale = new Vector3f();
		}
	}
}