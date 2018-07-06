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
public class EFormula extends BooleanFormula {
	public BooleanFormula path;

	public EFormula(BooleanFormula aPath) {
		path = aPath;
	}

	@Override
	public String plainOutput() {
		return "(E" + path.plainOutput() + ")";
	}

	@Override
	public boolean isTemporal() {
		return true;
	}

	@Override
	public boolean isNested() {
		return path.isNested();
	}

	@Override
	public boolean hasLinearTemplate() {
		return path.hasLinearTemplate();
	}

	@Override
	public boolean isPathFormula() {
		return false;
	}

	@Override
	public BooleanFormula evaluate() {
		path = path.evaluate();
		if (!path.isPathFormula()) {
			return path;
		}
		return this;
	}

	@Override
	public BooleanFormula normalize() {
		path = path.normalize();
		return this;
	}

	@Override
	public BooleanFormula pushNegation() {
		return new AFormula(path.pushNegation());
	}
}
