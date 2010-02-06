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

import game.HUD.HudMessageBox;
import game.HUD.UserHud;
import game.base.PhysicsGame;
import game.common.GameConf;
import game.input.GameInputHandler;
import game.menu.MainMenu;
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
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
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
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.font2d.Text2D;
import com.jmex.physics.StaticPhysicsNode;

/** Class GraphicalWorld <br>
 * The main graphics class which contains all the graphical objects and characters
 * 
 * @author Giuseppe Leone, Salvatore Loria, Andrea Martire
 */
public class GraphicalWorld extends PhysicsGame {
	
	/** an interface to communicate with the application core */
	WorldInterface core;
	
	/** a custom freeCamInput handler to control the player */
	GameInputHandler inputHandler;
    
    /** list of the characters */
    LinkedList<GraphicalCharacter> characters; 
    
    /** list of the bullets */
    LinkedList<Bullet> bullets;
    
    /** list of the ammo packages */
    LinkedList<GraphicalAmmoPackage> ammoPackages;
    int ammoPackagesCounter = 0;
    
    /** list of the energy packages */
    LinkedList<GraphicalEnergyPackage> energyPackages;
    int energyPackagesCounter = 0;
    
    List< Node > items;
    int itemsCounter = 0;
    
    /** the player, in single player mode */
    public GraphicalPlayer player;

    /** set to false when you don't want to do the world update */
	boolean enabled = true;
	
	/** HUD */
	Quad crosshair;
	UserHud userHud;
	
	/** game menu */
	MainMenu menu;
	
	/** audio controller */
	SoundManager audio;
	private boolean audioEnabled;
	
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
	
	Text cameraPosition;

	public boolean isLoaded = false;
	
	/** GraphicalWorld constructor <br>
	 * Initialize the game graphics
	 * 
	 * @param core - (WorldInterface) the logic world
	 * @param isLoaded 
	 * @param loadingFrame - (LoadingFrame) the loading frame
	 */
    public GraphicalWorld( WorldInterface core, MainMenu menu, boolean isLoaded ) {
        this.core = core;
        this.menu = menu;
        this.isLoaded = isLoaded;
        
        setAudioEnabled(Boolean.valueOf(
        		GameConf.getSetting( GameConf.SOUND_ENABLED ) ) );
    }
    
	/** Used for debug mode */
	public InputHandler freeCamInput;
	
    @Override
    protected void setupGame() {
    	menu.setLoadingText( GameConf.getPhrase( GameConf.LOADING_INIT ) );
    	menu.setProgress(5);
        setupInit();
        menu.setLoadingText( GameConf.getPhrase( GameConf.LOADING_ENVIRONMENT ) );
        menu.setProgress(20);
        setupEnvironment();
        menu.setLoadingText( GameConf.getPhrase( GameConf.LOADING_PLAYERS ) );
        menu.setProgress(60);
        setupPlayer();
        menu.setLoadingText( GameConf.getPhrase( GameConf.LOADING_ENEMIES ) );
        menu.setProgress(80);
        setupEnemies();
        menu.setLoadingText( GameConf.getPhrase( GameConf.LOADING_INPUT ) );
        setupCamera();
        setupInput();
        menu.setProgress(100);
        freeCamInput = new FirstPersonHandler( cam, 200, 1 );
        freeCamInput.setEnabled( false );
        
        menu.hideMenu();
    }

