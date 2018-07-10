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
public class AndFormula extends BooleanFormula {
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;

	private AndFormula(BooleanFormula left, BooleanFormula right) {
		pathLeft = left;
		pathRight = right;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new AndFormula(left, right);
	}

	@Override
	public String plainOutput() {
		return "( "
				+ pathLeft.plainOutput()
				+ (pathLeft.isTemporal() || pathRight.isTemporal() ? " && "
						: " & ") + pathRight.plainOutput() + " )";
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
		if (pathLeft.hasLinearTemplate() && pathRight.hasLinearTemplate()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isPathFormula() {
		return false;
	}

	@Override
	public boolean isACTL() {
		if (pathLeft.isACTL() && pathRight.isACTL()) {
			return true;
		}
		if (pathLeft.isACTL() && !pathRight.isTemporal()) {
			return true;
		}
		if (!pathLeft.isTemporal() && pathRight.isACTL()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isECTL() {
		if (pathLeft.isECTL() && pathRight.isECTL()) {
			return true;
		}
		if (pathLeft.isECTL() && !pathRight.isTemporal()) {
			return true;
		}
		if (!pathLeft.isTemporal() && pathRight.isECTL()) {
			return true;
		}
		return false;
	}

	@Override
	public BooleanFormula evaluate() {
		pathLeft = pathLeft.evaluate();
		if (pathLeft == BooleanConstant.FALSE) {
			return BooleanConstant.FALSE;
		}
		pathRight = pathRight.evaluate();
		if (pathRight == BooleanConstant.FALSE) {
			return BooleanConstant.FALSE;
		}

		if (pathLeft == BooleanConstant.TRUE) {
			return pathRight;
		}
		if (pathRight == BooleanConstant.TRUE) {
			return pathLeft;
		}

		return this;
	}

	@Override
	public BooleanFormula normalize() {
		pathLeft = pathLeft.normalize();
		pathRight = pathRight.normalize();
		return this;
	}

	@Override
	public BooleanFormula pushNegation() {
		return OrFormula.makeFormula(pathLeft.pushNegation(),
				pathRight.pushNegation());
	}
}
