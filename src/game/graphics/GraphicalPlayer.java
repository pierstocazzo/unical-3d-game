/**
 * Copyright (C) 2010 Salvatore Loria, Andrea Martire, Giuseppe Leone
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package game.graphics;

import game.HUD.HudMessageHandler;
import game.HUD.UserHud;
import game.common.GameConf;
import game.common.GameTimer;
import game.graphics.GameAnimationController.Animation;
import game.sound.SoundManager;
import game.sound.SoundManager.SoundType;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.material.Material;

/** Class <code>PhysicsPlayer</code> <br>
 * 
 * Represents a graphical player viewed in third person view
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 */
public class GraphicalPlayer extends GraphicalCharacter {

	/**
	 * Player Collision node
	 */
	Node playerCollisionNode;
	
    /**
     * boolean variable used by the input handler 
     * to set the movement to perform
     */
    boolean walkingBackwards, walkingForward, running, turningLeft, turningRight, nowTurning;
	
    
	/** GraphicalPlayer constructor <br>
     * Create a new graphical player 
     * 
     * @param id - (String) the character's identifier
     * @param world - (GraphicalWorld) the graphical world in witch the character lives
     * @param model - (Node) the 3d model to apply
     */
    public GraphicalPlayer( String id, GraphicalWorld world, Node model ) {
        
    	this.id = id;
    	this.world = world;
    	this.model = model;
    	
        createPlayer();
        
        super.previousShootTime = 0;
    }

	void createPlayer() {
        characterNode = new Node("character node");
        body = world.getPhysicsSpace().createDynamicNode();
        
        Box bodyGeometry = new Box("body", new Vector3f(-1.5f,-5,-1.5f), new Vector3f(1.5f,5,1.5f));
        bodyGeometry.setCullHint( CullHint.Always );

        body.attachChild(bodyGeometry);
        bodyGeometry.setLocalTranslation(0,3,0);
        
        body.generatePhysicsGeometry();
	    
	    body.setAffectedByGravity(false);
	    body.setMaterial( Material.GHOST );
	
	    characterNode.attachChild(body);
	    characterNode.attachChild(model);
		
	    /**
	     * Setting up character's collisionNode
	     */
	    playerCollisionNode = new Node("collision");
	    world.getRootNode().attachChild(playerCollisionNode);
	    
	    Box frontal = new Box("frontal", new Vector3f(-1,0,-1), new Vector3f(1,10,-.9f));
	    frontal.clearTextureBuffers();
	    frontal.setModelBound(new BoundingBox());
		frontal.updateModelBound();
		frontal.setLocalTranslation(0,0,5);
		playerCollisionNode.attachChild(frontal);
		
		frontal.setCullHint(CullHint.Always);
		
		Box backy = new Box("backy", new Vector3f(-1,0,-1), new Vector3f(1,10,-.9f));
		backy.clearTextureBuffers();
		backy.setModelBound(new BoundingBox());
		backy.updateModelBound();
		backy.setLocalTranslation(0,0,-2.5f);
		playerCollisionNode.attachChild(backy);
		
		backy.setCullHint(CullHint.Always);
		
		Box left = new Box("left", new Vector3f(-1,0,-1.5f), new Vector3f(-.9f,10,1.5f));
	    left.clearTextureBuffers();
	    left.setModelBound(new BoundingBox());
		left.updateModelBound();
		left.setLocalTranslation(3,0,0);
		playerCollisionNode.attachChild(left);
	    
		left.setCullHint(CullHint.Always);
		
		Box right = new Box("right", new Vector3f(-1,0,-1.5f), new Vector3f(-.9f,10,1.5f));
	    right.clearTextureBuffers();
	    right.setModelBound(new BoundingBox());
		right.updateModelBound();
		right.setLocalTranslation(-1.5f,0,0);
		playerCollisionNode.attachChild(right);
	    
		right.setCullHint(CullHint.Always);
		
		/**
		 * end collision node
		 */	    
	    
	    /** initialize the animation */ 
		animationController = new GameAnimationController( model.getController(0) );
	}
	
	public Vector3f getPosition() {
		return characterNode.getWorldTranslation();
	}