	public void setupInit() {
   	
		/** Setting up collision node */
		collisionNode = new Node("collision");
		rootNode.attachChild(collisionNode);
		
		characters = new LinkedList<GraphicalCharacter>();
		bullets = new LinkedList<Bullet>();
		ammoPackages = new LinkedList<GraphicalAmmoPackage>();
		energyPackages = new LinkedList<GraphicalEnergyPackage>();
		items = new LinkedList<Node>();
		
		menu.setProgress(10);
		
    	resolution = new Vector2f(
    			Integer.valueOf( GameConf.getSetting( GameConf.RESOLUTION_WIDTH ) ),
    			Integer.valueOf( GameConf.getSetting( GameConf.RESOLUTION_HEIGHT ) ) );
    	
    	cameraPosition = Text2D.createDefaultTextLabel("cameraPosition");
		cameraPosition.setLocalTranslation(getResolution().x/2, getResolution().y * 3/4, 0 );
    	
		menu.setProgress(15);
		
    	ExplosionFactory.warmup();
    	
		if( isAudioEnabled() ) {
			audio = new SoundManager( cam );
		}
    }
    
	public void setupEnvironment() {
	    rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
	    environment = new Environment( this );
	
	    setupEnergyPackages();
//	    setupAmmoPackages();
	    
	    userHud = new UserHud(this);
	}

	/** Create graphic players
	 *  and place them in positions setted in the logic game
	 */
	public void setupPlayer() {
		
		// TODO rivedere setupPlayer per multiplayer
		
		Node model = ModelLoader.loadModel("game/data/meshes/soldier/soldier.jme", 
				"game/data/meshes/soldier/soldier.jpg", 1 );
	    model.setLocalTranslation(0, -2f, 0);   

	    menu.setProgress(65);
	    
		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/meshes/soldier/AR15.jpg" ),
	            Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear);
	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    ts.setEnabled(true);
	    ts.setTexture(texture);
		model.getChild( "Regroup05" ).setRenderState( ts );
		menu.setProgress(70);
	    
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
        for( String id : core.getEnemiesIds() ) {
        	enemiesCounter++;
    		Node model = ModelLoader.loadModel("game/data/meshes/soldier/soldier.jme", 
    				"game/data/meshes/soldier/soldier.jpg", 1 );
    	    model.setLocalTranslation(0, -2f, 0);   
    	    
    		Texture texture = TextureManager.loadTexture( Loader.load( "game/data/meshes/soldier/AR15.jpg" ),
    	            Texture.MinificationFilter.Trilinear,
    	            Texture.MagnificationFilter.Bilinear);
    	    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    	    ts.setEnabled(true);
    	    ts.setTexture(texture);
    		model.getChild( "Regroup05" ).setRenderState( ts );
    		
            GraphicalEnemy enemy = new GraphicalEnemy( id, this, 5, 100,  model );
            
            Vector3f position = core.getPosition(id);
            position.setY( environment.getHeight( position.x, position.z ) + 2 );
        	enemy.getCharacterNode().getLocalTranslation().set( position );
        	characters.add( enemy );
        	rootNode.attachChild( enemy.getCharacterNode() );
        }
    }
    
    public void setupCamera() {
        cam.setLocation( player.getCharacterNode().getLocalTranslation().clone() );
        cam.setFrustumPerspective(45.0f,
        		Float.valueOf( GameConf.getSetting( GameConf.RESOLUTION_WIDTH ) ) / 
        		Float.valueOf( GameConf.getSetting( GameConf.RESOLUTION_HEIGHT ) ),
        		1, environment.farPlane);
        cam.update();
    }

    public void setupInput() {
    	mouseLookHandler = new MouseLookHandler( cam, 1 );
    	mouseLookHandler.setEnabled(false);
    	
        KeyBindingManager.getKeyBindingManager().set( "freeCamAction", KeyInput.KEY_C );
        KeyBindingManager.getKeyBindingManager().set( "switch_message", KeyInput.KEY_RETURN);
        KeyBindingManager.getKeyBindingManager().set( "exit", KeyInput.KEY_ESCAPE );
        
        inputHandler = new GameInputHandler( player, cam );
        
        // setup CollisionHandler
        collisionHandler = new CollisionHandler( inputHandler, collisionNode );
    }

	@Override
    protected void update() {
		if( !enabled )
			return;
		
		if( isAudioEnabled() )
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
			
    		collisionHandler.update();
	        inputHandler.update(tpf);
	        freeCamInput.update(tpf);
	        
	        if(freeCamInput.isEnabled())
	        {
	        	cameraPosition.print(cam.getLocation().toString());
	        	cameraPosition.setLocalTranslation(
	        			getResolution().x/2 - cameraPosition.getWidth()/2,
	        			cameraPosition.getLocalTranslation().y, 0);
	        	if(!userHud.hudNode.hasChild(cameraPosition))
	        		userHud.hudNode.attachChild(cameraPosition);
	        }
	        else
	        	cameraPosition.removeFromParent();
	        
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
	        userHud.update();
	        
	        /** print the crosshair only in first person view */
	        if( inputHandler.isFirstPerson() ) {
	        	userHud.showCrosshair( true );
	        } else {
	        	userHud.showCrosshair( false );
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
			if(isAudioEnabled())
				SoundManager.playSound(SoundType.WALK, cam.getLocation().clone());
		if( player.running && isAudioEnabled())
				SoundManager.playSound(SoundType.RUN, cam.getLocation().clone());
    }
    
	private void checkVictory() {
		if(!victory){
			// Just for single player 
			int tot = characters.size() - 1;
			if( tot == 0 ) {
				userHud.hudMsgBox.createMessageBox(HudMessageBox.VICTORY);
				if(isAudioEnabled())
					SoundManager.playSound(SoundType.VICTORY, cam.getLocation().clone());
				victory = true;
			}
		}
	}

	private void updateInput() {
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "freeCamAction", false ) ) {
            freeCam = !freeCam;
        }
        
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "switch_message", false ) ) {
            userHud.hudMsgBox.switchToNext();
        }
        
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "exit", false ) ) {
        	/* switch to in game menu */
        	menu.switchToPanel("inGamePanel");
        	menu.setVisible(true);
        	menu.toFront();
        	
    		stopGame();
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
			
			float y = environment.getHeight( position );
			
			while( y < 30 && y > 200 ) {
				position.setX( r.nextInt( (int) dimension ) - dimension/2 );
				position.setZ( r.nextInt( (int) dimension ) - dimension/2 );
				y = environment.getHeight( position );
			}
			
			position.setY( environment.getHeight( position ) );
			GraphicalEnergyPackage energyPack = new GraphicalEnergyPackage( id, this, position );
			energyPackages.add( energyPack );
		}
	}
	
