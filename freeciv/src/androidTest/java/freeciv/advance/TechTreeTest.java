package java.freeciv.advance;

import junit.framework.TestCase;
import net.hackcasual.freeciv.advance.TechTree;
import net.hackcasual.freeciv.models.Advance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylvain121 on 02/05/14.
 */
public class TechTreeTest extends TestCase {
    /**
     * define tech
     */
    Advance iron = new Advance("iron", 1, "blablabla", 10, 10, 0, 0, 0);
    Advance legion = new Advance("legion", 2, "blablabla", 10, 10, 1, 0, 0);
    Advance pottery = new Advance("pottery", 3, "blablabla", 10, 10, 0, 0, 0);
    Advance well = new Advance("well", 4, "blablabla", 10, 10, 0, 0, 0);
    Advance car = new Advance("car", 5, "blablabla", 10, 10, 1, 4, 0);
    Advance movingCitern = new Advance("movingCitern", 5, "blablabla", 10, 10, 5, 3, 0);

    /**
     *       tech tree final result
     *
     *       +-----------+           +---------------+
     *       |           |           |               |
     *       |  iron     +----------->    legion     |
     *       |           +------+    |               |
     *       +-----------+      |    +---------------+
     *       |
     *       +-----------+      |
     *       |           |      |
     *       | pottery   +--------------------------------+
     *       |           |      |                         |
     *       +-----------+      |                         |
     *                          |                         |
     *       +-----------+      |    +---------------+    |       +---------------+
     *       |           |      +---->               |    +------->               |
     *       |  well     +----------->    car        +--+         |  movingCitern |
     *       |           |           |               |  +--------->               |
     *       +-----------+           +---------------+            +---------------+
     */

    Advance [] techTree = { iron, legion, pottery, well, car, movingCitern };
    /**
     * tech tree parser instance
     */
    private TechTree instance;

    public void setUp() throws Exception {
        super.setUp();
        instance = new TechTree(techTree);
    }

    /**
     * convert advance array to ArrayList
     */
    public void arrayToArrayListTest() {
        List<Advance> row0 = new ArrayList<Advance>();
        row0.add(iron);
        row0.add(pottery);
        row0.add(well);
        row0.add(legion);
        row0.add(car);
        row0.add(movingCitern);

        ArrayList<Advance> advance = instance.arrayToArrayList(techTree);
        assertEquals(row0, advance);

    }
    /**
     * arrange tech by display columns
     */
    public void columnsGroupTest (){

        List<Advance> row0 = new ArrayList<Advance>();
        row0.add(iron);
        row0.add(pottery);
        row0.add(well);
        List<Advance> row1 = new ArrayList<Advance>();
        row1.add(legion);
        row1.add(car);
        List<Advance> row2 = new ArrayList<Advance>();
        row2.add(movingCitern);

        instance.columnsGroup();
        List<List<Advance>> columnTechTree = instance.getGroupedTechTree();
        assertEquals(columnTechTree.get(0), row0);
        assertEquals(columnTechTree.get(1), row1);
        assertEquals(columnTechTree.get(2), row2);
    }

}
