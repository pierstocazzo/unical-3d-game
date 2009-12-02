package slashWork.game.base;

import slashWork.game.input.action.FirstPersonAction;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;

public abstract class PassGame extends PhysicsGame {

	InputHandler input;
	
	@Override
	protected void setupGame() {
		input = new FirstPersonHandler(cam, 50, 1);
		simpleInitGame();
	}

	@Override
	protected void update() {
		input.update( tpf );
		simpleUpdate();
	}
	
	protected abstract void simpleInitGame();
	
	protected abstract void simpleUpdate();

}
