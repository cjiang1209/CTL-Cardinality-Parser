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
public class DeadlockFormula implements BooleanFormula {

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
}
