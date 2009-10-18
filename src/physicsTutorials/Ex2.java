/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package physicsTutorials;

import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;

/** Esempio 2 
 * 	caduta di quattro box di materiali diversi su un piano inclinato
*/
public class Ex2 extends SimplePhysicsGame {
	protected void simpleInitGame() {
		// creo il nodo fisico statico del piano
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( staticNode );

		// creo due piani
        final Box floor1 = new Box( "floor", new Vector3f(), 50, 0.25f, 50 );
        final Box floor2 = new Box( "floor", new Vector3f(), 50, 0.25f, 50 );
        
        // impostazione texture (la cartella images dev'essere nella src!)
        Texture texture = TextureManager.loadTexture(
                Ex2.class.getClassLoader().getResource("images/Fieldstone.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(texture);
        
        // imposto la texture a entrambi i piani
        floor1.setRenderState(ts);
        floor2.setRenderState(ts);

        // li attacco al nodo fisico statico
        staticNode.attachChild( floor1 );
        staticNode.attachChild( floor2 );

        // inclino il primo piano e sposto il secondo contiguo al primo
        floor1.getLocalRotation().fromAngleNormalAxis(0.3f, new Vector3f(0, 0, -1));
        floor2.getLocalTranslation().set(97, -15, 0);
        
        // genero la geometria fisica
        staticNode.generatePhysicsGeometry();
        
        // creo un primo box di materiale default (colore grigio)
        final DynamicPhysicsNode dynamicNode = createBox();        
        dynamicNode.getLocalTranslation().set( 0, 5, -5 );
        
        // creo un secondo box di materiale ice (colore blu)
        final DynamicPhysicsNode iceQ = createBox();
        iceQ.setMaterial( Material.ICE );
        iceQ.computeMass();
        color( iceQ, new ColorRGBA( 0.5f, 0.5f, 0.9f, 1 ) );
        iceQ.getLocalTranslation().set( 0, 5, 0 );
        
        // creo un terzo box di materiale rubber (colore giallo)
        final DynamicPhysicsNode rubber = createBox();
        rubber.setMaterial( Material.RUBBER );
        rubber.computeMass();
        color( rubber, new ColorRGBA( 0.9f, 0.9f, 0.3f, 1 ) );
        rubber.getLocalTranslation().set( 0, 5, 5 );

        // creo mio materiale, imposto densità ecc
        final Material customMaterial = new Material( "supra-stopper" );
        customMaterial.setDensity( 0.05f );
        MutableContactInfo contactDetails = new MutableContactInfo();
        contactDetails.setBounce( 0 );
        contactDetails.setMu( 1000 );
        customMaterial.putContactHandlingDetails( Material.DEFAULT, contactDetails );
        
        // creo un quarto box del mio materiale (colore rosso)
        final DynamicPhysicsNode stopper = createBox();
        stopper.setMaterial( customMaterial );
        stopper.computeMass();
        color( stopper, new ColorRGBA( 1, 0, 0, 1 ) );
        stopper.getLocalTranslation().set( 0, 5, 10 );

        // parte in pausa, per iniziare premi P
        pause = true;
    }

	/** Funzione che colora uno Spatial
	 * 
	 * @param spatial
	 * @param color
	 */
    private void color( Spatial spatial, ColorRGBA color ) {
        final MaterialState materialState = display.getRenderer().createMaterialState();
        materialState.setDiffuse( color );
        spatial.setRenderState( materialState );
	}
    
    /** Funzione che crea un box (dimensioni (1,1,1) centrato nell'origine) e lo attacca al rootNode
     * 
     * @return DynamicPhysicsNode
     */
    private DynamicPhysicsNode createBox() {
        DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( dynamicNode );
        final Box visualFallingBox = new Box( "falling box", new Vector3f(), 1, 1, 1 );
        dynamicNode.attachChild( visualFallingBox );
        dynamicNode.generatePhysicsGeometry();
        return dynamicNode;
	}
    
	public static void main( String[] args ) {
        Logger.getLogger( "" ).setLevel( Level.WARNING ); // to see the important stuff
        new Ex2().start();
    }
}