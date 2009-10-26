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
import com.jmex.physics.Joint;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.impl.ode.joints.HingeJointAxis;
import com.jmex.physics.material.Material;
import joework.app.AssHoleBaseGame;

/**
 *
 * @author joseph
 */
public class testPhysicsSpheres extends AssHoleBaseGame {

    StaticPhysicsNode staticNode;

    public static void main( String args[] ) {
        testPhysicsSpheres m = new testPhysicsSpheres();
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
        Box floor = new Box("piano", Vector3f.ZERO, new Vector3f(200, .2f, 200));
        floor.setRandomColors();

        staticNode.attachChild(floor);
        staticNode.generatePhysicsGeometry();
    }

    @Override
    protected void setupCharacters() {
        // Creo una serie di sfere
        int i = 0;
        for ( i = 0; i < 5; i++ ) {
            // Creo la geometria delle sfere
            Sphere sfera = new Sphere("Sfera"+i, 20, 20, 5);
            sfera.updateModelBound();

            // Creo il nodo fisico delle sfere
            DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
            dynamicNode.attachChild(sfera);
            dynamicNode.generatePhysicsGeometry();
            dynamicNode.getLocalTranslation().set((i+1)*10, 10, 100);
            dynamicNode.setMaterial(Material.GLASS);
            dynamicNode.setMass(100);
            dynamicNode.computeMass();

            // Creo la joint
            Joint sphereJoint = getPhysicsSpace().createJoint();
            // Aggiunta di un asse di rotazione per la joint
            RotationalJointAxis sphereJointAxis = sphereJoint.createRotationalAxis();
            // Impostiamo l'asse di rotazione
            sphereJointAxis.setDirection(new Vector3f(0,0,1));
            // Attacchiamo la sfera al Joint
            sphereJoint.attach(dynamicNode);
            // Ancoriamo la Joint
            sphereJoint.setAnchor(new Vector3f((i+1)*10, 50, 100));

            rootNode.attachChild(dynamicNode);
        }

        // Creo la sfera che colpirà le altre
        // Creo la geometria delle sfere
        Sphere sfera = new Sphere("Sfera"+i, 20, 20, 5);
        sfera.updateModelBound();

        // Creo il nodo fisico delle sfere
        DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
        dynamicNode.attachChild(sfera);
        dynamicNode.generatePhysicsGeometry();
        dynamicNode.getLocalTranslation().set((i+1)*16.72f, 50, 100);
        dynamicNode.setMaterial(Material.GLASS);
        dynamicNode.setMass(100);
        dynamicNode.computeMass();

        // Creo la joint
        Joint sphereJoint = getPhysicsSpace().createJoint();
        // Aggiunta di un asse di rotazione per la joint
        RotationalJointAxis sphereJointAxis = sphereJoint.createRotationalAxis();
        // Impostiamo l'asse di rotazione
        sphereJointAxis.setDirection(new Vector3f(0,0,1));
        // Attacchiamo la sfera al Joint
        sphereJoint.attach(dynamicNode);
        // Ancoriamo la Joint
        sphereJoint.setAnchor(new Vector3f((i+1)*10, 50, 100));

        rootNode.attachChild(dynamicNode);

    }

    @Override
    protected void setupPlayer() {
        // todo
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
    protected void simpleUpdate() {
        input.update(tpf);
        //characterController.update(tpf);
    }
}
