package proposta.base;

import java.util.logging.Level;
import java.util.logging.Logger;

import proposta.main.ThreadController;

import com.jme.app.AbstractGame;

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
import com.jme.system.GameSettings;
import com.jme.system.JmeException;
import com.jme.system.PropertiesGameSettings;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.ThrowableHandler;
import com.jme.util.Timer;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.PhysicsSpace;


/** 
 * Implementation of a game loop for a physics based game.
 * <p>
 * This class defines a pure high speed game loop that runs as fast as CPU/GPU
 * will allow
 * 
 * @author Salvatore Loria, Giuseppe Leone, Andrea Martire.
 */
public abstract class PhysicsGame extends AbstractGame {

    // Main scene components:
	
	/** main camera node */
    protected Camera cam;
    
    /** the root node, everything visible must be attached to this */
    protected Node rootNode;
    
    /** main timer to calculate framerate etc */
    public Timer timer;
    
    // Time per FrameRate
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

    // Pause the game
    protected boolean pause;

    // Added for Physics
    private PhysicsSpace physicsSpace;
    private float physicsSpeed = 1;
    private boolean firstFrame = true;
    
    /** The main logger */
    public static final Logger logger = Logger.getLogger( PhysicsGame.class.getName() );
    
	protected ThrowableHandler throwableHandler;

	/** thread controller */
	public ThreadController threadController;
	
	/** pass manager for terrain splatting etc */
	protected BasicPassManager passManager;

	public boolean isThread;
	
	
    /**  
     *  PhysicsGame constructor<br>
     *  Set up game properties
     */
    public PhysicsGame() {
        System.setProperty( "jme.stats", "set" );
    }

    /**
     * Main game loop: render and update as fast as possible.
     */
    public final void start() {
        logger.info( "Application started." );
        try {
            getAttributes();

            if ( !finished ) {
                initSystem();

                assertDisplayCreated();

                initGame();

                // main loop
                while ( !finished && !display.isClosing() ) {
                    // handle freeCamInput events prior to updating the scene
                    // - some applications may want to put this into update of
                    // the game state
                    InputSystem.update();

                    // update game state, do not use interpolation parameter
                    update(-1.0f);

                    // render, do not use interpolation parameter
                    render(-1.0f);

                    // swap buffers
                    display.getRenderer().displayBackBuffer();

                    Thread.yield();
                }
            }
        } catch ( Throwable t ) {
            logger.logp( Level.SEVERE, this.getClass().toString(), "start()", "Exception in game loop", t );
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
     * Get the exception handler if one has been set.
     * 
     * @return the exception handler, or {@code null} if not set.
     */
	protected ThrowableHandler getThrowableHandler() {
		return throwableHandler;
	}

	/**
	 *
	 * @param throwableHandler
	 */
	protected void setThrowableHandler(ThrowableHandler throwableHandler) {
		this.throwableHandler = throwableHandler;
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
        // Recalculate the framerate.
        timer.update();
        // Update tpf to time per frame according to the Timer.
        tpf = timer.getTimePerFrame();

        // Execute updateQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

        if ( firstFrame ) {
            // drawing and calculating the first frame usually takes longer than the rest
            // to avoid a rushing simulation we reset the timer
            timer.reset();
            firstFrame = false;
        }

        /**
         * Key handler
         */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_pause", false ) ) {
            pause = !pause;
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
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "exit", false ) ) {
        	if( isThread ) {
	        	//vado in wait e passo il testimone a swing
	        	threadController.waitThread();
	        	//reset del timer per evitare problemi nel resume del gioco
	        	timer.reset();
	        	if(threadController.close)//se swing comanda esci
	        		finish();
	        	else
	        		display.recreateWindow(settings.getWidth(),settings.getHeight(),
	        				settings.getDepth(),settings.getFrequency(),settings.isFullscreen());
        	} else {
        		finish();
        	}
	        	
        }
        
        if ( !pause ) {
            
        	update();
        	
            if ( tpf > 0.2 || Float.isNaN( tpf ) ) {
                getPhysicsSpace().update( 0.2f * physicsSpeed );
            } else {
                getPhysicsSpace().update( tpf * physicsSpeed );
            }

            rootNode.updateGeometricState(tpf, true);

			passManager.updatePasses(tpf);
        }
    }

