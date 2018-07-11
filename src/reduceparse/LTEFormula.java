/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

import java.util.ArrayList;

/**
 *
 * @author BenjaminSmith
 */
public class LTEFormula extends BooleanFormula {
	public BooleanFormula pathLeft;
	public BooleanFormula pathRight;
	public boolean negated;

	private LTEFormula(BooleanFormula left, BooleanFormula right) {
		this(left, right, false);
	}
	
	private LTEFormula(BooleanFormula left, BooleanFormula right, boolean neg) {
		pathLeft = left;
		pathRight = right;
		negated = neg;
	}

	public static BooleanFormula makeFormula(BooleanFormula left,
			BooleanFormula right) {
		return new LTEFormula(left, right);
	}

	@Override
	public String plainOutput() {
		return "( " + pathLeft.plainOutput() + (negated ? " > " : " <= ") + pathRight.plainOutput()
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
			ConstantExpression constantLeft = (ConstantExpression) pathLeft;
			TokenCountExpression tokenCountRight = (TokenCountExpression) pathRight;
			if (constantLeft.getConstantValue() == 0) {
				return BooleanConstant.TRUE;
			} else if (constantLeft.getConstantValue() == 1) {
				// e.g. 1 <= a + b + c
				BooleanFormula head = null;
				for (String place : tokenCountRight.cardPlace) {
					ArrayList<String> cardPlace = new ArrayList<String>();
					cardPlace.add(place);
					if (head == null) {
						head = EqualFormula.makeFormula(
								new TokenCountExpression(cardPlace,
										tokenCountRight.theModel), 1);
					} else {
						head = OrFormula.makeFormula(head,
								EqualFormula.makeFormula(
										new TokenCountExpression(cardPlace,
												tokenCountRight.theModel), 1));
					}
				}
				return head;
			} else if (constantLeft.getConstantValue() == tokenCountRight.cardPlace
					.size()) {
				// e.g. 3 <= a + b + c
				BooleanFormula head = null;
				for (String place : tokenCountRight.cardPlace) {
					ArrayList<String> cardPlace = new ArrayList<String>();
					cardPlace.add(place);
					if (head == null) {
						head = EqualFormula.makeFormula(
								new TokenCountExpression(cardPlace,
										tokenCountRight.theModel), 1);
					} else {
						head = AndFormula.makeFormula(head,
								EqualFormula.makeFormula(
										new TokenCountExpression(cardPlace,
												tokenCountRight.theModel), 1));
					}
				}
				return head;
			} else if (constantLeft.getConstantValue() > tokenCountRight.cardPlace
					.size()) {
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
				return this;
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
		return new LTEFormula(pathLeft, pathRight, true);
	}
}
