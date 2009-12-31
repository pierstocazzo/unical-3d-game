package proposta.graphics;

import com.jme.input.InputHandler;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;

public class Character {

	/** the character identifier */
	public String id;
  
    /** the main node of the character */
    Node characterNode;
    
	/** The body of the character: a Capsule that contains the model */
    DynamicPhysicsNode body; 
    
    /** 3d model applied to the character */
    Node model;
    
	/** animation controller */
	CustomAnimationController animationController;
	
	/** the graphical world in which the character live */
    GraphicalWorld world;

    /** the force applied to the character to jump */
    Vector3f jumpVector;
    
    /** an util handler that detect the contact between the character and the ground */
    InputHandler contactDetect = new InputHandler();
    
    /** Utility quaternion */
    Quaternion quaternion;
    
    /** true if the character is shooting */
    boolean shooting = false;

	float previousTime;

	protected boolean enabled = true;
	
	public Node getCharacterNode() {
		return characterNode;
	}

	public void update(float time) {
	}

	public PhysicsNode getCharacterFeet() {
		return null;
	}

	public PhysicsNode getCharacterBody() {
		return body;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public void hideModel() {
		model.removeFromParent();
	}

	public void showModel() {
		body.attachChild(model);
	}
}