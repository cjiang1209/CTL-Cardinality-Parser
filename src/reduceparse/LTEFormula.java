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
public class LTEFormula extends BooleanFormula {
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
		return "( " + pathLeft.plainOutput() + " <= " + pathRight.plainOutput()
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

	@Override
	public boolean isPathFormula() {
		return false;
	}

	@Override
	public boolean isACTL() {
		return false;
	}

	@Override
	public boolean isECTL() {
		return false;
	}

	@Override
	public BooleanFormula evaluate() {
		if (pathLeft instanceof ConstantExpression
				&& pathRight instanceof TokenCountExpression) {
			ConstantExpression ce = (ConstantExpression) pathLeft;
			if (ce.getConstantValue() == 0) {
				return BooleanConstant.TRUE;
			} else if (ce.getConstantValue() == 1) {
				return EqualFormula.makeFormula(pathRight, 1);
			} else {
				assert (ce.getConstantValue() >= 2);
				return BooleanConstant.FALSE;
			}
		}
		if (pathLeft instanceof TokenCountExpression
				&& pathRight instanceof TokenCountExpression) {
			TokenCountExpression tokenCountLeft = (TokenCountExpression) pathLeft;
			TokenCountExpression tokenCountRight = (TokenCountExpression) pathRight;
			if (tokenCountLeft.cardPlace.size() == 1
					&& tokenCountRight.cardPlace.size() == 1) {
				return OrFormula.makeFormula(
						EqualFormula.makeFormula(pathLeft, 0),
						EqualFormula.makeFormula(pathRight, 1));
			} else {
				// TODO: To be implemented
				return OrFormula.makeFormula(
						EqualFormula.makeFormula(pathLeft, 0),
						EqualFormula.makeFormula(pathRight, 1));
			}
		}
		return this;
	}

	@Override
	public BooleanFormula normalize() {
		return this;
	}

	@Override
	public BooleanFormula pushNegation() {
		throw new UnsupportedOperationException();
	}
}
