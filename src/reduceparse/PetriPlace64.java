/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;


/**
 * @author Benjamin Smith bensmith@iastate.edu
 * @see <a href="http://orcid.org/0000-0003-2607-9338">My ORCID is: 0000-0003-2607-9338</a>
 *
 */
public class PetriPlace64 implements Comparable {

    final String name;				// The name or "ID" of this Petri Net Place
    final long marking;				// The initial marking value of this place
    final boolean hasInitial;		// Records whether there is a non-default initial marking
    
    double sortScoreA;				// For sorting by some currently unknown factors
    double sortScoreB;				// For sorting by some currently unknown factors
	
	PetriModel64 myModel;

    private static final long DEFAULT_MARKING = 0L;

    /**
     * Constructor, where initial marking is non-default.
     *
     * @param aName Identifier for the place
     * @param aMark	(optional) Initial marking for the place
     */
    public PetriPlace64(String aName, long aMark) {
        name = aName;
        marking = aMark;
        hasInitial = true;
        sortScoreA = 0.0;
        sortScoreB = 0.0;
    }


    /**
     * Constructor, where initial marking is the default value.
     * @param aName Identifier (String) for this place
     */
    public PetriPlace64(String aName) {
        name = aName;
        marking = DEFAULT_MARKING;
        hasInitial = false;
        sortScoreA = 0.0;
        sortScoreB = 0.0;
    }


    /* (non-Javadoc)
     * Return this object in string form, formatted for the Smart tool.
     *
     * <P>Places with non-default initial markings produce a separate line
     * indicating the initial value.
     * @see java.lang.Object#toString()
     */
    /* @Override
    public String toString() {
        String result = "place " + name + ";";
        if ((hasInitial) && (marking != 0)) {
            result += "\n\tinit(" + name + ":" + marking + ");";
        }
        return result;
    }
    */
    @Override
    public String toString() {
        String result = "place " + myModel.translateName(name) + "; // " + name;
        if ((hasInitial) && (marking != 0)) {
            result += "\n\tinit(" + myModel.translateName(name) + ":" + marking + "); // " + name + " = " + marking;
        }
        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PetriPlace64 other = (PetriPlace64) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


    @Override
    public int compareTo(Object o) {
        // enable sorting by name as last resort (There should be no duplicate names)
        PetriPlace64 other = (PetriPlace64)o;
        if (this.sortScoreA > other.sortScoreA) return -1;
        if (this.sortScoreA < other.sortScoreA) return 1;
        if (this.sortScoreB > other.sortScoreB) return -1;
        if (this.sortScoreB < other.sortScoreB) return 1;
        if (this.marking < other.marking) return -1;
        if (this.marking > other.marking) return 1;
        return name.compareTo(other.name);
    }
}

