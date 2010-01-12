package game.graphics;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
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

/** Classe per la lettura del file xml generato dall'editor di scena FreeWorld3D<br>
 * 
 * Libreria necessaria: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author slash17
 */
public class Scene {

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
	
	/** hashmap containing the id and the model's path of each cashed mesh */
	HashMap<String, String> cachedMeshes;
	
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
	 * 
	 */
	private void loadTerrainLayers() {
		// TODO load terrain layers
	}
	
	/**
	 *  Create a list of all items of the scene with their position, scale and rotation 
	 */
	@SuppressWarnings("unchecked")
	private void loadItems() {
		items = new LinkedList<Item>();
		
		/* Il file generato da FW3D contiene diversi elementi, quello che ci interessa �
		 * l'elemento SceneLayer figlio di SceneLayers, a sua volta figlio di root
		 * SceneLayer contiene tutte le informazioni sui modelli 3d, posizione, rotazione, scala
		 */
		Element sceneLayer = xmlRoot.getChild("SceneLayers").getChild("SceneLayer");
		
		/* prendo tutti gli elementi figli di SceneLayer (ovvero le info sui modelli 3d) */
		List<Element> children = sceneLayer.getChildren();
		Iterator<Element> it = children.iterator();
		
		/* scorro gli elementi */
		while( it.hasNext() ) {
			Element curr = it.next();
			
			Item item = new Item();
			
			/* estrazione del vettore posizione dell'oggetto */
			
			String positionString = curr.getAttributeValue("Position");
			String[] positionArray = positionString.split(",");
			
			Vector3f position = new Vector3f();
			position.x = Float.valueOf( positionArray[0] );
			position.y = Float.valueOf( positionArray[1] );
			position.z = Float.valueOf( positionArray[2] );
			
			item.position = position;
			
			/* estrazione del quaternione che rappresenta la rotazione dell'oggetto */
			
			String rotationString = curr.getAttributeValue("Quat");
			String[] rotationArray = rotationString.split(",");
			
			Quaternion rotation = new Quaternion();
			rotation.x = Float.valueOf( rotationArray[0] );
			rotation.y = Float.valueOf( rotationArray[1] );
			rotation.z = Float.valueOf( rotationArray[2] );
			rotation.w = Float.valueOf( rotationArray[3] );
			
			item.rotation = rotation;
			
			/* estrazione del vettore di ridimensionamento dell'oggetto */
			
			String scaleString = curr.getAttributeValue("Scale");
			String[] scaleArray = scaleString.split(",");
			
			Vector3f scale = new Vector3f();
			scale.x = Float.valueOf( scaleArray[0] );
			scale.y = Float.valueOf( scaleArray[1] );
			scale.z = Float.valueOf( scaleArray[2] );
			
			item.scale = scale;
			
			item.meshId = curr.getAttributeValue("CachedMeshId");
			
			/* creo un nuovo item e lo aggiungo alla mia lista */
			items.add( item );
		}
	}
	
	/** Function loadCashedMeshes() <p>
	 */
	@SuppressWarnings("unchecked")
	private void loadCashedMeshes() {
		cachedMeshes = new HashMap<String, String>();
		
		Element meshGroup = xmlRoot.getChild("CachedMeshGroups").getChild("CachedMeshGroup");
		Iterator<Element> it = meshGroup.getChildren().iterator();
		
		while( it.hasNext() ) {
			Element mesh = it.next();
			String id = mesh.getAttributeValue("Id");
			String filePath = dataDirectory + "/models/environment/" + mesh.getAttributeValue("Name");
			cachedMeshes.put( id, filePath );
		}
	}
	
	public HashMap<String, String> getCachedMeshes() {
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
}