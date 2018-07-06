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
public class Property {
	public String propID;
	public String varID;
	public BooleanFormula theFormula;
	public int propType; // 1:AG 0:EF

	public Property(String id, BooleanFormula aFormula) {
		propID = id;
		String test = "CTLCardinality-";
		varID = "CTLCardinality_"
				+ id.substring(
						id.lastIndexOf("CTLCardinality-") + test.length(),
						id.length());
		theFormula = aFormula;
		propType = aFormula.plainOutput().startsWith("(reachable &!") ? 1 : 0;

	}

	public String toString() {
		String result = "PROPERTY: " + propID + " \t"
				+ theFormula.plainOutput();
		return result;
	}

	public String smartOut(PetriModel64 theModel) {
		StringBuilder sb = new StringBuilder();
		sb.append("\ttrace " + varID + " := " + "BMC( "
				+ theFormula.plainOutput() + " , __BOUND__ )");
		sb.append("; // ");
		return sb.toString();
	}

	public String smartOutBottom() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nautomodel." + varID + ";");
		// String delim = "";
		// sb.append(") // " + boundFormulaName);
		return sb.toString();
	}

	public boolean isTemporal() {
		return theFormula.isTemporal();
	}

	public boolean isNested() {
		return theFormula.isNested();
	}

	public boolean hasLinearTemplate() {
		return theFormula.hasLinearTemplate();
	}
}