	/**
	 * 
	 * @param direction - (Vector3f) the direction of the shoot
	 */
	public void shoot( Vector3f direction ) {
		if( world.getCore().shoot(id) ) {
			Bullet bullet = new Bullet( id , world, 
					world.getCore().getWeapon(id), 
					world.getCam().getLocation().add( world.getCam().getDirection().mult( 6 ) ) );
			world.bullets.add( bullet );
			bullet.shoot(direction);
			world.shoot( world.getCam().getLocation() );
		} else {
			UserHud.addMessage(HudMessageHandler.AMMO_FINISHED);
			if(world.isAudioEnabled())
				SoundManager.playSound(SoundType.NOAMMO, world.getCam().getLocation());
		}
	}

	/** Function <code>update</code> <br>
	 * Update the physics character 
	 * @param time
	 */
	public void update( float time ) {
	    if( world.getCore().isAlive( id ) == true ) {

	    	playerCollisionNode.getLocalTranslation().set( characterNode.getWorldTranslation() );
		    body.getWorldTranslation().set( characterNode.getWorldTranslation() );
		    
		    if( !world.getCore().isMoving(id) ) {
		    	animationController.runAnimation( Animation.IDLE );
		    }

		    if( shooting ) {
		    	if( GameTimer.getTimeInSeconds() - previousShootTime > 
		    		world.getCore().getWeapon(id).getLoadTime() ) {
		    		previousShootTime = GameTimer.getTimeInSeconds();
		    		shoot( world.getCam().getDirection() );
		    	}
		    }
		    updateMovements();
		    // update core
		    world.getCore().setPosition( id, characterNode.getWorldTranslation() );
	    } else {
	    	die();
	    }
	}
	
