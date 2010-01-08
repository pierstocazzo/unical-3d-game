package utils;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/** Classe di test per la lettura del file xml generato da FreeWorld3D<br>
 * In questo test leggo e stampo a video la posizione di ogni mesh caricata in FW3D.
 * Come potete immaginare è la base del sistema di caricamento della scena da file xml.
 * In modo del tutto simile si può leggere ovviamente qualsiasi file xml, lo useremo anche
 * per il nostro game.cfg contenente tutti i parametri di gioco.
 * 
 * Libreria necessaria: <a href="http://www.jdom.org">JDOM</a>
 * 
 * @author slash17
 */
public class XmlReader {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			/* creo un builder attraverso cui carico il file xml in un oggetto Document */
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build( new File("src/utils/world.cfg") );
			
			/* salvo l'elemento root del file xml */
			Element root = doc.getRootElement();
			
			/* prendo l'elemento SceneLayer figlio di SceneLayers, a sua volta figlio di root
			 * in SceneLayer FW3D mette le informazioni sui modelli 3d, posizione, rotazione, scala
			 */
			Element sceneLayer = root.getChild("SceneLayers").getChild("SceneLayer");
			
			/* prendo tutti gli elementi figli di SceneLayer (ovvero i modelli 3d) */
			List<Element> children = sceneLayer.getChildren();
			Iterator<Element> it = children.iterator();
			
			while( it.hasNext() ) {
				/* per ogni modello 3d ne stampo la posizione */
				Element curr = it.next();
				System.out.println( "name = " + curr.getAttributeValue("Name") +
									"\nposition = " + curr.getAttributeValue("Position") );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
