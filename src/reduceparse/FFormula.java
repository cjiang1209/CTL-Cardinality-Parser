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
public class FFormula extends BooleanFormula {
	public BooleanFormula path;

	public FFormula(BooleanFormula aPath) {
		path = aPath;
	}

	@Override
	public String plainOutput() {
		return "F(" + path.plainOutput() + ")";
	}

	@Override
	public boolean isTemporal() {
		return true;
	}

	@Override
	public boolean isNested() {
		if (path.hasLinearTemplate()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		return path.hasLinearTemplate();
	}

	@Override
	public boolean isPathFormula() {
		return true;
	}

	@Override
	public BooleanFormula evaluate() {
		path = path.evaluate();
		if (path == BooleanConstant.TRUE || path == BooleanConstant.FALSE) {
			return path;
		}
		return this;
	}
}
