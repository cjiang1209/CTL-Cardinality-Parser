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
public class UFormula extends BooleanFormula {
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
		return "( " + pathLeft.plainOutput() + " ) U ("
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
			return pathRight;
		}
		if (pathLeft == BooleanConstant.TRUE) {
			return new FFormula(pathRight);
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

	public BooleanFormula pushNegationAU() {
		System.out.println("Negating AU...");

		// \neg A p U q = EG \neg q OR E \neg q U (\neg p AND \neg q)

		BooleanFormula negPathRight1 = pathRight.pushNegation();
		BooleanFormula negPathRight2 = pathRight.pushNegation();
		BooleanFormula negPathRight3 = pathRight.pushNegation();
		BooleanFormula negPathLeft = pathLeft.pushNegation();

		BooleanFormula eg = new EFormula(new GFormula(negPathRight1));
		BooleanFormula eu = new EFormula(new UFormula(negPathRight2,
				AndFormula.makeFormula(negPathLeft, negPathRight3)));
		return OrFormula.makeFormula(eg, eu);
	}
}
