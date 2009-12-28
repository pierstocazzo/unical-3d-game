package game.graphics;

import game.HUD.UserHud;
import game.base.Game;
import game.input.ThirdPersonHandler;
import game.menu.LoadingFrame;
import game.sound.SoundManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import jmetest.TutorialGuide.ExplosionFactory;
import utils.Loader;
import utils.ModelLoader;

import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseLookHandler;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.physics.StaticPhysicsNode;

/** Class GraphicalWorld <br>
 * The main graphics class which contains all the graphical objects and characters
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 */
public class GraphicalWorld extends Game {
	
	/** an interface to communicate with the application core */
	WorldInterface core;
	
	/** a custom freeCamInput handler to control the player */
	ThirdPersonHandler inputHandler;
    
    /** hashmap of the characters */
    HashMap< String, Character > characters; 
    
    /** hashmap of the bullets */
	HashMap< String, Bullet > bullets;
    int bulletsCounter = 0;
    
    /** hashmap of the ammo packages */
	HashMap< String, AmmoPackage > ammoPackages;
    int ammoPackagesCounter = 0;
    
    /** hashmap of the energy packages */
	HashMap< String, EnergyPackage > energyPackages;
    int energyPackagesCounter = 0;
    
    /** the player, in single player mode */
    public Player player;

    /** set to false when you don't want to do the world update */
	boolean enabled = true;
	
	/** HUD node */
	public Node hudNode;
	/** very very basic hud */
	Quad crosshair;
	Text gameOver;
	UserHud userHud;
	
	/** audio controller */
	SoundManager audio;
	boolean audioEnabled;
	
	boolean freeCam;

	MouseLookHandler mouseLookHandler;
	
	/** the world dimension (the world is a square) */
	float dimension;

	/** screen resolution */
	Vector2f resolution;

