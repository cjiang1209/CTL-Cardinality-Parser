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
public class LTEFormula implements BooleanFormula {
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;

	private LTEFormula(BooleanFormula left, BooleanFormula right) {
		pathLeft = left;
		pathRight = right;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new LTEFormula(left, right);
	}

	@Override
	public String plainOutput() {
		if (pathLeft instanceof ConstantExpression
				&& pathRight instanceof TokenCountExpression) {
			ConstantExpression ce = (ConstantExpression) pathLeft;
			if (ce.getConstantValue() == 0) {
				return "TRUE";
			}
			else if (ce.getConstantValue() == 1) {
				return pathRight.plainOutput() + " == 1";
			}
			else {
				return "FALSE";
			}
		}
		if (pathLeft instanceof TokenCountExpression
				&& pathRight instanceof TokenCountExpression) {
			return "( " + pathLeft.plainOutput() + " == 0 ) | ( "
					+ pathRight.plainOutput() + " == 1 )";
		}

		return pathLeft.plainOutput() + " <= " + pathRight.plainOutput();
	}

	@Override
	public boolean isTemporal() {
		return false;
	}

	@Override
	public boolean isNested() {
		return false;
	}

	@Override
	public boolean hasLinearTemplate() {
		return true;
	}
}
