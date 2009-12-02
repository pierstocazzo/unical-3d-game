package andreaFolder;

import javax.swing.ImageIcon;

import slashWork.customGame.Game2;
import slashWork.game.base.Game;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;


import joework.app.AssHoleBaseGame;

public class testPhysicsGame extends Game{

	Node player;
	TerrainBlock terrain;
	private FirstPersonHandler input;
	
	public static void main(String[] argc){
		testPhysicsGame app = new testPhysicsGame();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	@Override
	public void setupCamera() {
		// TODO Auto-generated method stub
		//cam.setLocation(new Vector3f(0,500,300));
	}

	@Override
	public void setupEnvironment() {
		// generiamo i dati di un terreno casuale
		   MidPointHeightMap heightMap = new MidPointHeightMap( 64, 1f );
		   // creiamo un vettore di ridimensionamento del terreno
		   Vector3f terrainScale = new Vector3f(4, 0.0575f, 4);
		   // creiamo il nostro blocco di terreno, attraverso i dati generati prima
		   terrain = new TerrainBlock("Terrain", heightMap.getSize(), terrainScale,
				   						heightMap.getHeightMap(), new Vector3f(0, 0, 0));

		   terrain.setModelBound(new BoundingBox());
		   terrain.updateModelBound();
		   
		   // generiamo le textures...
		   ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
		   // per le zone basse usiamo l'erba
		   pt.addTexture(new ImageIcon(Game2.class.getClassLoader().getResource("data/texture/grassb.png")), -128, 0, 128);
		   // per le zone di media altezza usiamo terra e rocce
		   pt.addTexture(new ImageIcon(Game2.class.getClassLoader().getResource("data/texture/dirt.jpg")), 0, 128, 255);
		   // per le zone alte usiamo la neve!
		   pt.addTexture(new ImageIcon(Game2.class.getClassLoader().getResource("data/texture/highest.jpg")), 128, 255, 384);
		   // il numero passato alla funzione significa che creeremo textures n x n
		   pt.createTexture(32);
		   // il nostro proceduralblabla � in grado di miscelare le textures per creare un terreno un po pi� realistico
		   
		   // ora applichiamo le textures create al nostro terreno
		   TextureState ts = display.getRenderer().createTextureState();
		   Texture t1 = TextureManager.loadTexture(pt.getImageIcon().getImage(),
		                   Texture.MinificationFilter.BilinearNearestMipMap,
		                   Texture.MagnificationFilter.Bilinear,
		   	        true);
		   ts.setTexture(t1, 0);
		   terrain.setRenderState( ts );
		   
		   rootNode.attachChild( terrain );
	}

	@Override
	public void setupInit() {
		
		DynamicPhysicsNode dynamicNode = getPhysicsSpace().createDynamicNode();
		dynamicNode.createBox("box");
		rootNode.attachChild(dynamicNode);
		dynamicNode.setLocalTranslation(0,0,0);
		
		showPhysics = true;
		pause = true;
	
		//cam.setLocation(new Vector3f(0,2,0));
	}

	@Override
	public void setupInput() {
		
		input = new FirstPersonHandler(cam,50,1);
	}

	@Override
	public void setupPlayer() {
		/*URL modelURL= Game.class.getClassLoader().getResource("data/model/tree1.jme");
		//URL textureURL = Game.class.getClassLoader().getResource("data/texture/tree1.tga");
        
		Node model = null;
        try {
			model = (Node) BinaryImporter.getInstance().load( modelURL );
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//model.setName("plant");
        //model.updateRenderState();
        player.attachChild(model);
        rootNode.attachChild(player);
		*/
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		input.update(tpf);
		cam.update();
	}
	@Override
	public void setupEnemies() {
		// TODO Auto-generated method stub
		
	}

}
