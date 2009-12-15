package proposta.main;

import proposta.graphics.GraphicalWorld;

import java.nio.FloatBuffer;
import java.util.HashMap;

import utils.Loader;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Disk;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

public class WorldMap2D {
	
	/** the graphical world to represent in the 2d map */
	GraphicalWorld world;
	
	/** the 2d Map */
	Quad map;
	
	int textureWidth;
    int textureHeight;
    
    /** This HashMap contains the disk of each character */
    HashMap<String, Disk> characters;
    
    /* parameters used to create the map proportionally to 
     * the display resolution and to the world dimensions 
     */
    float displayWidth;
    float worldDimension;
    float mapDimension;
	float scale;
	
	/** WorldMap2D constructor 
	 * Create a 2d Map of the graphical world.
	 * @param world - (GraphicalWorld) the graphical world to represent in the 2d map
	 */
	public WorldMap2D( GraphicalWorld world ){
		this.world = world;
		
		// init parameters
		this.displayWidth = world.getResolution().x;
		this.worldDimension = world.getDimension();
		this.mapDimension = displayWidth / 5.6f;
		this.scale = worldDimension / mapDimension;
		
		characters = new HashMap<String, Disk>();
		
		map = new Quad( "hudmap", mapDimension, mapDimension );
		world.getHudNode().attachChild( map );
		
		Vector3f pos = new Vector3f( displayWidth - mapDimension/2, mapDimension/2, 0 );
		map.setLocalTranslation( pos );

        // create the texture state to handle the texture
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture( TextureManager.loadTexture( Loader.load(
		   "jmetest/data/texture/terrain/terrainlod.jpg" ), false ) );

        // initialize texture width
        textureWidth = ts.getTexture().getImage().getWidth();
        // initialize texture height
        textureHeight = ts.getTexture().getImage().getHeight();

        // activate the texture state
        ts.setEnabled(true);
        map.setRenderState(ts);

        final FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
        texCoords.put(getUForPixel(0)).put( getVForPixel(0) );
        texCoords.put(getUForPixel(0)).put( getVForPixel(textureHeight) );
        texCoords.put(getUForPixel(textureWidth)).put( getVForPixel(textureHeight) );
        texCoords.put(getUForPixel(textureWidth)).put( getVForPixel(0) );
        // assign texture coordinates to the quad
        map.setTextureCoords( new TexCoords( texCoords ) );
        
        map.updateRenderState();     
        map.lock();
        
		for( String id : world.getCore().getPlayersId() ) {
			createDisk( id, ColorRGBA.blue );
		}
		
		for( String id : world.getCore().getEnemiesId() ){
			createDisk( id, ColorRGBA.red );
		}
	}
	
	/**
	 * Update the 2d WorldMap 
	 */
	public void update() {
		// Clean previously printed disks
		for( String id : characters.keySet() )
			world.getHudNode().detachChild( characters.get(id) );
		
		// set the new position and render each disk
		for( String id : world.getCore().getCharactersId() ) {
			characters.get( id ).setLocalTranslation( calculatePosition( world.getCore().getCharacterPosition( id ) ) );
			world.getHudNode().attachChild( characters.get( id ) );
		}
	}
	
	/** Function that create a new disk with this color, and add it into the hashmap
	 * with the id: characterId + "disk" (es: player1disk)
	 * @param id - (String) the character's id
	 * @param color - (ColorRGBA) the color to set to the disk
	 */
	void createDisk( String id, ColorRGBA color ) {
		Disk disk = new Disk( id + "disk", 5, 5, 5 );
		disk.clearTextureBuffers();
		disk.setDefaultColor( color );
		world.getHudNode().attachChild( disk );
		characters.put( id, disk );
	}
	
	/** Utility function that calculate the correct position where to print the disk in the map
	 * 
	 * @param pos - (Vector3f) the world position of the character to display in the 2d map
	 * @return the position where to print the disk
	 */
	Vector3f calculatePosition( Vector3f pos ) {
		return new Vector3f( displayWidth - mapDimension/2 + pos.x/scale, mapDimension/2 + pos.z/-scale, 0 );
	}
	
	private float getUForPixel(int xPixel) {
        return (float) xPixel / textureWidth;
    }

    private float getVForPixel(int yPixel) {
        return 1f - (float) yPixel / textureHeight;
    }
}