package slashWork.customGame;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class ForceFieldFence extends Node { 
    /** ForceFieldFence constructor
     * Crea la nostra recinzione
     * @param name nome dell'oggetto
     */
    public ForceFieldFence(String name) {
        super(name);
        buildFence();
    }

	private void buildFence() {
		// un macello per creare una recinzione completamente a mano! molto meglio caricare un modello 3d
		// in breve, tutto questo crea prima i 4 piantoni ai quattro angoli della recinzione
		// poi li unisce con dei puntelli
		// poi mette la rete
		
		// creo il mio cilindro che sarà poi replicato 4 volte (saranno le 4 torri (o piantoni) agli angoli)
		Cylinder towerGeometry = new Cylinder( "tower", 10, 10, 1, 10 );
		// lo ruoto di 90° (Pi/2) rispetto all'asse x, in quanto di default è in orizzontale
		Quaternion rotate90x = new Quaternion();
		rotate90x.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));

		towerGeometry.setModelBound( new BoundingBox() );
		towerGeometry.updateModelBound();
		// ora lo replichiamo..o meglio creiamo quattro mesh che condividono 
		// una sola geometria, appunto quella del cilindro di prima
		SharedMesh tower1 = new SharedMesh("post1", towerGeometry);
		tower1.setLocalTranslation(new Vector3f(0,0.5f,0));
		tower1.setLocalRotation(rotate90x);
		SharedMesh tower2 = new SharedMesh("post2", towerGeometry);
		tower2.setLocalTranslation(new Vector3f(32,0.5f,0));
		tower2.setLocalRotation(rotate90x);
		SharedMesh tower3 = new SharedMesh("post3", towerGeometry);
		tower3.setLocalTranslation(new Vector3f(0,0.5f,32));
		tower3.setLocalRotation(rotate90x);
		SharedMesh tower4 = new SharedMesh("post4", towerGeometry);
		tower4.setLocalTranslation(new Vector3f(32,0.5f,32));
		tower4.setLocalRotation(rotate90x);
		// i quattro cilindri fittizi vengono spostati nei quattro angoli
		// li attacco tutti ad un unico nodo
		Node towerNode = new Node("towerNode");
		towerNode.attachChild( tower1 );
		towerNode.attachChild( tower2 );
		towerNode.attachChild( tower3 );
		towerNode.attachChild( tower4 );
		// lo aggiungo alla coda degli oggetti opachi da renderizzare prima di quelli trasparenti
		towerNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		// carico una texture per le torri
		TextureState towerTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture towerTexture = TextureManager.loadTexture(Game.class.getClassLoader()
		                  .getResource("texture/post.jpg"),
		                  Texture.MinificationFilter.BilinearNearestMipMap,
		                  Texture.MagnificationFilter.Bilinear); 
		towerTextureState.setTexture( towerTexture );
		
        towerNode.setRenderState( towerTextureState );
        // ecco il nodo delle torri pronto

		
		// stesso procedimento per creare i puntelli (struts) tra le torri della mia recinzione
		Cylinder strutGeometry = new Cylinder("strut", 10,10, 0.125f, 32);
		strutGeometry.setModelBound(new BoundingBox());
		strutGeometry.updateModelBound();
		// preparo il quaternion per la rotazione dei puntelli 1 e 4
		// devo ruotarli di 90 gradi rispetto ad y
		Quaternion rotate90y = new Quaternion();
		rotate90y.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));
		// i miei 4 mesh
		SharedMesh strut1 = new SharedMesh("strut1", strutGeometry);
		strut1.setLocalRotation(rotate90y);
		strut1.setLocalTranslation(new Vector3f(16,3f,0));
		SharedMesh strut2 = new SharedMesh("strut2", strutGeometry);
		strut2.setLocalTranslation(new Vector3f(0,3f,16));
		SharedMesh strut3 = new SharedMesh("strut3", strutGeometry);
		strut3.setLocalTranslation(new Vector3f(32,3f,16));
		SharedMesh strut4 = new SharedMesh("strut4", strutGeometry);
		strut4.setLocalRotation(rotate90y);
		strut4.setLocalTranslation(new Vector3f(16,3f,32));
		// metto tutti i puntelli in un unico nodo
		Node strutNode = new Node("strutNode");
		strutNode.attachChild(strut1);
		strutNode.attachChild(strut2);
		strutNode.attachChild(strut3);
		strutNode.attachChild(strut4);
		// aggiungo anche lo strutNode alla coda degli oggetti opachi
		strutNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		// gli carico una texture
		TextureState strutTextureState= DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture strutTexture = TextureManager.loadTexture(Game.class.getClassLoader()
		                  .getResource("texture/rust.jpg"),
		                  Texture.MinificationFilter.BilinearNearestMipMap,
		                  Texture.MagnificationFilter.Bilinear);
		strutTextureState.setTexture( strutTexture );
		
		strutNode.setRenderState( strutTextureState );
		// ed ecco il nodo dei puntelli
		
		// questa sarebbe la rete..stesso procedimento, questa volta usando un box
		Box forceFieldGeometry = new Box("forceField", new Vector3f(-16, -3f, -0.1f), new Vector3f(16f, 3f, 0.1f));
		forceFieldGeometry.setModelBound(new BoundingBox());
		forceFieldGeometry.updateModelBound();
		
		SharedMesh forceField1 = new SharedMesh("forceField1",forceFieldGeometry);
		forceField1.setLocalTranslation(new Vector3f(16,0,0));
		SharedMesh forceField2 = new SharedMesh("forceField2",forceFieldGeometry);
		forceField2.setLocalTranslation(new Vector3f(16,0,32));
		SharedMesh forceField3 = new SharedMesh("forceField3",forceFieldGeometry);
		forceField3.setLocalTranslation(new Vector3f(0,0,16));
		forceField3.setLocalRotation( rotate90y );
		SharedMesh forceField4 = new SharedMesh("forceField4",forceFieldGeometry);
		forceField4.setLocalTranslation(new Vector3f(32,0,16));
		forceField4.setLocalRotation( rotate90y );
		
		Node forceFieldNode = new Node("forceFieldNode");
		forceFieldNode.attachChild(forceField1);
		forceFieldNode.attachChild(forceField2);
		forceFieldNode.attachChild(forceField3);
		forceFieldNode.attachChild(forceField4);
		// la rete la aggiungiamo alla coda degli oggetti trasparenti
		forceFieldNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		// carichiamo la texture
		TextureState forceFieldTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture forceFieldTexture = TextureManager.loadTexture(Game.class.getClassLoader()
		                  .getResource("texture/reflector.jpg"),
		                  Texture.MinificationFilter.BilinearNearestMipMap,
		                  Texture.MagnificationFilter.Bilinear);
		 
		forceFieldTexture.setWrap(Texture.WrapMode.Repeat);
		forceFieldTexture.setTranslation(new Vector3f());
		forceFieldTextureState.setTexture( forceFieldTexture );

		forceFieldNode.setRenderState( forceFieldTextureState );
		// ecco il nodo della rete

		// rendiamo la rete della recinzione trasparente
		BlendState trasparence = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		trasparence.setBlendEnabled(true);
		trasparence.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		trasparence.setDestinationFunction(BlendState.DestinationFunction.One);
		trasparence.setTestEnabled(true);
		trasparence.setTestFunction(BlendState.TestFunction.GreaterThan);
		trasparence.setEnabled(true);
		forceFieldNode.setRenderState( trasparence );
		
		// attacchiamo tutto al nostro rootNode
		this.attachChild( forceFieldNode );
		this.attachChild( towerNode );
		this.attachChild( strutNode );
	}
}
