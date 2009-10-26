package joework.controller;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.contact.ContactCallback;
import com.jmex.physics.contact.MutableContactInfo;
import com.jmex.physics.contact.PendingContact;
import com.jmex.physics.material.Material;

/**
 * This class is based on the original work of Christian Teister
 * @url http://www.jmonkeyengine.com/forum/index.php?PHPSESSID=3af499349ad806156594fe479a65dcb5&topic=10356.msg79987#msg79987
 * @author Christian Teister, Joseph
 * @date 25.10.2009
 */

public class CharacterController {

	private DynamicPhysicsNode capsule;
	private Camera cam;

	private final Quaternion standRotation = new Quaternion(0.70710677f, 0, 0, 0.70710677f);

	private boolean movingForward, movingBackward, strafingLeft, strafingRight;
	private int jump; // This is an int, because sometimes isOnGround is false,
	// when the player is on ground

	public static final Material PLAYER = new Material("player");

	public static final float MAX_SPEED = 10;
	public static final float CAM_OFFSET_Y = 3.5f;

	private boolean firstPerson = true;
	private int isOnGround = 0;

	private Vector3f contactNormal;

	private Vector3f groundVelocity; // The velocity of the object we are standing on.

	public CharacterController(Node parentNode, PhysicsSpace physics, Vector3f position, Camera cam, boolean firstPerson) {
		this.cam = cam;
		this.firstPerson = firstPerson;

		capsule = physics.createDynamicNode();
		capsule.createCapsule("player capsule");

		initMaterial();

		capsule.getLocalTranslation().set(position);

		capsule.setLocalScale(new Vector3f(2.5f, 2.5f, 6));

		// Force the capsule to stand
		capsule.setLocalRotation(new Quaternion(standRotation));

		capsule.computeMass();
		capsule.setMass(80);

		parentNode.attachChild(capsule);

		contactNormal = new Vector3f();

		ContactCallback onGroundCallBack = new ContactCallback() {
			public boolean adjustContact(PendingContact c) {

                            if (c.getNode2() == capsule) {

					c.getContactNormal(contactNormal);

					// we are standing on something
					if (contactNormal.y < -0.7f) {
						isOnGround = 4;
                                                groundVelocity = null;/* TODO */
					}
				} else if (c.getNode1() == capsule) {

					c.getContactNormal(contactNormal);
					contactNormal.multLocal(-1);

					// we are standing on something
					if (contactNormal.y > -0.7f) {
						isOnGround = 4;

						groundVelocity = null;/* TODO */

					}
				}
				return false;
			}
		};
		physics.getContactCallbacks().add(onGroundCallBack);
	}

	public void initMaterial() {
		PLAYER.setDebugColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));
		PLAYER.setDensity(2.8f);

		MutableContactInfo info = new MutableContactInfo();
		info.setMu(Float.POSITIVE_INFINITY); // Don´t set this to 0, because then we could go through objects
		info.setBounce(0f);
		PLAYER.putContactHandlingDetails(null, info);
		PLAYER.putContactHandlingDetails(Material.DEFAULT, info);

		capsule.setMaterial(PLAYER);
	}

	public Spatial getCapsule() {
		return capsule.getChild(0);
	}

	public void setPosition(Vector3f _newPos) {
		capsule.setLocalTranslation(_newPos.subtractLocal(0, CAM_OFFSET_Y, 0));
		capsule.clearDynamics();
	}

	float minDot = Float.MAX_VALUE;

	public void update(float tpf) {
		// Don´t let the capsule rotate
		capsule.setAngularVelocity(new Vector3f(0, 0, 0));

		// Force the capsule to stand
		capsule.setLocalRotation(new Quaternion(standRotation));

		// Caculate the movement with velocity
		Vector3f lookDir = new Vector3f(cam.getDirection());
		lookDir.y = 0;
		lookDir.normalizeLocal();

		Vector3f currentVelocity = new Vector3f();
		capsule.getLinearVelocity(currentVelocity);

		Vector3f velocity = new Vector3f();

		if (movingForward) {
			movingForward = false;
			velocity.addLocal(lookDir.mult(MAX_SPEED));
		}
		if (movingBackward) {
			movingBackward = false;
			velocity.addLocal(lookDir.mult(-MAX_SPEED));
		}
		if (strafingLeft) {
			strafingLeft = false;
			velocity.addLocal(lookDir.crossLocal(new Vector3f(0, 1, 0)).multLocal(-MAX_SPEED));
		}
		if (strafingRight) {
			strafingRight = false;
			velocity.addLocal(lookDir.crossLocal(new Vector3f(0, 1, 0)).multLocal(MAX_SPEED));
		}

		velocity.y = 0;
		// Limit the speed, we can apply by ourself
		if (velocity.lengthSquared() > MAX_SPEED * MAX_SPEED) {
			velocity.normalizeLocal().multLocal(MAX_SPEED);
		}

		// In the air we can only modify the speed, not set it 100%
		if (isOnGround <= 0) {
			currentVelocity.add(velocity.multLocal(tpf),velocity);
		}

		// y-Velocity is calculated by the physics system: gravity and jumping
		velocity.setY(currentVelocity.y);

		if (groundVelocity != null) {// We take the ground-velocity as a base
			groundVelocity.y = 0; // Don´t take vertical velocity of the ground
			velocity.addLocal(groundVelocity);
		}

		// Jumping
		if (jump > 0 && isOnGround>0) {
			jump = 0;
			velocity.setY(velocity.y+13.5f);
		}

		capsule.clearDynamics();

		capsule.setLinearVelocity(velocity);


		// Update the position of the camera in first-person-mode
		if (firstPerson) {
			Vector3f camPos = new Vector3f(getCapsule().getWorldTranslation());
			camPos.y += CAM_OFFSET_Y;
			cam.setLocation(camPos);
		}

		jump--;
		isOnGround--;
		if(isOnGround<=0)
			groundVelocity = null;
	}

	public void jump() {
		// Try to start jumping till the next 10 frames
		// We have to do it this way, because sometimes isOnGround is not correct
		jump = 10;
	}

	public void moveForward() {
		movingForward = true;
	}

	public void moveBackward() {
		movingBackward = true;
	}

	public void strafeLeft() {
		strafingLeft = true;
	}

	public void strafeRight() {
		strafingRight = true;
	}

}
