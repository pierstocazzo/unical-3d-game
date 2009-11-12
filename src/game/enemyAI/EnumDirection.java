package game.enemyAI;

import com.jme.math.Vector3f;

public enum EnumDirection {
	FORWARD ( 1, 0, 0 ),
	BACKWARD ( -1, 0, 0 ),
	RIGHT ( 0, 0, 1 ),
	LEFT ( 0, 0, -1 ), 
	REST ( 0, 0, 0 );
	
	int x, y, z;
	
	EnumDirection( int x, int y, int z ){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f toVector(){
		return new Vector3f(x, y, z);
	}
	
}
