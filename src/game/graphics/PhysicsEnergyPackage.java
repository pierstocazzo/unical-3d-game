package game.graphics;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

public class PhysicsEnergyPackage {
	
	String id;
	
	DynamicPhysicsNode physicsPack;
	
	Box pack;
	
	GraphicalWorld world;
	
	Vector3f position;
	
	InputHandler contactDetect;
	
	PhysicsEnergyPackage( String id, GraphicalWorld world, Vector3f position ) {
		this.id = id;
		this.world = world;
		this.position = new Vector3f( position );
		this.position.setY( 30 );
		this.contactDetect = new InputHandler();
		createPhysics();
		contactDetector();
	}
	
	public void createPhysics() {
		physicsPack = world.getPhysicsSpace().createDynamicNode();
		world.getRootNode().attachChild( physicsPack );
		pack = new Box( id, new Vector3f(), 1, 1, 1 );
		pack.setRandomColors();
		physicsPack.attachChild( pack );
		physicsPack.generatePhysicsGeometry();
		physicsPack.getLocalTranslation().set( position );
	}
	
	public void contactDetector() {
        SyntheticButton energyPackCollisionHandler = physicsPack.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                
                for( String playerId : world.getCore().getPlayersId() ) {
	                if ( contactInfo.getNode1() == world.characters.get(playerId).getCharacterFeet() || 
	                	 contactInfo.getNode2() == world.characters.get(playerId).getCharacterFeet() ) {
	                	
	                   world.getCore().catchEnergyPack( playerId, id );
	                   deletePackage();
	                }
                }
            }
        };
        
        contactDetect.addAction( collisionAction, energyPackCollisionHandler, false );
	}

	public void deletePackage() {
		world.getRootNode().detachChild( physicsPack );
		physicsPack.detachAllChildren();
		physicsPack.delete();
		
		world.energyPackages.remove( id );
	}

	public void update(float time) {
		contactDetect.update(time);
	}
}
