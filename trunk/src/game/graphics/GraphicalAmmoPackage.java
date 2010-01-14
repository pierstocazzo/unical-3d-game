package game.graphics;

import java.util.Iterator;

import game.HUD.HudMessageHandler;
import game.HUD.UserHud;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

/**
*/
public class GraphicalAmmoPackage {
 
	String id;
	
	DynamicPhysicsNode physicsPack;
	
	Box pack;
	
	GraphicalWorld world;
	
	Vector3f position;
	
	InputHandler contactDetect;
	
	boolean contact = false;

	boolean enabled;
	
	GraphicalAmmoPackage( String id, GraphicalWorld world, Vector3f position ) {
		this.id = id;
		this.world = world;
		this.position = new Vector3f( position );
		this.position.setY( position.getY() + 10 );
		this.contactDetect = new InputHandler();
		createPhysics();
		contactDetector();
		enabled = true;
	}
	
	public void createPhysics() {
		physicsPack = world.getPhysicsSpace().createDynamicNode();
		world.getRootNode().attachChild( physicsPack );
		physicsPack.getLocalTranslation().set( position );
		pack = new Box( id, new Vector3f(), 1, 1, 1 );
		pack.setRandomColors();
		pack.updateRenderState();
		pack.lockShadows();
		pack.lockMeshes();
		physicsPack.attachChild( pack );
		physicsPack.generatePhysicsGeometry();
	}
	
	public void contactDetector() {
        SyntheticButton ammoPackCollisionHandler = physicsPack.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {

        	public void performAction( InputActionEvent evt ) {
        		ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
        		
        		Iterator<GraphicalCharacter> it = world.characters.iterator();
        		while( it.hasNext() ) {
        			GraphicalCharacter character = it.next();
    				if ( contactInfo.getNode1() == character.getCharacterBody() || 
    					 contactInfo.getNode2() == character.getCharacterBody() ) {

    					if( world.getCore().catchAmmoPack( character.id, id ) ) {
    						deletePackage();
    						return;
    					} else {
    						UserHud.addMessage( HudMessageHandler.MAX_AMMO );
    					}
    				}
        		}
        	}
        };
        
        contactDetect.addAction( collisionAction, ammoPackCollisionHandler, false );
	}

	public void deletePackage() {
		physicsPack.removeFromParent();
		physicsPack.detachAllChildren();
		physicsPack.delete();
		enabled = false;
	}

	public void update(float time) {
		contactDetect.update(time);
	}
}
