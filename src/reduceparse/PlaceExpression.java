/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

/**
 *
 * @author Ben
 */
public class PlaceExpression extends BooleanFormula {
	public String placeName;

	public PlaceExpression(String name) {
		placeName = name;
	}

	@Override
	public String plainOutput() {
		return "( " + placeName + " )";
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
