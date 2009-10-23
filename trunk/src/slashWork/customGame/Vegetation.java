package slashWork.customGame;


import java.util.ArrayList;
import java.util.Random;

import slashWork.customGame.Plant;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;

/**
 * here you can load vegetation for your terrain
 * 
 * @author Johannes Weber
 */
public class Vegetation
{

    private static Random rnd = new Random();

    public static ArrayList<Spatial> vegetationList = new ArrayList<Spatial>();

    /**
     * loads trees
     * 
     * @param terrain
     *            to get the pos of the trees on the terrain
     * @param amount
     *            the amount of trees of one type to load
     */
    public static void loadTrees(TerrainBlock terrain, int amount)
    {
        Node tree = Plant.createPlant("trees", "tree", 9,
                " vegetation/trees");
        tree.setLocalScale(0.025f);
        tree.setLocalTranslation(calcRandomPos(terrain));
        // set trees a bit unter the terrain
        Vector3f tmp = new Vector3f(tree.getLocalTranslation());
        tmp.y -= 2;
        tree.setLocalTranslation(tmp);
        terrain.getParent().attachChild(tree);

        vegetationList.add(tree);
//        clone(tree, amount, terrain, 0.3f, true);
    }

    /**
     * loads bushes
     * 
     * @param terrainPage
     *            to get the pos of the bushes on the terrain
     * @param amount
     *            the amount of bushes of one type to load
     */
    public static void loadBushes(TerrainBlock terrain, int amount)
    {
        Node bush = Plant.createPlant("bushes", "bush", 10,
                "vegetation/bushes");
        bush.setLocalScale(0.025f);
        bush.setLocalTranslation(calcRandomPos(terrain));
        terrain.getParent().attachChild(bush);

        vegetationList.add(bush);
//        clone(bush, amount, terrainPage, 0.05f, false);
    }

    /**
     * loads plants
     * 
     * @param terrainPage
     *            to get the pos of the plants on the terrain
     * @param amount
     *            the amount of plants of one type to load
     */
    public static void loadPlants(TerrainBlock terrain, int amount)
    {
        Node plant = Plant.createPlant("plants", "plant", 24,
                "vegetation/plants");
        plant.setLocalScale(0.025f);
        plant.setLocalTranslation(calcRandomPos(terrain));
        terrain.getParent().attachChild(plant);
        vegetationList.add(plant);
//        clone(plant, amount, terrainPage, 0.05f, false);
    }
    /**
     * calculates a random pos over the terrain if it is out of the terrain
     * calculate again -> recursive call
     * 
     * @return the position
     */
    private static Vector3f calcRandomPos(TerrainBlock terrain)
    {
        Vector3f stepScale = terrain.getStepScale();
        int xz = terrain.getSize();
        // in this case X and Z are equal --> terrain is a quad
        float realTerrainSizeX = xz * stepScale.x;
        float realTerrainSizeZ = xz * stepScale.z;
        float realPosMaxX = realTerrainSizeX / 2;
        float realPosMaxZ = realTerrainSizeZ / 2;
        float x = rnd.nextFloat() * realTerrainSizeX - realPosMaxX;
        float z = rnd.nextFloat() * realTerrainSizeZ - realPosMaxZ;
        float height = terrain.getHeight(x, z);
        while (Float.isNaN(height))
        {
            x = rnd.nextFloat() * realTerrainSizeX - realPosMaxX;
            z = rnd.nextFloat() * realTerrainSizeZ - realPosMaxZ;
            height = terrain.getHeight(x, z);
        }
        return new Vector3f(x, height, z);
    }
}
