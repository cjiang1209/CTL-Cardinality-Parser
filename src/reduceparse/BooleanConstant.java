package reduceparse;

public class BooleanConstant extends BooleanFormula {
	public boolean value;

	public static BooleanConstant TRUE = new BooleanConstant(true);
	public static BooleanConstant FALSE = new BooleanConstant(false);

	private BooleanConstant(boolean v) {
		value = v;
	}

	@Override
	public String plainOutput() {
		return value ? "TRUE" : "FALSE";
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
		return value ? BooleanConstant.FALSE : BooleanConstant.TRUE;
	}
}
