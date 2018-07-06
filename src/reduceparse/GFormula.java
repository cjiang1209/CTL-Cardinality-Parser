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
public class GFormula  implements BooleanFormula {
	public BooleanFormula path;
	
	public GFormula(BooleanFormula aPath) {
		path = aPath;
	}
	
	@Override
	public String plainOutput() {
		return "G( " + path.plainOutput() + ")";
	}
        
       
	/*public String plainOutput() {
		return "(G " + path.plainOutput() + ")";
	}*/
	
}
