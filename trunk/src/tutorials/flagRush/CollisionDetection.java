package tutorials.flagRush;

import java.util.logging.Logger;

import jmetest.effects.RenParticleEditor;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleSystem;
import com.jmex.effects.particles.SimpleParticleInfluenceFactory;

public class CollisionDetection{
 
    public static final int BounceOff = 0;
    public static final int Stop = 1;
 
	private Vehicle player;
	private Node target;
	private int NextMove;
	private ParticleSystem particleGeom;
	private float Zextent;
 
	public CollisionDetection(Vehicle Player, Node Target,int NextMove) {
        this.player = Player;
        this.target = Target;
        this.NextMove = NextMove;
        this.Zextent =((BoundingBox)player.getWorldBound()).zExtent;
        createParticles();
    }
	public CollisionDetection(Vehicle Player, Spatial Target,int NextMove) {
        this.player = Player;
        this.target = (Node)Target;
        this.NextMove = NextMove;
        this.Zextent =((BoundingBox)player.getWorldBound()).zExtent;
        createParticles();
    }
 
    public void ProcessCollisions() {
    	if (NextMove == BounceOff){//se la collisione è a rimbalzo
               if (player.hasCollision(target, false)){
            	//se la ruota anteriore collide con l'obiettivo(uno dei nodi dell'albero fence)
        		if (player.frontwheel.hasCollision(target, false)){
        			player.setVelocity(-Math.abs(player.getVelocity()));//inverto velocità
        			if (player.getVelocity() > -0.5)//porto la velocità ad un minimo fissato
        				player.setVelocity(-0.5f);
        			//imposto le particelle che scaturiscono dall'impatto
            		        particleGeom.setLocalTranslation(0F,player.frontwheel.getLocalTranslation().y*0.0025f, Zextent);
        			particleGeom.forceRespawn();
        		} else if(player.backwheel.hasCollision(target, false)){
        			player.setVelocity(Math.abs(player.getVelocity()));
        			if (player.getVelocity() < 0.5)
        				player.setVelocity(0.5f);
            		        particleGeom.setLocalTranslation(0F,player.backwheel.getLocalTranslation().y*0.0025f, -Zextent);
        			particleGeom.forceRespawn();
        		}
        	}
    	}else if (NextMove == Stop){
               if (player.hasCollision(target, false)){
        		if (player.frontwheel.hasCollision(target, false)){
                                player.setMaxSpeed(0f);
                                player.setMinSpeed(15f);
        		} else if(player.backwheel.hasCollision(target, false)){
                                player.setMaxSpeed(25f);
                                player.setMinSpeed(0f);
        		} else {
                                player.setMaxSpeed(25f);
                                player.setMinSpeed(15f);
                        }
        	}
    	}
    }
 
 
    private void createParticles(){
    	//imposto tipo di reazione alla collisione
		particleGeom = ParticleFactory.buildParticles("collsion", 300);
    	particleGeom.addInfluence(SimpleParticleInfluenceFactory
    			.createBasicGravity(new Vector3f(0, -0.01f, 0), true));
    	particleGeom.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
    	particleGeom.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
    	particleGeom.setMinimumLifeTime(2000.0f);
    	particleGeom.setMaximumLifeTime(4000.0f);
    	particleGeom.setInitialVelocity(.004f);
    	particleGeom.recreate(FastMath.nextRandomInt(6, 20));
    	particleGeom.setStartSize(.05f);
    	particleGeom.setEndSize(.02f);
    	particleGeom.setStartColor(new ColorRGBA(1.0f, 0.796875f, 0.1992f, 1.0f));
    	particleGeom.setEndColor(new ColorRGBA(1.0f, 1.0f, 0.5976f, 1.0f));
    	//particleGeom.warmUp(120);
    	particleGeom.getParticleController().setRepeatType(Controller.RT_CLAMP);
 
    	BlendState as = (BlendState) particleGeom.getRenderState(RenderState.RS_BLEND);
    	if (as == null) {
        	as = DisplaySystem.getDisplaySystem().getRenderer()
        		.createBlendState();
        	as.setBlendEnabled(true);
        	as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        	as.setTestEnabled(true);
        	as.setTestFunction(BlendState.TestFunction.GreaterThan);
        	particleGeom.setRenderState(as);
        	particleGeom.updateRenderState();
    	}
    	as.setDestinationFunction(BlendState.DestinationFunction.One);
    	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    	ts.setTexture(TextureManager.loadTexture(RenParticleEditor.class
            	.getClassLoader().getResource(
                    	"jmetest/data/texture/flaresmall.jpg"),
                    Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear));
    	particleGeom.setRenderState(ts);
 
    	player.attachChild(particleGeom);
    	particleGeom.updateRenderState();
    }
}
