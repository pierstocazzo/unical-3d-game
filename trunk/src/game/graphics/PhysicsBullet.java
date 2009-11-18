package game.graphics;

import game.core.EnumWeaponType;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.geometry.PhysicsSphere;

public class PhysicsBullet {
	
	String id;
	
	Node bullet;
	DynamicPhysicsNode physicsBullet;
	PhysicsSphere bulletGeometry;
	
	InputHandler contactDetect = new InputHandler();
	
	GraphicalWorld world;
	Vector3f direction;
	EnumWeaponType weaponType;
	Vector3f initialPosition;
	
	
	public PhysicsBullet( String id, GraphicalWorld world, Vector3f direction, EnumWeaponType weaponType, Vector3f position ) {
		this.id = id;
		this.world = world;
		this.direction = new Vector3f( direction );
		this.initialPosition = new Vector3f( position );
		this.weaponType = weaponType;
		this.bullet = new Node( id );
		// Create the bullet Physics
		physicsBullet = world.getPhysicsSpace().createDynamicNode();
		physicsBullet.setName( id );
		bulletGeometry = physicsBullet.createSphere(id);
		bulletGeometry.setLocalScale( 1 );
		physicsBullet.attachChild(bulletGeometry);
		
		bullet.attachChild(physicsBullet);
		bullet.setLocalTranslation( initialPosition );
		
		world.getRootNode().attachChild( bullet );
		
		physicsBullet.generatePhysicsGeometry();

		physicsBullet.addForce( direction.mult( weaponType.getPower() ) );
		
		contactDetection();
	}
	
	void contactDetection() {
        SyntheticButton bulletCollisionEventHandler = physicsBullet.getCollisionEventHandler();
        contactDetect.addAction( new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                if ( contactInfo.getNode1() == physicsBullet || contactInfo.getNode2() == physicsBullet ) {
                	if( bullet.getParent() == world.getRootNode() ) {
	                	world.bullets.remove(id);
                		physicsBullet.clearDynamics();
	            		physicsBullet.detachAllChildren();
	            		physicsBullet.delete();
	            		bullet.detachAllChildren(); 
	            		world.getRootNode().detachChild( bullet );
                	}
                }
            }
        }, bulletCollisionEventHandler, false );
    }
	
	public void update( float time ) {
		contactDetect.update(time);
	}
}
