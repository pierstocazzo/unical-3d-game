package physicsTutorials;

import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.geometry.PhysicsBox;
import com.jmex.physics.material.Material;
import com.jmex.physics.util.SimplePhysicsGame;

public class Ex1 extends SimplePhysicsGame {

	/** Main 
	 * @param args
	 */
	public static void main(String[] args)  {
		Ex1 game = new Ex1();
		// visualizzazione menu iniziale
		game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		game.start();
	}

	@Override
	protected void simpleInitGame() {

		// crea un nodo fisico statico, al quale sarà attaccato il pavimento
        StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
        // attacco il nodo statico al root
        rootNode.attachChild( staticNode );
        // creo il mio pavimento, che è un box (dimensioni default (1,1,1)) attaccato al nodo statico
        PhysicsBox floorBox = staticNode.createBox( "floor" );
        // cambio le dimensioni del box pavimento a (50,0.5,50) cioè un pavimento 50x50 di spessore 0.5
        floorBox.getLocalScale().set( 50, 0.1f, 50 );
        
        // creo un gruppo di nodi dinamici (conterrà tutti i box che farò cadere dall'alto sul pavimento)
        Node dynamicNodeR = new Node( "gruppoAB" );
        // ciclo di creazione dei vari box "cadenti"
        for ( int i = 0; i < 10; i++ ) {
            DynamicPhysicsNode n = getPhysicsSpace().createDynamicNode();
            n.createBox("Box" + i);
            // non ridimensiono i box, ma li piazzo ad altezze diverse dal centro (0,0,0)
            n.getLocalTranslation().set( 0, 5*(i+1), 0 );
            // setto la massa di ogni box, il primo 100, l'ultimo 1100 :-)
            n.setMass(100*(i+1));
            // setto il materiale del box, materiali diversi hanno comportamenti diversi!
            n.setMaterial(Material.IRON);
            // aggiorno la massa in base alla densità del materiale
            n.computeMass();
            // aggiungo il box appena creato al mio gruppo
            dynamicNodeR.attachChild(n);
        }
        
        // attacco al root il mio gruppo (quindi tutti i box)
        rootNode.attachChild( dynamicNodeR );

/* creazione sfera con texture 2d */
        
//		Sphere s = new Sphere("Sphere", 30, 30, 25);
//		s.setLocalTranslation(0,0,-40);
//		s.setModelBound(new BoundingBox());
//		s.updateModelBound();
//		
//		Texture texture = TextureManager.loadTexture(
//	                Ex1.class.getClassLoader().getResource(
//	                "images/rockwall2.jpg"),
//	                Texture.MinificationFilter.Trilinear,
//	                Texture.MagnificationFilter.Bilinear);
//		TextureState ts = display.getRenderer().createTextureState();
//		ts.setEnabled(true);
//		ts.setTexture(texture);
//	
//		s.setRenderState(ts);
//	
//		rootNode.attachChild(s);
        
        // visualizza la fisica degli oggetti
        showPhysics = true;
    }

    @Override
    protected void simpleUpdate() {
        // TODO
    }

    @Override
    protected void simpleRender() {
        // TODO
    }
}
