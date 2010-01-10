package game.core;

import java.io.Serializable;

import game.common.Direction;
import game.common.GameTimer;
import game.common.State;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
@SuppressWarnings("serial")
public class AI implements Serializable {
	
	LogicWorld world;
	
	Vector3f findDirection;
	Vector3f moveDirection;

	public AI( LogicWorld world ) {
		this.world = world;
		this.findDirection = new Vector3f();
		this.moveDirection = new Vector3f();
	}
	
	public void updateState( String id ) {

		LogicEnemy enemy = (LogicEnemy) world.characters.get( id );
		float maxAlertTime = LogicEnemy.ALERT_RANGE;

		float distance;
		enemy.shootDirection.set( Vector3f.ZERO );
		for ( String playerId : world.getPlayersIds() ) {
			distance = enemy.position.distance( world.getPosition( playerId ) );

			if ( enemy.enemyNextInAttack() && enemy.state != State.ALERT ){
				enemy.state = State.ATTACK;
				enemy.alertTime = GameTimer.getTimeInSeconds();
			}

			switch ( enemy.state ) {
			case DEFAULT:
				enemy.alertTime = GameTimer.getTimeInSeconds() - 20;
				if ( distance <= enemy.state.getViewRange() ) {
					enemy.state = State.ALERT;
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;

			case ALERT:
				if ( distance <= enemy.state.getActionRange() ) {
					enemy.state = State.ATTACK;
					updateState( id );
				} else if ( distance > enemy.state.getViewRange() ) {
					/* if the player goes away from the actionRange of this enemy, 
					 * he remains in alert state for "ALERT_RANGE" seconds
					 */
					if( GameTimer.getTimeInSeconds() - enemy.alertTime > maxAlertTime ) {
						enemy.state = State.DEFAULT;
					}
				}
				if ( distance <= enemy.state.getViewRange() ) 
					enemy.alertTime = GameTimer.getTimeInSeconds();
				break;

			case ATTACK:
				if ( distance > enemy.state.getViewRange() ) {
					enemy.state = State.FIND;
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				calculateShootDirection( id, playerId );
				enemy.alertTime = GameTimer.getTimeInSeconds();
				break;

			case FIND:
				enemy.alertTime = GameTimer.getTimeInSeconds();
				if ( distance <= enemy.state.getActionRange() )
					enemy.state = State.FINDATTACK;
				break;

			case FINDATTACK:
				if ( distance > enemy.state.getViewRange() ) {
					enemy.state = State.FIND;
				} else {
					calculateShootDirection( id, playerId );
					enemy.alertTime = GameTimer.getTimeInSeconds();
				}
				break;
			}
		}
	}

	private void calculateShootDirection( String enemyId, String playerId ) {
		LogicEnemy enemy = (LogicEnemy) world.characters.get( enemyId );
		enemy.shootDirection.set( world.characters.get(playerId).position.subtract( enemy.position ).normalize() );
		// aggiungo un certo errore ruotando il vettore di un angolo random tra 0 e 10 gradi 
		float angle = FastMath.DEG_TO_RAD * ( FastMath.rand.nextFloat() % enemy.errorAngle );
		Quaternion q = new Quaternion().fromAngleAxis( angle, Vector3f.UNIT_Y );
		q.mult( enemy.shootDirection, enemy.shootDirection );
	}
	
	public Vector3f getMoveDirection( String id ) {
		
		LogicEnemy enemy = (LogicEnemy) world.characters.get(id);
		
		moveDirection.set( Vector3f.ZERO );
		
		float distance;
		
		/** utility vectors */
		Vector3f currentPosition = new Vector3f( enemy.position );
		currentPosition.setY(0);

		if( enemy.state == State.FIND ) {
			if( enemy.firstFind ) {
				findDirection.set( enemy.shootDirection );
				findDirection.setY(0);
				enemy.movementStartPosition.set( currentPosition );
				enemy.firstFind = false;
			}
			
			distance = Math.abs( currentPosition.distance( enemy.movementStartPosition ) );
			if( distance > 80 ) {
				if( enemy.comingBack == false ) {
					findDirection.negateLocal();
					enemy.comingBack = true;
				}
			}

			if( enemy.comingBack == true && distance <= 7 && distance >= 0 ) {
				enemy.comingBack = false;
				enemy.firstFind = true;
				enemy.state = State.ALERT;
				enemy.movementStartPosition.set( currentPosition );
			}
			
			return findDirection;
		} else {
			/** The enemy will move only if he is in default state. When he attack or is in alert he rests
			 */
			if( enemy.currentMovement.getDirection() != Direction.REST 
					&& enemy.state == State.DEFAULT ) {
				/** calculate distance between the current movement's start position and
				 *  the current character position
				 */
				distance = Math.abs( currentPosition.distance( enemy.movementStartPosition ) );
				/** If this distance is major than the lenght specified in the movement 
				 *  start the next movement
				 */
				if( distance >= enemy.currentMovement.getLength() ) {
					System.out.println("fuck");
					enemy.movementStartPosition.set( currentPosition );
					enemy.getNextMovement();
				}
				
				moveDirection.set( enemy.currentMovement.getDirection().toVector() );
				
				
			}
			return moveDirection;
		}
	}
}