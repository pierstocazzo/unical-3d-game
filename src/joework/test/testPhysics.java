/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.test;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import joework.app.AssHoleBaseGame;

/**
 *
 * @author joseph
 */
public class testPhysics extends AssHoleBaseGame {

    StaticPhysicsNode staticNode;
    DynamicPhysicsNode dynamicNode;
    private boolean playerOnFloor = false;

    public static void main( String args[] ) {
        testPhysics m = new testPhysics();
        m.setConfigShowMode(ConfigShowMode.AlwaysShow);
        m.start();
    }

    @Override
    protected void setupInit() {
        staticNode = getPhysicsSpace().createStaticNode();
        dynamicNode = getPhysicsSpace().createDynamicNode();

        rootNode.attachChild(staticNode);
        rootNode.attachChild(dynamicNode);

        pause = true;
        showPhysics = true;
    }

    @Override
    protected void setupEnvironment() {
        Box floor = new Box("piano", Vector3f.ZERO, new Vector3f(200, .2f, 200));
        floor.setRandomColors();

        staticNode.attachChild(floor);
        staticNode.generatePhysicsGeometry();
    }

    @Override
    protected void setupCharacters() {
        // todo
    }

    @Override
    protected void setupPlayer() {
        Box player = new Box("player", new Vector3f(-1, -1, -1), new Vector3f(1,1,1));
        player.setRandomColors();

        dynamicNode.setLocalTranslation(100, 50, 100);
        
        dynamicNode.attachChild(player);
        dynamicNode.generatePhysicsGeometry();

    }

    @Override
    protected void setupCamera() {
        cam.setLocation(new Vector3f(100,50,300));
        cam.update();
    }

    @Override
    protected void setupInput() {
        input = new FirstPersonHandler(cam, 200, 1);

        // i quattro movimenti Home - Fine - Canc - PgGi�
        input.addAction( new MoveAction( new Vector3f( -2, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_H, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 2, 0, 0 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_K, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 0, 0, -2 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_U, InputHandler.AXIS_NONE, false );
        input.addAction( new MoveAction( new Vector3f( 0, 0, 2 ) ),
                InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_J, InputHandler.AXIS_NONE, false );

        // per far saltare il nostro box applichiamo una forza verso l'alto (per il salto premere SPAZIO)
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                if ( playerOnFloor && evt.getTriggerPressed() ) {
                    dynamicNode.addForce( new Vector3f( 0, 500, 0 ) );
                }
            }
        }, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false );

        // evento di collisione
        SyntheticButton playerCollisionEventHandler = dynamicNode.getCollisionEventHandler();
        input.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == staticNode || contactInfo.getNode2() == staticNode ) {
                    playerOnFloor = true;
                }
            }
        }, playerCollisionEventHandler, false );

        // ad ogni step fisico reimpostiamo a false playerOnFloor
        // in modo da non permettere salti quando si � gi� in aria
        getPhysicsSpace().addToUpdateCallbacks( new PhysicsUpdateCallback() {
            public void beforeStep( PhysicsSpace space, float time ) {
                playerOnFloor = false;
            }
            public void afterStep( PhysicsSpace space, float time ) {
            }
        } );
    }

    @Override
    protected void simpleUpdate() {
        input.update(tpf);
    }


    /**
     * Classe per gestire i movimenti
     * non molto diversa da quella gi� vista
     */
    private class MoveAction extends InputAction {
        private Vector3f direction;

        public MoveAction( Vector3f direction ) {
            this.direction = direction;
        }
        public void performAction( InputActionEvent evt ) {
            if ( evt.getTriggerPressed() ) {
                // quando viene premuto un testo, impostiamo il movimento nella direzione passata al costruttore
                dynamicNode.addForce( direction.multLocal(2) );
            } else {
                // appena rilasciamo il tasto il movimento si azzera...
                //dynamicNode.getMaterial().setSurfaceMotion( Vector3f.ZERO );
            }
        }
    }
}
