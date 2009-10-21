package customGame;

import java.net.MalformedURLException;
import java.util.List;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;
import com.jme.animation.AnimationController;
import com.jme.animation.BoneAnimation;
import com.jme.animation.Bone;

/**
 * Sample SimpleGame class which extends the behavior of XmlWorld to execute
 * the first animation of a Bone named with suffix 'SuperBone.
 * This Spatial will be created automatically by the HottBJ Exporter
 * if your Blender scene has an Armature with Blender's default name of
 * 'Armature' (and you select it for export).
 *
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 * @see #main(String[])
 */
public class XmlAnimator extends XmlWorld {
    /**
     * Instantiate a jME game world, loading the specified jME XML xmlModels
     * into the scene, then executing the first animation present.
     *
     * @param args
     *     <CODE><PRE>
     *     Syntax:  java... XmlAnimator [-r] file:model1-jme.xml...
     *     </PRE><CODE>
     *     where "-r" means to display the settings widget.
     *
     */
    static public void main(String[] args){
        XmlAnimator main = new XmlAnimator();
    	main.start();
    }

    /**
     * Loads and runs the animation.
     */
    protected void simpleInitGame() {
        super.simpleInitGame();
        AnimationController ac = getAnimationController();
        System.out.println("Available animations:");
        for (BoneAnimation anim : ac.getAnimations())
            System.out.println("    " + anim.getName());
        ac.setActiveAnimation("walk");
        ac.setActive(true);
        System.err.println("Executing animation '"
                + ac.getActiveAnimation().getName() + "'");
    }

    /**
     * Gets the AnimationController of the descendant Bone Spatial named
     * 'ArmatureSuperBone'"
     */
    public AnimationController getAnimationController() {
        List<Spatial> armatures =
                rootNode.descendantMatches(Bone.class, ".+SuperBone");
        if (armatures.size() < 1)
            throw new IllegalStateException("Sorry.  Program assumes "
                    + "you have a node named with suffix 'SuperBone'");
        if (armatures.get(0).getControllerCount() != 1)
            throw new IllegalStateException(
                    "Armature should have 1 controller, but has "
                    + armatures.get(0).getControllerCount());
        Controller controller = armatures.get(0).getController(0);
        if (!(controller instanceof AnimationController))
            throw new IllegalStateException(
                    "Controller is of unexpected type: "
                    + controller.getClass().getName());
        return (AnimationController) controller;
    }
}
