package utils;
import game.common.Item;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/** Classe per la lettura del file xml generato dall'editor di scena FreeWorld3D<br>
 * 
 * Libreria necessaria: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author slash17
 */
public class SceneXmlReader {

	public static String DEFAULT_SCENE_FILE = "src/game/data/level/island.xml";
	
	@SuppressWarnings("unchecked")
	public static List getItems( String sceneFilePath ) {
		
		LinkedList<Item> items = new LinkedList<Item>();
		
		try {
			/* carico il file xml */
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build( new File( sceneFilePath ) );
			
			Element root = doc.getRootElement();
			
			/* Il file generato da FW3D contiene diversi elementi, quello che ci interessa è
			 * l'elemento SceneLayer figlio di SceneLayers, a sua volta figlio di root
			 * SceneLayer contiene tutte le informazioni sui modelli 3d, posizione, rotazione, scala
			 */
			Element sceneLayer = root.getChild("SceneLayers").getChild("SceneLayer");
			
			/* prendo tutti gli elementi figli di SceneLayer (ovvero le info sui modelli 3d) */
			List<Element> children = sceneLayer.getChildren();
			Iterator<Element> it = children.iterator();
			
			/* scorro gli elementi */
			while( it.hasNext() ) {
				Element curr = it.next();
				
				/* estrazione del nome del file */
				String fileName = curr.getAttributeValue("Name");
				
				/* estrazione del vettore posizione dell'oggetto */
				
				String positionString = curr.getAttributeValue("Position");
				String[] positionArray = positionString.split(",");
				
				Vector3f position = new Vector3f();
				position.x = Float.valueOf( positionArray[0] );
				position.y = Float.valueOf( positionArray[1] );
				position.z = Float.valueOf( positionArray[2] );
				
				/* estrazione del quaternione che rappresenta la rotazione dell'oggetto */
				
				String rotationString = curr.getAttributeValue("Quat");
				String[] rotationArray = rotationString.split(",");
				
				Quaternion rotation = new Quaternion();
				rotation.x = Float.valueOf( rotationArray[0] );
				rotation.y = Float.valueOf( rotationArray[1] );
				rotation.z = Float.valueOf( rotationArray[2] );
				rotation.w = Float.valueOf( rotationArray[3] );
				
				/* estrazione del vettore di ridimensionamento dell'oggetto */
				
				String scaleString = curr.getAttributeValue("Scale");
				String[] scaleArray = scaleString.split(",");
				
				Vector3f scale = new Vector3f();
				scale.x = Float.valueOf( scaleArray[0] );
				scale.y = Float.valueOf( scaleArray[1] );
				scale.z = Float.valueOf( scaleArray[2] );
				
				/* creo un nuovo item e lo aggiungo alla mia lista */
				items.push( new Item( fileName, position, rotation, scale ) );
			}
		} catch (Exception e) {
			Logger.getAnonymousLogger().log( Level.SEVERE, 
					"Error loading the scene file, make sure the path is correct" );
			getItems( DEFAULT_SCENE_FILE );
		}
		
		return items;
	}
}