/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.test;

import com.jme.input.FirstPersonHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.terrain.TerrainBlock;
import game.base.CustomGame;
import java.util.logging.Logger;

/**
 *
 * @author joseph
 */
public class testPhysicsBullet extends CustomGame {
    private static final Logger logger = Logger.getLogger(testPhysicsBullet.class.getName());

    StaticPhysicsNode staticNode;
    TerrainBlock terrain;

    public static void main( String args[] ) {
        testPhysicsBullet m = new testPhysicsBullet();
        m.setConfigShowMode(ConfigShowMode.AlwaysShow);
        m.start();
    }

    @Override
    protected void setupInit() {
        staticNode = getPhysicsSpace().createStaticNode();

        rootNode.attachChild(staticNode);

        pause = true;
        showPhysics = true;
    }

    @Override
    protected void setupEnvironment() {
        Box floorGeometry = new Box("floor", Vector3f.ZERO, new Vector3f(200, .2f, 200));
        floorGeometry.setRandomColors();

        staticNode.attachChild(floorGeometry);
        staticNode.generatePhysicsGeometry();
    }

    @Override
    protected void setupPlayer() {
        // todo
    }

    @Override
    protected void setupCamera() {
        cam.setLocation(new Vector3f(100,30,100));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, 1000);
        cam.update();
    }

    @Override
    protected void setupChaseCamera() {
        // todo
    }

    @Override
    protected void setupInput() {
        input = new FirstPersonHandler(cam, 200, 1);

        InputAction fireAction = new FireAction();

        input.addAction( fireAction, "fireaction", KeyInput.KEY_F, false );

    }

    @Override
    protected void simpleUpdate() {
        input.update(tpf);
    }

    @Override
    protected void setupEnemies() {
        // todo
    }

    public class FireAction extends InputAction {
        int bullets = 0;
        
        public void performAction(InputActionEvent evt) {
            //if ( evt.getTriggerPressed() ) {
                bullets = bullets + 1;
                logger.info("Shoot: " + bullets);



                //Creo il bullet
                // Sphere bullet = new Sphere
            //}
        }
    }
}
