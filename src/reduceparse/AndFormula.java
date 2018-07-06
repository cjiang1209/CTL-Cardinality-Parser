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
public class AndFormula implements BooleanFormula {
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
						: " & ") + pathRight.plainOutput()
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
		if (pathLeft.hasLinearTemplate() && pathRight.hasLinearTemplate()) {
			return true;
		}
		return false;
	}
}
