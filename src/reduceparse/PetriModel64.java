/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Petri Net Model object.
 *
 * <P>Stores the combined elements of a Petri net model.
 *
 * @author Benjamin Smith	bensmith@iastate.edu
 * @see <a href="http://orcid.org/0000-0003-2607-9338">My ORCID is: 0000-0003-2607-9338</a>
 *
 */
public class PetriModel64 {
    ArrayList<PetriPlace64> thePlaces;      // Many tools and users expect places to have an order.
    Collection<PetriTransition> theTrans;
    Collection<PetriArc64> theArcs;

    HashSet<String> placeSet;
	
	HashMap<String, String> nameTrans;	// old -> new (to handle "Smart" naming conventions)

    /**
     * Constructor.
     *
     * @param places	An ordered collection of Petri Net Places
     * @param trans		A collection of Petri Net Transitions
     * @param arcs		A collection of Petri Net Arcs
     */
    public PetriModel64(ArrayList<PetriPlace64> places, Collection<PetriTransition> trans, Collection<PetriArc64> arcs) {
        thePlaces = new ArrayList<>(places);
        theTrans = trans;
        theArcs = arcs;
		
		nameTrans = new HashMap<>();

		// Do name translations, and fill place set
		placeSet = new HashSet<>();
		int current = 1;
		for (PetriPlace64 pp : thePlaces) {
			placeSet.add(pp.name);
			String placeName = "p" + current;
			current++;
			nameTrans.put(pp.name, placeName);
			pp.myModel = this;
		}
		current = 1;
		for (PetriTransition pt : theTrans) {
			String transName = "t" + current;
			current++;
			nameTrans.put(pt.id, transName);
			pt.myModel = this;
		}
		for (PetriArc64 pa : theArcs) {
			pa.myModel = this;
		}
		
        System.out.println("Petri model created: " + thePlaces.size() + " places, " + trans.size() + " transitions, " + arcs.size() + " arcs. ");
    }

    /**
     * @return the number of places in this model
     */
    public int getNumPlaces() {
        return thePlaces.size();
    }

    /**
     * @return the number of transitions in this model
     */
    public int getNumTrans() {
        return theTrans.size();
    }

    /**
     * @return the number of arcs in this model
     */
    public int getNumArcs() {
        return theArcs.size();
    }
	
	public String translateName(String oldName) {
		return nameTrans.get(oldName);
	}
}
