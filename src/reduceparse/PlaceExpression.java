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
public class PlaceExpression implements BooleanFormula {
	public String placeName;
	
	public PlaceExpression(String name) {
		placeName = name;
	}
	
	
	
	@Override
	public String plainOutput() {
		return "( " + placeName + " )";
	}
	
}
