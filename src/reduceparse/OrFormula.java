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
public class OrFormula extends BooleanFormula {
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;

	private OrFormula(BooleanFormula left, BooleanFormula right) {
		pathLeft = left;
		pathRight = right;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new OrFormula(left, right);
	}

	@Override
	public String plainOutput() {
		return "( " + pathLeft.plainOutput() + " | " + pathRight.plainOutput()
				+ " )";
	}

	@Override
	public boolean isTemporal() {
		if (pathLeft.isTemporal() || pathRight.isTemporal()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isNested() {
		if (pathLeft.isNested() || pathRight.isNested()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		if (!pathLeft.isTemporal() || !pathRight.isTemporal()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isPathFormula() {
		return false;
	}

	@Override
	public BooleanFormula evaluate() {
		pathLeft = pathLeft.evaluate();
		if (pathLeft == BooleanConstant.TRUE) {
			return BooleanConstant.TRUE;
		}
		pathRight = pathRight.evaluate();
		if (pathRight == BooleanConstant.TRUE) {
			return BooleanConstant.TRUE;
		}

		if (pathLeft == BooleanConstant.FALSE) {
			return pathRight;
		}
		if (pathRight == BooleanConstant.FALSE) {
			return pathLeft;
		}

		return this;
	}
}