	/**
     */
	private void updateMovements() {

		Vector3f prevLoc = new Vector3f( characterNode.getLocalTranslation());
		Vector3f loc = new Vector3f(prevLoc);

		lookAtAction( world.getCam().getDirection() );

		if( running && walkingForward ) {
			if( world.getCore().getWeapon(id).isHeavy() == false )
				run( Integer.valueOf( GameConf.getParameter( "runVelocity" ) ) );
			else {
				moveForward( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
			}
		} 
		else if( walkingForward ) {
			moveForward( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
			if( turningRight ) {
				turnRight( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
			} 
			else if( turningLeft ) {
				turnLeft( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
			} 
		} 
		else if( walkingBackwards ) {
			moveBackward( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
		} 
		else if( turningRight ) {
			turnRight( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
		} 
		else if( turningLeft ) {
			turnLeft( Integer.valueOf( GameConf.getParameter( "walkVelocity" ) ) );
		} 
		else {
			rest();
		}

		if ( walkingBackwards && walkingForward && !nowTurning ) {
			characterNode.getLocalTranslation().set(prevLoc);
			return;
		}
		
		Vector3f calcVector = new Vector3f();
		characterNode.getLocalTranslation().subtract( loc, loc );
        if ( !loc.equals( Vector3f.ZERO ) ) {
            float distance = loc.length();
            if ( distance != 0 && distance != 1.0f )
                loc.divideLocal(distance);
            float faceAngle = FastMath.atan2( loc.z, loc.x );
            characterNode.getLocalRotation().fromAngleNormalAxis( -( faceAngle - FastMath.HALF_PI ), Vector3f.UNIT_Y );
            characterNode.getLocalRotation().getRotationColumn( 2, calcVector ).multLocal( distance );

            characterNode.getLocalTranslation().set( prevLoc );
            characterNode.getLocalTranslation().addLocal( calcVector );
        }
	}
        
    private void run( float speed ) {
        setRunning( true );
        Vector3f targetLocation = characterNode.getLocalTranslation();
        Vector3f rot = new Vector3f( world.getCam().getDirection());
        rot.y = 0;
        rot.normalizeLocal();
        targetLocation.addLocal(rot.multLocal(( speed * GameTimer.getTimePerFrame() )));
    }
    
    private void moveForward( float speed ) {
        setMoving( true );
        Vector3f targetLocation = characterNode.getLocalTranslation();
        Vector3f rot = new Vector3f( world.getCam().getDirection());
        rot.y = 0;
        rot.normalizeLocal();
        targetLocation.addLocal(rot.multLocal(( speed * GameTimer.getTimePerFrame() )));
    }
    
    private void moveBackward( float speed ) {
		setMoving( true );
        Vector3f targetLocation = characterNode.getLocalTranslation();
        	Vector3f rot = new Vector3f( world.getCam().getDirection());
            rot.y = 0;
        rot.normalizeLocal();
        targetLocation.subtractLocal(rot.multLocal(( speed * GameTimer.getTimePerFrame() )));
    }
    
    private void turnLeft( float speed ) {
		setMoving( true );
        Vector3f targetLocation = characterNode.getLocalTranslation();
        	Vector3f rot = new Vector3f( world.getCam().getLeft());
            rot.y = 0;
        rot.normalizeLocal();
        targetLocation.addLocal(rot.multLocal(( speed * GameTimer.getTimePerFrame() )));
    }
    
    private void turnRight( float speed ) {
		setMoving( true );
        Vector3f targetLocation = characterNode.getLocalTranslation();
        	Vector3f rot = new Vector3f( world.getCam().getLeft());
            rot.y = 0;
        rot.normalizeLocal();
        targetLocation.subtractLocal(rot.multLocal(( speed * GameTimer.getTimePerFrame() )));
    }

	public void lookAtAction( Vector3f direction ) {
		Vector3f vectorToLookAt = new Vector3f( this.getCollision().getWorldTranslation() );
	    vectorToLookAt.addLocal( direction.x, 0, direction.z );
		this.getCollision().lookAt( vectorToLookAt, Vector3f.UNIT_Y );
	}

	public void hide( boolean b ) {
		if( b ) 
			model.removeFromParent();
		else
			characterNode.attachChild( model );
	}
	
	public void die() {
    	body.detachAllChildren();
    	body.delete();
    	world.getRootNode().detachChild( characterNode );
	}

	/** Function <code>rest()</code> <br>
     * Activate the idle animation
     */
    public void rest() {
    	animationController.runAnimation( Animation.IDLE );
    }

	/** Function <code>getCharacterNode</code> <br>
     *  Return the main Node of the physics character
     * 
     * @return the main Node of the physics character
     */
    public Node getCharacterNode() {
        return characterNode;
    }

    /** Function <code>getCharacterBody</code> <br>
     *  Return the body Node of the physics character
     * 
     * @return the body Node of the physics character (a capsule)
     */
    public DynamicPhysicsNode getCharacterBody() {
        return body;
    }

    public Node getCollision() {
		return playerCollisionNode;
	}

	/** Function <code>getModel</code> <br>
	 * 
	 * @return (Node) the character 3d model
	 */
    public Node getModel() {
        return model;
    }

	public void setRunning(boolean running) {
    	if( running == true ) {
    		animationController.runAnimation( Animation.RUN );
    	}
    	this.running = running;
		world.getCore().setMoving( id, running );
	}

	/** Function <code>setMovingForward</code> <p>
	 * If the boolean parameter is true, set the character's status to moving forward and activate the right animation
	 * @param moving - (boolean)
	 */
	public void setMoving( boolean moving ) {
    	if( moving == true ) {
    		animationController.runAnimation( Animation.WALK );
    	} 
		
		world.getCore().setMoving( id, moving );
	}

	/** Function <code>setJumping</code> <p>
	 * If the boolean parameter is true, set the character's status to jumping and activate the right animation
	 * @param jumping - (boolean)
	 */
	public void setJumping( boolean jumping ) {	
    	if( jumping == true ) {
    		animationController.runAnimation( Animation.JUMP );
    	}
		world.getCore().setJumping( id, jumping );
	}

	/**
	 * @return true if the character is shooting
	 */
	public boolean isShooting() {
		return shooting;
	}

	/**
	 * @param shooting 
	 */
	public void setShooting( boolean shooting ) {
		this.shooting = shooting;
	}
	
	public void setWalkingBackwards(boolean walkingBackwards) {
		this.walkingBackwards = walkingBackwards;
	}

	public void setWalkingForward(boolean walkingForward) {
		this.walkingForward = walkingForward;
	}

	public void setTurningRight(boolean turningRight) {
		this.turningRight = turningRight;
	}

	public void setTurningLeft(boolean turningLeft) {
		this.turningLeft = turningLeft;
	}

	public void nextWeapon() {
		world.getCore().nextWeapon( id );
	}
}