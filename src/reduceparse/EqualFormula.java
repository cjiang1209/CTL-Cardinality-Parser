package reduceparse;

public class EqualFormula extends BooleanFormula {
	public BooleanFormula path;
	public long value;

	private EqualFormula(BooleanFormula path, long value) {
		assert (path instanceof TokenCountExpression);
		this.path = path;
		assert (value == 0 || value == 1);
		this.value = value;
	}

	public static BooleanFormula makeFormula(BooleanFormula path, long value) {
		return new EqualFormula(path, value);
	}

	@Override
	public String plainOutput() {
		return "( " + path.plainOutput() + " == " + value + " )";
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
		// Binary only
		assert (value == 0 || value == 1);
		return EqualFormula.makeFormula(path, 1 - value);
	}
}
