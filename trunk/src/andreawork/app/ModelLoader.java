package andreawork.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import testGame.Game;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.physics.DynamicPhysicsNode;

public class ModelLoader {
	public Node loadMd3(String pathModel, Node rootNode, DisplaySystem display){

		Node model = null;

		try {
			MaxToJme converter = new MaxToJme();
//			ObjToJme converter = new ObjToJme();
//			Md3ToJme converter = new Md3ToJme();
//			Md2ToJme converter = new Md2ToJme();
//			AseToJme converter = new AseToJme();

//			C1.setProperty("mtllib",maxFile);

			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			//"data/model/nordhorse.md3"
			URL url = Game.class.getClassLoader().getResource(pathModel);

			converter.convert( url.openStream(),BO);
			model = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
//			model = (Node)BinaryImporter.getInstance().load( url );
			//scale it to be MUCH smaller than it is originally
			model.setLocalScale(0.025f);
			model.setModelBound(new BoundingBox());
			model.updateModelBound();

			Quaternion quaternion1 = new Quaternion();
			quaternion1.fromAngleAxis(FastMath.PI/2, new Vector3f(0, 1, 0));
			model.setLocalRotation(quaternion1);
		} catch (IOException e) {
		   e.printStackTrace();
		}

        Texture texture = TextureManager.loadTexture(
                Game.class.getClassLoader().getResource("data/model/nordhorse_color.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);

        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(texture);
        model.setRenderState( ts );
		return model;
	}
}
