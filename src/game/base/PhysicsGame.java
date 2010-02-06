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

package game.base;

import game.common.GameConf;
import game.common.GameTimer;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.ThrowableHandler;
import com.jme.util.geom.Debugger;
import com.jmex.audio.AudioSystem;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;


/** 
 * Implementation of a game loop for a physics based game.
 * <p>
 * This class defines a pure high speed game loop that runs as fast as CPU/GPU
 * will allow
 * 
 * @author Salvatore Loria Giuseppe Leone, Andrea Martire.
 */
public abstract class PhysicsGame {

    // Main scene components:
	/** Flag for running the system. */
    protected boolean finished;
    
    /** Renderer used to display the game */
    protected DisplaySystem display;
    
	/** main camera node */
    protected Camera cam;
    
    /** the root node, everything visible must be attached to this */
    protected Node rootNode;
    
    // Time per Frame
    protected float tpf;

    // Main render options
    protected int alphaBits = 0;
    protected int depthBits = 8;
    protected int stencilBits = 0;
    protected int samples = 0;

    // ONLY FOR DEBUG PURPOSE
    protected WireframeState wireState;
    protected LightState lightState;
    protected boolean showPhysics;
    protected boolean showBounds;
    
    // Added for Physics
    private PhysicsSpace physicsSpace;
    private float physicsSpeed = 1;
    private boolean firstFrame = true;
    
    /** The main logger */
    public static final Logger logger = Logger.getLogger( PhysicsGame.class.getName() );
    
	protected ThrowableHandler throwableHandler;
	
	/** pass manager for terrain splatting and water effects */
	protected BasicPassManager passManager;

	/** set to false when you don't want to do the world update */
	protected boolean enabled = true;

	/** pause the game */
	protected boolean pause = false;

