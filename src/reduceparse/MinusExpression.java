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
public class MinusExpression implements BooleanFormula {
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;

	private MinusExpression(BooleanFormula left, BooleanFormula right) {
		pathLeft = left;
		pathRight = right;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new MinusExpression(left, right);
	}

	@Override
	public String plainOutput() {
		return "( " + pathLeft.plainOutput() + " - " + pathRight.plainOutput()
				+ " )";
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