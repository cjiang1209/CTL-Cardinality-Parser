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
public class AFormula implements BooleanFormula {
	public BooleanFormula path;

	public AFormula(BooleanFormula aPath) {
		path = aPath;
	}

	@Override
	public String plainOutput() {
		return "(A" + path.plainOutput() + ")";
	}

	/*
	 * public String plainOutput() { return "(A " + path.smartOutput() + ")"; }
	 */

	@Override
	public boolean isTemporal() {
		return true;
	}
	
	@Override
	public boolean isNested() {
		return path.isNested();
	}

	@Override
	public boolean hasLinearTemplate() {
		return path.hasLinearTemplate();
	}
}