//	private void setupAmmoPackages() {
//		for( int i = 0; i < core.getEnemiesIds().size(); i++ ) {
//			ammoPackagesCounter++;
//			GraphicalAmmoPackage ammoPack = new GraphicalAmmoPackage( "ammo" + ammoPackagesCounter, this, Vector3f.ZERO );
//			ammoPack.pack.setCullHint( CullHint.Always );
//			ammoPackages.add( ammoPack );
//		}
//	}

	public float getDimension() {
		return dimension;
	}

	public WorldInterface getCore() {
		return core;
	}
	
	public StaticPhysicsNode getGround() {
		return environment.ground;
	}
	
	public void shoot( Vector3f position ) {
		if( isAudioEnabled() )
	    	SoundManager.playSound( SoundType.SHOOT, position.clone() );
	}
	
	public void explode( Vector3f position ) {
		if( isAudioEnabled() ) 
			SoundManager.playSound( SoundType.EXPLOTION, position.clone() );
	}
    
	public void playDeathSound() {
		if( isAudioEnabled() ) 
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

	public void setAudioEnabled(boolean audioEnabled) {
		this.audioEnabled = audioEnabled;
	}

	public boolean isAudioEnabled() {
		return audioEnabled;
	}
	
    public void setMenu( MainMenu menu ) {
		this.menu = menu;
	}

	public MainMenu getMenu() {
		return menu;
	}

	@Override
	public void showPause(boolean pause) {
		userHud.showPause( pause );
	}
}
