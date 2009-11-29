package slashWork.game.graphics;

import slashWork.game.core.WeaponType;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.geometry.PhysicsSphere;

public class PhysicsBullet {
	
	/** the bullet's identifier */
	String id;
	
	/** the bullet Node */
	Node bullet;
	
	/** the bullet physicsNode */
	DynamicPhysicsNode physicsBullet;
	
	/** the bullet geometry */
	PhysicsSphere bulletGeometry;
	
	/** an input handler used to detect bullet's collisions */
	InputHandler contactDetect = new InputHandler();
	
	/** the graphical world witch contains the bullet */
	GraphicalWorld world;
	
	/** the direction of the shoot */
	Vector3f direction;
	
	/** the type of the weapon witch shoot the bullet */
	WeaponType weaponType;
	
	/** PhysicsBullet constructor <br>
	 * 
	 * @param id - (String) the identifier of the bullet
	 * @param world - (GraphicalWorld) the graphical world witch contains the bullet
	 * @param direction - (Vector3d) the direction of the shoot 
	 * @param weaponType - (WeaponType) the type of the weapon witch shoot the bullet
	 * @param position - (Vector3f) the position where to create the bullet
	 */
	public PhysicsBullet( String id, GraphicalWorld world, Vector3f direction, WeaponType weaponType, Vector3f position ) {
		this.id = id;
		this.world = world;
		this.direction = new Vector3f( direction );
		this.weaponType = weaponType;
		this.bullet = new Node( id );
		
		// Create the bullet Physics
		physicsBullet = world.getPhysicsSpace().createDynamicNode();
		physicsBullet.setName( "physics" + id );
		bulletGeometry = physicsBullet.createSphere( id + "geometry" );
		bulletGeometry.setLocalScale( 0.3f );
		physicsBullet.attachChild( bulletGeometry );
		bullet.attachChild( physicsBullet );
		bullet.setLocalTranslation( position );
		
		Sphere s = new Sphere( id, 10, 10, 0.05f );
		s.setDefaultColor( ColorRGBA.yellow );
		physicsBullet.attachChild( s );
		
		world.getRootNode().attachChild( bullet );
		
		contactDetection();
		
		physicsBullet.addForce( direction.mult( weaponType.getPower() ) );
	}
	
	/** Function used to detect a contact between the bullet and something
	 *  when a contact is detected, the bullet is detached from the rootnode
	 */
	void contactDetection() {
        SyntheticButton collisionHandler = physicsBullet.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
            	ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
        		physicsBullet.clearDynamics();
        		physicsBullet.detachAllChildren();
        		physicsBullet.delete();
        		bullet.detachAllChildren(); 
        		world.getRootNode().detachChild(bullet);
        		world.bullets.remove( id );
        		
        		/** Control if the bullet hit a character */
        		for( String id : world.characters.keySet() ) {
        			if( ( contactInfo.getNode1() == world.characters.get(id).getCharacterBody() ||
        				contactInfo.getNode2() == world.characters.get(id).getCharacterBody() ) ||
        				( contactInfo.getNode1() == world.characters.get(id).getCharacterFeet() ||
                		contactInfo.getNode2() == world.characters.get(id).getCharacterFeet() ) ){
        				GraphicalWorld.logger.info( id + " e stato colpito da un proiettile");
        				world.getCore().characterShoted( id, weaponType.getDamage() );
        			}
        		}
            }
        };
        
        /** the action to do when a collision is detected */
        contactDetect.addAction( collisionAction, collisionHandler, false );
    }
	
	/** ContactDetect update
	 * @param time
	 */
	public void update( float time ) {
		contactDetect.update(time);
	}
}
