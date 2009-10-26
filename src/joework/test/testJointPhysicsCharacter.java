/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package joework.test;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.material.Material;
import joework.app.AssHoleBaseGame;
import joework.input.PhysicsInputHandler;

/**
 *
 * @author joseph
 */
public class testJointPhysicsCharacter extends AssHoleBaseGame {

    PhysicsInputHandler myInput;
    StaticPhysicsNode staticNode;
    DynamicPhysicsNode dynamicNode;

    public static void main( String args[] ) {
        testJointPhysicsCharacter m = new testJointPhysicsCharacter();
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
        Quaternion quat = new Quaternion().fromAngleAxis(FastMath.PI/6, new Vector3f(1,0,0));

        floor.setRandomColors();

        staticNode.attachChild(floor);
        staticNode.setLocalRotation(quat);
        staticNode.generatePhysicsGeometry();
    }

    @Override
    protected void setupCharacters() {
        // todo
    }

    @Override
    protected void setupPlayer() {
        
        MutableContactInfo contactMaterial = new MutableContactInfo();
        contactMaterial.setBounce(0);
        contactMaterial.setMu(10);
        contactMaterial.setDampingCoefficient(0);

        Material characterMaterial = new Material("materiale_personaggio");
        characterMaterial.setDensity(0.95f);
        characterMaterial.putContactHandlingDetails(characterMaterial, contactMaterial);
        

        Sphere player = new Sphere("player", 20, 20, 5);
        player.setRandomColors();

        dynamicNode.setLocalTranslation(100, 50, 100);
        
        dynamicNode.attachChild(player);
        dynamicNode.generatePhysicsGeometry();
        dynamicNode.setMaterial(characterMaterial);
        //dynamicNode.setMass(1);
        dynamicNode.computeMass();

    }

    @Override
    protected void setupCamera() {
        cam.setLocation(new Vector3f(100,30,200));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, 1000);
        cam.update();
    }

    @Override
    protected void setupInput() {
        myInput = new PhysicsInputHandler(dynamicNode, cam); // todo: Attach the terrain too

        // evento di collisione
        SyntheticButton playerCollisionEventHandler = dynamicNode.getCollisionEventHandler();
        myInput.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == staticNode || contactInfo.getNode2() == staticNode ) {
                    myInput.setTargetOnFloor(true);
                }
            }
        }, playerCollisionEventHandler, false );
    }

    @Override
    protected void simpleUpdate() {
        myInput.update(tpf);
    }
}
