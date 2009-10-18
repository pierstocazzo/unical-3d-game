package customGame;

import customGame.Plant;

import java.util.ArrayList;
import java.util.Random;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.TerrainPage;

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
    private static void clone(Spatial toClone, int amount,
            TerrainPage terrainPage, float randomScale, boolean isTree)
    {
//        CloneImportExport cc1 = new CloneImportExport();
//        
//        cc1.loadClone()
//        
//        cc1.addProperty("vertices");
//        cc1.addProperty("normals");
//        cc1.addProperty("colors");
//        cc1.addProperty("texcoords");
//        cc1.addProperty("indices");
//
//        /**
//         * erst wird die Größe des Terrains berechnet (ist eigentlich in x und z
//         * gleich groß --> viereck) (hängt aber auch noch vom scalefactor ab!!)
//         * das terrain befindet sich in 0/0/0 --> dort ist der mittelpunkt -->
//         * (wurde dort hingesetzt) ==> das terrain geht bei einer size von 129
//         * und scalefactor 10/1/10 in X von -645 bis 645 (dito in Z), Die
//         * Gesamtgröße ist 1290 ==> berechnete Random Größe von X (ist im
//         * Bereich 0 - 1290) davon 645 (realPosMaxX) abziehen also: wenn wert(im
//         * Bereich 0-1290) größer als 645 ist der Endwert im positiven Bereich
//         * wenn er kleiner als 645 ist, ist der Endwert im negativen Bereich,
//         * also auf dem terrain in der "linken" hälfte dito für Z evtl fallen
//         * ein paar bäume noch raus...sollten aber nicht viele sein geht auch
//         * für unterschiedliche Werte im Scalefactor, weil X und Z separat
//         * berechnet werden
//         */
//        Spatial s1;
//        Random r = new Random();
//
//        float scaleAll;
//        for (int i = 0; i < amount; i++)
//        {
//            s1 = cc1.createCopy();
//
//            s1.setLocalTranslation(calcRandomPos(terrainPage));
//
//            // set random rotation
//            Quaternion rot = new Quaternion();
//            rot.fromAngleAxis(FastMath.DEG_TO_RAD * r.nextFloat() * 360,
//                    new Vector3f(0, 1, 0));
//            s1.setLocalRotation(rot);
//            scaleAll = toClone.getLocalScale().x;
//            // for example range scale - 0.1 to scale + 0.1
//            scaleAll = rnd.nextFloat() * (randomScale + randomScale) + scaleAll
//                    - randomScale;
//            s1.setLocalScale(scaleAll);
//
//            if (isTree)
//            {
//                // set trees a bit unter the terrain
//                Vector3f tmp = new Vector3f(s1.getLocalTranslation());
//                tmp.y -= 2;
//                s1.setLocalTranslation(tmp);
//            }
//
//            vegetationList.add(s1);
//            terrainPage.getParent().attachChild(s1);
//        }
//
//        //        Vector3f ws = terrainPage.getWorldScale();
//        //        System.out.println("ws" + ws.x + "|" + ws.y + "|" + ws.z);
//        //        Vector3f wt = terrainPage.getWorldTranslation();
//        //        System.out.println("wt" + wt.x + "|" + wt.y + "|" + wt.z);
//        //        Vector3f ls = terrainPage.getLocalScale();
//        //        System.out.println("ls" + ls.x + "|" + ls.y + "|" + ls.z);
//        //        Vector3f lt = terrainPage.getLocalTranslation();
//        //        System.out.println("lt" + lt.x + "|" + lt.y + "|" + lt.z);
//        //        Vector3f ss = terrainPage.getStepScale();
//        //        System.out.println("ss" + ss.x + "|" + ss.y + "|" + ss.z);

    }
}
