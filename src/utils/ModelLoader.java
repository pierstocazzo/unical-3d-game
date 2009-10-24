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
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.xml.XMLImporter;
import com.jmex.model.converters.AseToJme;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.ObjToJme;

/**
 * Classe statica che permette di caricare un modello 3d ed eventualmente 
 * applicargli una texture, scalarlo o ruotarlo. <br>
 * Modelli supportati: 3ds, md2, md3, obj, ase, xml, jme<br>
 * 
 * <br>Esempio d'uso:<br>
 * <code>ModelLoader.loadModel("model.3ds", "texture.jpg", 1, new Quaternion())
 * </code>
 * 
 * @author slash17
 * @author andrea (l'idea era sua)
 */
public class ModelLoader {
    private static final Logger logger = Logger.getLogger(TestX3DLoading.class.getName());
	static FormatConverter converter;
	static XMLImporter xmlImporter;

	/** Funzione che permette di caricare un modello 3d ed eventualmente 
	 * applicargli una texture, scalarlo o ruotarlo. <br>
	 * Modelli supportati: 3ds, md2, md3, obj, ase, xml, jme. <br>
	 * Esempio d'uso:<br>
	 * <code>ModelLoader.loadModel("model.3ds", "texture.jpg", 1, new Quaternion())
	 * </code>
	 * 
	 * @param modelPath -> (String) il path del modello 3d 
	 * @param texturePath -> (String) il path della texture NB:<code>""</code> per non applicare nessuna texture
	 * @param scaleFactor -> (float) il fattore di ridimensionamento 
	 * del modello NB: 1 per non ridimensionare
	 * @param rotation -> (Quaternion) la rotazione che si vuole dare al 
	 * modello NB: <code>new Quaternion()</code> per non ruotare
	 * @return (Node) Ritorna il nodo a cui è attaccato il modello 
	 */
	public static Node loadModel( String modelPath, String texturePath, float scaleFactor, Quaternion rotation ){

		Node model = null;
		converter = null;
		xmlImporter = null;

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
			else if ( Pattern.matches(".+.[xX][mM][lL]", modelPath) ) {
				logger.info("Loading xml file...");
				xmlImporter = new XMLImporter();
			}
			else if ( Pattern.matches(".+.[jJ][mM][eE]", modelPath) ) {
				logger.info("Loading jme file...");
			}
			else throw new MalformedObjectNameException();
			
			// model loading
			
			URL modelUrl = ModelLoader.class.getClassLoader().getResource( modelPath );
			
			if( converter != null ) {
				ByteArrayOutputStream BO = new ByteArrayOutputStream();
				if( converter instanceof ObjToJme )
					converter.setProperty("mtllib", modelUrl );
				converter.convert( modelUrl.openStream(),BO);
				model = (Node)BinaryImporter.getInstance().load( new ByteArrayInputStream( BO.toByteArray() ) );
			}
			else if( xmlImporter != null )
				model = (Node) xmlImporter.load( modelUrl );				
			else 
				model = (Node) BinaryImporter.getInstance().load( modelUrl );
			
			
			logger.info("Model loaded");
			
			model.setLocalScale( scaleFactor );
			model.setLocalRotation( rotation );
			
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			
			if( Pattern.matches( ".+", texturePath )) {
				Texture texture = TextureManager.loadTexture(
		                ModelLoader.class.getClassLoader().getResource( texturePath ),
		                Texture.MinificationFilter.Trilinear,
		                Texture.MagnificationFilter.Bilinear);
	
		        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		        ts.setEnabled(true);
		        ts.setTexture(texture);
				model.setRenderState( ts );
				
				logger.info( "Texture " + texturePath + " applied" );
			}
			else logger.info( "No texture to apply" );
			
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
