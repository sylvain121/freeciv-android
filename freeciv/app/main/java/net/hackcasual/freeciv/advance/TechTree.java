package net.hackcasual.freeciv.advance;

import net.hackcasual.freeciv.models.Advance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylvain121 on 02/05/14.
 * Singleton  : create tech tree structure from game data at game start
 */
public class TechTree {

    private final Advance[] advances;

    /**
     * Constructor
     * @param advances
     */
    public TechTree( Advance[] advances) {
        this.advances = advances;
    }

    public void columnsGroup () {
        
    }

    public List<List<Advance>> getGroupedTechTree() {
        return null;
    }

    public ArrayList<Advance> arrayToArrayList(Advance[] techTree) {
        return null;
    }
}
