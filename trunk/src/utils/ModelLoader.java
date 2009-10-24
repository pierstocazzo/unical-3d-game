package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.management.MalformedObjectNameException;

import jmetest.renderer.loader.TestX3DLoading;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.AseToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.ObjToJme;

/**
 * Classe per il caricamento di modelli 3d.
 * Modelli supportati: 3ds, md2, md3, obj, ase<br>
 * 
 * <br>Esempio d'uso:
 * <pre><code>ModelLoader.loadModel( "data/model/bike.3ds", 1, new Quaternion() )
 * </code></pre>
 * 
 * @author slash17
 * @author andrea (l'idea era sua)
 */
public class ModelLoader {
    private static final Logger logger = Logger.getLogger(TestX3DLoading.class.getName());
	static FormatConverter converter;

	/** Funzione che permette di caricare un modello 3d ed eventualmente 
	 * scalarlo o ruotarlo. <br>
	 * Modelli supportati: 3ds, md2, md3, obj, ase. <br>
	 * Esempio d'uso:
	 * <pre><code>ModelLoader.loadModel( "data/model/bike.3ds", 1, new Quaternion() )
	 * </code></pre>
	 * 
	 * @param modelPath -> (String) il path del modello 3d 
	 * @param scaleFactor -> (float) il fattore di ridimensionamento 
	 * del modello NB: 1 per non ridimensionare
	 * @param rotation -> (Quaternion) la rotazione che si vuole dare al 
	 * modello NB: <code>new Quaternion()</code> per non ruotare
	 * @return (Node) Ritorna il nodo a cui è attaccato il modello 
	 */
	public static Node loadModel( String modelPath, float scaleFactor, Quaternion rotation ){

		Node model = null;
		/* 
		 * In questa parte controllo che tipo di file devo convertire e 
		 * setto il convertitore adatto
		 */
		try {
			if ( Pattern.matches(".+.[mM][dD]3", modelPath) ) {
				logger.info("Loading md3 file...");
				converter = new Md3ToJme();
			}	
			else if ( Pattern.matches(".+.[mM][dD]2", modelPath) ) {
				logger.info("Loading md2 file...");
				converter =  new Md2ToJme();
			}	
			else if ( Pattern.matches(".+.3[dD][sS]", modelPath) ) {
				logger.info("Loading 3ds file...");
				converter = new MaxToJme();
			}	
			else if ( Pattern.matches(".+.[oO][bB][jJ]", modelPath) ) {
				logger.info("Loading obj file...");
				converter = new ObjToJme();
			}	
			else if ( Pattern.matches(".+.[aA][sS][eE]", modelPath) ) {
				logger.info("Loading ase file...");
				converter = new AseToJme();
			}	
			else throw new MalformedObjectNameException();
			
			// carico il modello
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			URL modelUrl = ModelLoader.class.getClassLoader().getResource( modelPath );

			converter.convert( modelUrl.openStream(),BO);
			model = (Node)BinaryImporter.getInstance().load( new ByteArrayInputStream( BO.toByteArray() ) );
			
			logger.info("DONE: Model loaded");
			
			model.setLocalScale( scaleFactor );
			model.setLocalRotation( rotation );
			
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			
		} catch (NullPointerException e) {
			logger.log(Level.SEVERE, "Failed to load model\nMake sure the path you entered is correct\n" + e);
			System.exit(0);
		} catch (MalformedObjectNameException e) {
			logger.log(Level.SEVERE, "You are trying to load an unsupported model\n");
			System.exit(0);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Load Failed\n");
			System.exit(0);
		}
		
		return model;
	}

}
