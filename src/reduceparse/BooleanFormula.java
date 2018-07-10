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
public abstract class BooleanFormula {
	public abstract String plainOutput();

	public abstract boolean isTemporal();
	public abstract boolean isNested();
	public abstract boolean hasLinearTemplate();

	public abstract boolean isPathFormula();
	public abstract boolean isACTL();
	public abstract boolean isECTL();

	public abstract BooleanFormula evaluate();

	// Transformed into negation normal form
	public abstract BooleanFormula normalize();
	public abstract BooleanFormula pushNegation();
}
