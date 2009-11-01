/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package andreaFolder.ArchitetturaStatiSistemaDaAdattare;

import java.util.logging.Level;
import java.util.logging.Logger;

import joework.app.AssHoleBaseGame;


import com.jme.app.AbstractGame;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.Timer;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

/**
 * <p>
 * In questo test viene illustrato come utilizzare il sistema dello stato di gioco. Non si può estendere da
 * SimpleGame perché un sacco di funzioni di SimpleGames (ad esempio, camera, rootNode e input)
 * sono state delegate nelle sottoclassi. Quindi questa classe è
 * Essenzialmente una versione alleggerita di SimpleGame, dove la funzione INIT crea un
 * GameStateManager e vi associa un MenuState.
 * </p>
 * 
 * <p>
 * It also has a special way to reach the finish method, using a singleton instance
 * and a static exit method.
 * </p>
 * 
 * @author Per Thulin
 * @author Andrea
 */
public class TestGameStateSystem extends AssHoleBaseGame {
    private static final Logger logger = Logger
            .getLogger(TestGameStateSystem.class.getName());
	
	/** Usato solo nel metodo di uscita statico */
	private static AbstractGame instance;
	
	/**
	 * This is called every frame in BaseGame.start()
	 * 
	 * @param interpolation unused in this implementation
	 * @see AbstractGame#update(float interpolation)
	 */
	protected final void update(float interpolation) {
		// Ricalcola il framerate
		timer.update();
		tpf = timer.getTimePerFrame();
		
		// Aggiorna lo stato del game in base al tpf
		GameStateManager.getInstance().update(tpf);
	}
	
	/**
	 * E' invocato ogni frame in BaseGame.start(), after update()
	 * 
	 * @param interpolation unused in this implementation
	 * @see AbstractGame#render(float interpolation)
	 */
	protected final void render(float interpolation) {	
		// Clears the previously rendered information.
		display.getRenderer().clearBuffers();
		// Render the current game state.
		GameStateManager.getInstance().render(tpf);
	}
	
	/**
	 * Creates display, sets  up camera, and binds keys.  Called in BaseGame.start() directly after
	 * the dialog box.
	 * 
	 * @see AbstractGame#initSystem()
	 */
	protected final void initSystem() {
		try {
			/** Get a DisplaySystem acording to the renderer selected in the startup box. */
			display = DisplaySystem.getDisplaySystem(settings.getRenderer());
			/** Creo finestra con i parametri fissati all'avvio */
			display.createWindow(
					settings.getWidth(),
					settings.getHeight(),
					settings.getDepth(),
					settings.getFrequency(),
					settings.isFullscreen());		
		}
		catch (JmeException e) {
			/** Se il displaysystem non è stato inizializzato correttamente, esce. */
            logger.log(Level.SEVERE, "Could not create displaySystem", e);
			System.exit(1);
		}
		
		/** Get a high resolution timer for FPS updates. */
		timer = Timer.getTimer();
		
	}
	
	/**
	 * Called in BaseGame.start() after initSystem().
	 * 
	 * @see AbstractGame#initGame()
	 */
	protected final void initGame() {		
		instance = this;
		display.setTitle("Test Game State System");
		
		// Crea il gameStateManager
		GameStateManager.create();
		// Adds a new GameState to the GameStateManager. In order for it to get
		// processed (rendered and updated) it needs to get activated.
		GameState menu = new MenuState("menu");
		menu.setActive(true);
		GameStateManager.getInstance().attachChild(menu);
	}
	
	/**
	 * Cleans up the keyboard and game state system.
	 * 
	 * @see AbstractGame#cleanup()
	 */
	protected void cleanup() {
		logger.info("Cleaning up resources.");
		
		// Performs cleanup on all loaded game states.
		GameStateManager.getInstance().cleanup();
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TestGameStateSystem app = new TestGameStateSystem();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}
	
	/**
	 * Metodo Statico usato per l'uscita
	 */
	public static void exit() {
		instance.finish();
	}

	@Override
	protected void reinit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupCamera() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupCharacters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupChaseCamera() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupEnvironment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupPlayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void simpleUpdate() {
		// TODO Auto-generated method stub
		
	}
	
}
