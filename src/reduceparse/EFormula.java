/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

/**
 *
 * @author BenjaminSmith
 */
public class EFormula implements BooleanFormula {
	public BooleanFormula path;
	
	
	public EFormula(BooleanFormula aPath) {
		path = aPath;
	}
	
	@Override
	public String plainOutput() {
		return "(E" + path.plainOutput() + ")";
	}
	
        
       /* public String plainOutput() {
		return "(E " + path.smartOutput() + ")";
	}*/
}
