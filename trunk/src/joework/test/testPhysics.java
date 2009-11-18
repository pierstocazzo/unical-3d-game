/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.test;

import com.jme.input.FirstPersonHandler;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import game.base.CustomGame;

/**
 *
 * @author joseph
 */
public class testPhysics extends CustomGame {

    StaticPhysicsNode staticNode;
    DynamicPhysicsNode dynamicNode;

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
    protected void setupPlayer() {
        Sphere player = new Sphere("player", 20, 20, 5);
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
    }

    @Override
    protected void update() {
        input.update(tpf);
        //characterController.update(tpf);
    }

    @Override
    protected void setupEnemies() {
        // todo
    }
}