    /**
     * Main game loop: render and update as fast as possible.
     */
    public final void start() {
        logger.info( "Application started." );
        try {
            if ( !finished ) {
                initSystem();

                assertDisplayCreated();

                initGame();

                // main loop
                while ( !finished && !display.isClosing() ) {
                	if( enabled ) {
	                    InputSystem.update();
	
	                    update(-1.0f);
	
	                    render(-1.0f);
	
	                    // swap buffers
	                    display.getRenderer().displayBackBuffer();
	
	                    Thread.yield();
                	}
                	// else manage inGameMenu frame and check pause game
                	else {
                		boolean wait = true;
                		while( wait ) {
                			if( enabled ) {
		                		display.recreateWindow( 
		                			GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ),
		                			GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT ),
		                			GameConf.getIntSetting( GameConf.RESOLUTION_DEPTH ),
		                			GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ),
		                			Boolean.valueOf(
		                				GameConf.getSetting( GameConf.IS_FULLSCREEN )
		                			)
		                        );
		                		wait = false;
                			}
                			if( finished )
                				wait = false;
                		}
	                }
                }
            }
        } catch ( Throwable t ) {
            logger.logp( Level.OFF, this.getClass().toString(), "start()", "Exception in game loop", t );
            if ( throwableHandler != null ) {
				throwableHandler.handle(t);
			}
        }

        cleanup();
        
        logger.info( "Application ending." );

        if (display != null)
            display.reset();
        
        quit();
    }

    /**
     * Updates the timer, sets tpf, updates the freeCamInput and updates the fps
     * string. Also checks keys for toggling pause, bounds, normals, lights,
     * wire etc.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#update(float interpolation)
     */
    protected void update( float interpolation ) {
        // Recalculate the time.
        GameTimer.update();
        // Update tpf to time per frame according to the Timer.
        tpf = GameTimer.getTimePerFrame();

        // Execute updateQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

        if ( firstFrame ) {
            GameTimer.reset();
            firstFrame = false;
        }

        /**
         * Key handler
         */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_pause", false ) ) {
            setPause( !isPause() );
        }
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("step", true ) ) {
            update();
            rootNode.updateGeometricState(tpf, true);
        }
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_wire", false ) ) {
            wireState.setEnabled( !wireState.isEnabled() );
            rootNode.updateRenderState();
        }
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_lights", false ) ) {
            lightState.setEnabled( !lightState.isEnabled() );
            rootNode.updateRenderState();
        }
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_physics", false ) ) {
            showPhysics = !showPhysics;
        }
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_bounds", false ) ) {
            showBounds  = !showBounds;
        }
        
    	if ( !isPause()  ) {
    		showPause( false );
    		update();
    	
            if ( tpf > 0.2 || Float.isNaN( tpf ) ) {
                getPhysicsSpace().update( 0.2f * physicsSpeed );
            } else {
                getPhysicsSpace().update( tpf * physicsSpeed );
            }
            passManager.updatePasses(tpf);
            rootNode.updateGeometricState(tpf, true);
    	} else {
    		showPause( true );
    	}
    }
    
    /**
     * <code>assertDisplayCreated</code> determines if the display system was
     * successfully created before use.
     * 
     * @throws JmeException
     *             if the display system was not successfully created
     */
    protected void assertDisplayCreated() throws JmeException {
        if (display == null) {
            logger.severe("Display system is null.");

            throw new JmeException("Window must be created during"
                    + " initialization.");
        }
        if (!display.isCreated()) {
            logger.severe("Display system not initialized.");

            throw new JmeException("Window must be created during"
                    + " initialization.");
        }
    }

    /** set if to show that the game is in pause */
    public abstract void showPause( boolean pause );

	/**
     * Clears stats, the buffers and renders bounds and normals if on.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    protected void render( float interpolation ) {
        Renderer renderer = display.getRenderer();
        /** Clears the previously rendered information. */
        renderer.clearBuffers();

        // Execute renderQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();

        /** Draw the rootNode and all its children. */
        renderer.draw(rootNode);
        
        /** Have the PassManager render. */
        passManager.renderPasses(display.getRenderer());

        doDebug(renderer);
    }

    /**
     * Creates display, sets up camera, and binds keys. Called in
     * BaseGame.start() directly after the dialog box.
     *
     */
    protected void initSystem() throws JmeException {
        try {
        	 display = DisplaySystem.getDisplaySystem( "LWJGL" );

             display.setMinDepthBits( depthBits );
             display.setMinStencilBits( stencilBits );
             display.setMinAlphaBits( alphaBits );
             display.setMinSamples( samples );

             // Create a window with the startup box's information.
             display.createWindow(
            	GameConf.getIntSetting( GameConf.RESOLUTION_WIDTH ),
         		GameConf.getIntSetting( GameConf.RESOLUTION_HEIGHT ),
         		GameConf.getIntSetting( GameConf.RESOLUTION_DEPTH ),
         		GameConf.getIntSetting( GameConf.RESOLUTION_FREQUENCY ),
         		Boolean.valueOf(
         			GameConf.getSetting( GameConf.IS_FULLSCREEN )
         		)
            );

            cam = display.getRenderer().createCamera( display.getWidth(), display.getHeight() );
        } catch ( JmeException e ) {
            System.exit( 1 );
        }

        /** Set a black background. */
        display.getRenderer().setBackgroundColor( ColorRGBA.black.clone() );

        /** Set up how our camera sees. */
        cameraPerspective();
        Vector3f loc = new Vector3f( 0.0f, 0.0f, 25.0f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        /** Move our camera to a correct place and orientation. */
        cam.setFrame( loc, left, up, dir );
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();
        /** Assign the camera to this renderer. */
        display.getRenderer().setCamera( cam );

        /** Get a high resolution timer for FPS updates. */
        GameTimer.initTimer();

        /** Sets the title of our display. */
        String className = getClass().getName();
        if ( className.lastIndexOf( '.' ) > 0 ) className = className.substring( className.lastIndexOf( '.' )+1 );
        display.setTitle( className );

        /**
         * Assign Keys
         */
        KeyBindingManager.getKeyBindingManager().set( "toggle_pause", KeyInput.KEY_P );
        KeyBindingManager.getKeyBindingManager().set( "step", KeyInput.KEY_ADD );
        KeyBindingManager.getKeyBindingManager().set( "toggle_wire", KeyInput.KEY_T );
        KeyBindingManager.getKeyBindingManager().set( "toggle_lights", KeyInput.KEY_L );
        KeyBindingManager.getKeyBindingManager().set( "toggle_physics", KeyInput.KEY_V );
        KeyBindingManager.getKeyBindingManager().set( "toggle_bounds", KeyInput.KEY_B );

        setPhysicsSpace( PhysicsSpace.create() );
    }

    protected void cameraPerspective() {
        cam.setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 1, 10000 );
        cam.setParallelProjection( false );
        cam.update();
    }

    /**
     * Creates rootNode, lighting and render states. 
     */
    protected void initGame() {
    	
    	/** init pass manager */
    	passManager = new BasicPassManager();
    	
        /** Create rootNode */
        rootNode = new Node( "rootNode" );

        /**
         * Create a wirestate to toggle on and off. Starts disabled with default
         * width of 1 pixel.
         */
        wireState = display.getRenderer().createWireframeState();
        wireState.setEnabled( false );
        rootNode.setRenderState( wireState );

        /**
         * Create a ZBuffer to display pixels closest to the camera above
         * farther ones.
         */
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled( true );
        buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo );
        rootNode.setRenderState( buf );

        // CullState
        CullState cs = display.getRenderer().createCullState();
        cs.setEnabled(true);
        cs.setCullFace(CullState.Face.Back);
        rootNode.setRenderState(cs);

        /** Set up a basic, default light. */
        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        light.setLocation( new Vector3f( 100, 100, 100 ) );
        light.setEnabled( true );

        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled( false );
        lightState.attach( light );
        rootNode.setRenderState( lightState );

        /** Let derived classes initialize. */
        setupGame();

        GameTimer.reset();

        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
    }

    /**
     * Called near end of initGame(). Must be defined by derived classes.
     */
    protected abstract void setupGame();

    /**
     * Can be defined in derived classes for custom updating. Called every frame
     * in update.
     */
    protected abstract void update();

    /**
     * Cleans up the system.
     *
     */
    protected void cleanup() {
        TextureManager.doTextureCleanup();
        if (display != null && display.getRenderer() != null) display.getRenderer().cleanup();
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();
        if( AudioSystem.isCreated() ) {
        	AudioSystem.getSystem().cleanup();
        }
    }

    /**
     * clean up the display and then closes the JVM.
     */
    protected void quit() {
        if (display != null)
            display.close();
        System.exit( 0 );
    }

    /**
     * @return speed
     */
    public float getPhysicsSpeed() {
        return physicsSpeed;
    }

    /**
     * The multiplier for the physics time. Default is 1, which means normal speed. 0 means no physics processing.
     * @param physicsSpeed new speed
     */
    public void setPhysicsSpeed( float physicsSpeed ) {
        this.physicsSpeed = physicsSpeed;
    }

    /**
     * @return the physics space for this game
     */
    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    /**
     * @param physicsSpace The physics space for this game
     */
    protected void setPhysicsSpace(PhysicsSpace physicsSpace) {
        if ( physicsSpace != this.physicsSpace ) {
            if ( this.physicsSpace != null ) this.physicsSpace.delete();
            this.physicsSpace = physicsSpace;
        }
    }
    
    protected void doDebug(Renderer r) {
        if ( showPhysics ) PhysicsDebugger.drawPhysics( getPhysicsSpace(), r );
        if ( showBounds ) Debugger.drawBounds( rootNode, r, true );
    }
    
    public Camera getCam() {
    	return cam;
    }
    
    public Node getRootNode() {
    	return rootNode;
    }

	public void setPause( boolean pause ) {
		this.pause = pause;
	}

	public boolean isPause() {
		return pause;
	}
	
	public void stopGame() {
		enabled = false;
	}
	
	public void resumeGame() {
		enabled = true;
	}
	
    /**
     * <code>finish</code> breaks out of the main game loop. 
     */
    public void finish() {
        finished = true;
    }
}
