package proposta.main;

import proposta.graphics.GraphicalWorld;

import java.nio.FloatBuffer;
import java.util.ArrayList;

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

public class HudMap {
	
	GraphicalWorld world;
	Vector3f pos;
	Quad map;
	int textureWidth;
    int textureHeight;
    Disk disk;
    ArrayList<Disk> diskArray;
    
    float width;
    float worldDim;
    float worldZ;
    float mapDim;
	float scale;

	public HudMap(GraphicalWorld gWorld){
		this.world = gWorld;
		
		width = gWorld.getResolution().x;
		
		worldDim = gWorld.getDimension();
		
		mapDim = width / 5.6f;
		
		scale = worldDim / mapDim;
		
		diskArray = new ArrayList<Disk>();
		
		map = new Quad( "hudmap", mapDim, mapDim );
		this.world.hudNode.attachChild( map );
		
		pos = new Vector3f( width - mapDim/2, mapDim/2, 0 );
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
        texCoords.put(getUForPixel(0)).put(getVForPixel(0));
        texCoords.put(getUForPixel(0)).put(getVForPixel(textureHeight));
        texCoords.put(getUForPixel(textureWidth)).put(getVForPixel(textureHeight));
        texCoords.put(getUForPixel(textureWidth)).put(getVForPixel(0));
        // assign texture coordinates to the quad
        map.setTextureCoords(new TexCoords(texCoords));
        // apply the texture state to the quad
        map.updateRenderState();     
        
        map.lock();
	}
	
	public void update(){
		//clean disk stack
		for(int i=0; i< diskArray.size(); i++)
			world.hudNode.detachChild( diskArray.get(i) );
		
		for( String id : world.getCore().getPlayersId() ) {
			pos.set( world.getCore().getCharacterPosition( id ) );
			disk = new Disk( id, 5, 5, 5 );
			disk.clearTextureBuffers();
			disk.setDefaultColor( ColorRGBA.blue );
			world.hudNode.attachChild(disk);
			disk.setLocalTranslation( width - mapDim/2 + pos.x/scale, mapDim/2 + pos.z/-scale, 0 );
			diskArray.add(disk);
		}
		
		for( String id : world.getCore().getEnemiesId() ){
			pos.set( world.getCore().getCharacterPosition( id ) );
			disk = new Disk( id, 5, 5, 5 );
			disk.clearTextureBuffers();
			disk.setDefaultColor( ColorRGBA.red );
			world.hudNode.attachChild(disk);
			disk.setLocalTranslation( width - mapDim/2 + pos.x/scale, mapDim/2 + pos.z/-scale, 0 );
			diskArray.add(disk);
		}
	}
	
	private float getUForPixel(int xPixel) {
        return (float) xPixel / textureWidth;
    }

    private float getVForPixel(int yPixel) {
        return 1f - (float) yPixel / textureHeight;
    }
}