    /**
     * Clears stats, the buffers and renders bounds and normals if on.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    protected void render( float interpolation ) {
        Renderer renderer = super.display.getRenderer();
        /** Clears the previously rendered information. */
        renderer.clearBuffers();

        // Execute renderQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();

        /** Draw the rootNode and all its children. */
        renderer.draw(rootNode);
        
        /** Have the PassManager render. */
        passManager.renderPasses(display.getRenderer());

        /** Call simpleRender() in any derived classes. */
        simpleRender();

        doDebug(renderer);
    }

    /**
     * Creates display, sets up camera, and binds keys. Called in
     * BaseGame.start() directly after the dialog box.
     *
     * @see AbstractGame#initSystem()
     */
    protected void initSystem() throws JmeException {
        try {
            display = DisplaySystem.getDisplaySystem(settings.getRenderer() );

            display.setMinDepthBits( depthBits );
            display.setMinStencilBits( stencilBits );
            display.setMinAlphaBits( alphaBits );
            display.setMinSamples( samples );

            // Create a window with the startup box's information.
            display.createWindow(
                    settings.getWidth(),
                    settings.getHeight(),
                    settings.getDepth(),
                    settings.getFrequency(),
                    settings.isFullscreen()
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
        timer = Timer.getTimer();

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
        KeyBindingManager.getKeyBindingManager().set( "exit", KeyInput.KEY_ESCAPE );

        setPhysicsSpace( PhysicsSpace.create() );
    }

    protected void cameraPerspective() {
        cam.setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 1, 10000 );
        cam.setParallelProjection( false );
        cam.update();
    }

    /**
     * Creates rootNode, lighting, statistic text, and other basic render
     * states. Called in BaseGame.start() after initSystem().
     *
     * @see AbstractGame#initGame()
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

        timer.reset();

        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();

        //timer.reset();
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
     * Can be defined in derived classes for custom rendering.
     * Called every frame in render.
     */
    protected void simpleRender() {
        //do nothing
    }

    /**
     * unused
     *
     * @see AbstractGame#reinit()
     */
    protected void reinit() {
        //do nothing
    }

    /**
     * Cleans up the keyboard.
     *
     * @see AbstractGame#cleanup()
     */
    protected void cleanup() {
        TextureManager.doTextureCleanup();
        if (display != null && display.getRenderer() != null) display.getRenderer().cleanup();
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();
    }

    /**
     * Calls the quit of BaseGame to clean up the display and then closes the JVM.
     */
    @Override
    protected void quit() {
        if (display != null)
            display.close();
        System.exit( 0 );
    }

    /**
     * @return speed set by {@link #setPhysicsSpeed(float)}
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
     * @return the physics space for this simple game
     */
    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    /**
     * @param physicsSpace The physics space for this simple game
     */
    protected void setPhysicsSpace(PhysicsSpace physicsSpace) {
        if ( physicsSpace != this.physicsSpace ) {
            if ( this.physicsSpace != null ) this.physicsSpace.delete();
            this.physicsSpace = physicsSpace;
        }
    }
    
    protected void doDebug(Renderer r) {
        if ( showPhysics ) PhysicsDebugger.drawPhysics( getPhysicsSpace(), r );
    }
    
    /**
     * @see AbstractGame#getNewSettings()
     */
    protected GameSettings getNewSettings() {
        return new BaseGameSettings();
    }

    /**
     * A PropertiesGameSettings which defaults Fullscreen to TRUE.
     */
    static class BaseGameSettings extends PropertiesGameSettings {
        static {
            // This is how you programmatically override the DEFAULT_*
            // settings of GameSettings.
            // You can also make declarative overrides by using
            // "game-defaults.properties" in a CLASSPATH root directory (or
            // use the 2-param PropertiesGameSettings constructor for any name).
            // (This is all very different from the user-specific
            // "properties.cfg"... or whatever file is specified below...,
            // which is read from the current directory and is session-specific).
            defaultFullscreen = Boolean.TRUE;
            defaultSettingsWidgetImage = "/jmetest/data/images/Monkey.png";
        }
        /**
         * Populates the GameSettings from the (session-specific) .properties
         * file.
         */
        BaseGameSettings() {
            super("properties.cfg");
            load();
        }
    }
    
    public Camera getCam() {
    	return this.cam;
    }
    
    public Node getRootNode() {
    	return this.rootNode;
    }
}
