package physicsTutorials;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

public class Ex6 extends SimplePhysicsGame {
	
    private static final Vector3f ZERO = new Vector3f( 0, 0, 0 );
    private StaticPhysicsNode floor;
    private DynamicPhysicsNode player;
    private boolean playerOnFloor = false;
	
    protected void simpleInitGame() {
        // creo dei piani su cui giocare
        createFloor();

        // creo il box player
        player = createBox();
        player.setName( "player" );
        color( player, new ColorRGBA( 0, 0, 1, 1 ) );
        // lo sistemo al centro del primo piano
        player.getLocalTranslation().set( 8, 1, 0 );
        // gli sposto il centro di massa un po più giù
        player.computeMass();
        player.setCenterOfMass( new Vector3f( 0, -0.5f, 0 ) );
        // per il movimento del box, gli impostiamo un materiale custum, a cui setteremo il surfacemotion
        final Material playerMaterial = new Material( "player material" );
        player.setMaterial( playerMaterial );
        // vedi MoveAction

        // i quattro movimenti Home - Fine - Canc - PgGiù
        input.addAction( new MoveAction( new Vector3f( -2, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_DELETE, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 2, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_PGDN, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 0, 0, -2 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_HOME, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 0, 0, 2 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_END, InputHandler.AXIS_NONE, false );

        // per far saltare il nostro box applichiamo una forza verso l'alto (per il salto premere SPAZIO)
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( playerOnFloor && evt.getTriggerPressed() ) {
                    player.addForce( new Vector3f( 0, 500, 0 ) );
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false );

        // per capire quando il box è poggiato sul piano, usiamo una variabile booleana playerOnFloor
        // se piano e box hanno colliso il nostro box si trova sul piano, quindi impostiamo playerOnFloor a true

        // evento di collisione
        SyntheticButton playerCollisionEventHandler = player.getCollisionEventHandler();
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == floor || contactInfo.getNode2() == floor ) {
                    playerOnFloor = true;
                }
            }
        }, playerCollisionEventHandler, false );

        // ad ogni step fisico reimpostiamo a false playerOnFloor
        // in modo da non permettere salti quando si è già in aria
        getPhysicsSpace().addToUpdateCallbacks( new PhysicsUpdateCallback() {
            public void beforeStep( PhysicsSpace space, float time ) {
                playerOnFloor = false;
            }
            public void afterStep( PhysicsSpace space, float time ) {
            }
        } );
    }

    private void createFloor() {
        // creiamo il primo box del piano
        floor = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( floor );
        final Box visualFloorBox = new Box( "floor", new Vector3f(), 5, 0.25f, 5 );
        floor.attachChild( visualFloorBox );
        // il secondo, leggermente inclinato, a trampolino
        visualFloorBox.getLocalRotation().fromAngleNormalAxis( 0.1f, new Vector3f( 0, 0, -1 ) );
        final Box visualFloorBox2 = new Box( "floor", new Vector3f(), 5, 0.25f, 5 );
        floor.attachChild( visualFloorBox2 );
        visualFloorBox2.getLocalTranslation().set( 9.7f, -0.5f, 0 );
        // e il terzo, dopo il trampolino
        final Box visualFloorBox3 = new Box( "floor", new Vector3f(), 5, 0.25f, 5 );
        floor.attachChild( visualFloorBox3 );
        visualFloorBox3.getLocalTranslation().set( -11, 0, 0 );
        
        floor.generatePhysicsGeometry();
    }

    /**
     * Classe per gestire i movimenti
     * non molto diversa da quella già vista
     */
    private class MoveAction extends InputAction {
        private Vector3f direction;

        public MoveAction( Vector3f direction ) {
            this.direction = direction;
        }
        public void performAction( InputActionEvent evt ) {
            if ( evt.getTriggerPressed() ) {
                // quando viene premuto un testo, impostiamo il movimento nella direzione passata al costruttore
                player.getMaterial().setSurfaceMotion( direction );
            } else {
                // appena rilasciamo il tasto il movimento si azzera...
                player.getMaterial().setSurfaceMotion( ZERO );
            }
        }
    }

    // metodi già visti in precedenza
    private void color( Spatial spatial, ColorRGBA color ) {
        final MaterialState materialState = display.getRenderer().createMaterialState();
        materialState.setDiffuse( color );
        spatial.setRenderState( materialState );
    }

    private DynamicPhysicsNode createBox() {
        DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( dynamicNode );
        final Box visualFallingBox = new Box( "falling box", new Vector3f(), 0.5f, 0.5f, 0.5f );
        dynamicNode.attachChild( visualFallingBox );
        dynamicNode.generatePhysicsGeometry();
        return dynamicNode;
    }

    @Override
    protected void simpleUpdate() {
        // semplice modo per far seguire il player dalla camera...proprio quello che ci serve!
        cam.getLocation().x = player.getLocalTranslation().x;
        cam.update();
    }

    public static void main( String[] args ) {
        Logger.getLogger( "" ).setLevel( Level.WARNING ); // to see the important stuff
        new Ex6().start();
    }
}
