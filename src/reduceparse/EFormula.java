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
	public boolean isACTL() {
		return false;
	}

	@Override
	public boolean isECTL() {
		if (path instanceof XFormula) {
			BooleanFormula subpath = ((XFormula) path).path;
			if (!subpath.isTemporal() || subpath.isECTL()) {
				return true;
			} else {
				return false;
			}
		} else if (path instanceof FFormula) {
			BooleanFormula subpath = ((FFormula) path).path;
			if (!subpath.isTemporal() || subpath.isECTL()) {
				return true;
			} else {
				return false;
			}
		} else if (path instanceof GFormula) {
			BooleanFormula subpath = ((GFormula) path).path;
			if (!subpath.isTemporal() || subpath.isECTL()) {
				return true;
			} else {
				return false;
			}
		} else if (path instanceof RFormula) {
			BooleanFormula subpathLeft = ((RFormula) path).pathLeft;
			BooleanFormula subpathRight = ((RFormula) path).pathRight;
			if ((!subpathLeft.isTemporal() || subpathLeft.isECTL())
					&& (!subpathRight.isTemporal() || subpathRight.isECTL())) {
				return true;
			} else {
				return false;
			}
		} else {
			assert (path instanceof UFormula);
			BooleanFormula subpathLeft = ((UFormula) path).pathLeft;
			BooleanFormula subpathRight = ((UFormula) path).pathRight;
			if ((!subpathLeft.isTemporal() || subpathLeft.isECTL())
					&& (!subpathRight.isTemporal() || subpathRight.isECTL())) {
				return true;
			} else {
				return false;
			}
		}
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
