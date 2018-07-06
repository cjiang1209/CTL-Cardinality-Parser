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
public class UFormula implements BooleanFormula {
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;

	private UFormula(BooleanFormula left, BooleanFormula right) {
		pathLeft = left;
		pathRight = right;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new UFormula(left, right);
	}

	@Override
	public String plainOutput() {
		return "U( " + pathLeft.plainOutput() + "," + pathRight.plainOutput()
				+ " )";
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
		if (pathLeft.isTemporal() || pathRight.isNested()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		if (!pathLeft.isTemporal()) {
			return true;
		}
		return false;
	}
}
