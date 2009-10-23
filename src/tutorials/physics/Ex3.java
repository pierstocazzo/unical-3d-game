package tutorials.physics;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.util.SimplePhysicsGame;

public class Ex3 extends SimplePhysicsGame {
    private DynamicPhysicsNode dynamicNode;
	private InputHandler physicsStepInputHandler;

    protected void simpleInitGame() {
        // creo un piano come prima
        StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( staticNode );
        Vector3f v = new Vector3f();
        final Box piano = new Box("piano", v, 50, 1, 50);
        staticNode.attachChild( piano );
        staticNode.generatePhysicsGeometry();

        // creo una sfera che cade sul piano
        dynamicNode = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( dynamicNode );
        final Sphere sfera = new Sphere( "sfera", new Vector3f(), 30, 30, 5 );      
        dynamicNode.attachChild( sfera );        
        dynamicNode.generatePhysicsGeometry();
        dynamicNode.getLocalTranslation().set( 0, 5, 0 );

        // creo il mio inputHandler
        physicsStepInputHandler = new InputHandler();
        // faccio in modo che venga aggiornato ad ogni step
        getPhysicsSpace().addToUpdateCallbacks( new PhysicsUpdateCallback() {
			public void afterStep(PhysicsSpace space, float time) {
			}
			public void beforeStep(PhysicsSpace space, float time) {
				// aggiorna il mio inputHandler prima di compiere ogni step fisico
				physicsStepInputHandler.update( time );
			}
        } );

        // gestisco i movimenti della sfera
        /*					 (avanti)
         * 						8 
         * 		(sinistra) 4		6 (destra)
         * 						2 
         * 					(indietro)
         */					
        physicsStepInputHandler.addAction( new MyInputAction( new Vector3f( 1000, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_NUMPAD6, InputHandler.AXIS_NONE, true );
        
        physicsStepInputHandler.addAction( new MyInputAction( new Vector3f( -1000, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_NUMPAD4, InputHandler.AXIS_NONE, true );
        
        physicsStepInputHandler.addAction( new MyInputAction( new Vector3f( 0, 0, -1000 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_NUMPAD8, InputHandler.AXIS_NONE, true );
        
        physicsStepInputHandler.addAction( new MyInputAction( new Vector3f( 0,0, 1000 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_NUMPAD2, InputHandler.AXIS_NONE, true );

        //showPhysics = true;
    }

    /** Classe MyInputAction: estende InputAction
     * @override performAction
     * @author slash17
     * @param Vector3f direction
     * @param Vector3f force
     */
    private class MyInputAction extends InputAction {
    	Vector3f direction;
    	Vector3f force = new Vector3f();
    	
    	public MyInputAction( Vector3f vect ){
    		direction = vect;
    	}
    	// metodo semplice che assegna la forza al nostro oggetto
        public void performAction( InputActionEvent evt ) {
        	// la forza da assegnare è il vettore direzione moltiplicato per il tempo passato
        	force.set( direction ).multLocal( evt.getTime() );
            dynamicNode.addForce( force );
        }
    }

    @Override
    protected void simpleUpdate() {
    	// dopo la caduta dal piano, la sfera viene riportata su
        if ( dynamicNode.getLocalTranslation().y < -20 ) {
        	// gli tolgo qualsiasi forza
            dynamicNode.clearDynamics();
            // e la ripiazzo al solito posto 
            dynamicNode.getLocalTranslation().set( 0, 5, 0 );
        }
    }

    /**
     * The main method to allow starting this class as application.
     *
     * @param args command line arguments
     */
    public static void main( String[] args ) {
        Logger.getLogger( "" ).setLevel( Level.WARNING ); // to see the important stuff
        new Ex3().start();
    }
}
