package utils;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.app.SimpleGame;
import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.system.DisplaySystem;
import com.sceneworker.SceneWorker;
import com.sceneworker.app.ISceneWorkerApp;
import com.sceneworker.app.SceneWorkerAppHandler;


public class SceneWorkerTest extends SimpleGame implements ISceneWorkerApp  {

	private SceneWorkerAppHandler sceneWorkerHandler;
	
	@Override
	protected void simpleInitGame() {
		SceneWorker.inst().initialiseSceneWorkerAndMonitor();
		SceneMonitor.getMonitor().registerNode(rootNode, "root");
		
		// initialize the application handler so we get tools palette, input handler and rendering
        sceneWorkerHandler = new SceneWorkerAppHandler(this);
        sceneWorkerHandler.initialise();
		
		Box box = new Box("box", Vector3f.ZERO, 10, 10, 10 );
		rootNode.attachChild(box);
	}

	public static void main(String[] args) {
		SceneWorkerTest test = new SceneWorkerTest();
		test.setConfigShowMode(ConfigShowMode.AlwaysShow);
		test.start();
	}
	
	@Override
	protected void  simpleUpdate() {
	  // update scene worker tools
	  sceneWorkerHandler.update();
	}

	@Override
	protected void simpleRender() {
	  // do scene worker render
	  sceneWorkerHandler.render();
	}
	
	// get the current input handler
	public InputHandler getInputHandler() {
		return input;
	}

	// get the root node of the scene
	public Node getRootNode() {
		return rootNode;
	}

	// get the tpf parameter for updates
	public float getTimePerFrame() {
		return tpf;
	}

	/* set the input handler : SceneWorker has it's own input handler which can be swapped in and out by pressing '1' on the keyboard. 
	This will toggle between the applications input handler or scene workers */
	public void setInputHandler(InputHandler cl_ip) {
		input = cl_ip;
	}

	// get the display system       
	public DisplaySystem getDisplaySystem() {
		return display;
	}

	// get the current camera
	public Camera getCamera() {
		return cam;
	}
}
