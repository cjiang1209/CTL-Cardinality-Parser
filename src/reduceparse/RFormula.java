/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

public class RFormula extends BooleanFormula {	
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;

	private RFormula(BooleanFormula left, BooleanFormula right) {
		pathLeft = left;
		pathRight = right;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new RFormula(left, right);
	}

	@Override
	public String plainOutput() {
		return "( " + pathLeft.plainOutput() + " ) R ("
				+ pathRight.plainOutput() + " )";
	}

	/*
	 * public String plainOutput() { return "( " + pathLeft.plainOutput() +
	 * " U " + pathRight.plainOutput() + " )"; }
	 */

	@Override
	public boolean isTemporal() {
		return true;
	}

	@Override
	public boolean isNested() {
		if (pathLeft.isNested() || pathRight.isNested()) {
			return true;
		}
		if (pathLeft.isTemporal() || pathRight.isTemporal()) {
//		if (pathLeft.isTemporal() || pathRight.isNested()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		if (!pathRight.isTemporal()) {
			// return pathLeft.hasLienarTemplate();
			return true;
		}
		return false;
	}

	@Override
	public boolean isPathFormula() {
		return true;
	}

	@Override
	public boolean isACTL() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isECTL() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BooleanFormula evaluate() {
		pathRight = pathRight.evaluate();
		if (pathRight == BooleanConstant.FALSE) {
			return BooleanConstant.FALSE;
		}

		pathLeft = pathLeft.evaluate();
		if (pathLeft == BooleanConstant.FALSE) {
			return new GFormula(pathRight);
		}
		if (pathLeft == BooleanConstant.TRUE) {
			return pathRight;
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
		throw new UnsupportedOperationException();
	}
}
