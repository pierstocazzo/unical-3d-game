package andreaFolder.input;


import andreaFolder.graphics.GraphicalWorld;

import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

public class ExitAction extends InputAction {
	GraphicalWorld gWorld;
	
	public ExitAction(GraphicalWorld gWorld) {
		this.gWorld = gWorld;
	}

	@Override
	public void performAction(InputActionEvent evt) {
		System.out.println("Game: Premuto esc");
    	
    	//vado in wait e passo il testimone a swing
    	gWorld.tc.waitThread();
	}

}
