package andreawork.app;

import jmetest.TutorialGuide.ExplosionFactory;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jmex.effects.particles.ParticleController;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;


public class ExplosionTest extends SimplePhysicsGame{
	public static void main(String[] args){
		ExplosionTest ex = new ExplosionTest();
		ex.start();
	}

	@Override
	protected void simpleInitGame() {
		// TODO Auto-generated method stub
		// crea un nodo fisico statico, al quale sar� attaccato il pavimento
        StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
        // attacco il nodo statico al root
        rootNode.attachChild( staticNode );
        // creo il mio pavimento, che � un box (dimensioni default (1,1,1)) attaccato al nodo statico
        PhysicsBox floorBox = staticNode.createBox( "floor" );
        // cambio le dimensioni del box pavimento a (50,0.5,50) cio� un pavimento 50x50 di spessore 0.5
        floorBox.getLocalScale().set( 50, 0.1f, 50 );
        
        // creo un gruppo di nodi dinamici (conterr� tutti i box che far� cadere dall'alto sul pavimento)
        Node dynamicNodeR = new Node( "gruppoAB" );
        // ciclo di creazione dei vari box "cadenti"
        for(int z=0; z<4; z++)
	        for(int j=0; j<4; j++)
		        for ( int i = 0; i < 4; i++ ) {
		            DynamicPhysicsNode n = getPhysicsSpace().createDynamicNode();
		            n.createBox("Box" + i);
		            // non ridimensiono i box, ma li piazzo ad altezze diverse dal centro (0,0,0)
		            n.getLocalTranslation().set( i*1, 0.5f+z, j*1 );
		            // setto la massa di ogni box, il primo 100, l'ultimo 1100 :-)
		            n.setMass(100);
		            // setto il materiale del box, materiali diversi hanno comportamenti diversi!
		            n.setMaterial(Material.ICE);
		            // aggiorno la massa in base alla densit� del materiale
		            n.computeMass();
		            // aggiungo il box appena creato al mio gruppo
		            dynamicNodeR.attachChild(n);
		        }
        
        //Inserire l'esplosione e' semplicissimo - per la texture poi si vede
        ExplosionManager.createExplosion(getPhysicsSpace(), new Vector3f(2,0,2), 2000f, 20f);
        ParticleMesh exp = ExplosionFactory.getSmallExplosion(); 
        exp.setLocalScale(0.001f);
        rootNode.attachChild(exp);
        exp.forceRespawn();
        rootNode.attachChild(exp);
        
        // attacco al root il mio gruppo (quindi tutti i box)
        rootNode.attachChild( dynamicNodeR );
        
        // visualizza la fisica degli oggetti
        showPhysics = true;
        pause = true;
	}
}