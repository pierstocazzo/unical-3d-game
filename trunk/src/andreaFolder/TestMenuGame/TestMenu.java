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

package andreaFolder.TestMenuGame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import jmetest.awt.swingui.HelloJMEDesktop;
import com.jme.app.SimpleGame;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyboardLookHandler;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.awt.swingui.JMEDesktop;

/**
 * Example for using Swing within a jME game: Some frames, buttons and textfields are shown above
 * and on a spinning box. See {@link HelloJMEDesktop} for a shorter example.
 *
 * @see com.jmex.awt.swingui.JMEDesktop
 */
public class TestMenu extends SimpleGame {
    private JMEDesktop jmeDesktop;
    private Node desktopNode;
    private KeyboardLookHandler lookHandler;

    public TestMenu() {
    }

    protected void simpleUpdate() {
        if ( jmeDesktop.getFocusOwner() == null ) {
            lookHandler.setEnabled( true );
        } else {
            lookHandler.setEnabled( false );
        }
    }

    public static void main( String[] args ) throws Exception {
        TestMenu testJMEDesktop = new TestMenu();
        testJMEDesktop.setConfigShowMode( ConfigShowMode.AlwaysShow );
        testJMEDesktop.start();
    }

    /**
     * Called near end of initGame(). Must be defined by derived classes.
     */
    protected void simpleInitGame() {
        display.setTitle( "jME-Desktop test" );
        display.getRenderer().setBackgroundColor( ColorRGBA.blue.clone() );

        // move the 'default' keys (debug normals, toggle lighting, etc.) to a separated input handler
        InputHandler handlerForDefaultKeyActions = input;
        // remove the first person nested handlers
        handlerForDefaultKeyActions.removeAllFromAttachedHandlers();
        // create a new handler for our input
        input = new InputHandler();
        // add the default handler as a child
        input.addToAttachedHandlers( handlerForDefaultKeyActions );
        // create another look handler
        lookHandler = new KeyboardLookHandler( cam, 50, 1 );
        // and nest it
        input.addToAttachedHandlers( lookHandler );

        jmeDesktop = new JMEDesktop( "test internalFrame" );
        jmeDesktop.setup( display.getWidth(), display.getHeight(), false, input );
        jmeDesktop.setLightCombineMode( Spatial.LightCombineMode.Off );
        desktopNode = new Node( "desktop node" );
        desktopNode.attachChild( jmeDesktop );
        rootNode.attachChild( desktopNode );
        rootNode.setCullHint( Spatial.CullHint.Never );
        //createBoxBorder();

        perspective();
        fullScreen();

        jmeDesktop.getJDesktop().setBackground( new Color( 1, 1, 1, 0.2f ) );

        try {
            SwingUtilities.invokeAndWait( new Runnable() {
                public void run() {
                    // Only access the Swing UI from the Swing event dispatch thread!
                    // See SwingUtilities.invokeLater()
                    // and http://java.sun.com/docs/books/tutorial/uiswing/concurrency/index.html for details.
                    createSwingStuff( "Nuova Partita", display.getWidth()/4, display.getHeight()/4 + 100);
                    createSwingStuff( "Esci", display.getWidth()/4, display.getHeight()/4 + 150);
                }
            } );
        } catch ( InterruptedException e ) {
            // ok - just leave
            return;
        } catch ( InvocationTargetException e ) {
            throw new RuntimeException( e );
        }

        createCustomCursor();
    }


    private void createCustomCursor() {
        cursor = new AbsoluteMouse( "cursor", display.getWidth(), display.getHeight() );

        // Get a picture for my mouse.
        TextureState ts = display.getRenderer().createTextureState();
        URL cursorLoc = TestMenu.class.getClassLoader().getResource(
                "jmetest/data/cursor/cursor1.png" );
        Texture t = TextureManager.loadTexture( cursorLoc, Texture.MinificationFilter.NearestNeighborNoMipMaps,
                Texture.MagnificationFilter.Bilinear, Image.Format.GuessNoCompression, 1, true );
        ts.setTexture( t );
        cursor.setRenderState( ts );

        // Make the mouse's background blend with what's already there
        BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled( true );
        as.setSourceFunction( BlendState.SourceFunction.SourceAlpha );
        as.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha );
        as.setTestEnabled( true );
        as.setTestFunction( BlendState.TestFunction.GreaterThan );
        cursor.setRenderState( as );

        // Assign the mouse to an input handler
        cursor.registerWithInputHandler( input );

        statNode.attachChild( cursor );

        // important for JMEDesktop: use system coordinates
        cursor.setUsingDelta( false );
        cursor.getXUpdateAction().setSpeed( 1 );
        cursor.getYUpdateAction().setSpeed( 1 );

        cursor.setCullHint( Spatial.CullHint.Never );
    }

    private void perspective() {
        //desktopNode.getLocalRotation().fromAngleNormalAxis( -0.7f, new Vector3f( 1, 0, 0 ) );
        desktopNode.setLocalScale( 24f / jmeDesktop.getJDesktop().getWidth() );
        desktopNode.getLocalTranslation().set( 0, 0, 0 );
        desktopNode.setRenderQueueMode( Renderer.QUEUE_TRANSPARENT );
        desktopNode.setCullHint( Spatial.CullHint.Dynamic );
    }

    private void fullScreen() {
        final DisplaySystem display = DisplaySystem.getDisplaySystem();

        desktopNode.getLocalRotation().set( 0, 0, 0, 1 );
        desktopNode.getLocalTranslation().set( display.getWidth() / 2, display.getHeight() / 2, 0 );
        desktopNode.getLocalScale().set( 1, 1, 1 );
        desktopNode.setRenderQueueMode( Renderer.QUEUE_ORTHO );
        desktopNode.setCullHint( Spatial.CullHint.Never );
    }

    private AbsoluteMouse cursor;

    protected void createSwingStuff(final String label, int x, int y) {
        final JDesktopPane desktopPane = jmeDesktop.getJDesktop();

        JButton fullScreenButton = new JButton( "<html><big>"+label+"</big></html>" );
        fullScreenButton.setSize( fullScreenButton.getPreferredSize() );
        fullScreenButton.setLocation( x, y);
        desktopPane.add( fullScreenButton );
        fullScreenButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
            	//COSA ACCADE AL CLICK
            	if(label.equals("Nuova Partita")){
            		Logger.getAnonymousLogger().logp(Level.INFO, "",
                            "New Game","","");
            		
            	}
            	if(label.equals("Esci")){
            		Logger.getAnonymousLogger().logp(Level.INFO, "",
                            "Exit clicked","","");
            		finish();
            	}
            }
        } );

        desktopPane.repaint();
        desktopPane.revalidate();
    }


    protected void cleanup() {
        if ( jmeDesktop != null ) {
            jmeDesktop.dispose();
        }
        super.cleanup();
    }
}
