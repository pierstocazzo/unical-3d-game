package game.logic;

import com.jme.math.Vector3f;

import game.graphics.WorldInterface;

public abstract class LogicAdapter implements WorldInterface {

	@Override
	public abstract void move(String id, Vector3f position);

	@Override
	public abstract String printWorld();
}
