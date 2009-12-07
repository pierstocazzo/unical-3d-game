package proposta.graphics;

import java.net.URL;

import utils.Loader;

import com.jme.renderer.Camera;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.AudioTrack.TrackType;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class AudioManager {

	/** audio controller */
	public AudioSystem audio;
	
	/** Audio tracks */
	public static AudioTrack shoot;
	public static AudioTrack explosion;
	public static AudioTrack death;
	
	Camera cam;
	
	public AudioManager( Camera cam ) {
		this.cam = cam;
		
		initSound();
	}
	
	public void initSound() {
		audio = AudioSystem.getSystem();

		audio.getEar().trackOrientation(cam);
		audio.getEar().trackPosition(cam);

		AudioTrack backgroundMusic = getMusic( Loader.load("game/data/sound/game.ogg"));
		audio.getMusicQueue().setRepeatType(RepeatType.ALL);
		audio.getMusicQueue().setCrossfadeinTime(2.5f);
		audio.getMusicQueue().setCrossfadeoutTime(2.5f);
		audio.getMusicQueue().addTrack(backgroundMusic);
		audio.getMusicQueue().play();

		shoot = audio.createAudioTrack("/game/data/sound/mp5.ogg", false);
		shoot.setRelative(true);
		shoot.setMaxAudibleDistance(100000);
		shoot.setVolume(.7f);

		explosion = audio.createAudioTrack("/game/data/sound/explosion.ogg", false);
		explosion.setRelative(true);
		explosion.setMaxAudibleDistance(100000);
		explosion.setVolume(4.0f);

		death = audio.createAudioTrack("/game/data/sound/death.ogg", false);
		death.setRelative(true);
		death.setMaxAudibleDistance(100000);
		death.setVolume(4.0f);
	}
	
	private static AudioTrack getMusic( URL resource ) {
		// Create a non-streaming, non-looping, relative sound clip.
		AudioTrack sound = AudioSystem.getSystem().createAudioTrack( resource, true );
		sound.setType( TrackType.MUSIC );
		sound.setRelative(true);
		sound.setTargetVolume(0.7f);
		sound.setLooping(false);
		return sound;
	}
	
	public void update() {
		audio.update();
	}

	public void cleanup() {
		audio.cleanup();
	}
}
