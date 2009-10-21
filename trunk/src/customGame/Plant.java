/*
 * Copyright (c) 2005, Johannes Weber
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Wolkenstein, nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS &quot;AS IS&quot;
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

/*
 * Created on 17.07.2005
 */
package customGame;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.export.xml.XMLImporter;

/**
 * loads a plant and returns a node with the it
 * 
 * @author Johannes Weber
 */
public class Plant
{

    private static int i = 0;

    private static Random rnd = new Random();

    /**
     * creates a plant example: pathURL = "xmlModels/vegetation/trees", modelName =
     * "tree", plantTypes = 9 --> modelURL =
     * "xmlModels/vegetation/trees/tree1-9.jme"
     * the plantType specifies the amount of different plants there are.
     * the planttype is calculated by random 
     * @param pathURL
     * @param modelName
     * @param plantTypes
     * @param texURL
     * @return
     */
    public static Node createPlant(String pathURL, String modelName,
            int plantTypes, String texURL)
    {
        URL modelURL = Game.class.getClassLoader().getResource(/*pathURL + "/" + modelName + (rnd.nextInt(plantTypes) + 1) + "1.jme" */ "xmlModels/bike.jme" );
        URL textureURL = Game.class.getClassLoader().getResource(texURL + "/" + modelName + (rnd.nextInt(plantTypes) + 1) + ".tga");
        
        Node tmp = load(modelURL, textureURL);
        tmp.setName("plant" + i);

        tmp.updateRenderState();
        i++;
        return tmp;
    }

    private static Node load(URL modelURL, URL texURL)
    {    
        Node model = null;
		
		try {
			model = (Node) BinaryImporter.getInstance().load( modelURL );
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
			return model;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
