package physicsTutorials;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

/**
 * Learn how to use a physics collision as event in an Application.
 * <p/>
 * This class extends Lesson5, meaning it does everything that's done in Lesson5 plus the new stuff defined here.
 *
 * @author Irrisor
 */
public class Ex4 extends Ex3 {
    private StaticPhysicsNode lowerFloor;

    @Override
    protected void simpleInitGame() {
        // crea tutto ciò che viene creato nella Ex3
        super.simpleInitGame();

        // crea un piano sotto il vecchio piano
        lowerFloor = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( lowerFloor );
        lowerFloor.getLocalTranslation().set( 0, -10, 0 );
        final Box pianoInferiore = new Box("piano", new Vector3f(), 100, 0.5f, 100);
        lowerFloor.attachChild( pianoInferiore );
        lowerFloor.generatePhysicsGeometry();

        // creo un oggetto (SyntheticButton) che rileva collisioni di un qualche Spatial col piano inferiore
        final SyntheticButton collisionEventHandler = lowerFloor.getCollisionEventHandler();
        // lo aggiungo ai possibili eventi
        input.addAction( new MyCollisionAction(), collisionEventHandler, false );
    }

    /**
     * The main method to allow starting this class as application.
     *
     * @param args command line arguments
     */
    public static void main( String[] args ) {
        Logger.getLogger( "" ).setLevel( Level.WARNING ); // to see the important stuff
        new Ex4().start();
    }

    /**
     * Viene chiamata quando si verifica una collisione tra uno Spatial e il piano inferiore
     */
    private class MyCollisionAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
            // salvo i dati della collision
            final ContactInfo contactInfo = ( (ContactInfo) evt.getTriggerData() );
            DynamicPhysicsNode sphere;
            // se c'è stata una collisione tra il piano e la sfera...
            if ( contactInfo.getNode2() instanceof DynamicPhysicsNode ) {
                sphere = (DynamicPhysicsNode) contactInfo.getNode2();
            }
            else if ( contactInfo.getNode1() instanceof DynamicPhysicsNode ) {
                // se è stata tra sfera e piano...
                sphere = (DynamicPhysicsNode) contactInfo.getNode1();
            }
            else {
                // non dovrebbe succedere mai...
                return;
            }
            // infine viene rimessa la sfera che aveva colliso col piano inferiore alla sua posizione usuale
            sphere.clearDynamics();
            sphere.getLocalTranslation().set( 0, 5, 0 );
        }
    }
}