	/** the environment */
	Environment environment;
	
	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) the logic world
	 * @param loadingFrame - (LoadingFrame) the loading frame
	 */
    public GraphicalWorld( WorldInterface core, LoadingFrame loadingFrame ) {
        this.core = core;
        super.loadingFrame = loadingFrame;
        
        audioEnabled = false;
    }

	public void setupInit() {
   	
		hudNode = new Node( "HUD" );
		rootNode.attachChild( hudNode );
		hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		characters = new HashMap<String, Character>();
		bullets = new HashMap<String, Bullet>();
		ammoPackages = new HashMap<String, AmmoPackage>();
		energyPackages = new HashMap<String, EnergyPackage>();
		
    	setCrosshair();
    	resolution = new Vector2f( settings.getWidth(), settings.getHeight() );
    	
		gameOver = Text.createDefaultTextLabel( "gameOver" );
		rootNode.attachChild(gameOver);
		
    	ExplosionFactory.warmup();
    	
		if( audioEnabled ) {
			audio = new SoundManager( cam );
		}
    }
    
	public void setupEnvironment() {
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
	    environment = new Environment( this );
	
	    setupEnergyPackages();
	    
	    userHud = new UserHud(this);
	}

	/** Create graphic players
	 *  and place them in positions setted in the logic game
	 */
	public void setupPlayer() {
		
		Node model = ModelLoader.loadModel("game/data/models/soldier/player.jme", 
				"game/data/models/soldier/soldato.jpg", 1 );
	    model.setLocalTranslation(0, -2f, 0);   
	    
		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/models/soldier/lr300map.jpg" ),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear);
	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    ts.setEnabled(true);
	    ts.setTexture(texture);
		model.getChild( "weapon" ).setRenderState( ts );
	    
	    for( String id : core.getPlayersIds() ) {
	    	player = new Player( id, this, 20, 100, model );
	        player.getCharacterNode().getLocalTranslation().set( core.getPosition(id) );
	        rootNode.attachChild( player.getCharacterNode() );
	        characters.put( player.id, player );
	    }
	}

	/** Create graphic characters
     *  and place them in positions setted in the logic game
     */
    public void setupEnemies() { 	    	
        for( String id : core.getEnemiesIds() ) {
        	Node model = ModelLoader.loadModel("game/data/models/soldier/enemy.jme", 
        			"game/data/models/soldier/soldier.jpg", 1f, new Quaternion());
            model.setLocalTranslation(0, -2f, 0);  
            
			Texture texture = TextureManager.loadTexture( Loader.load( "game/data/models/soldier/lr300map.jpg" ),
	                Texture.MinificationFilter.Trilinear,
	                Texture.MagnificationFilter.Bilinear);
	        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	        ts.setEnabled(true);
	        ts.setTexture(texture);
			model.getChild( "weapon" ).setRenderState( ts );

            Enemy enemy = new Enemy( id, this, 20, 100,  model );
            
            Vector3f position = core.getPosition(id);
            position.setY( environment.getTerrain().getHeight( position.x, position.z ) + 1 );
        	enemy.getCharacterNode().getLocalTranslation().set( position );
        	characters.put( id, enemy );
        	rootNode.attachChild( characters.get(id).getCharacterNode() );
        }
    }
    
    public void setupCamera() {
        cam.setLocation(new Vector3f(160,30,160));
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, 10000.0f);
        cam.update();
    }

    public void setupInput() {
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    	
        KeyBindingManager.getKeyBindingManager().set( "freeCamAction", KeyInput.KEY_C );
        KeyBindingManager.getKeyBindingManager().set( "shoot", KeyInput.KEY_F );
        KeyBindingManager.getKeyBindingManager().set( "firstPerson", KeyInput.KEY_R );
    	
        HashMap<String, Object> handlerProps = new HashMap<String, Object>();
        handlerProps.put(ThirdPersonHandler.PROP_DOGRADUAL, "true");
        handlerProps.put(ThirdPersonHandler.PROP_TURNSPEED, ""+(1.0f * FastMath.PI));
        handlerProps.put(ThirdPersonHandler.PROP_LOCKBACKWARDS, "false");
        handlerProps.put(ThirdPersonHandler.PROP_CAMERAALIGNEDMOVE, "true");
        inputHandler = new ThirdPersonHandler( player, cam, handlerProps);
    }

	@Override
    protected void update() {
		if( !enabled )
			return;
		
		if( audioEnabled )
			audio.update();
    	
		if( core.isAlive( player.id ) == false ) {
    		gameOver();
    	} else {
    		userHud.update();
	        inputHandler.update(tpf);
	        freeCamInput.update(tpf);
	        float camMinHeight = environment.getTerrain().getHeight(cam.getLocation()) + 2f;
	        if (!Float.isInfinite(camMinHeight) && !Float.isNaN(camMinHeight)
	                && cam.getLocation().y <= camMinHeight) {
	            cam.getLocation().y = camMinHeight;
	            cam.update();
	        }
	        
	        environment.update();
	        
	        float characterMinHeight = environment.getTerrain().getHeight(player.getCharacterNode()
	                .getLocalTranslation()) - 7.5f ;
	        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {
	            player.getCharacterNode().getLocalTranslation().y = characterMinHeight;
	        }
	        
	        updateCharacters(tpf);
	        
	        /** print the crosshair only in first person view */
	        if( inputHandler.isFirstPerson() ) {
	        	if( !hudNode.hasChild( crosshair ) )
	        		hudNode.attachChild( crosshair );
	        } else {
	        	if( hudNode.hasChild( crosshair ) )
	        		hudNode.detachChild( crosshair );
	        }
	        
		    if( freeCam ) {
		    	inputHandler.setEnabled( false );
		    	freeCamInput.setEnabled( true );
		    } else {
		    	freeCamInput.setEnabled( false );
		    	inputHandler.setEnabled( true );
		    }
	        
	        updateBullets(tpf);
	        updateAmmoPackages(tpf);
	        updateEnergyPackages(tpf);
    	}
    	
		updateInput();
    }
    
	private void updateInput() {
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("freeCamAction", false ) ) {
            freeCam = !freeCam;
        }
	}

	private void gameOver() {
		gameOver.print( "Game Over" );
        gameOver.setLocalScale( 3 );
        gameOver.setTextColor( ColorRGBA.red );
		gameOver.setLocalTranslation(new Vector3f(display.getWidth() / 2f - gameOver.getWidth() / 2,
				display.getHeight() / 2f - gameOver.getHeight() / 2, 0));
		
		playDeathSound();
		
		enabled = false;
	}

	/** Function updateCharacters<br>
	 * Call the update method of each character contained in the characters hashMap
	 */
	private void updateCharacters( float time ) {
		Collection<Character> c = new LinkedList<Character>( characters.values() );
		Iterator<Character> it = c.iterator();
		while( it.hasNext() ) {
			it.next().update(time);
		}
		c.clear();
		c = null;
	}
	
	/** Function updateBullets <br>
	 * Call the update method of each bullet contained in the bullets hashMap
	 */
	private void updateBullets( float time ) {
		Collection<Bullet> c = new LinkedList<Bullet>( bullets.values() );
		Iterator<Bullet> it = c.iterator();
		while( it.hasNext() ) {
			it.next().update(time);
		}
		c.clear();
		c = null;
	}
	
	/** Function updateAmmoPackages <br>
	 * Call the update method of each ammo pack contained in the ammoPackages hashMap
	 */
    public void updateAmmoPackages( float time ) {
		Collection<AmmoPackage> c = new LinkedList<AmmoPackage>( ammoPackages.values() );
		Iterator<AmmoPackage> it = c.iterator();
    	while( it.hasNext() ) {
    		it.next().update(time);
    	}
		c.clear();
		c = null;
    }
    
	/** Function updateEnergyPackages <br>
	 * Call the update method of each energy pack contained in the energyPackages hashMap
	 */
    public void updateEnergyPackages( float time ) {
		Collection<EnergyPackage> c = new LinkedList<EnergyPackage>( energyPackages.values() );
		Iterator<EnergyPackage> it = c.iterator();
    	while( it.hasNext() ) {
    		it.next().update(time);
    	}
		c.clear();
		c = null;
    }

	private void setupEnergyPackages() {
		for( String id : core.getEnergyPackagesIds() ) {
			Vector3f position = new Vector3f();
			Random r = new Random();
			position.setX( r.nextInt( (int) dimension ) );
			position.setZ( r.nextInt( (int) dimension ) );
			position.setY( environment.getTerrain().getHeight(position.x, position.z) +10 );
			EnergyPackage e = new EnergyPackage( id, this, position );
			energyPackages.put( e.id, e );
		}
	}
	
	public Node getHudNode() {
		return hudNode;
	}

	public float getDimension() {
		return dimension;
	}

	public void setCrosshair() {
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
	    as.setBlendEnabled(true);
	    as.setTestEnabled(false);
	    as.setSourceFunction( SourceFunction.SourceAlpha );
	    as.setDestinationFunction( DestinationFunction.OneMinusSourceAlpha );
	    as.setEnabled(true);
		TextureState cross = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		cross.setTexture( TextureManager.loadTexture( Loader.load(
		                "game/data/images/crosshair.png" ), false ) );
		cross.setEnabled(true);
		crosshair = new Quad( "quad", 30, 30 );
		crosshair.setLocalTranslation( DisplaySystem.getDisplaySystem().getWidth() / 2,
				DisplaySystem.getDisplaySystem().getHeight() / 2, 0 );
		crosshair.setRenderState(as);
		crosshair.setRenderState(cross);
		
		hudNode.attachChild(crosshair);
		hudNode.updateGeometricState( 0.0f, true );
		hudNode.updateRenderState();
	}

	public WorldInterface getCore() {
		return core;
	}
	
	public StaticPhysicsNode getGround() {
		return environment.ground;
	}
	
	protected void cleanup() {
		super.cleanup();
		if( audioEnabled )
			audio.cleanup();
	}
	
	public void shoot( Vector3f position ) {
		if( audioEnabled ) {
	    	SoundManager.explosion.setWorldPosition( position.clone() );
	    	SoundManager.explosion.setVolume( 0.7f );
			SoundManager.shoot.play();
		}
	}
	
	public void explode( Vector3f position ) {
		if( audioEnabled ) {
	    	SoundManager.explosion.setWorldPosition( position.clone() );
	    	SoundManager.explosion.setVolume( 5 );
			SoundManager.explosion.play();
		}
	}
    
	public void playDeathSound() {
		if( audioEnabled ) {
	    	SoundManager.death.setWorldPosition( cam.getLocation().clone() );
	    	SoundManager.death.setVolume( 5 );
			SoundManager.death.play();
		}
	}
	
    public Vector2f getResolution() {
		return resolution;
	}

	public BasicPassManager getPass() {
		return passManager;
	}

	public LightState getLight() {
		return lightState;
	}
}
