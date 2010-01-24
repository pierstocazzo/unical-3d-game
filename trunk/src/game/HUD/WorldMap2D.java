package game.HUD;

import game.graphics.GraphicalWorld;

import java.util.LinkedList;
import java.util.List;

import utils.Loader;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Disk;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Class WorldMap2D
 * 
 * It displays a map at screen right
 * 
 * @author andrea
 */
public class WorldMap2D {
	
	/** the graphical world to represent in the 2d map */
	GraphicalWorld world;
	
	/** the 2d Map */
	Quad map;
    
    /** This HashMap contains the disk of each character */
    List<Spot> characters;
    
    /** display's width */
    float displayWidth;
    
    /** the x and z dimension of the 3d world to represent */
    float worldDimension;
    
    /** the x and y dimension of the map (it's a square) */
    float mapDimension;
    
    /** the extension of the map (in both x and y axis) from his center */
    float mapExtension;
    
    /** the map's scale */
	float mapScale;
	
	/** time variable */
	long oldTicks = 0;

	UserHud hud;
	
	/** 
	 * WorldMap2D constructor 
	 * 
	 * Create a 2d Map of the graphical world.
	 * 
	 * @param world - (GraphicalWorld) the graphical world to represent in the 2d map
	 * @param userHud 
	 */
	public WorldMap2D( GraphicalWorld world, UserHud userHud ){
		this.world = world;
		this.hud = userHud;
		
	    /* parameters used to create the map proportionally to 
	     * the display resolution and to the world dimensions 
	     */
		this.displayWidth = world.getResolution().x;
		this.worldDimension = world.getDimension();
		
		/* mapDimension is the dimension in pixel of the map and depends by display resolution
		 * 5.6 is the better ratio (display width / map dimension) we have found
		 * NB: we don't take care of the display height, cause our map must be a square whatever 
		 * is the aspect ratio of the user display 
		 * ( so in both 1280x1024 and 1280x800 resolutions our map is a 228x228 quad )
		 */
		this.mapDimension = displayWidth / 5.6f;
		
		this.mapExtension = mapDimension / 2;
		this.mapScale = worldDimension / mapDimension;
		
		/* initialize the list that will contains all the characters disks */
		characters = new LinkedList<Spot>();
		
		map = new Quad( "hudmap", mapDimension, mapDimension );
		hud.hudNode.attachChild( map );
		
		/* place the map in the lower right angle of the display */
		Vector3f pos = new Vector3f( displayWidth - mapExtension, mapExtension, 0 );
		map.setLocalTranslation( pos );

        /* load the texture to set to the map */
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture( TextureManager.loadTexture( Loader.load( "game/data/images/map.jpg" ), true ) );
        ts.setEnabled(true);
        map.setRenderState(ts);
        
        /* we don't need to change again the aspects of the map, so lock it */
        map.lock();
        
        /* create a blue disk for each player... */
		for( String id : world.getCore().getPlayersIds() ) {
			createSpot( id, ColorRGBA.blue );
		}
		/* ...and a red one for each enemy */
		for( String id : world.getCore().getEnemiesIds() ){
			createSpot( id, ColorRGBA.red );
		}
	}
	
	/** Update the 2d WorldMap 
	 */
	public void update() {
		// set the new position of each spot...or remove it
		for( Spot sp : characters ) {
			Vector3f pos = world.getCore().getPosition( sp.characterId );
			if( pos != null ) {
				sp.spot.setLocalTranslation( calculatePosition( pos ) );
			} else {
				sp.spot.removeFromParent();
			}
		}
	}
	
	/** Function that create a new spot with this color, and add it into the hashmap
	 * with the id: characterId + "disk" (es: player1disk)
	 * 
	 * @param id - (String) the character's id
	 * @param color - (ColorRGBA) the color to set to the disk
	 */
	void createSpot( String id, ColorRGBA color ) {
		Disk spot = new Disk( id + "disk", 5, 5, 5 );
		spot.setDefaultColor( color );
		hud.hudNode.attachChild( spot );
		spot.lockBounds();
		spot.lockMeshes();
		spot.lockShadows();
		characters.add( new Spot( id, spot ) );
	}
	
	/** Utility function that calculate the correct position where to print the disk in the map
	 * 
	 * @param pos - (Vector3f) the world position of the character to display in the 2d map
	 * @return the position where to print the disk
	 */
	Vector3f calculatePosition( Vector3f pos ) {
		return new Vector3f( displayWidth - mapExtension + pos.z/mapScale, mapExtension + pos.x/mapScale, 0 );
	}
	
	class Spot {
		String characterId;
		Disk spot;
		
		Spot( String characterId, Disk spot ) {
			this.characterId = characterId;
			this.spot = spot;
		}
	}
}