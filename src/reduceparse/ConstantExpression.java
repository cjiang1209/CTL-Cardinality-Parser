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
public class ConstantExpression extends BooleanFormula {
	public long numConstant;

	public ConstantExpression(long num) {
		numConstant = num;
	}

	public long getConstantValue() {
		return numConstant;
	}

	@Override
	public String plainOutput() {
		return "( " + numConstant + " )";
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
		return this;
	}
}
