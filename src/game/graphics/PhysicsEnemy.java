package game.graphics;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.material.Material;

public class PhysicsEnemy {
	String id;
	
	DynamicPhysicsNode model;
	
	public PhysicsEnemy( String id, GraficalWorld world ) {
		this.id = id;
		
		/** per ora i nemici sono dei box! */
		Box box = new Box("box", new Vector3f(0, 0, 0), 2,2,2);
		box.setModelBound( new BoundingBox() );
		box.updateModelBound();
		
		model = world.getPhysicsSpace().createDynamicNode();

		model.attachChild( box );
		model.generatePhysicsGeometry();
		model.setMass( 10000 );
		model.setMaterial( Material.IRON );
	}
}
