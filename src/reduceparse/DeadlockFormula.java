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
public class DeadlockFormula extends BooleanFormula {

	private static BooleanFormula theDeadlock = new DeadlockFormula();

	private DeadlockFormula() {
	}

	public static BooleanFormula getDeadlock() {
		return theDeadlock;
	}

	@Override
	public String plainOutput() {
		return "(DEADLOCK)";
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
	public BooleanFormula evaluate() {
		theDeadlock = theDeadlock.evaluate();
		if (theDeadlock == BooleanConstant.TRUE
				|| theDeadlock == BooleanConstant.FALSE) {
			return theDeadlock;
		}

		return this;
	}

	@Override
	public BooleanFormula normalize() {
		theDeadlock.normalize();
		return this;
	}

	@Override
	public BooleanFormula pushNegation() {
		throw new UnsupportedOperationException();
	}
}
