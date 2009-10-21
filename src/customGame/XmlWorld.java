package customGame;

import java.net.URL;
import com.jme.input.MouseInput;
import com.jme.input.FirstPersonHandler;
import com.jme.util.export.xml.XMLImporter;
import com.jme.bounding.BoundingBox;
import com.jme.app.SimpleGame;
import com.jme.scene.Node;

public class XmlWorld extends SimpleGame {

    static public void main(String[] args) {
        XmlWorld xmlWorld = new XmlWorld();
        xmlWorld.start();
    }

    protected void simpleInitGame() {
        URL url = XmlWorld.class.getClassLoader().getResource("xmlModels/prova-jme.xml");
        try {
            MouseInput.get().setCursorVisible(true);
            ((FirstPersonHandler) input).setButtonPressRequired(true);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        System.out.println(url);
        loadModel( url );
    }

    protected void loadModel(URL modelUrl) {
        Node loadedSpatial = null;
        try {
            loadedSpatial = (Node) XMLImporter.getInstance().load(modelUrl);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to load URL: " + modelUrl, e);
        }
        loadedSpatial.setModelBound(new BoundingBox());
        rootNode.attachChild(loadedSpatial);
        loadedSpatial.updateModelBound();
        rootNode.updateRenderState();
    }
}
