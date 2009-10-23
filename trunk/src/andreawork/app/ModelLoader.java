package andreawork.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

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
		 * NB: la stampa è solo momentanea per debug, in seguito meglio
		 * usare un logger
		 * NB2: andrebbero gestite le exeptions
		 */
		if ( Pattern.matches(".+.[mM][dD]3", modelPath) ) {
			System.out.println("E' un md3");
			converter = new Md3ToJme();
		}	
		if ( Pattern.matches(".+.[mM][dD]2", modelPath) ) {
			System.out.println("E' un md2");
			converter =  new Md2ToJme();
		}	
		if ( Pattern.matches(".+.3[dD][sS]", modelPath) ) {
			System.out.println("E' un 3ds");
			converter = new MaxToJme();
		}	
		if ( Pattern.matches(".+.[oO][bB][jJ]", modelPath) ) {
			System.out.println("E' un obj");
			converter = new ObjToJme();
		}	
		if ( Pattern.matches(".+.[aA][sS][eE]", modelPath) ) {
			System.out.println("E' un ase");
			converter = new AseToJme();
		}	
		// carico il modello
		try {
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			URL modelUrl = ModelLoader.class.getClassLoader().getResource( modelPath );

			converter.convert( modelUrl.openStream(),BO);
			model = (Node)BinaryImporter.getInstance().load( new ByteArrayInputStream( BO.toByteArray() ) );

			model.setLocalScale( scaleFactor );
			model.setLocalRotation( rotation );
			
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return model;
	}

}
