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
public class TokenCountExpression extends BooleanFormula {
	public ArrayList<String> cardPlace;
	public String boundFormulaName;
	public String varID;
	public PetriModel64 theModel;

	public TokenCountExpression(ArrayList<String> plc, PetriModel64 theModel) {
		cardPlace = new ArrayList<>(plc);
		this.theModel = theModel;
	}

	@Override
	public String plainOutput() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		String delim = "";
		for (String plc : cardPlace) {
			sb.append(delim + "tk(" + theModel.translateName(plc) + ")");
			delim = " + ";
		}
		sb.append(")");
		return sb.toString();
	}

	public String smartOut(PetriModel64 theModel) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tbigint " + varID + " := run_for_MCC_UPPERBOUNDS({");
		String delim = "";
		for (String plc : cardPlace) {
			sb.append(delim + theModel.translateName(plc));
			delim = ",";
		}
		sb.append("}); // " + boundFormulaName);
		return sb.toString();
	}

	public String smartOutBottom() {
		StringBuilder sb = new StringBuilder();
		sb.append("\tprint(\"" + boundFormulaName + " \", automodel." + varID
				+ ", \" ");
		sb.append("TECHNIQUES SEQUENTIAL_PROCESSING DECISION_DIAGRAMS\\n\");");
		// String delim = "";
		for (String trans : cardPlace) {
			// sb.append(delim + "#tokens(\"" + trans + "\")");
			// delim = " | ";
		}
		// sb.append(") // " + boundFormulaName);
		return sb.toString();
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
