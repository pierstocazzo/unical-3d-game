package proposta.graphics;

import utils.Loader;

import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

/**
*/
public class PhysicsAmmoPackage {
 
	
	String id;
	
	DynamicPhysicsNode physicsPack;
	
	Box pack;
	
	GraphicalWorld world;
	
	Vector3f position;
	
	InputHandler contactDetect;
	
	PhysicsAmmoPackage( String id, GraphicalWorld world, Vector3f position ) {
		this.id = id;
		this.world = world;
		this.position = new Vector3f( position );
		this.position.setY( position.getY() + 10 );
		this.contactDetect = new InputHandler();
		createPhysics();
		contactDetector();
	}
	
	public void createPhysics() {
		physicsPack = world.getPhysicsSpace().createDynamicNode();
		world.getRootNode().attachChild( physicsPack );
		physicsPack.getLocalTranslation().set( position );
		pack = new Box( id, new Vector3f(), 1, 1, 1 );
//		pack.setRandomColors();
//		pack.updateRenderState();
		
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(TextureManager.loadTexture(
                Loader.load( "jmetest/data/images/Monkey.jpg" ), Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear));
        
        pack.setRenderState(ts);
        world.getRootNode().updateRenderState();
        
		physicsPack.attachChild( pack );
		physicsPack.generatePhysicsGeometry();
	}
	
	public void contactDetector() {
        SyntheticButton ammoPackCollisionHandler = physicsPack.getCollisionEventHandler();
        
        InputAction collisionAction = new InputAction() {
            public void performAction( InputActionEvent evt ) {
                ContactInfo contactInfo = (ContactInfo) evt.getTriggerData();
                
                for( String playerId : world.getCore().getPlayersId() ) {
	                if ( contactInfo.getNode1() == world.characters.get(playerId).getCharacterBody() || 
	                	 contactInfo.getNode2() == world.characters.get(playerId).getCharacterBody() ) {
	                	
	                   if( world.getCore().catchAmmoPack( playerId, id ) )
	                	   deletePackage();
	                }
                }
            }
        };
        
        contactDetect.addAction( collisionAction, ammoPackCollisionHandler, false );
	}

	public void deletePackage() {
		world.getRootNode().detachChild( physicsPack );
		physicsPack.detachAllChildren();
		physicsPack.delete();
		
		world.ammoPackages.remove( id );
	}

	public void update(float time) {
		contactDetect.update(time);
	}
}
