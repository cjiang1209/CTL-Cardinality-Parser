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
public class NotFormula implements BooleanFormula {
	public BooleanFormula path;

	public NotFormula(BooleanFormula toNot) {
		path = toNot;
	}

	@Override
	public String plainOutput() {
		return "(! " + path.plainOutput() + ")";
	}

	@Override
	public boolean isTemporal() {
		if (path.isTemporal()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isNested() {
		if (path.isNested()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		return true;
	}
}
