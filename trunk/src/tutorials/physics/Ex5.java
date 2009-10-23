package tutorials.physics;

import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.TranslationalJointAxis;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

public class Ex5 extends SimplePhysicsGame {

	@Override
	protected void simpleInitGame() {
		// creo il mio piano e la mia sfera fisica
		StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
        rootNode.attachChild( staticNode );
        
        final Box floor = new Box("floor", new Vector3f(), 5, 0.25f, 5 );
        staticNode.attachChild( floor );
        staticNode.generatePhysicsGeometry();
        
        DynamicPhysicsNode dynamicSphereNode = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( dynamicSphereNode );  
        dynamicSphereNode.getLocalTranslation().set( 3.4f, 5, 0 );

        final Sphere sphere = new Sphere( "sphere", new Vector3f(), 30, 30, 1 );
        dynamicSphereNode.attachChild( sphere );
        dynamicSphereNode.generatePhysicsGeometry();
                
        // per legare la sfera all'ambiente uso un joint
        final Joint sphereJoint = getPhysicsSpace().createJoint();
        // creiamo un asse di rotazione per il notro joint (l'effetto desiderato è quello
        // di usare la sfera come un pendolo!
        final RotationalJointAxis rotationalAxis = sphereJoint.createRotationalAxis();
        // gli assegnamo la direzione z
        rotationalAxis.setDirection( new Vector3f( 0, 0, 1 ) );
        // attacchiamo la sfera al joint
        sphereJoint.attach( dynamicSphereNode );
        // ancoriamo il nostro joint ad un punto virtuale con y=5 (5 unità sopra il piano)
        sphereJoint.setAnchor( new Vector3f( 0, 5, 0 ) );
        
        // creiamo due box..
        DynamicPhysicsNode boxNode1 = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( boxNode1 );
        //boxNode1.createBox( "box1" );
        final Box box1 = new Box("box1", new Vector3f(), 0.5f, 0.5f, 0.5f );
        boxNode1.attachChild( box1 );
        boxNode1.generatePhysicsGeometry();
        DynamicPhysicsNode boxNode2 = getPhysicsSpace().createDynamicNode();
        rootNode.attachChild( boxNode2 );
        //boxNode2.createBox( "box1" );
        final Box box2 = new Box("box2", new Vector3f(), 0.5f, 0.5f, 0.5f );
        boxNode2.attachChild( box2 );
        boxNode2.generatePhysicsGeometry();
        
        // li posizioniamo...
        boxNode1.getLocalTranslation().set( 0, 1, 0 );
        boxNode2.getLocalTranslation().set( 0.7f, 1, 0 );
        //box1 molto leggero, box2 con pochissimo attrito (di ghiaccio!)
        boxNode1.setMass( 5 );
        boxNode2.setMaterial( Material.ICE );
        
        // creiamo un joint per i box
        final Joint boxJoint = getPhysicsSpace().createJoint();
        // creiamo un asse di traslazione con direzione x
        final TranslationalJointAxis translationalAxis = boxJoint.createTranslationalAxis();
        translationalAxis.setDirection( new Vector3f( 1, 0, 0 ) );
        // attacchiamo i due box al joint
        boxJoint.attach( boxNode1, boxNode2 );
        // impostiamo dei limiti di traslazione
        translationalAxis.setPositionMinimum( 0 );
        translationalAxis.setPositionMaximum( 10 );

        // godiamoci il doppio pendolo!
        
        
        showPhysics = true;
        pause = true;
	}
	
	public static void main(String[] args) {
		Ex5 object = new Ex5();
		object.start();
	}
}
