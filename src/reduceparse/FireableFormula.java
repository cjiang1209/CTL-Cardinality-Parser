/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

import java.util.*;
/**
 *
 * @author BenjaminSmith
 */
public class FireableFormula implements BooleanFormula {
	public ArrayList<String> fireTrans;
        public PetriModel64 theModel;
	
	public FireableFormula(ArrayList<String> trans,PetriModel64 theModel) {
		fireTrans = new ArrayList<>(trans);
                this.theModel = theModel;
	}
	
	@Override
	public String plainOutput() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		String delim = "";
		for (String trans : fireTrans) {
                        String trans_chg = getChanged(trans);
			sb.append(delim + "(" + trans_chg + ")");
			delim = " | ";
		}
		sb.append(")");
		return sb.toString();
	}
	
        public String getChanged(String trans)
        {
            String trans_in_model = theModel.translateName(trans);
            String res="";
            for (PetriArc64 pa : theModel.theArcs) {
                //Look for all the arcs where trans is the destination
                //Retrieve the source places and the cardinalities on the arcs
                if(pa.target.equals(trans))
                
                        if(res=="") res += "(tk("+theModel.translateName(pa.source)+")>="+pa.cardinality+")"; 
                        else res += " & (tk("+theModel.translateName(pa.source)+")>="+pa.cardinality+")";
		}
            
            res ="potential("+res+")"; 
            return res;
        }
        
        
}
