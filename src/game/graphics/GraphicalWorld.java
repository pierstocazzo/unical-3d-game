package game.graphics;

import game.HUD.HudMessageBox;
import game.HUD.UserHud;
import game.base.Game;
import game.common.GameConfiguration;
import game.input.ThirdPersonHandler;
import game.menu.LoadingFrame;
import game.sound.SoundManager;
import game.sound.SoundManager.SoundType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import jmetest.TutorialGuide.ExplosionFactory;
import utils.Loader;
import utils.ModelLoader;

import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseLookHandler;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
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
    
    /** list of the characters */
    List<GraphicalCharacter> characters; 
    
    /** list of the bullets */
	List<Bullet> bullets;
    
    /** list of the ammo packages */
	List<GraphicalAmmoPackage> ammoPackages;
    int ammoPackagesCounter = 0;
    
    /** list of the energy packages */
	List<GraphicalEnergyPackage> energyPackages;
    int energyPackagesCounter = 0;
    
    List< Node > items;
    int itemsCounter = 0;
    
    /** the player, in single player mode */
    public GraphicalPlayer player;

    /** set to false when you don't want to do the world update */
	boolean enabled = true;
	
	/** HUD node */
	public Node hudNode;
	/** very very basic hud */
	Quad crosshair;
	Text gameOver;
	public UserHud userHud;
	
	/** audio controller */
	SoundManager audio;
	public boolean audioEnabled;
	
	boolean freeCam;

	MouseLookHandler mouseLookHandler;
	
	/** the world dimension (the world is a square) */
	float dimension;

	/** screen resolution */
	Vector2f resolution;

	/** the environment */
	Environment environment;

	int playersCounter = 0;
	int enemiesCounter = 0;
	
	boolean endGame = false;
	boolean victory = false;

	Node collisionNode;

	CollisionHandler collisionHandler;
	
	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) the logic world
	 * @param loadingFrame - (LoadingFrame) the loading frame
	 */
    public GraphicalWorld( WorldInterface core, LoadingFrame loadingFrame ) {
        this.core = core;
        super.loadingFrame = loadingFrame;
        
        audioEnabled = Boolean.valueOf( GameConfiguration.isSoundEnabled() );
    }

	public void setupInit() {
   	
		/** Setting up collision node */
		collisionNode = new Node("collision");
		rootNode.attachChild(collisionNode);
		
		hudNode = new Node( "HUD" );
		rootNode.attachChild( hudNode );
		hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		characters = new LinkedList<GraphicalCharacter>();
		bullets = new LinkedList<Bullet>();
		ammoPackages = new LinkedList<GraphicalAmmoPackage>();
		energyPackages = new LinkedList<GraphicalEnergyPackage>();
		items = new LinkedList<Node>();
		
		loadingFrame.setProgress(10);
		
    	setCrosshair();
    	resolution = new Vector2f( settings.getWidth(), settings.getHeight() );
    	
		gameOver = Text.createDefaultTextLabel( "gameOver" );
		rootNode.attachChild(gameOver);
		
		loadingFrame.setProgress(15);
		
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
		
		// TODO rivedere setupPlayer per multiplayer
		
		Node model = ModelLoader.loadModel("game/data/meshes/soldier/player.jme", 
				"game/data/meshes/soldier/soldato.jpg", 1 );
	    model.setLocalTranslation(0, -2f, 0);   

	    loadingFrame.setProgress(65);
	    
		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/meshes/soldier/lr300map.jpg" ),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear);
	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    ts.setEnabled(true);
	    ts.setTexture(texture);
		model.getChild( "weapon" ).setRenderState( ts );
		loadingFrame.setProgress(70);
	    
	    for( String id : core.getPlayersIds() ) {
	    	playersCounter++;
	    	player = new GraphicalPlayer( id, this, model );
	        player.getCharacterNode().getLocalTranslation().set( core.getPosition(id) );
	        rootNode.attachChild( player.getCharacterNode() );
	        characters.add( player );
	    }
	}

	/** Create graphic characters
     *  and place them in positions setted in the logic game
     */
    public void setupEnemies() { 
    	int difference = 20/core.getEnemiesIds().size();
    	
        for( String id : core.getEnemiesIds() ) {
        	enemiesCounter++;
    		Node model = ModelLoader.loadModel("game/data/meshes/soldier/prova.ms3d", 
    				"game/data/meshes/soldier/soldier.jpg", 1 );
    	    model.setLocalTranslation(0, -2f, 0);   
    	    
    		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/meshes/soldier/AR15.jpg" ),
    	            Texture.MinificationFilter.Trilinear,
    	            Texture.MagnificationFilter.Bilinear);
    	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    	    ts.setEnabled(true);
    	    ts.setTexture(texture);
    		model.getChild( "weapon" ).setRenderState( ts );
    		
			loadingFrame.setProgress( 80 + difference );

            GraphicalEnemy enemy = new GraphicalEnemy( id, this, 5, 100,  model );
            
            Vector3f position = core.getPosition(id);
            position.setY( environment.getHeight( position.x, position.z ) + 2 );
        	enemy.getCharacterNode().getLocalTranslation().set( position );
        	characters.add( enemy );
        	rootNode.attachChild( enemy.getCharacterNode() );
        	
        	difference = difference + 20/core.getEnemiesIds().size();
        }
    }
    
    public void setupCamera() {
        cam.setLocation( player.getCharacterNode().getLocalTranslation().clone() );
        cam.setFrustumPerspective(45.0f, (float)this.settings.getWidth() / (float)this.settings.getHeight(), 1, environment.farPlane);
        cam.update();
    }

    public void setupInput() {
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    	
        KeyBindingManager.getKeyBindingManager().set( "freeCamAction", KeyInput.KEY_C );
    	
        inputHandler = new ThirdPersonHandler( player, cam );
        
        // setup CollisionHandler
        collisionHandler = new CollisionHandler( inputHandler, collisionNode );
    }

	@Override
    protected void update() {
		if( !enabled )
			return;
		
		if( audioEnabled )
			audio.update();
    	
		if( core.isAlive( player.id ) == false ) {
			userHud.hudMsgBox.update();
			gameOver();
    	} else {
    		checkVictory();
    		
    		player.getCollision().updateWorldBound();
    		player.getCollision().updateGeometricState(0, true);
    		
    		collisionNode.updateWorldBound();
    		collisionNode.updateGeometricState(0, true);
			
    		userHud.update();
    		collisionHandler.update();
	        inputHandler.update(tpf);
	        freeCamInput.update(tpf);
	        
	        if( !inputHandler.isFirstPerson() ) {
		        float camMinHeight = environment.getHeight( cam.getLocation() ) + 1;
		        if (!Float.isInfinite(camMinHeight) && !Float.isNaN(camMinHeight)
		                && cam.getLocation().y <= camMinHeight) {
		            cam.getLocation().y = camMinHeight;
		            cam.update();
		        }
	        }
	        
	        environment.update();
	        
	        float characterMinHeight = environment.getHeight( player.getCharacterNode()
	                .getLocalTranslation() ) + 2;
	        if (!Float.isInfinite(characterMinHeight) && !Float.isNaN(characterMinHeight)) {
	            player.getCharacterNode().getLocalTranslation().y = characterMinHeight;
	        }
	        
	        updateCharacters(tpf);
	        updateRenderOptimizer();
	        
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
		
		player.getCollision().updateWorldBound();
		player.getCollision().updateGeometricState(0, true);
		
		collisionNode.updateWorldBound();
		collisionNode.updateGeometricState(0, true);
		updateInput();
		
		if( player.walkingBackwards || player.walkingForward )
			if(audioEnabled)
				SoundManager.playSound(SoundType.WALK, cam.getLocation().clone());
		if( player.running && audioEnabled)
				SoundManager.playSound(SoundType.RUN, cam.getLocation().clone());
    }
    
	private void checkVictory() {
		if(!victory){
			// Just for single player 
			int tot = characters.size() - 1;
			if( tot == 0 ) {
				userHud.hudMsgBox.createMessageBox(HudMessageBox.VICTORY);
				if(audioEnabled)
					SoundManager.playSound(SoundType.VICTORY, cam.getLocation().clone());
				victory = true;
			}
		}
	}

	private void updateInput() {
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand("freeCamAction", false ) ) {
            freeCam = !freeCam;
        }
	}

	private void gameOver() {
		if(!endGame){
			endGame = true;
			playDeathSound();
			userHud.hudMsgBox.createMessageBox(HudMessageBox.GAMEOVER);
		}
	}

	/** Function updateCharacters<br>
	 * Call the update method of each character contained in the characters hashMap
	 */
	private void updateCharacters( float time ) {
		Iterator<GraphicalCharacter> it = characters.iterator();
		while( it.hasNext() ) {
			GraphicalCharacter character = it.next();
			character.update(time);
			if( core.isAlive( character.id ) == false )
				it.remove();
		}
	}
	
	/** Function updateBullets <br>
	 * Call the update method of each bullet contained in the bullets hashMap
	 */
	private void updateBullets( float time ) {
		Iterator<Bullet> it = bullets.iterator();
		while( it.hasNext() ) {
			Bullet bullet = it.next();
			bullet.update(time);
			if( bullet.enabled == false ) 
				it.remove();
		}
	}
	
	/** Function updateAmmoPackages <br>
	 * Call the update method of each ammo pack contained in the ammoPackages hashMap
	 */
    private void updateAmmoPackages( float time ) {
		Iterator<GraphicalAmmoPackage> it = ammoPackages.iterator();
    	while( it.hasNext() ) {
    		GraphicalAmmoPackage ammoPack = it.next();
    		ammoPack.update(time);
    		if( ammoPack.enabled == false )
    			it.remove();
    	}
    }
    
	/** Function updateEnergyPackages <br>
	 * Call the update method of each energy pack contained in the energyPackages hashMap
	 */
    private void updateEnergyPackages( float time ) {
		Iterator<GraphicalEnergyPackage> it = energyPackages.iterator();
    	while( it.hasNext() ) {
    		GraphicalEnergyPackage energyPack = it.next();
    		energyPack.update(time);
    		if( energyPack.enabled == false )
    			it.remove();
    	}
    }

	private void setupEnergyPackages() {
		for( String id : core.getEnergyPackagesIds() ) {
			Vector3f position = new Vector3f();
			Random r = new Random();
			position.setX( r.nextInt( (int) dimension ) - dimension/2 );
			position.setZ( r.nextInt( (int) dimension ) - dimension/2 );
			position.setY( environment.getHeight( position ) );
			GraphicalEnergyPackage energyPack = new GraphicalEnergyPackage( id, this, position );
			energyPackages.add( energyPack );
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
		crosshair.lock();
		
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
	
	public void shoot( Vector3f position ) {
		if( audioEnabled )
	    	SoundManager.playSound( SoundType.SHOOT, position.clone() );
	}
	
	public void explode( Vector3f position ) {
		if( audioEnabled ) 
			SoundManager.playSound( SoundType.EXPLOTION, position.clone() );
	}
    
	public void playDeathSound() {
		if( audioEnabled ) 
			SoundManager.playSound( SoundType.DEATH, cam.getLocation().clone() );
	}
	
	private void updateRenderOptimizer() {
		float distance;
		Vector3f camPos = new Vector3f( cam.getLocation() );
		camPos.setY(0);
		Vector3f pos = new Vector3f();
		
		for( GraphicalCharacter character : characters ) {
			pos.set( core.getPosition( character.id ) ).setY(0);

			distance = camPos.distance( pos );

			if ( distance > 500 ) {
				if ( character.isEnabled() ) {
					character.hideModel();
					character.setEnabled(false);
				}
			} else {
				if ( !character.isEnabled() ) {
					character.showModel();
					character.setEnabled(true);
				}
			}
		}
		
		for( GraphicalEnergyPackage energyPack : energyPackages ) {
			pos.set( energyPack.position ).setY(0);
			
			distance = camPos.distance( pos );
			
			if( distance > 500 ) {
				energyPack.physicsPack.setActive( false );
				energyPack.physicsPack.removeFromParent();
			} else {
				energyPack.physicsPack.setActive( true );
				rootNode.attachChild( energyPack.physicsPack );
			}
		}
		
		for( GraphicalAmmoPackage ammoPack : ammoPackages ) {
			pos.set( ammoPack.position ).setY(0);
			
			distance = camPos.distance( pos );
			
			if( distance > 500 ) {
				ammoPack.physicsPack.setActive( false );
				ammoPack.physicsPack.removeFromParent();
			} else {
				ammoPack.physicsPack.setActive( true );
				rootNode.attachChild( ammoPack.physicsPack );
			}
		}
		
		for( Node item : items ) {
			pos.set( item.getLocalTranslation() ).setY(0);
			
			distance = camPos.distance( pos );
			
			if( distance > 500 ) {
				item.removeFromParent(); 
			} else {
				collisionNode.attachChild( item ); 
			}
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

	public Node getCollisionNode() {
		return collisionNode;
	}
}
