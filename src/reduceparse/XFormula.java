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
public class XFormula implements BooleanFormula {
	public BooleanFormula path;

	public XFormula(BooleanFormula aPath) {
		path = aPath;
	}

	@Override
	public String plainOutput() {
		return "X( " + path.plainOutput() + ")";
	}

	/*
	 * public String plainOutput() { return "(X " + path.plainOutput() + ")"; }
	 */

	@Override
	public boolean isTemporal() {
		return true;
	}

	@Override
	public boolean isNested() {
		if (path.isNested()) {
			return true;
		}
		if (path.isTemporal()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		return path.hasLinearTemplate();
	}
}
