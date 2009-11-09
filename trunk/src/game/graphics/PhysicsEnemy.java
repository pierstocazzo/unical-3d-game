package game.graphics;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.material.Material;

public class PhysicsEnemy {
	String id;
	
	DynamicPhysicsNode model;
	Vector3f position;
	
	public PhysicsEnemy( String id, GraphicalWorld world, Vector3f position) {
		this.id = id;
		
		/** per ora i nemici sono dei box! */
		Box box = new Box("box", new Vector3f(-1,-1,-1), new Vector3f(1,1,1));
		box.setModelBound( new BoundingBox() );
		box.updateModelBound();
		
		
		model = world.getPhysicsSpace().createDynamicNode();

		model.setLocalTranslation(position);
		
		model.attachChild( box );
		model.generatePhysicsGeometry();
		model.setMass( 10000 );
		model.setMaterial( Material.IRON );
	}
}
