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
public class AFormula extends BooleanFormula {
	public BooleanFormula path;

	public AFormula(BooleanFormula aPath) {
		path = aPath;
	}

	@Override
	public String plainOutput() {
		return "(A" + path.plainOutput() + ")";
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
	public boolean isACTL() {
		if (path instanceof XFormula) {
			BooleanFormula subpath = ((XFormula) path).path;
			if (!subpath.isTemporal() || subpath.isACTL()) {
				return true;
			} else {
				return false;
			}
		} else if (path instanceof FFormula) {
			BooleanFormula subpath = ((FFormula) path).path;
			if (!subpath.isTemporal() || subpath.isACTL()) {
				return true;
			} else {
				return false;
			}
		} else if (path instanceof GFormula) {
			BooleanFormula subpath = ((GFormula) path).path;
			if (!subpath.isTemporal() || subpath.isACTL()) {
				return true;
			} else {
				return false;
			}
		} else {
			assert (path instanceof UFormula);
			BooleanFormula subpathLeft = ((UFormula) path).pathLeft;
			BooleanFormula subpathRight = ((UFormula) path).pathRight;
			if ((!subpathLeft.isTemporal() || subpathLeft.isACTL())
					&& (!subpathRight.isTemporal() || subpathRight.isACTL())) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean isECTL() {
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
		return new EFormula(path.pushNegation());
	}
}
